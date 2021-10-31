package com.github.capitansissy.service;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Launcher {
  @NotNull
  @Contract(pure = true)
  private static String companyName() {
    return "yWorks";
  }

  public static void main(String[] args) {
    List<Integer> integers = new ArrayList<>();
    integers.add(1);
    integers.add(2);
    integers.add(3);
    Functional<Integer> functional = new Functional<>();
    assert (functional.sum(integers) == 6);

    System.out.println(String.format("Hello %s", companyName()));
  }
}
