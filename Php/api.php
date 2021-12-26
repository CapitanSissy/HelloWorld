<?php
  $POST = array_filter($_POST);
  if(!empty($POST)) {
    /*=-=-=-=-=-[ addUserInput ]-=-=-=-=-=*/
    if(isset($POST['GetInput'])) {
      echo addUserInput($POST['GetInput']['arg0']);
    }
    /*=-=-=-=-=-[ NO ACCESS ]-=-=-=-=-=*/
    else {
      echo $language['NO_ACCESS'];
    }
    
  }
