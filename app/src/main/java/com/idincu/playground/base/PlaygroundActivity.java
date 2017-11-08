package com.idincu.playground.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.idincu.playground.event.CommonEvent;
import com.idincu.playground.event.UiEvent;

import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by grace on 2017-11-07.
 */
public class PlaygroundActivity extends AppCompatActivity {
  protected PlaygroundApplication application;
  protected CompositeDisposable compositeDisposable;
  protected Disposable uiEventStreamSubscription;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    application = PlaygroundApplication.getContext();
    compositeDisposable = new CompositeDisposable();
  }

  @Override public void setContentView(@LayoutRes int layoutResID) {
    super.setContentView(layoutResID);
    ButterKnife.bind(this);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    compositeDisposable.dispose();
  }

  @Override protected void onStart() {
    super.onStart();
    uiEventStreamSubscription = PlaygroundApplication.getContext()
        .getUiEventBus()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::subscribeUiEvent, Throwable::printStackTrace);
  }

  @Override protected void onStop() {
    super.onStop();
    uiEventStreamSubscription.dispose();
  }

  protected void subscribeEvent(CommonEvent event) {
  }

  protected void subscribeUiEvent(UiEvent uiEvent) {
  }
}
