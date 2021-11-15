package com.github.capitansissy.service.interfaces.soap;

import com.github.capitansissy.constants.Defaults;
import com.github.capitansissy.constants.webservice.soap.Actions;
import com.github.capitansissy.constants.webservice.soap.Interfaces;
import com.github.capitansissy.constants.webservice.soap.Operations;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

@WebService(name = Interfaces.GENERAL_WEBSERVICE_NAME, targetNamespace = Defaults.TARGET_NAME_SPACE)
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
@XmlRootElement(name = Interfaces.GENERAL_WEBSERVICE_NAME, namespace = Defaults.TARGET_NAME_SPACE)
public interface SGeneral {
  @WebMethod(operationName = Operations.GLOBAL_GET_DATE, action = Actions.GLOBAL_GET_DATE)
  @WebResult(name = Operations.GLOBAL_GET_DATE, partName = Operations.GLOBAL_GET_DATE_RESPONSE, targetNamespace = Defaults.TARGET_NAME_SPACE)
  @RequestWrapper(targetNamespace = Defaults.TARGET_NAME_SPACE)
  @ResponseWrapper(targetNamespace = Defaults.TARGET_NAME_SPACE)
  @XmlElement(name = Operations.GLOBAL_GET_DATE, namespace = Defaults.TARGET_NAME_SPACE)
  String GetDate();

  @WebMethod(operationName = Operations.GLOBAL_GET_VERSION, action = Actions.GLOBAL_GET_VERSION)
  @WebResult(name = Operations.GLOBAL_GET_VERSION, partName = Operations.GLOBAL_GET_VERSION_RESPONSE, targetNamespace = Defaults.TARGET_NAME_SPACE)
  @RequestWrapper(targetNamespace = Defaults.TARGET_NAME_SPACE)
  @ResponseWrapper(targetNamespace = Defaults.TARGET_NAME_SPACE)
  @XmlElement(name = Operations.GLOBAL_GET_VERSION, namespace = Defaults.TARGET_NAME_SPACE)
  String GetVersion();
}
