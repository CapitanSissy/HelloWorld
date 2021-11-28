package com.github.capitansissy.service.handler;

import com.github.capitansissy.Logger;
import com.github.capitansissy.constants.Defaults;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.soap.SOAPFaultException;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

public class MacAddressValidator implements SOAPHandler<SOAPMessageContext> {
  private Logger logger = new Logger();

  @Override
  public boolean handleMessage(SOAPMessageContext context) {
    logger.setLog("Server : handleMessage()......", Defaults.Log4J.INFO);
    boolean isOutBound = (boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
    // true: for outbound messages
    // false: for inbound
    if (!isOutBound) {
      try {
        SOAPMessage soapMessage = context.getMessage();
        SOAPEnvelope soapEnvelope = soapMessage.getSOAPPart().getEnvelope();
        SOAPHeader soapHeader = soapEnvelope.getHeader();

        //if no header, add one
        if (soapHeader == null) {
          soapHeader = soapEnvelope.addHeader();
          generateMessage(soapMessage, "No SOAP header.");
        }

        //Get client mac address from SOAP header
        Iterator it = soapHeader.extractHeaderElements(SOAPConstants.URI_SOAP_ACTOR_NEXT);

        if (it == null || !it.hasNext()) {
          generateMessage(soapMessage, "No header block for next actor.");
        }

        //if no mac address found? throw exception
        Node macNode = (Node) Objects.requireNonNull(it).next();
        String macValue = (macNode == null) ? null : macNode.getValue();

        if (macValue == null) {
          generateMessage(soapMessage, "No mac address in header block.");
        }

        //if mac address is not match, throw exception
        if (!Objects.requireNonNull(macValue).equals("4C-80-93-4D-20-D3")) {
          generateMessage(soapMessage, "Invalid mac address, access is denied.");
        }
        soapMessage.writeTo(System.out);
      } catch (Exception e) {
        logger.setLog(e.getMessage(), Defaults.Log4J.DEBUG);
      }
    }
    // continue other handler chain
    return true;
  }

  @Override
  public boolean handleFault(SOAPMessageContext context) {
    logger.setLog("Server : handleFault()......", Defaults.Log4J.INFO);
    return true;
  }

  @Override
  public void close(MessageContext context) {
    logger.setLog("Server : close()......", Defaults.Log4J.INFO);
  }

  @Override
  public Set<QName> getHeaders() {
    logger.setLog("Server : getHeaders()......", Defaults.Log4J.INFO);
    return null;
  }

  private void generateMessage(SOAPMessage soapMessage, String reason) {
    try {
      SOAPBody soapBody = soapMessage.getSOAPPart().getEnvelope().getBody();
      SOAPFault soapFault = soapBody.addFault();
      soapFault.setFaultString(reason);
      throw new SOAPFaultException(soapFault);
    } catch (SOAPException e) {
      logger.setLog(e.getMessage(), Defaults.Log4J.DEBUG);
    }
  }


}
