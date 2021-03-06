package com.idincu.playground.ui;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.widget.Toast;

import com.annimon.stream.Stream;
import com.idincu.playground.R;
import com.idincu.playground.base.AllItemRenderableByStore;
import com.idincu.playground.base.EachItemRenderableByStore;
import com.idincu.playground.base.PlaygroundActivity;
import com.idincu.playground.event.UiEvent;
import com.idincu.playground.model.File;
import com.idincu.playground.utils.FileUtils;
import com.snatik.storage.Storage;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;

public class ExplorerActivity extends PlaygroundActivity
    implements AllItemRenderableByStore<List<File>>, EachItemRenderableByStore<File> {
  private static final String TAG = ExplorerActivity.class.getSimpleName();

  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.recycler_files) RecyclerView recyclerFiles;

  ExplorerActionModeCallback explorerActionModeCallback;
  ActionMode actionMode;

  FilesRecyclerAdapter filesRecyclerAdapter;
  Storage storage;

  // Ui State
  private boolean inActionMode;
  private String currentPath;

  @Override public void renderEachItem(File file) {
    filesRecyclerAdapter.getFiles().add(file);
    filesRecyclerAdapter.notifyItemInserted(filesRecyclerAdapter.getFiles().size() - 1);
  }

  @Override public void render(List<File> files) {
    Log.i(TAG, "render: files size = " + files.size());
    filesRecyclerAdapter.setFiles(files);
    filesRecyclerAdapter.notifyDataSetChanged();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_explorer);

    setupToolbar();
    setupRecyclerView();
  }

  private void setupToolbar() {
    inActionMode = false;
    explorerActionModeCallback = new ExplorerActionModeCallback(this);
  }

  private void updatePath(@NonNull String path) {
    currentPath = path;
    toolbar.setTitle(currentPath);
  }

  private void setupRecyclerView() {
    storage = new Storage(application.getApplicationContext());

    filesRecyclerAdapter = new FilesRecyclerAdapter();
    recyclerFiles.setLayoutManager(new LinearLayoutManager(this));
    recyclerFiles.setAdapter(filesRecyclerAdapter);

    application.getFilesStore().bindFilesRenderable(this);
    application.getFilesStore().bindFileRenderable(this);

    fetchFilesIfCanAccessToStorage();
  }

  private void fetchFilesIfCanAccessToStorage() {
    RxPermissions rxPermissions = new RxPermissions(this);
    Disposable disposable = rxPermissions
        .request(Manifest.permission.READ_EXTERNAL_STORAGE)
        .subscribe(granted -> {
          if (!granted) {
            Toast.makeText(this, R.string.toast_permission_denied, Toast.LENGTH_SHORT).show();
            return;
          }

          application.getFilesStore().setFiles(fetchFiles());
        });

    compositeDisposable.add(disposable);
  }

  private List<File> fetchFiles() {
    application.getFilesStore().setFileDepth(0);
    return fetchFiles(storage.getExternalStorageDirectory());
  }

  private List<File> fetchFiles(@NonNull String path) {
    updatePath(path);
    return Stream.of(storage.getFiles(path))
        .map(file -> {
          Log.i(TAG, "fetchFiles: file is " + file);
          return File.builder()
              .name(file.getName())
              .editDate(new Date(file.lastModified()))
              .mimeType(FileUtils.getMimeTypeFromFile(file))
              .size(file.getUsableSpace())
              .readableSize(storage.getReadableSize(file))
              .isDirectory(file.isDirectory())
              .path(file.getAbsolutePath())
              .build();
        })
        .toList();
  }

  private void deleteFileIfHasWritePermission(@NonNull List<File> files) {
    RxPermissions rxPermissions = new RxPermissions(this);
    Disposable disposable = rxPermissions
        .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        .subscribe(granted -> {
          if (!granted) {
            Toast.makeText(this, R.string.toast_write_permission_denied, Toast.LENGTH_SHORT).show();
            return;
          }

          Stream.of(files).forEach(file -> {
            if (file.isDirectory()) {
              storage.deleteDirectory(file.getPath());
            } else {
              storage.deleteFile(file.getPath());
            }
          });

          application.getFilesStore().setFiles(fetchFiles(storage.getFile(currentPath).getAbsolutePath()));
          actionMode.finish();

          Snackbar.make(findViewById(R.id.container), R.string.snack_file_deleted, Snackbar.LENGTH_SHORT)
              .show();
        });

    compositeDisposable.add(disposable);
  }

  @Override protected void subscribeUiEvent(@NonNull UiEvent uiEvent) {
    Log.i(TAG, "subscribeUiEvent: observe event " + uiEvent);
    if (uiEvent.getType() == UiEvent.EventType.CLICK) {
      onRecyclerViewItemClicked(uiEvent);
    } else if (uiEvent.getType() == UiEvent.EventType.LONG_CLICK) {
      onRecyclerViewItemLongClicked(uiEvent);
    } else if (uiEvent.getType() == UiEvent.EventType.ITEM_CLICK_ACTION_MODE) {
      onMenuItemClickedAtActionMode(uiEvent);
    } else if (uiEvent.getType() == UiEvent.EventType.DISABLE_ACTION_MODE) {
      onActionModeDisabled();
    }
  }

  private void onRecyclerViewItemClicked(@NonNull UiEvent uiEvent) {
    File file = (File) uiEvent.getView().getTag();

    if (inActionMode) {
      explorerActionModeCallback.performSelectItem((File) uiEvent.getView().getTag());
      return;
    }

    if (file.isDirectory()) {
      application.getFilesStore().setFiles(fetchFiles(file.getPath()));
      application.getFilesStore().increaseFileDepth();
      return;
    }

    readFileIfKnownMimeType(file);
  }

  private void onRecyclerViewItemLongClicked(@NonNull UiEvent uiEvent) {
    if (inActionMode) {
      uiEvent.setType(UiEvent.EventType.CLICK);
      application.getUiEventBus().onNext(uiEvent);
      return;
    }

    inActionMode = true;
    actionMode = toolbar.startActionMode(explorerActionModeCallback);

    filesRecyclerAdapter.enableActionMode((File) uiEvent.getView().getTag());
    uiEvent.setType(UiEvent.EventType.CLICK);
    application.getUiEventBus().onNext(uiEvent);
  }

  private void onMenuItemClickedAtActionMode(@NonNull UiEvent uiEvent) {
    if (uiEvent.getMenuItem().getItemId() == R.id.action_share) {
      shareFile();
    } else if (uiEvent.getMenuItem().getItemId() == R.id.action_delete) {
      deleteFileIfHasWritePermission(explorerActionModeCallback.getFiles());
    }
  }

  private void shareFile() {
    ArrayList<Uri> uris = new ArrayList<>();

    uris.addAll(
        Stream.of(explorerActionModeCallback.getFiles())
            .map(file -> FileProvider.getUriForFile(
                this,
                application.getPackageName() + ".fileprovider",
                storage.getFile(file.getPath())
                )
            )
            .toList()
    );

    Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
    intent.putExtra(Intent.EXTRA_SUBJECT, "files");
    intent.setType("*/*");
    intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    startActivity(intent);
    actionMode.finish();
  }

  private void onActionModeDisabled() {
    filesRecyclerAdapter.disableActionMode();
    inActionMode = false;
  }

  private void readFileIfKnownMimeType(@NonNull File file) {
    try {
      Log.i(TAG, "readFileIfKnownMimeType: file mimetype " + file.getMimeType() + " / " + file.getPath());
      Intent intent = new Intent(Intent.ACTION_VIEW);
      Uri apkURI = FileProvider.getUriForFile(
          this,
          application.getPackageName() + ".fileprovider",
          storage.getFile(file.getPath())
      );
      intent.setDataAndType(apkURI, file.getMimeType());
      intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
      startActivity(intent);
    } catch (Exception e) {
      e.printStackTrace();
      Toast.makeText(application, R.string.toast_file_open_failed, Toast.LENGTH_SHORT).show();
    }
  }

  @Override public void onBackPressed() {
    if (inActionMode) {
      actionMode.finish();
      return;
    }
    if (application.getFilesStore().getFileDepth() != 0) {
      application.getFilesStore().setFiles(fetchFiles(storage.getFile(currentPath).getParent()));
      application.getFilesStore().decreaseFileDepth();
      return;
    }
    super.onBackPressed();
  }
}
