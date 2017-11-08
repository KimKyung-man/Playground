package com.idincu.playground.store;

import com.annimon.stream.Stream;
import com.idincu.playground.base.AllItemRenderableByStore;
import com.idincu.playground.base.EachItemRenderableByStore;
import com.idincu.playground.model.File;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import lombok.Getter;
import lombok.Setter;

public class FilesStore implements Serializable {
  @Setter @Getter int fileDepth;
  List<File> files;
  List<AllItemRenderableByStore<List<File>>> allItemSubscribers;
  List<EachItemRenderableByStore<File>> eachItemSubscribers;

  public FilesStore() {
    this.fileDepth = 0;
    this.files = new ArrayList<>();
    this.allItemSubscribers = new ArrayList<>();
    this.eachItemSubscribers = new ArrayList<>();
  }

  public void bindFilesRenderable(AllItemRenderableByStore<List<File>> allItemSubscriber) {
    this.allItemSubscribers.add(allItemSubscriber);
  }

  public void bindFileRenderable(EachItemRenderableByStore<File> eachItemSubscriber) {
    this.eachItemSubscribers.add(eachItemSubscriber);
  }

  public Observable<File> getFiles() {
    return Observable.fromIterable(files);
  }

  public void setFiles(List<File> files) {
    this.files = files;
    Stream.of(allItemSubscribers).forEach(allItemByStore -> allItemByStore.render(files));
  }

  public void addFile(File file) {
    files.add(file);
    Stream.of(eachItemSubscribers)
        .forEach(eachItemByStore -> eachItemByStore.renderEachItem(file));
  }

  public void increaseFileDepth() {
    fileDepth++;
  }

  public void decreaseFileDepth() {
    fileDepth--;
  }

}
