package com.idincu.playground.base;

/**
 * Created by grace on 2017-11-07.
 */

public interface AllItemRenderableByStore<T extends Iterable> {
  void render(T items);
}
