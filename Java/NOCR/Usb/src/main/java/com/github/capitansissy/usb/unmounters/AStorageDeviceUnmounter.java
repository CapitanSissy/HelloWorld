package com.github.capitansissy.usb.unmounters;

import com.github.capitansissy.constants.Tools;
import com.github.capitansissy.usb.UsbDevice;

import java.io.IOException;

public abstract class AStorageDeviceUnmounter {
  private static AStorageDeviceUnmounter instance;

  public static synchronized AStorageDeviceUnmounter getInstance() {
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

  public abstract void unmount(UsbDevice usbDevice) throws IOException;

}
