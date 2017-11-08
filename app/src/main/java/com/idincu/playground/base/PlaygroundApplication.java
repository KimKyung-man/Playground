package com.idincu.playground.base;

import android.app.Application;

import com.idincu.playground.event.CommonEvent;
import com.idincu.playground.event.UiEvent;
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
  private static PublishSubject<UiEvent> uiEventBus;

  @Override public void onCreate() {
    super.onCreate();
    PlaygroundApplication.context = this;
    PlaygroundApplication.filesStore = new FilesStore();
    PlaygroundApplication.eventBus = PublishSubject.create();
    PlaygroundApplication.uiEventBus = PublishSubject.create();
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

  public PublishSubject<UiEvent> getUiEventBus() {
    return uiEventBus == null ? uiEventBus = PublishSubject.create() : uiEventBus;
  }

  public void post(CommonEvent event) {
    getEventBus().onNext(event);
  }

  public void postUiEvent(UiEvent event) {
    getUiEventBus().onNext(event);
  }
}
