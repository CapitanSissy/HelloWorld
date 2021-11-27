package com.github.capitansissy.encapsulation;

import java.io.Serializable;
import java.util.Locale;

public class Language implements Serializable {
  private Locale locale;
  private String dir;
  private String lang;

  public Language(Locale locale, String dir, String lang) {
    this.locale = locale;
    this.dir = dir;
    this.lang = lang;
  }

  public Locale getLocale() {
    return locale;
  }

  public String getDir() {
    return dir;
  }

  public String getLang() {
    return lang;
  }

}
