package com.business;

import com.business.datamart.DataMart;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class BrokerSubscriber {
    private static final String BROKER_URL = "tcp://localhost:61616";
    private static final Gson gson = new Gson();

    private Connection connection;
    private Session session;
    private DataMart datamart;

    public BrokerSubscriber(DataMart datamart) {
        this.datamart = datamart;
    }

    public void start() throws JMSException {
        ConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
        connection = factory.createConnection();
        connection.setClientID("BusinessUnit");
        connection.start();

        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        String[] topics = {"News", "YouTube"};

        for (String topicName : topics) {
            Topic topic = session.createTopic(topicName);
            MessageConsumer consumer = session.createDurableSubscriber(topic, topicName + "Sub");

            consumer.setMessageListener(message -> {
                if (message instanceof TextMessage) {
                    try {
                        String text = ((TextMessage) message).getText();
                        JsonObject obj = JsonParser.parseString(text).getAsJsonObject();

                        if (topicName.equals("News")) {
                            var event = gson.fromJson(obj, com.newsfeeder.NewsEvent.class);
                            datamart.insertNewsEvent(event);
                        } else if (topicName.equals("YouTube")) {
                            var event = gson.fromJson(obj, com.youtubefeeder.VideoEvent.class);
                            datamart.insertVideoEvent(event);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            System.out.println("Suscrito y escuchando topic: " + topicName);
        }
    }
}
