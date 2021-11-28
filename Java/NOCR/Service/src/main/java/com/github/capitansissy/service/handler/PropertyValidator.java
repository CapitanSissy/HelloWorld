package com.github.capitansissy.service.handler;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.soap.SOAPFaultException;
import java.util.Iterator;
import java.util.Set;

public class PropertyValidator implements SOAPHandler<SOAPMessageContext> {
  private final String VALID_PROPERTY = "RANDOM";

  @Override
  public boolean handleMessage(SOAPMessageContext context) {
    boolean isOutBound = (boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
    if (!isOutBound) {
      try {
        // Get the SOAP Message and grab the headers
        SOAPMessage soapMsg = context.getMessage();
        SOAPEnvelope soapEnv = soapMsg.getSOAPPart().getEnvelope();
        SOAPHeader soapHeader = soapEnv.getHeader();

        Iterator<?> headerIterator = soapHeader.extractHeaderElements(SOAPConstants.URI_SOAP_ACTOR_NEXT);
        if (headerIterator != null && headerIterator.hasNext()) {
          Node propertyNode = (Node) headerIterator.next();
          String property = null;
          if (propertyNode != null)
            property = propertyNode.getValue();
          if (VALID_PROPERTY.equals(property)) {
            soapMsg.writeTo(System.out);
          } else {
            SOAPBody soapBody = soapMsg.getSOAPPart().getEnvelope().getBody();
            SOAPFault soapFault = soapBody.addFault();
            soapFault.setFaultString("Invalid Property");
            throw new SOAPFaultException(soapFault);
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return true;
  }

  @Override
  public boolean handleFault(SOAPMessageContext context) {
    return true;
  }

  @Override
  public void close(MessageContext context) {
  }

  @Override
  public Set<QName> getHeaders() {
    return null;
  }
}