package com.github.capitansissy.service;

import com.github.capitansissy.constants.Defaults;
import com.github.capitansissy.usb.UsbDetector;
import com.github.capitansissy.usb.UsbDevice;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

public class Launcher {
  private static boolean IS_ATTACHED = false;

  @NotNull
  @Contract(pure = true)
  private static String companyName() {
    return "NOCR";
  }

  private static void run() {
    System.out.println(String.format("Hello %s", companyName()));
  }

  public static void main(String[] args) throws IOException {
    UsbDetector usbDetector = new UsbDetector();
    List<UsbDevice> removableDevices = usbDetector.getRemovableDevices();
    if (removableDevices.size() > 0) {
      for (UsbDevice usbDevice : removableDevices) {
        if (usbDevice.getUuid().toLowerCase().equals(Defaults.DEVICE_ID)) {
          IS_ATTACHED = true;
        }
      }
    }

    if (IS_ATTACHED) {
      run();
    } else {
      System.out.println("Hardware key not detected");
    }

    // Display all the USB storage devices currently connected
    // usbDetector.getRemovableDevices().forEach(System.out::println);

    // Add an event listener to be notified when an USB storage device is connected or removed
    // usbDetector.addDriveListener(System.out::println);
    // usbDetector.removeDriveListener(System.out::println);

    // Unmount a device
    // usbDetector.unmountStorageDevice(usbDetector.getRemovableDevices().get(0));
  }
}
