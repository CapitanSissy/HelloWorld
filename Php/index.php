<?php
  include_once 'header.php';
  include_once './languages/fa.php';
  include_once 'defaults.php';

  try {
    $soapClient = new SoapClient(constant("wsdlUrl"), ['stream_context' => stream_context_create(['ssl' => ['allow_self_signed' => true, 'verify_peer' => false, 'verify_peer_name' => false]])]);
    // var_dump($soapClient->__getFunctions());

    $response = $soapClient -> __soapCall("sayHello", array()) -> return;
    echo "<!DOCTYPE html>".$response;

  } catch(SoapFault $e) {
    $message = $e -> getMessage();
    if (strpos($message, 'Parsing WSDL') !== false) {
      echo '<span class="blink red">'.$language['WEB_SERVICE_NOT_RUNNING']."</span><br/><br/>";
    } else if (strpos($message, 'Could not connect to host') !== false) {
      echo '<span class="blink red">'.$language['COULD_NOT_CONNECT_TO_HOST']."</span><br/><br/>";
    }
    else {
      echo $message;
    }
  }
