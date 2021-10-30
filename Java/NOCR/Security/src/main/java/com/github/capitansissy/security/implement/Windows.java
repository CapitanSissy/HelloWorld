package com.github.capitansissy.security.implement;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

public class Windows {
  private static String serialNumber = null;

  public static String getSerialNumber() {
    OutputStream outputStream;
    InputStream inputStream;
    Runtime runtime = Runtime.getRuntime();
    Process process;
    if (serialNumber != null) {
      return serialNumber;
    }
    try {
      process = runtime.exec(new String[]{"wmic", "bios", "get", "serialnumber"});
      outputStream = process.getOutputStream();
      inputStream = process.getInputStream();
      outputStream.close();
      Scanner scanner = new Scanner(inputStream);
      while (scanner.hasNext()) {
        String next = scanner.next();
        if ("SerialNumber".equals(next)) {
          serialNumber = scanner.next().trim();
          break;
        }
      }
      inputStream.close();
      if (serialNumber == null) {
        throw new RuntimeException("Cannot find computer SN");
      }
    } catch (Exception exception) {
      exception.printStackTrace();
    }
    return serialNumber;
  }

}