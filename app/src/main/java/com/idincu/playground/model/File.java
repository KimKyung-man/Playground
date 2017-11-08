package com.idincu.playground.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by grace on 2017-11-07.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class File implements Parcelable {
  String name;
  String mimeType;
  long size;
  String readableSize;
  Date editDate;
  boolean isDirectory;
  String path;
  boolean isSelected;

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.name);
    dest.writeString(this.mimeType);
    dest.writeLong(this.size);
    dest.writeString(this.readableSize);
    dest.writeLong(this.editDate != null ? this.editDate.getTime() : -1);
    dest.writeByte(this.isDirectory ? (byte) 1 : (byte) 0);
    dest.writeString(this.path);
    dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
  }

  protected File(Parcel in) {
    this.name = in.readString();
    this.mimeType = in.readString();
    this.size = in.readLong();
    this.readableSize = in.readString();
    long tmpEditDate = in.readLong();
    this.editDate = tmpEditDate == -1 ? null : new Date(tmpEditDate);
    this.isDirectory = in.readByte() != 0;
    this.path = in.readString();
    this.isSelected = in.readByte() != 0;
  }

  public static final Creator<File> CREATOR = new Creator<File>() {
    @Override public File createFromParcel(Parcel source) {
      return new File(source);
    }

    @Override public File[] newArray(int size) {
      return new File[size];
    }
  };
}
