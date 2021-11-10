package com.github.capitansissy.usb.unmounters;

import com.github.capitansissy.usb.UsbDevice;
import com.github.tuupertunut.powershelllibjava.PowerShell;
import com.github.tuupertunut.powershelllibjava.PowerShellExecutionException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class AsWindows extends AStorageDeviceUnmounter {
  private static final String CMD_UNMOUNT_1 = "$ErrorActionPreference = 'SilentlyContinue'";
  private static final String CMD_UNMOUNT_2 = "(New-Object -comObject Shell.Application).Namespace(17).ParseName('X:').InvokeVerb('Eject')";

  @Override
  public void unmount(@NotNull UsbDevice usbDevice) throws IOException {
    try (PowerShell psSession = PowerShell.open()) {
      psSession.executeCommands(CMD_UNMOUNT_1, CMD_UNMOUNT_2.replace("X:", usbDevice.getDevice()));
      Thread.sleep(1000L);
    } catch (PowerShellExecutionException | InterruptedException e) {
      throw new IOException(e);
    }
  }
}
