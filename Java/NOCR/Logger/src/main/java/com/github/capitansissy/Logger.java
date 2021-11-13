package com.github.capitansissy;

public class Logger {
  private org.apache.log4j.Logger logger;

  public Logger() {
    logger = org.apache.log4j.Logger.getLogger(Logger.class);
  }

  public Logger(Class<?> clazz) {
    logger = org.apache.log4j.Logger.getLogger(clazz);
  }

  public void setLog(String Message, int Type) {
    switch (Type) {
      case 0:  // Default.Log4J.INFO
        logger.info(Message);
        break;
      case 1:  // Default.Log4J.DEBUG
        logger.debug(Message);
        break;
      case 2:  // Default.Log4J.ERROR
        logger.error(Message);
        break;
      case 3:  // Default.Log4J.WARNING
        logger.warn(Message);
        break;
      case 4:  // Default.Log4J.TRACE
        logger.trace(Message);
        break;
      default: // Default.Log4J.FATAL
        logger.fatal(Message);
        break;
    }
  }

}