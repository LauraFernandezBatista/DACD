package com.newsfeeder;

import com.google.gson.Gson;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.List;

public class NewsPublisher {
    private static final String BROKER_URL = "tcp://localhost:61616";
    private static final String TOPIC = "News";
    private static final String SOURCE = "news-feeder";

    private final Gson gson = new Gson();

    public void publishArticles(List<NewsArticle> articles) throws Exception {
        ConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
        Connection connection = factory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic(TOPIC);
        MessageProducer producer = session.createProducer(topic);
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);

        for (NewsArticle article : articles) {
            NewsEvent event = new NewsEvent(SOURCE, article);
            String json = gson.toJson(event);
            TextMessage msg = session.createTextMessage(json);
            producer.send(msg);
            System.out.println("[NewsPublisher] Enviado: " + json);
        }

        connection.close();
    }
}

