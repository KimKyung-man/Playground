package com.idincu.playground.utils;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by grace on 2017-11-08.
 */

public class FormattingUtils {
  private static final String TAG = FormattingUtils.class.getSimpleName();

  public static String convertToDateString(Date date) {
    @SuppressLint("SimpleDateFormat")
    DateFormat df = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm");
    return df.format(date);
  }
}
