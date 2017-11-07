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
import com.idincu.playground.model.File;
import com.snatik.storage.Storage;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.Date;
import java.util.List;

import butterknife.BindView;

public class ExplorerActivity extends PlaygroundActivity {
  private static final String TAG = ExplorerActivity.class.getSimpleName();

  @BindView(R.id.recycler_files) RecyclerView recyclerFiles;

  FilesRecyclerAdapter filesRecyclerAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_explorer);

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
    rxPermissions
        .request(Manifest.permission.READ_EXTERNAL_STORAGE)
        .subscribe(granted -> {
          if (!granted) {
            Toast.makeText(this, R.string.toast_permission_denied, Toast.LENGTH_SHORT).show();
            return;
          }

          application.getFilesStore().setFiles(fetchFiles());
        });
  }

  private List<File> fetchFiles() {
    Storage storage = new Storage(application.getApplicationContext());
    Log.i(TAG, "fetchFiles: " + storage.getExternalStorageDirectory() + " / " + storage.getFiles(storage.getExternalStorageDirectory()));
    List<File> files = Stream.of(storage.getFiles(storage.getExternalStorageDirectory()))
        .map(file -> {
          Log.i(TAG, "fetchFiles: file is " + file);
          return File.builder()
              .name(file.getName())
              .editDate(new Date(file.lastModified()))
              .mimeType(getMimeTypeFromFile(this, file))
              .size(file.getUsableSpace())
              .build();
        })
        .toList();
    Log.i(TAG, "fetchFiles: files = " + files);

    return files;
  }

  private static String getMimeTypeFromFile(Context context, java.io.File file) {
    Uri uri = Uri.fromFile(file);
    ContentResolver cR = context.getContentResolver();
    return cR.getType(uri);
  }

}
