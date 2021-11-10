package com.github.capitansissy.usb.process;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Slf4j
public class CommandExecutor implements Closeable {
  private final String command;
  private final BufferedReader input;
  private final Process process;

  public CommandExecutor(final String command) throws IOException {
    if (log.isTraceEnabled()) {
      log.trace("Running command: {}", command);
    }
    this.command = command;
    this.process = Runtime.getRuntime().exec(command);
    this.input = new BufferedReader(new InputStreamReader(process.getInputStream()));
  }

  public void processOutput(final Consumer<String> method) throws IOException {
    String outputLine;
    while ((outputLine = this.readOutputLine()) != null) {
      method.accept(outputLine);
    }
  }

  public boolean checkOutput(final Predicate<String> method) throws IOException {
    String outputLine;
    while ((outputLine = this.readOutputLine()) != null) {
      if (method.test(outputLine)) {
        return true;
      }
    }
    return false;
  }

  @Nullable
  private String readOutputLine() throws IOException {
    if (input == null) {
      throw new IllegalStateException("You need to call 'executeCommand' method first");
    }
    final String outputLine = input.readLine();
    if (outputLine != null) {
      return outputLine.trim();
    }
    return null;
  }

  @Override
  public void close() throws IOException {
    try {
      int exitValue = process.waitFor();
      if (exitValue != 0) {
        log.warn("Abnormal command '{}' termination. Exit value: {}", command, exitValue);
      }
    } catch (InterruptedException e) {
      log.error("Error while waiting for command '{}' to complete", command, e);
    }
    if (input != null) {
      try {
        input.close();
      } catch (IOException e) {
        log.error(e.getMessage(), e);
      }
    }
    process.destroy();
  }

}
