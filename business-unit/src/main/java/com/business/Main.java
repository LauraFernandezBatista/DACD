package com.business;

import com.business.datamart.DataMart;
import com.business.gui.MainWindow;

public class Main {
    private static final String DB_FILE = "businessunit.db";
    private static final String EVENTSTORE_ROOT = "eventstore";

    public static void main(String[] args) {
        new Main().start();
    }

    public void start() {
        try {
            DataMart datamart = new DataMart(DB_FILE);

            EventLoader.loadHistoricEvents(EVENTSTORE_ROOT, datamart);

            BrokerSubscriber subscriber = new BrokerSubscriber(datamart);
            new Thread(() -> {
                try {
                    subscriber.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

            javax.swing.SwingUtilities.invokeLater(() -> new MainWindow(EVENTSTORE_ROOT));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
