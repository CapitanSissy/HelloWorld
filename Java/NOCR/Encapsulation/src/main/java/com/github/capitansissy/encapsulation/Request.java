package com.github.capitansissy.encapsulation;

import java.io.Serializable;
import java.sql.Timestamp;

public class Request implements Serializable {
  private String ip;
  private String mac;
  private Timestamp timestamp;

  public Request(String ip, Timestamp timestamp) {
    this.ip = ip;
    this.timestamp = timestamp;
  }

  public void setTimestamp(Timestamp timestamp) {
    this.timestamp = timestamp;
  }

  public Timestamp getTimestamp() {
    return timestamp;
  }

  public String getIp() {
    return ip;
  }

  public String getMac() {
    return mac;
  }

}
