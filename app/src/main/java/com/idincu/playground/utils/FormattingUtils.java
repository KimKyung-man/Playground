package com.idincu.playground.utils;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by grace on 2017-11-08.
 */

public class FormattingUtils {
  private static final String[] FILE_SIZE_UNITS = new String[]{"B", "kB", "MB", "GB", "TB"};

  public static String convertToByteString(long size) {
    if (size <= 0) {
      return "0";
    }
    int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
    return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + FILE_SIZE_UNITS[digitGroups];
  }

  public static String convertToDateString(Date date) {
    @SuppressLint("SimpleDateFormat")
    DateFormat df = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss");
    return df.format(date);
  }
}
