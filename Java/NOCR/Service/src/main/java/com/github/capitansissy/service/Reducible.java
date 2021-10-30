package com.github.capitansissy.service;

@Deprecated
public interface Reducible<T> {
  public T call(T a, T b);
}