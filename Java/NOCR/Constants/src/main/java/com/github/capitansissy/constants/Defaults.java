package com.github.capitansissy.constants;

import com.github.capitansissy.security.AES;

import java.io.Serializable;

public class Defaults implements Serializable {
  public static final String PUBLIC_DATABASE_KEY = "8L%TQtBI1$9V+CJu0#xQtL%n!Eg3p27dO@*v11lFGlE2stTJdRVwe8J3Iyn-MMohiP6-hpFPHnJX*u6oP8JoqlFRtCMno?yP&HsO6H#j_yN-xle&n$izy8Fp8r0vR%QNoT6J%1+d71&PfFYi9Tyj=Rcm_bH9S=8U2yWf4tSqdsuv$J$V1QHh8G7AHrS-jSemJ#bA&Nz@Tvyi!7#J$MINUTES_AGO_CHANGED#cJ7YvHgTLo&JE4VhpMKyFoBd*s4@cBh7F99YLOwv!AS-q";
  public static final String INTERNAL_SECURITY_KEY = "036^VX@RC";

  public static final String TARGET_NAME_SPACE = "https://helloworld.apachat.com";

  public static final String DEVICE_ID = "d80008c4";
  public static final String CHARSET = "UTF-8";
  public static final String MESSAGE_CONTEXT_KEY = "com.sun.xml.internal.ws.http.exchange";

  static final String OS_NAME = System.getProperty("os.name");
  static final String REGULAR_EXPRESSION_FOR_IP_ADDRESS = "\\d{1,3}(?:\\.\\d{1,3}){3}(?::\\d{1,5})?";

  public static final String C3P0_DATASOURCE_JDBCURL = "jdbc:mysql://%1$s:%2$s/%3$s?useSSL=%4$s&useUnicode=true&characterEncoding=UTF-8&characterSetResults=UTF-8";
  public static final String DEFAULT_SCHEMA = AES.decrypt(Tools.getResourceValue("db", "project.database.default.schema"), INTERNAL_SECURITY_KEY);
  public static final String DATA_SCHEMA = String.format("%1$s_data", DEFAULT_SCHEMA);
  public static final String LOG_SCHEMA = String.format("%1$s_log", DEFAULT_SCHEMA);

  public static final boolean DEBUG_MODE = Boolean.parseBoolean(AES.decrypt(Tools.getResourceValue("structure", "debug.mode"), INTERNAL_SECURITY_KEY));

  static class Text {
    static final String REGULAR_EXPRESSION_FOR_UNREFINED_TEXT = "[^a-zA-Z0-9\\u0020\\u200C\\u005F\\p{InArabic}&&\\PN]";
  }

  public static class MediaType {
    public static final String TEXT_HTML = "text/html; charset=UTF-8";
  }

  public static class CharsetName {
    public static final String FROM = "ISO-8859-1";
    public static final String TO = "UTF-8";
  }

  public static class Slugs {
    public static final String None = "";
    public static final String Slash = "/";
    public static final String Underscore = "_";
    public static final String Space = " ";
  }

  public static class Log4J {
    public static final int INFO = 0;
    public static final int DEBUG = 1;
    public static final int ERROR = 2;
    public static final int WARNING = 3;
    public static final int TRACE = 4;
    public static final int FATAL = 5;
  }

  public static class DateTime {
    static final String NORMAL = "yyyy-MM-dd HH:mm:ss";
    public static final String UNDERSCORE = "yyyy-MM-dd_HHmmss";
    public static final String MONITORING = "HH:mm:ss:SSS";
    static final String TIME = "HH:mm:ss";
  }

  public static class URL {
    public static final String PROTOCOL = "TLS";
    public static final String SLD = "localhost";
    public static final String TLD = "";
    public static final String DIRECTORY = "fa/v1";

    public static class LOCATIONS {
      public static final String HELLOWORLD = "helloworld/";
    }

    public static class PORTS {
      public static final String SOAP_GENERAL = "8081";
      public static final String RESTFUL_GENERAL = "8082";
    }
  }

  public static class SSL {
    public static final String KEYSTORE_FILE_PATH = AES.decrypt(Tools.getResourceValue("structure", "keystore.file.path"), INTERNAL_SECURITY_KEY);
    public static final String KEYSTORE_PASSWORD = AES.decrypt(Tools.getResourceValue("structure", "keystore.password"), INTERNAL_SECURITY_KEY);
    public static final String TRUSTSTORE_FILE_PATH = AES.decrypt(Tools.getResourceValue("structure", "truststore.file.path"), INTERNAL_SECURITY_KEY);
    public static final String TRUSTSTORE_PASSWORD = AES.decrypt(Tools.getResourceValue("structure", "truststore.password"), INTERNAL_SECURITY_KEY);
  }

  public static class Tables {
    public static final String TBL_CONNECTION_PARAMETERS = "tbl_connection_parameters";
  }


}
