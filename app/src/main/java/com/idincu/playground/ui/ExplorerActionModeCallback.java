package com.idincu.playground.ui;

import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import com.idincu.playground.R;
import com.idincu.playground.base.PlaygroundActivity;
import com.idincu.playground.base.PlaygroundApplication;
import com.idincu.playground.event.UiEvent;
import com.idincu.playground.model.File;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

/**
 * Created by grace on 2017-11-08.
 */

public class ExplorerActionModeCallback implements ActionMode.Callback {
  private static final String TAG = ExplorerActionModeCallback.class.getSimpleName();

  private PlaygroundActivity activity;
  @Getter private List<File> files;

  public ExplorerActionModeCallback(PlaygroundActivity activity) {
    this.activity = activity;
  }

  public void tintIcon(Menu menu, @IdRes int id) {
    // icon tint color 미지원.
    Drawable drawable = DrawableCompat.wrap(menu.findItem(id).getIcon());
    DrawableCompat.setTint(drawable, ContextCompat.getColor(activity, R.color.colorPrimary));
    menu.findItem(id).setIcon(drawable);
  }

  public void performSelectItem(File file) {
    if (files.contains(file)) {
      files.remove(file);
      return;
    }

    files.add(file);
  }

  @Override public boolean onCreateActionMode(ActionMode mode, Menu menu) {
    mode.getMenuInflater().inflate(R.menu.action_explorer, menu);

    tintIcon(menu, R.id.action_share);
    tintIcon(menu, R.id.action_delete);

    files = new ArrayList<>();
    return true;
  }

  @Override public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
    return true;
  }

  @Override public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
    Log.i(TAG, "onActionItemClicked: " + item);
    PlaygroundApplication.getContext()
        .postUiEvent(new UiEvent("action.mode.item.click", UiEvent.EventType.ITEM_CLICK_ACTION_MODE).attach(item));
    return true;
  }

  @Override public void onDestroyActionMode(ActionMode mode) {
    files.clear();
    PlaygroundApplication.getContext()
        .postUiEvent(new UiEvent("toolbar.disable.action.mode", UiEvent.EventType.DISABLE_ACTION_MODE));
  }
}
