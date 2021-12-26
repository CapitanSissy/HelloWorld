<?php
  header("Content-Security-Policy: default-src 'self' script-src 'self' https://* 'unsafe-inline' 'unsafe-eval'; connect-src 'self' img-src * 'self'");
  header("Referrer-Policy: strict-origin");
  header('X-Frame-Options: DENY');
  header('X-XSS-Protection: 1; mode=block');
  header('X-Content-Type-Options: nosniff');  
  header('X-Permitted-Cross-Domain-Policies: none');  
  header('X-Powered-By: IIS 8.5');  


