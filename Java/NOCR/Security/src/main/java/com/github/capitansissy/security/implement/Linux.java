package com.github.capitansissy.security.implement;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.*;

public class Linux {
  private static String serialNumber = null;

  @NotNull
  @Contract("_ -> new")
  private static BufferedReader read(@NotNull String command) throws IOException {
    OutputStream outputStream;
    InputStream inputStream;
    Process process;

    Runtime runtime = Runtime.getRuntime();
    process = runtime.exec(command.split(" "));
    outputStream = process.getOutputStream();
    inputStream = process.getInputStream();
    outputStream.close();
    return new BufferedReader(new InputStreamReader(inputStream));
  }

  private static void readDmidecode() throws IOException {
    String line;
    String marker = "Serial Number:";
    BufferedReader bufferedReader;

    bufferedReader = read("dmidecode -t system");
    while ((line = bufferedReader.readLine()) != null) {
      if (line.indexOf(marker) != -1) {
        serialNumber = line.split(marker)[1].trim();
        break;
      }
    }
    if (bufferedReader != null) {
      bufferedReader.close();
    }
  }

  private static void readLshal() throws IOException {
    String line;
    String marker = "system.hardware.serial =";
    BufferedReader bufferedReader;

    bufferedReader = read("lshal");
    while ((line = bufferedReader.readLine()) != null) {
      if (line.indexOf(marker) != -1) {
        serialNumber = line.split(marker)[1].replaceAll("\\(string\\)|(\\')", "").trim();
        break;
      }
    }
    if (bufferedReader != null) {
      bufferedReader.close();
    }
  }

  public static String getSerialNumber() {
    try {
      if (serialNumber == null) {
        readDmidecode();
      }
      if (serialNumber == null) {
        readLshal();
      }
    } catch (Exception exception) {
      throw new RuntimeException("Cannot find computer SN");
    }
    return serialNumber;
  }

}