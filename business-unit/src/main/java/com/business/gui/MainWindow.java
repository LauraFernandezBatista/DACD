package com.business.gui;

import com.business.EventLoader;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainWindow extends JFrame {
    private JList<String> historyList;
    private final String eventstoreRoot;

    public MainWindow(String eventstoreRoot) {
        this.eventstoreRoot = eventstoreRoot;

        setTitle("Business Unit");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel historyPanel = new JPanel(new BorderLayout());
        historyList = new JList<>();
        JScrollPane scrollHistory = new JScrollPane(historyList);

        JButton refreshButton = new JButton("Actualizar Historial");
        refreshButton.addActionListener(e -> cargarHistorial());

        historyPanel.add(scrollHistory, BorderLayout.CENTER);
        historyPanel.add(refreshButton, BorderLayout.SOUTH);

        tabbedPane.addTab("Historial de Búsquedas", historyPanel);
        add(tabbedPane);

        cargarHistorial();

        setVisible(true);
    }

    private void cargarHistorial() {
        List<String> topics = EventLoader.loadSearchHistoryFromEventStore(eventstoreRoot);
        DefaultListModel<String> model = new DefaultListModel<>();
        for (String t : topics) model.addElement(t);
        historyList.setModel(model);
    }
}
