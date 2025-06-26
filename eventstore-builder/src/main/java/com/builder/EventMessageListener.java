package com.builder;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class EventMessageListener implements MessageListener {
    private final String topicName;
    private final EventFileWriter fileWriter = new EventFileWriter();

    public EventMessageListener(String topicName) {
        this.topicName = topicName;
    }

    @Override
    public void onMessage(Message msg) {
        try {
            if (!(msg instanceof TextMessage)) return;
            String text = ((TextMessage) msg).getText();
            System.out.println("[Consumer] Recibido: " + text);

            JsonObject obj = JsonParser.parseString(text).getAsJsonObject();
            String ts = obj.get("ts").getAsString();
            String ss = obj.get("ss").getAsString();

            ZonedDateTime zdt = ZonedDateTime.parse(ts).withZoneSameInstant(ZoneOffset.UTC);
            String date = zdt.toLocalDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

            fileWriter.writeEvent(topicName, ss, date, text);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
