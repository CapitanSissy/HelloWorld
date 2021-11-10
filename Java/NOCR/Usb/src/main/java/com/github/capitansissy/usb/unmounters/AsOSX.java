package com.github.capitansissy.usb.unmounters;

import com.github.capitansissy.usb.UsbDevice;
import org.jetbrains.annotations.NotNull;

public class AsOSX extends AsLinuxAndOSX {
  @Override
  public void unmount(@NotNull final UsbDevice usbDevice) {
    unmount("diskutil unmountDisk " + usbDevice.getDevice());
    unmount("sudo diskutil unmountDisk " + usbDevice.getDevice());
  }
}
