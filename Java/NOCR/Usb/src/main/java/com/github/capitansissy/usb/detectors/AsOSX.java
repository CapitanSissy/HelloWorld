package com.github.capitansissy.usb.detectors;

import com.github.capitansissy.constants.Tools;
import com.github.capitansissy.usb.UsbDevice;
import com.github.capitansissy.usb.process.CommandExecutor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class AsOSX extends AStorageDevice {
  private static final String CMD_SYSTEM_PROFILER_USB = "system_profiler SPUSBDataType";
  private static final Pattern macOSXPattern_MOUNT = Pattern.compile("^.*Mount Point: (.+)$");

  private static final String CMD_DF = "df -l";
  private static final String CMD_DISKUTIL = "diskutil info ";

  private static final String DISK_PREFIX = "/dev/disk";

  private static final String INFO_MOUNTPOINT = "Mount Point";
  private static final String INFO_PROTOCOL = "Protocol";
  private static final String INFO_USB = "USB";
  private static final String INFO_NAME = "Volume Name";
  private static final String INFO_UUID = "Volume UUID";

  private static final int MACOSX_MOUNTAINLION = 8;

  private int macosVersion = -1;

  AsOSX() {
    super();
    final String version = Tools.getOsVersion();
    final String[] versionParts = version.split("\\.");
    if (versionParts.length > 1) {
      try {
        macosVersion = Integer.parseInt(versionParts[1]);
      } catch (NumberFormatException nfe) {
        log.error(nfe.getMessage(), nfe);
      }
    }

  }


  @Override
  public List<UsbDevice> getStorageDevices() {
    final ArrayList<UsbDevice> listDevices = new ArrayList<>();
    if (macosVersion >= MACOSX_MOUNTAINLION) {
      try (final CommandExecutor commandExecutor = new CommandExecutor(CMD_DF)) {
        commandExecutor.processOutput((String outputLine) -> {
          final String[] parts = outputLine.split("\\s");
          final String device = parts[0];
          if (device.startsWith(DISK_PREFIX)) {
            final DiskInfo disk = getDiskInfo(device);
            if (disk.isUSB()) {
              getUSBDevice(disk.getMountPoint(), disk.getName(), disk.getDevice(), disk.getUuid())
                .ifPresent(listDevices::add);
            }
          }
        });
      } catch (IOException e) {
        log.error(e.getMessage(), e);
      }
    } else {
      try (final CommandExecutor commandExecutor = new CommandExecutor(CMD_SYSTEM_PROFILER_USB)) {
        commandExecutor.processOutput(outputLine -> {
          final Matcher matcher = macOSXPattern_MOUNT.matcher(outputLine);
          if (matcher.matches()) {
            getUSBDevice(matcher.group(1))
              .ifPresent(listDevices::add);
          }
        });
      } catch (IOException e) {
        log.error(e.getMessage(), e);
      }
    }
    return listDevices;
  }


  private DiskInfo getDiskInfo(final String device) {
    final DiskInfo disk = new DiskInfo(device);
    final String command = CMD_DISKUTIL + disk.getDevice();
    try (final CommandExecutor commandExecutor = new CommandExecutor(command)) {
      commandExecutor.processOutput(outputLine -> {
        final String[] parts = outputLine.split(":");
        if (parts.length > 1) {
          switch (parts[0].trim()) {
            case INFO_MOUNTPOINT:
              disk.setMountPoint(parts[1].trim());
              break;
            case INFO_PROTOCOL:
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
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }
    return disk;
  }
}
