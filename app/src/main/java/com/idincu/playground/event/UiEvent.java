package com.idincu.playground.event;

import android.view.MenuItem;
import android.view.View;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by grace on 2017-11-07.
 */
@Data
public class UiEvent implements Serializable {
  public enum EventType {
    CLICK, LONG_CLICK, DISABLE_ACTION_MODE, ITEM_CLICK_ACTION_MODE // FLICKING, SWIPE ... any event
  }

  String name; // event id
  EventType type;
  View view;
  MenuItem menuItem;

  public UiEvent(String name, EventType type) {
    this.name = name;
    this.type = type;
  }

  public UiEvent attach(View view) {
    this.view = view;
    return this;
  }

  public UiEvent attach(MenuItem menuItem) {
    this.menuItem = menuItem;
    return this;
  }
}
