package com.github.capitansissy.usb.detectors;

import com.github.capitansissy.constants.Tools;
import com.github.capitansissy.usb.UsbDevice;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Slf4j
public abstract class AStorageDevice {
  private static AStorageDevice instance;

  public static synchronized AStorageDevice getInstance() {
    if (instance == null) {
      switch (Tools.getOsType()) {
        case WINDOWS:
          instance = new AsWindows();
          break;

        case LINUX:
          instance = new AsLinux();
          break;

        case MAC_OS:
          instance = new AsOSX();
          break;
      }
    }
    return instance;
  }

  AStorageDevice() {
  }

  public abstract List<UsbDevice> getStorageDevices();

  static Optional<UsbDevice> getUSBDevice(final String rootPath) {
    return getUSBDevice(rootPath, null, null, null);
  }

  static Optional<UsbDevice> getUSBDevice(final String rootPath, final String deviceName, final String device, final String uuid) {
    final File root = new File(rootPath);
    if (!root.isDirectory()) {
      log.trace("Invalid root found: {}", root);
      return Optional.empty();
    }
    log.trace("Device found: {}", root.getPath());
    try {
      return Optional.of(new UsbDevice(root, deviceName, device, uuid));
    } catch (IllegalArgumentException e) {
      log.debug("Could not add Device: {}", e.getMessage(), e);
    }
    return Optional.empty();
  }

}
