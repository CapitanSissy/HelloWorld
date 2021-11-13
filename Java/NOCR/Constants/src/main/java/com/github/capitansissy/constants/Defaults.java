package com.github.capitansissy.constants;

import java.io.Serializable;

public class Defaults implements Serializable {
  public static final String INTERNAL_SECURITY_KEY = "036^VX@RC";

  public static final String TARGET_NAME_SPACE = "https://helloworld.apachat.com";

  public static final String DEVICE_ID = "d80008c4";
  public static final String CHARSET = "UTF-8";
  static final String OS_NAME = System.getProperty("os.name");

  public static class Slugs {
    public static final String None = "";
    public static final String Slash = "/";
    public static final String Underscore = "_";

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
    public static final String PROTOCOL = "http";
    public static final String SLD = "localhost";
    public static final String TLD = "";
    public static final String DIRECTORY = "fa/v1";

    public static class LOCATIONS {
      public static final String HELLOWORLD = "helloworld/";
    }

    public static class PORTS {
      public static final String GENERAL = "8081";
    }
  }

}
