package com.github.capitansissy.messages;

import com.github.capitansissy.constants.Defaults;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;

public class ResourceAsStream implements Serializable {
  private String property;
  private Locale locale;

  public ResourceAsStream() {
    super();
  }

  public ResourceAsStream(String property) {
    super();
    this.property = property;
    this.locale = null;
  }

  public ResourceAsStream(String property, Locale locale) {
    super();
    this.property = property;
    this.locale = locale;
  }

  public ResourceAsStream(Locale locale) {
    this.locale = locale;
  }

  public String get(String property) {
    return get(property, this.locale);
  }

  @NotNull
  private String get(String property, Locale locale) {
    try {
      if (locale == null) {
        return new String(ResourceBundle.getBundle("messages").getString(property).getBytes(Defaults.CharsetName.FROM), Defaults.CharsetName.TO);
      } else {
        return new String(ResourceBundle.getBundle("messages", locale).getString(property).getBytes(Defaults.CharsetName.FROM), Defaults.CharsetName.TO);
      }
    } catch (Exception e) {
      return Defaults.Slugs.None;
    }
  }

  @Override
  public String toString() {
    if (property != null) {
      return get(property, locale);
    } else {
      return Defaults.Slugs.None;
    }
  }

}
