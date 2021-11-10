package com.github.capitansissy.usb;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.File;

@Getter
@ToString
@EqualsAndHashCode
public class UsbDevice {
  private final File rootDirectory;
  private final String deviceName;
  private final String device;
  private final String uuid;

  public UsbDevice(final File rootDirectory, String deviceName, final String device, final String uuid) {
    if (rootDirectory == null || !rootDirectory.isDirectory()) {
      throw new IllegalArgumentException("Invalid root file!");
    }
    this.rootDirectory = rootDirectory;
    if (deviceName == null || deviceName.isEmpty()) {
      deviceName = rootDirectory.getName();
    }
    this.device = device;
    this.deviceName = deviceName;
    this.uuid = uuid;
  }

  public boolean canRead() {
    return rootDirectory.canRead();
  }

  public boolean canWrite() {
    return rootDirectory.canWrite();
  }

  public boolean canExecute() {
    return rootDirectory.canExecute();
  }

  public String getSystemDisplayName() {
    String driveLetter = rootDirectory.getPath();
    if (driveLetter.endsWith("\\")) {
      driveLetter = driveLetter.substring(0, driveLetter.length() - 1);
    }
    return deviceName + " (" + driveLetter + ")";
  }

}
