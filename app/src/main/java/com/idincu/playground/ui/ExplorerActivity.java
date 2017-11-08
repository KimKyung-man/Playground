package com.idincu.playground.ui;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.annimon.stream.Stream;
import com.idincu.playground.R;
import com.idincu.playground.base.PlaygroundActivity;
import com.idincu.playground.event.UiEvent;
import com.idincu.playground.model.File;
import com.snatik.storage.Storage;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;

public class ExplorerActivity extends PlaygroundActivity {
  private static final String TAG = ExplorerActivity.class.getSimpleName();

  @BindView(R.id.recycler_files) RecyclerView recyclerFiles;

  FilesRecyclerAdapter filesRecyclerAdapter;
  Storage storage;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_explorer);

    storage = new Storage(application.getApplicationContext());

    filesRecyclerAdapter = new FilesRecyclerAdapter();
    recyclerFiles.setLayoutManager(new LinearLayoutManager(this));
    recyclerFiles.setAdapter(filesRecyclerAdapter);

    application.getFilesStore().bindFilesRenderable(files -> {
      Log.i(TAG, "render: files size = " + files.size());
      filesRecyclerAdapter.setFiles(files);
      filesRecyclerAdapter.notifyDataSetChanged();
    });
    application.getFilesStore().bindFileRenderable(file -> {
      filesRecyclerAdapter.getFiles().add(file);
      filesRecyclerAdapter.notifyItemInserted(filesRecyclerAdapter.getFiles().size() - 1);
    });

    fetchFilesIfCanAccessStorage();
  }

  private void fetchFilesIfCanAccessStorage() {
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

  private List<File> fetchFiles(String path) {
    return Stream.of(storage.getFiles(path))
        .map(file -> {
          Log.i(TAG, "fetchFiles: file is " + file);
          return File.builder()
              .name(file.getName())
              .editDate(new Date(file.lastModified()))
              .mimeType(getMimeTypeFromFile(this, file))
              .size(file.getUsableSpace())
              .readableSize(storage.getReadableSize(file))
              .isDirectory(file.isDirectory())
              .path(file.getAbsolutePath())
              .build();
        })
        .toList();
  }

  private static String getMimeTypeFromFile(Context context, java.io.File file) {
    Uri uri = Uri.fromFile(file);
    ContentResolver cR = context.getContentResolver();
    return cR.getType(uri);
  }

  @Override protected void subscribeUiEvent(UiEvent uiEvent) {
    Log.i(TAG, "subscribeUiEvent: observe event " + uiEvent);

    if (uiEvent.getType() == UiEvent.EventType.CLICK) {
      File file = (File) uiEvent.getView().getTag();
      if (file.isDirectory()) {
        application.getFilesStore().setFiles(fetchFiles(file.getPath()));
        application.getFilesStore().increateFileDepth();
      }
    }
  }

  @Override public void onBackPressed() {
    if (application.getFilesStore().getFileDepth() != 0) {
      application.getFilesStore().setFiles(fetchFiles());
      return;
    }
    super.onBackPressed();
  }
}
