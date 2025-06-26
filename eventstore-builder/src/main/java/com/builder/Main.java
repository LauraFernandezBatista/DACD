package com.builder;

public class Main {
    public static void main(String[] args) throws Exception {
        String[] topics = {"News", "YouTube"};

        EventStoreSubscriber subscriber = new EventStoreSubscriber();
        subscriber.start(topics);

        System.out.println("EventStoreBuilder activo. Ctrl+C para salir.");
        subscriber.waitForever();
    }
}
