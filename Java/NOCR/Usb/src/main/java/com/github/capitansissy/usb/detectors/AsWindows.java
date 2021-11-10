package com.github.capitansissy.usb.detectors;

import com.github.capitansissy.usb.UsbDevice;
import com.github.capitansissy.usb.process.CommandExecutor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class AsWindows extends AStorageDevice {
  private static final String WMIC_PATH = System.getenv("WINDIR") + "\\System32\\wbem\\wmic.exe";
  private static final String CMD_WMI_ARGS = "logicaldisk where drivetype=2 get DeviceID,VolumeSerialNumber,VolumeName";
  private static final String CMD_WMI_USB = WMIC_PATH + " " + CMD_WMI_ARGS;

  AsWindows() {
    super();
  }

  @Override
  public List<UsbDevice> getStorageDevices() {
    final ArrayList<UsbDevice> listDevices = new ArrayList<>();
    try (CommandExecutor commandExecutor = new CommandExecutor(CMD_WMI_USB)) {
      commandExecutor.processOutput(outputLine -> {
        final String[] parts = outputLine.split(" ");
        if (parts.length > 1 && !parts[0].isEmpty() && !parts[0].equals("DeviceID") && !parts[0].equals(parts[parts.length - 1])) {
          final String rootPath = parts[0] + File.separatorChar;
          final String uuid = parts[parts.length - 1];
          String volumeName = parseVolumeName(parts);
          if (volumeName.isEmpty()) {
            volumeName = getDeviceName(rootPath);
          }
          getUSBDevice(rootPath, volumeName, rootPath, uuid)
            .ifPresent(listDevices::add);
        }
      });
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }
    return listDevices;
  }

  @NotNull
  private String parseVolumeName(@NotNull String[] parts) {
    StringBuilder volumeLabel = new StringBuilder();
    for (int i = 1; i < parts.length - 1; i++) {
      if (!parts[i].isEmpty()) {
        if (volumeLabel.length() > 0) {
          volumeLabel.append(" ");
        }
        volumeLabel.append(parts[i]);
      }
    }
    return volumeLabel.toString();
  }

  private String getDeviceName(final String rootPath) {
    final File f = new File(rootPath);
    final FileSystemView v = FileSystemView.getFileSystemView();
    String name = v.getSystemDisplayName(f);
    if (name != null) {
      int idx = name.lastIndexOf('(');
      if (idx != -1) {
        name = name.substring(0, idx);
      }
      name = name.trim();
      if (name.isEmpty()) {
        name = null;
      }
    }
    return name;
  }
}
