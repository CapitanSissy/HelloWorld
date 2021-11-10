package com.github.capitansissy.usb.detectors;

import lombok.Data;

@Data
class DiskInfo {
  private String device;
  private String mountPoint;
  private String name;
  private String uuid;
  private boolean isUSB;

  DiskInfo(final String device) {
    this.device = device;
    this.mountPoint = "";
    this.name = "";
    this.uuid = "";
    this.isUSB = false;
  }
}
