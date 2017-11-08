package com.idincu.playground.event;

import android.view.View;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by grace on 2017-11-07.
 */
@Data
public class UiEvent implements Serializable {
  public enum EventType {
    CLICK, LONG_CLICK// FLICKING, SWIPE ... any event
  }

  String name; // event id
  EventType type;
  View view;

  public UiEvent(String name, EventType type) {
    this.name = name;
    this.type = type;
  }

  public UiEvent attach(View view) {
    this.view = view;
    return this;
  }
}
