package com.business.datamart;

import com.newsfeeder.NewsEvent;
import com.youtubefeeder.VideoEvent;

import java.sql.*;

public class DataMart {

    private Connection conn;

    public DataMart(String dbFile) throws SQLException {
        conn = DriverManager.getConnection("jdbc:sqlite:" + dbFile);
        setupDatabase();
    }

    private void setupDatabase() throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS news (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "ts TEXT," +
                    "ss TEXT," +
                    "title TEXT," +
                    "description TEXT," +
                    "url TEXT" +
                    ")");
            stmt.execute("CREATE TABLE IF NOT EXISTS videos (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "ts TEXT," +
                    "ss TEXT," +
                    "title TEXT," +
                    "description TEXT," +
                    "videoUrl TEXT" +   // CORREGIDO
                    ")");

        }
    }

    public void insertNewsEvent(NewsEvent e) throws SQLException {
        String sql = "INSERT INTO news (ts, ss, title, description, url) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.ts);
            ps.setString(2, e.ss);
            ps.setString(3, e.title);
            ps.setString(4, e.description);
            ps.setString(5, e.url);
            ps.executeUpdate();
        }
    }

    public void insertVideoEvent(VideoEvent v) throws SQLException {
        System.out.println("Insertando vídeo: " + v.title + " | " + v.description);

        String sql = "INSERT INTO videos (ts, ss, title, description, videoUrl) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, v.ts);
            ps.setString(2, v.ss);
            ps.setString(3, v.title);
            ps.setString(4, v.description);
            ps.setString(5, v.videoUrl);
            ps.executeUpdate();
        }
    }


}
