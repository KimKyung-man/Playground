package com.idincu.playground.event;

import lombok.Getter;

/**
 * Created by grace on 2017-11-07.
 */

public class CommonEvent {
  public static final String EVENT_REPLACE_LIST = "EVENT_REPLACE_LIST";
  public static final String EVENT_ADD_ITEM = "EVENT_ADD_ITEM";

  @Getter private String eventName;
  private Object model;

  public CommonEvent(String eventName) {
    this.eventName = eventName;
  }

  public CommonEvent(String eventName, Object model) {
    this.eventName = eventName;
    this.model = model;
  }

  public boolean containsModel() {
    return model != null;
  }

  public <T> T getModel(Class<T> clazz) {
    return clazz.cast(model);
  }
}
