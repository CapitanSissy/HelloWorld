<?php
  include_once 'defaults.php';

  /*=-=-=-=-=-=-=-=-=-=-[]-=-=-=-=-=-=-=-=-=-=*/
  function addUserInput($new) {
    try {
      $soapClient = new SoapClient(constant("wsdlUrl"), ['stream_context' => stream_context_create(['ssl' => ['allow_self_signed' => true, 'verify_peer' => false, 'verify_peer_name' => false]])]);
      return $soapClient -> __soapCall("GetInput", array(array("arg0" => $new))) -> return;
    } catch (SoapFault $e) {
      return $e -> getMessage();
    }
  }
  /*=-=-=-=-=-=-=-=-=-=-[]-=-=-=-=-=-=-=-=-=-=*/
  /*=-=-=-=-=-=-=-=-=-=-[]-=-=-=-=-=-=-=-=-=-=*/
  /*=-=-=-=-=-=-=-=-=-=-[]-=-=-=-=-=-=-=-=-=-=*/
  /*=-=-=-=-=-=-=-=-=-=-[]-=-=-=-=-=-=-=-=-=-=*/
