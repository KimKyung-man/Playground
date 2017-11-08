package com.idincu.playground.utils;

import android.webkit.MimeTypeMap;

import java.io.File;


/**
 * Created by grace on 2017-11-08.
 */

public class FileUtils {
  public static boolean isImageFile(String mimeType) {
    return mimeType != null && mimeType.startsWith("image");
  }

  public static String getMimeTypeFromFile(File file) {
    return MimeTypeMap.getSingleton()
        .getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(file.getPath()));
  }
}
