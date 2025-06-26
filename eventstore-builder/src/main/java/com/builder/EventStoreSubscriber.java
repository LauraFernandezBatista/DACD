package com.builder;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.concurrent.CountDownLatch;

public class EventStoreSubscriber {
    private static final String BROKER_URL = "tcp://localhost:61616";
    private static final String CLIENT_ID  = "EventStoreClient";

    private Connection connection;
    private Session session;

    public void start(String[] topics) throws JMSException {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
        connection = factory.createConnection();
        connection.setClientID(CLIENT_ID);
        connection.start();

        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        for (String topicName : topics) {
            Topic topic = session.createTopic(topicName);
            TopicSubscriber subscriber = session.createDurableSubscriber(topic, topicName + "Sub");

            subscriber.setMessageListener(new EventMessageListener(topicName));
        }
    }

    public void waitForever() throws InterruptedException {
        new CountDownLatch(1).await();
    }

}
