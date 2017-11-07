package com.idincu.playground.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.idincu.playground.R;
import com.idincu.playground.base.PlaygroundActivity;
import com.idincu.playground.model.File;

import java.util.ArrayList;
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
    application.getFilesStore().setFiles(createDefaultData());
  }

  private List<File> createDefaultData() {
    List<File> files = new ArrayList<>();
    files.add(new File("test1", "mimetype", 1024, new Date()));
    files.add(new File("test2", "mimetype", 2048, new Date()));
    files.add(new File("test3", "mimetype", 4096, new Date()));
    return files;
  }
}
