package com.builder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class EventFileWriter {

    public void writeEvent(String topicName, String ss, String date, String text) throws IOException {
        String dirPath = "eventstore/" + topicName + "/" + ss;
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dirPath + "/" + date + ".events");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(text);
            writer.newLine();
        }
        System.out.println("[Consumer] Guardado en: " + file.getPath());
    }
}

