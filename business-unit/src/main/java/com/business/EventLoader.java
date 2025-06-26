package com.business;

import com.business.datamart.DataMart;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EventLoader {
    private static final Gson gson = new Gson();

    public static void loadHistoricEvents(String root, DataMart datamart) {
        File base = new File(root);
        if (!base.exists()) return;

        for (File topicDir : base.listFiles()) {
            if (!topicDir.isDirectory()) continue;

            for (File ssDir : topicDir.listFiles()) {
                if (!ssDir.isDirectory()) continue;

                for (File eventFile : ssDir.listFiles()) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(eventFile))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            JsonObject obj = JsonParser.parseString(line).getAsJsonObject();
                            if (topicDir.getName().equals("News")) {
                                var e = gson.fromJson(obj, com.newsfeeder.NewsEvent.class);
                                datamart.insertNewsEvent(e);
                            } else if (topicDir.getName().equals("YouTube")) {
                                var v = gson.fromJson(obj, com.youtubefeeder.VideoEvent.class);
                                datamart.insertVideoEvent(v);
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    public static List<String> loadSearchHistoryFromEventStore(String root) {
        List<String> history = new ArrayList<>();
        File base = new File(root);
        if (!base.exists()) return history;

        DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (File topicDir : base.listFiles()) {
            if (!topicDir.isDirectory()) continue;
            String topicName = topicDir.getName();

            for (File ssDir : topicDir.listFiles()) {
                if (!ssDir.isDirectory()) continue;

                for (File eventFile : ssDir.listFiles()) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(eventFile))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            JsonObject obj = JsonParser.parseString(line).getAsJsonObject();
                            String tsRaw = obj.get("ts").getAsString();
                            ZonedDateTime ts = ZonedDateTime.parse(tsRaw, inputFormatter);
                            String formattedDate = ts.format(outputFormatter);

                            String title = obj.has("title") && !obj.get("title").isJsonNull()
                                    ? obj.get("title").getAsString()
                                    : "";

                            String entry = formattedDate + " - " + topicName + " - " + title;
                            history.add(entry);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }

        history.sort((a, b) -> b.compareTo(a));
        return history;
    }
}
