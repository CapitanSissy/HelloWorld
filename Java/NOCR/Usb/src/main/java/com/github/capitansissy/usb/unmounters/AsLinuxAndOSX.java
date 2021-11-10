package com.github.capitansissy.usb.unmounters;

import com.github.capitansissy.usb.process.CommandExecutor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
abstract class AsLinuxAndOSX extends AStorageDeviceUnmounter {
  void unmount(String unmountCommand) {
    try (CommandExecutor commandExecutor = new CommandExecutor(unmountCommand)) {
      Thread.sleep(1000L);
      commandExecutor.processOutput(log::trace);
      log.debug("Device successfully unmount: {}", unmountCommand);
    } catch (IOException | InterruptedException e) {
      log.error("Unable to unmount device: {}", unmountCommand, e);
    }
  }
}
