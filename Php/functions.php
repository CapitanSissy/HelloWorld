<?php
  include_once 'defaults.php';
  /*=-=-=-=-=-=-=-=-=-=-[]-=-=-=-=-=-=-=-=-=-=*/
  function addUserInput($new) {
    try {
      $soapClient = new SoapClient(constant("wsdlUrl"), array('soap_version' => SOAP_1_1));
      $soapClient -> __soapCall("GetInput", array(array("arg0" => $new)));
    } catch (SoapFault $e) {
      return $e -> getMessage();
    }
  }
