package com.github.capitansissy.service.handler;

import com.github.capitansissy.constants.Tools;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

public class SecurityValidator implements SOAPHandler<SOAPMessageContext> {
  private static final String WSSECURITY_SECEXT = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";

  @Override
  public boolean handleMessage(SOAPMessageContext context) {
    boolean isOutBound = (boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
    if (isOutBound) {
      try {
        SOAPMessage soapMessage = context.getMessage();
        MimeHeaders mimeHeaders = soapMessage.getMimeHeaders();
        SOAPEnvelope soapEnvelope = soapMessage.getSOAPPart().getEnvelope();

        if (soapEnvelope.getHeader() != null) {
          soapEnvelope.getHeader().detachNode();
        }
        SOAPHeader header = soapEnvelope.addHeader();

        SOAPElement security = header.addChildElement("Security", "ns1", WSSECURITY_SECEXT);
        SOAPElement BinarySecurityToken = security.addChildElement("BinarySecurityToken", "ns1");
        BinarySecurityToken.addAttribute(new QName("xmlns:wsu"), WSSECURITY_SECEXT);
        BinarySecurityToken.addAttribute(new QName("EncodingType"), "SSHA");
        BinarySecurityToken.addAttribute(new QName("ValueType"), "AccessManagerSSOSecurityToken");
        BinarySecurityToken.addAttribute(new QName("wsu:Id"), "OAMToken");
        BinarySecurityToken.addTextNode(String.valueOf(UUID.randomUUID()));

        // mimeHeaders.addHeader("X-Content-Type-Options", "nosniff");
        // mimeHeaders.addHeader("X-XSS-Protection", "1; mode=block");
        // mimeHeaders.addHeader("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate");
        // mimeHeaders.addHeader("Pragma", "no-cache");
        // mimeHeaders.addHeader("Expires", "0");
        // mimeHeaders.addHeader("X-Frame-Options", "DENY");
        // mimeHeaders.addHeader("Access-Control-Allow-Origin", "*");

        if (Tools.isDebugging()) {
          // soapMessage.writeTo(System.out);
          // System.out.println();
        }
      } catch (Exception e) {
        // TODO: Use logger
      }
    } else {
      try {
        if (Tools.isDebugging()) {
          System.out.println("Inbound message:");
          context.getMessage().writeTo(System.out);
          System.out.println();
          System.out.println("=-=-=-=-=-=-=-=-=-=");
        }
      } catch (Exception e) {
        // TODO: Use logger
      }
    }
    return true;
  }

  @Override
  public Set<QName> getHeaders() {
    return Collections.emptySet();
  }

  @Override
  public boolean handleFault(SOAPMessageContext context) {
    return true;
  }

  @Override
  public void close(MessageContext context) {
    // If you use return false, request will be close.
  }
}
