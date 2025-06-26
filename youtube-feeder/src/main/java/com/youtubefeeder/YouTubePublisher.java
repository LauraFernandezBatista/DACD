package com.youtubefeeder;

import com.google.gson.Gson;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.List;

public class YouTubePublisher {
    private static final String BROKER_URL = "tcp://localhost:61616";
    private static final String TOPIC = "YouTube";
    private static final String SOURCE = "youtube-feeder";

    private Connection connection;
    private Session session;
    private MessageProducer producer;
    private final Gson gson = new Gson();

    public YouTubePublisher() throws JMSException {
        ConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
        connection = factory.createConnection();
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic(TOPIC);
        producer = session.createProducer(topic);
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);
    }

    public void publishVideos(List<YouTubeVideo> videos) throws JMSException {
        for (YouTubeVideo video : videos) {
            VideoEvent event = new VideoEvent(SOURCE, video.title, video.description, video.videoUrl);
            String json = gson.toJson(event);
            TextMessage msg = session.createTextMessage(json);
            producer.send(msg);
            System.out.println("[YouTubePublisher] Enviado: " + json);
        }
    }

    public void close() throws JMSException {
        if (session != null) session.close();
        if (connection != null) connection.close();
    }
}