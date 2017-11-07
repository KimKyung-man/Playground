package com.idincu.playground.base;

import android.app.Application;

import com.idincu.playground.event.CommonEvent;
import com.idincu.playground.store.FilesStore;

import java.util.ArrayList;

import io.reactivex.subjects.PublishSubject;

/**
 * Created by grace on 2017-11-07.
 */
public class PlaygroundApplication extends Application {
  private static final String TAG = PlaygroundApplication.class.getSimpleName();

  private static PlaygroundApplication context;
  private static FilesStore filesStore;
  private static PublishSubject<CommonEvent> eventBus;

  @Override public void onCreate() {
    super.onCreate();
    PlaygroundApplication.context = this;
    PlaygroundApplication.filesStore = new FilesStore(new ArrayList<>(), new ArrayList<>());
    PlaygroundApplication.eventBus = PublishSubject.create();
  }

  public PlaygroundApplication getContext() {
    return context == null ? context = PlaygroundApplication.this : context;
  }

  public FilesStore getFilesStore() {
    return filesStore == null ? filesStore = new FilesStore(new ArrayList<>(), new ArrayList<>()) : filesStore;
  }

  public static PublishSubject<CommonEvent> getEventBus() {
    return eventBus == null ? eventBus = PublishSubject.create() : eventBus;
  }

  public void sendEvent(CommonEvent event) {
    getEventBus().onNext(event);
  }
}
