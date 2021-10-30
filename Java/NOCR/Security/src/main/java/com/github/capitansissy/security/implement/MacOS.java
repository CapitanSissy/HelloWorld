package com.github.capitansissy.security.implement;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class MacOS {
  private static String serialNumber = null;

  public static String getSerialNumber() {
    OutputStream outputStream;
    InputStream inputStream;
    Process process;
    Runtime runtime = Runtime.getRuntime();

    if (serialNumber != null) {
      return serialNumber;
    }

    try {
      process = runtime.exec(new String[]{"/usr/sbin/system_profiler", "SPHardwareDataType"});
      outputStream = process.getOutputStream();
      inputStream = process.getInputStream();
      outputStream.close();
      BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
      String line;
      String marker = "Serial Number";
      while ((line = br.readLine()) != null) {
        if (line.contains(marker)) {
          serialNumber = line.split(":")[1].trim();
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