package com.idincu.playground.store;

import android.os.Parcel;
import android.os.Parcelable;

import com.annimon.stream.Stream;
import com.idincu.playground.base.AllItemRenderableByStore;
import com.idincu.playground.base.EachItemRenderableByStore;
import com.idincu.playground.model.File;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FilesStore implements Parcelable {
  List<File> files;
  List<AllItemRenderableByStore<List<File>>> subscribers;
  List<EachItemRenderableByStore<File>> eachItemSubscribers;

  public Observable<File> getFiles() {
    return Observable.fromIterable(files);
  }

  public void setFiles(List<File> files) {
    this.files = files;
    Stream.of(subscribers).forEach(allItemByStore -> allItemByStore.render(files));
  }

  public void addFile(File file) {
    files.add(file);
    Stream.of(eachItemSubscribers)
        .forEach(eachItemByStore -> eachItemByStore.renderEachItem(file));
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeList(this.files);
  }

  protected FilesStore(Parcel in) {
    this.files = new ArrayList<>();
    in.readList(this.files, File.class.getClassLoader());
  }

  public static final Creator<FilesStore> CREATOR = new Creator<FilesStore>() {
    @Override public FilesStore createFromParcel(Parcel source) {
      return new FilesStore(source);
    }

    @Override public FilesStore[] newArray(int size) {
      return new FilesStore[size];
    }
  };
}
