package com.idincu.playground.base;

import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by grace on 2017-11-07.
 */
public class PlaygroundActivity extends AppCompatActivity {
  protected PlaygroundApplication application;

  @Override public void setContentView(@LayoutRes int layoutResID) {
    super.setContentView(layoutResID);
    ButterKnife.bind(this);
  }
}
