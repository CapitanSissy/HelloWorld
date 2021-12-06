package com.github.capitansissy.constants;

import com.github.capitansissy.encapsulation.Language;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.capitansissy.constants.Defaults.OS_NAME;
import static com.github.capitansissy.constants.OSType.*;


public class Tools implements Serializable {
  private final static Pattern debugPattern = Pattern.compile("-Xdebug|jdwp");

  public static boolean isDebugging() {
    for (String arg : ManagementFactory.getRuntimeMXBean().getInputArguments()) {
      if (debugPattern.matcher(arg).find()) {
        return true;
      }
    }
    return false;
  }

  public static OSType getOsType() {
    String osNameLower = OS_NAME.toLowerCase();
    if (osNameLower.startsWith("win")) {
      return WINDOWS;
    }
    if (osNameLower.startsWith("linux")) {
      return LINUX;
    }
    if (osNameLower.startsWith("mac")) {
      return MAC_OS;
    }
    throw new UnsupportedOperationException("Your Operative System (" + osNameLower + ") is not supported!");
  }

  public static String getOsVersion() {
    return System.getProperty("os.version");
  }

  @NotNull
  public static String getResourceValue(String PropertyFile, String Key) {
    return ResourceBundle.getBundle(PropertyFile).getString(Key);
  }

  @NotNull
  public static String toEN(@NotNull String numbers) {
    return numbers.replaceAll("۰", "0").
      replaceAll("۱", "1").
      replaceAll("۲", "2").
      replaceAll("۳", "3").
      replaceAll("۴", "4").
      replaceAll("۵", "5").
      replaceAll("۶", "6").
      replaceAll("۷", "7").
      replaceAll("۸", "8").
      replaceAll("۹", "9").
      replaceAll("٠", "0").
      replaceAll("١", "1").
      replaceAll("٢", "2").
      replaceAll("٣", "3").
      replaceAll("٤", "4").
      replaceAll("٥", "5").
      replaceAll("٦", "6").
      replaceAll("٧", "7").
      replaceAll("٨", "8").
      replaceAll("٩", "9");
  }

  @NotNull
  public static String toFA(@NotNull String numbers) {
    return numbers.replaceAll("0", "۰").
      replaceAll("1", "۱").
      replaceAll("2", "۲").
      replaceAll("3", "۳").
      replaceAll("4", "۴").
      replaceAll("5", "۵").
      replaceAll("6", "۶").
      replaceAll("7", "۷").
      replaceAll("8", "۸").
      replaceAll("9", "۹");
  }

  @NotNull
  public static String toAr(@NotNull String numbers) {
    return numbers.replaceAll("0", "٠").
      replaceAll("1", "١").
      replaceAll("2", "٢").
      replaceAll("3", "٣").
      replaceAll("4", "٤").
      replaceAll("5", "٥").
      replaceAll("6", "٦").
      replaceAll("7", "٧").
      replaceAll("8", "٨").
      replaceAll("9", "٩");
  }

  @NotNull
  public static String getText(@NotNull String unrefinedText) {
    return unrefinedText.trim().replaceAll(Defaults.Text.REGULAR_EXPRESSION_FOR_UNREFINED_TEXT, Defaults.Slugs.None);
  }

  public static Language getDefaultLanguage(int languageCode) {
    switch (languageCode) {
      case 0:
        return new Language(new Locale("fa", "IR"), "rtl", "fa");
      case 1:
        return new Language(new Locale("ar", "AE"), "rtl", "ar");
      default:
        return new Language(new Locale("en", "US"), "ltr", "en");
    }
  }

  public static String getIPAddress(String ip) {
    String tempIP = Defaults.Slugs.None;
    Pattern pattern = Pattern.compile(Defaults.REGULAR_EXPRESSION_FOR_IP_ADDRESS);
    Matcher matcher = pattern.matcher(ip);
    while (matcher.find()) {
      tempIP = matcher.group();
    }
    return tempIP;
  }

}
