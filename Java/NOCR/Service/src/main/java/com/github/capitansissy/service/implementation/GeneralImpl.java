package com.github.capitansissy.service.implementation;

import com.aminography.primecalendar.PrimeCalendar;
import com.aminography.primecalendar.hijri.HijriCalendar;
import com.aminography.primecalendar.persian.PersianCalendar;
import com.github.capitansissy.Logger;
import com.github.capitansissy.constants.Defaults;
import com.github.capitansissy.constants.Tools;
import com.github.capitansissy.constants.webservice.soap.Actions;
import com.github.capitansissy.constants.webservice.soap.Implementations;
import com.github.capitansissy.constants.webservice.soap.Operations;
import com.github.capitansissy.enumeration.Language;
import com.github.capitansissy.messages.ResourceAsStream;
import com.github.capitansissy.security.AES;
import com.github.capitansissy.service.interfaces.restful.RGeneral;
import com.github.capitansissy.service.interfaces.soap.SGeneral;
import com.sun.net.httpserver.HttpExchange;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@WebService(name = Implementations.GENERAL_WEBSERVICE_NAME,
  targetNamespace = Defaults.TARGET_NAME_SPACE,
  serviceName = Implementations.GENERAL_SERVICE_NAME,
  portName = Implementations.GENERAL_PORT_NAME)

public class GeneralImpl implements SGeneral, RGeneral {
  private Logger logger = new Logger();
  private ResourceAsStream resource = new ResourceAsStream(
    Tools.getDefaultLanguage(
      Integer.parseInt(
        Objects.requireNonNull(
          AES.decrypt(
            Tools.getResourceValue("structure", "default.language"),
            Defaults.INTERNAL_SECURITY_KEY))
      )));

  @Resource
  private WebServiceContext serviceContext;

  @Override
  @WebMethod(operationName = Operations.GLOBAL_GET_DATE, action = Actions.GLOBAL_GET_DATE)
  @WebResult(name = Operations.GLOBAL_GET_DATE, partName = Operations.GLOBAL_GET_DATE_RESPONSE, targetNamespace = Defaults.TARGET_NAME_SPACE)
  @RequestWrapper(targetNamespace = Defaults.TARGET_NAME_SPACE)
  @ResponseWrapper(targetNamespace = Defaults.TARGET_NAME_SPACE)
  @XmlElement(name = Operations.GLOBAL_GET_DATE, namespace = Defaults.TARGET_NAME_SPACE)
  public String GetDate() {
    MessageContext messageContext = serviceContext.getMessageContext();
    System.out.println(Tools.getIPAddress(
      ((HttpExchange) messageContext.get(Defaults.MESSAGE_CONTEXT_KEY))
        .getRemoteAddress()
        .getAddress()
        .toString()));

    if (Objects.equals(AES.decrypt(Tools.getResourceValue("structure", "default.language"),
      Defaults.INTERNAL_SECURITY_KEY), String.valueOf(Language.Farsi.getCode()))) {
      return new PersianCalendar().toPersian().getLongDateString();
    } else {
      return new PersianCalendar().toHijri().getLongDateString();
    }
  }

  @Override
  public String GetInput(String input) {
    return Objects.requireNonNull(AES.decrypt(resource.get("you.said"), Defaults.INTERNAL_SECURITY_KEY)).concat(Defaults.Slugs.Space).concat(Tools.getText(input));
  }

  @Override
  @WebMethod(operationName = Operations.GLOBAL_GET_VERSION, action = Actions.GLOBAL_GET_VERSION)
  @WebResult(name = Operations.GLOBAL_GET_VERSION, partName = Operations.GLOBAL_GET_VERSION_RESPONSE, targetNamespace = Defaults.TARGET_NAME_SPACE)
  @RequestWrapper(targetNamespace = Defaults.TARGET_NAME_SPACE)
  @ResponseWrapper(targetNamespace = Defaults.TARGET_NAME_SPACE)
  @XmlElement(name = Operations.GLOBAL_GET_VERSION, namespace = Defaults.TARGET_NAME_SPACE)
  public String GetVersion() {
    try {
      if (Objects.equals(AES.decrypt(Tools.getResourceValue("structure", "default.language"),
        Defaults.INTERNAL_SECURITY_KEY), String.valueOf(Language.Farsi.getCode()))) {
        PrimeCalendar primeCalendar = new PersianCalendar();
        primeCalendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(Tools.getResourceValue("structure", "release.candidate")));
        return String.format("%1$s-rc%2$s", Tools.getResourceValue("structure", "version.number"), Tools.toEN(primeCalendar.getShortDateString().replaceAll(Defaults.Slugs.Slash, Defaults.Slugs.Underscore)));
      } else {
        PrimeCalendar primeCalendar = new HijriCalendar();
        primeCalendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(Tools.getResourceValue("structure", "release.candidate")));
        return String.format("%1$s-rc%2$s", Tools.getResourceValue("structure", "version.number"), Tools.toEN(primeCalendar.getShortDateString().replaceAll(Defaults.Slugs.Slash, Defaults.Slugs.Underscore)));
      }
    } catch (Exception e) {
      logger.setLog(e.getMessage(), Defaults.Log4J.DEBUG);
      return String.format("%1$s-rc%2$s", Tools.getResourceValue("structure", "version.number"), new SimpleDateFormat(Defaults.DateTime.UNDERSCORE).format(new Date()));
    }
  }

  @Override
  public String sayHello() {
    return AES.decrypt(resource.get("hello.world"), Defaults.INTERNAL_SECURITY_KEY);
  }

}
