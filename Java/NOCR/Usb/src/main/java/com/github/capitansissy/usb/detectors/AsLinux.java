package com.github.capitansissy.usb.detectors;

import com.github.capitansissy.usb.UsbDevice;
import com.github.capitansissy.usb.process.CommandExecutor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class AsLinux extends AStorageDevice {

  private static final String CMD_DF = "df -l";
  private static final Pattern command1Pattern = Pattern.compile("^(\\/[^ ]+)[^%]+%[ ]+(.+)$");

  private static final String CMD_CHECK_USB = "udevadm info -q property -n ";

  private static final String INFO_BUS = "ID_BUS";
  private static final String INFO_USB = "usb";
  private static final String INFO_NAME = "ID_FS_LABEL";
  private static final String INFO_UUID = "ID_FS_UUID";

  private static final String DISK_PREFIX = "/dev/";

  AsLinux() {
    super();
  }

  private void readDiskInfo(@NotNull final DiskInfo disk) {
    final String command = CMD_CHECK_USB + disk.getDevice();
    try (final CommandExecutor commandExecutor = new CommandExecutor(command)) {
      commandExecutor.processOutput(outputLine -> {
        final String[] parts = outputLine.split("=");
        if (parts.length > 1) {
          switch (parts[0].trim()) {
            case INFO_BUS:
              disk.setUSB(INFO_USB.equals(parts[1].trim()));
              break;
            case INFO_NAME:
              disk.setName(parts[1].trim());
              break;
            case INFO_UUID:
              disk.setUuid(parts[1].trim());
              break;
          }
        }
      });
    } catch (final IOException e) {
      log.error(e.getMessage(), e);
    }
  }

  @Override
  public List<UsbDevice> getStorageDevices() {
    final ArrayList<UsbDevice> listDevices = new ArrayList<>();
    try (final CommandExecutor commandExecutor = new CommandExecutor(CMD_DF)) {
      commandExecutor.processOutput((String outputLine) -> {
        final Matcher matcher = command1Pattern.matcher(outputLine);
        if (matcher.matches()) {
          final String device = matcher.group(1);
          final String rootPath = matcher.group(2);
          if (device.startsWith(DISK_PREFIX)) {
            final DiskInfo disk = new DiskInfo(device);
            disk.setMountPoint(rootPath);
            readDiskInfo(disk);
            if (disk.isUSB()) {
              getUSBDevice(disk.getMountPoint(), disk.getName(), disk.getDevice(), disk.getUuid())
                .ifPresent(listDevices::add);
            }
          }
        }
      });
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }
    return listDevices;
  }
}