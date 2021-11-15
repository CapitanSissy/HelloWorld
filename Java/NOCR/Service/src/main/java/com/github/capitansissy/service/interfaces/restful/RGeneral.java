package com.github.capitansissy.service.interfaces.restful;

import org.jvnet.hk2.annotations.Contract;

@Contract
public interface RGeneral {
  String sayHello();

  String GetVersion();

  String GetDate();

  String GetInput(String input);
}