package com.github.capitansissy.usb.events;

import com.github.capitansissy.enumeration.DeviceEventType;
import com.github.capitansissy.usb.UsbDevice;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class UsbEvent {
  private final UsbDevice storageDevice;
  private final DeviceEventType eventType;
}
