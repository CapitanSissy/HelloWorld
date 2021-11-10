package com.github.capitansissy.usb;

import com.github.capitansissy.enumeration.DeviceEventType;
import com.github.capitansissy.usb.detectors.AStorageDevice;
import com.github.capitansissy.usb.events.UsbListener;
import com.github.capitansissy.usb.events.UsbEvent;
import com.github.capitansissy.usb.unmounters.AStorageDeviceUnmounter;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
public class UsbDetector implements Closeable {
  private static final long DEFAULT_POLLING_INTERVAL_MILLIS = 5000;

  private final Set<UsbDevice> connectedDevices;
  private final List<UsbListener> listeners;

  private long currentPollingInterval;
  private final ScheduledExecutorService taskExecutor;
  private ScheduledFuture<?> listenerTaskFuture;

  public UsbDetector() {
    this(DEFAULT_POLLING_INTERVAL_MILLIS);
  }

  private UsbDetector(final long pollingInterval) {
    listeners = new ArrayList<>();
    connectedDevices = new HashSet<>();
    currentPollingInterval = pollingInterval;
    taskExecutor = Executors.newSingleThreadScheduledExecutor();
  }

  public synchronized void setPollingInterval(final long pollingInterval) {
    if (pollingInterval <= 0) {
      throw new IllegalArgumentException("'pollingInterval' must be greater than 0");
    }
    currentPollingInterval = pollingInterval;
    if (listeners.size() > 0) {
      stop();
      start();
    }
  }

  private synchronized void start() {
    if (listenerTaskFuture == null || listenerTaskFuture.isDone()) {
      listenerTaskFuture = taskExecutor.scheduleAtFixedRate(new ListenerTask(), 0, currentPollingInterval, TimeUnit.MILLISECONDS);
    }
  }

  private synchronized void stop() {
    if (listenerTaskFuture != null) {
      listenerTaskFuture.cancel(false);
    }
  }

  public synchronized boolean addDriveListener(final UsbListener listener) {
    if (listeners.contains(listener)) {
      return false;
    }
    listeners.add(listener);
    start();
    return true;
  }

  public synchronized boolean removeDriveListener(final UsbListener listener) {
    final boolean removed = listeners.remove(listener);
    if (listeners.isEmpty()) {
      stop();
    }
    return removed;
  }

  public List<UsbDevice> getRemovableDevices() {
    return AStorageDevice.getInstance().getStorageDevices();
  }

  public void unmountStorageDevice(UsbDevice usbDevice) throws IOException {
    AStorageDeviceUnmounter.getInstance().unmount(usbDevice);
  }

  private void updateConnectedDevices(final List<UsbDevice> currentConnectedDevices) {
    final List<UsbDevice> removedDevices = new ArrayList<>();
    synchronized (this) {
      final Iterator<UsbDevice> itConnectedDevices = connectedDevices.iterator();
      while (itConnectedDevices.hasNext()) {
        final UsbDevice device = itConnectedDevices.next();
        if (currentConnectedDevices.contains(device)) {
          currentConnectedDevices.remove(device);
        } else {
          removedDevices.add(device);
          itConnectedDevices.remove();
        }
      }
      connectedDevices.addAll(currentConnectedDevices);
    }

    currentConnectedDevices.forEach(device ->
      sendEventToListeners(new UsbEvent(device, DeviceEventType.CONNECTED)));

    removedDevices.forEach(device ->
      sendEventToListeners(new UsbEvent(device, DeviceEventType.REMOVED)));
  }

  private void sendEventToListeners(final UsbEvent event) {
    final List<UsbListener> listenersCopy;
    synchronized (listeners) {
      listenersCopy = new ArrayList<>(listeners);
    }

    for (UsbListener listener : listenersCopy) {
      try {
        listener.usbDriveEvent(event);
      } catch (Exception ex) {
        log.error("An UsbListener threw an exception", ex);
      }
    }
  }

  @Override
  public void close() throws IOException {
    taskExecutor.shutdown();
    try {
      taskExecutor.awaitTermination(5, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      log.warn("Unable to wait for taskExecutor termination", e);
    }
  }

  private class ListenerTask implements Runnable {
    @Override
    public void run() {
      try {
        log.trace("Polling refresh task is running");
        List<UsbDevice> actualConnectedDevices = getRemovableDevices();
        updateConnectedDevices(actualConnectedDevices);
      } catch (Exception e) {
        log.error("Error while refreshing device list", e);
      }
    }
  }
}
