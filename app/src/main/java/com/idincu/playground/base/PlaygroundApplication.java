package com.idincu.playground.base;

import android.app.Application;

import com.idincu.playground.event.CommonEvent;
import com.idincu.playground.store.FilesStore;

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
    PlaygroundApplication.filesStore = new FilesStore();
    PlaygroundApplication.eventBus = PublishSubject.create();
  }

  public static PlaygroundApplication getContext() {
    return context;
  }

  public FilesStore getFilesStore() {
    return filesStore == null ? filesStore = new FilesStore() : filesStore;
  }

  public PublishSubject<CommonEvent> getEventBus() {
    return eventBus == null ? eventBus = PublishSubject.create() : eventBus;
  }

  public void sendEvent(CommonEvent event) {
    getEventBus().onNext(event);
  }
}
