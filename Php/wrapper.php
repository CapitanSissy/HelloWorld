<?php
  include_once 'header.php';
  include_once './languages/fa.php';
  include_once 'defaults.php';
  require_once('functions.php');

  try {
    $soapClient = new SoapClient(constant("wsdlUrl"), ['stream_context' => stream_context_create(['ssl' => ['allow_self_signed' => true, 'verify_peer' => false, 'verify_peer_name' => false]])]);
    // var_dump($soapClient->__getFunctions());

    $solarDate = $soapClient -> __soapCall("getDate", array()) -> getDate;
    $version = $soapClient -> __soapCall("getVersion", array()) -> getVersion;
    $vnumber = explode("-", $version);

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
?>
<!DOCTYPE html>
<html lang="fa" dir="rtl">
  <head>
    <title>&nbsp;</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport"/>
    <meta name="viewport" content="width=device-width"/>
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <meta http-equiv="content-language" content="fa"/>
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <meta name="security" content="1a5dd4ca-8ae0-4292-a436-c32930516c65">
    <meta name="theme-color" content="#f1f1f1">
    <link href="assets/css/style.css" rel="stylesheet" type="text/css" media="all" />
    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <script type="text/javascript" src="assets/js/soap-action.js"></script>
    <script src="https://kit.fontawesome.com/7524e05e75.js" crossorigin="anonymous"></script>
  </head>
  <body>
    <label class="text-ultralight">تاریخ شمسی در سرور:&nbsp;<label class="text-bold"><?php echo $solarDate; ?></label></label>
    <br/>
    <label class="text-ultralight">نسخه سرور:&nbsp;<label class="text-bold"><?php echo $vnumber[0]; ?></label>&nbsp;و کاندید انتشار:&nbsp;<label class="text-bold"><?php echo str_replace("_", "/", str_replace("rc", "", $vnumber[1])); ?></label></label>
    <br/>
    <br/>
    <input type="text" name="userInput" class="eighty_width"/>
    <button type="button" class="add" id="addUserInput">&nbsp;+&nbsp;</button>
  </body>
</html>