package com.github.capitansissy.service;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Deprecated
public class Functional<T extends Number> {
  protected T fold(Reducible<T> func, @NotNull List<T> list) {
    if (list.size() > 1) {
      return func.call(list.get(0), fold(func, list.subList(1, list.size() - 1)));
    } else {
      return list.get(0);
    }
  }

  // This will be obfuscated
  @Contract(pure = true)
  private double addDouble(double a, double b) {
    return a + b;
  }

  protected T sum(List<T> list) {
    class SumReducible<T extends Number> implements Reducible<T> {
      public T call(@NotNull T a, @NotNull T b) {
        Double sum = addDouble(a.doubleValue(), b.doubleValue());
        return (T) sum;
      }
    }

    return fold(new SumReducible<T>(), list);
  }
}