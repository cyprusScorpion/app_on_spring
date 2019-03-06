package com.yet.spring.core.loggers;

import com.yet.spring.core.beans.Event;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class FileEventLogger implements EventLogger {
  private String fileName;
  private File file;

  public FileEventLogger() {}

  public FileEventLogger(String fileName) {
    this.fileName = fileName;
  }

  public void logEvent(Event event) {
    try {
      FileUtils.writeStringToFile(file,
              event.toString() + "\n",
              "UTF-8",
              true);
    } catch (IOException e) {
      System.out.println("Here is error.");
      e.printStackTrace();
    }
  }

  public void init() throws IOException {
    this.file = new File(fileName);
    if (!file.exists()) {
      if (!file.createNewFile()) {
        throw new IOException("Can not create file");
      }
    }

    if (!file.canWrite()) {
      throw new IOException("File is inaccessible for writing");
    }
  }
}
