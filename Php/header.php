<?php
  header("X-XSS-Protection: 1; mode=block");
  header("Strict-Transport-Security: max-age=31536000; includeSubDomains; preload");
  header("X-Frame-Options: DENY");
  header("X-Content-Type-Options: nosniff");
  header("Content-Security-Policy: style-src 'self' 'unsafe-inline'; style-src-elem 'self' 'unsafe-inline' https://kit.fontawesome.com/7524e05e75.js; script-src 'nonce-sha256-aGVsbG93b3JsZA==' https://cdnjs.cloudflare.com/ajax/libs/jquery/3.4.1/jquery.min.js https://kit.fontawesome.com/7524e05e75.js; script-src-elem 'self' 'unsafe-inline' https://api.capitansissy.cw/assets/js/*.js https://cdnjs.cloudflare.com/ajax/libs/jquery/3.4.1/jquery.min.js https://kit.fontawesome.com/7524e05e75.js; connect-src 'self' https://*.fontawesome.com; frame-ancestors 'none'; img-src 'self' https://img.shields.io; object-src 'none'; font-src 'self' https://*.fontawesome.com; base-uri 'none';");
  header("X-Permitted-Cross-Domain-Policies: none");  
  header("Referrer-Policy: strict-origin");
  header("Feature-Policy: microphone 'none'; camera 'none'");
  header('X-Powered-By: IIS 8.5');  
