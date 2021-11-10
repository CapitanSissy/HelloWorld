package com.github.capitansissy.usb.unmounters;

import com.github.capitansissy.usb.UsbDevice;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class AsLinux extends AsLinuxAndOSX {
  @Override
  public void unmount(@NotNull UsbDevice usbDevice) throws IOException {
    unmount("umount " + usbDevice.getDevice());
    unmount("sudo umount " + usbDevice.getDevice());
  }

}
