package com.github.capitansissy.usb.events;

@FunctionalInterface
public interface UsbListener {
  void usbDriveEvent(UsbEvent event);
}
