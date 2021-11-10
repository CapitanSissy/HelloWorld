package com.github.capitansissy.service;

import com.github.capitansissy.usb.UsbDetector;
import com.github.capitansissy.usb.UsbDevice;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Launcher {
  @NotNull
  @Contract(pure = true)
  private static String companyName() {
    return "yWorks";
  }

  public static void main(String[] args) throws IOException {
    List<Integer> integers = new ArrayList<>();
    integers.add(1);
    integers.add(2);
    integers.add(3);
    Functional<Integer> functional = new Functional<>();
    assert (functional.sum(integers) == 6);

    System.out.println(String.format("Hello %s", companyName()));

    UsbDetector usbDetector = new UsbDetector();
    List<UsbDevice> removableDevices = usbDetector.getRemovableDevices();
    if (removableDevices.size() > 0) {
      for (UsbDevice usbDevice : removableDevices) {
        System.out.println(usbDevice.getUuid().toLowerCase());
      }
    }

    // Display all the USB storage devices currently connected
//    usbDetector.getRemovableDevices().forEach(System.out::println);

    // Add an event listener to be notified when an USB storage device is connected or removed
//    usbDetector.addDriveListener(System.out::println);
//    usbDetector.removeDriveListener(System.out::println);

    // Unmount a device
//    usbDetector.unmountStorageDevice(usbDetector.getRemovableDevices().get(0));

    System.out.println();

  }
}
