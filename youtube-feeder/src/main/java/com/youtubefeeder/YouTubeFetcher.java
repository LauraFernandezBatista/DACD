package com.youtubefeeder;

import com.google.gson.*;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class YouTubeFetcher {
    private static final String API_KEY = "AIzaSyABCVIV0YNFK5C2Nrdk2aWIPyCJoGfvypg";
    private static final String BASE_URL = "https://www.googleapis.com/youtube/v3/search?part=snippet&type=video&maxResults=5&q=";

    public List<YouTubeVideo> fetchVideos(String query) throws Exception {
        String endpoint = BASE_URL + URLEncoder.encode(query, "UTF-8") + "&key=" + API_KEY;
        HttpURLConnection conn = (HttpURLConnection) new URL(endpoint).openConnection();
        conn.setRequestMethod("GET");

        JsonObject response = JsonParser.parseReader(
                        new InputStreamReader(conn.getInputStream()))
                .getAsJsonObject();
        JsonArray items = response.getAsJsonArray("items");

        List<YouTubeVideo> videos = new ArrayList<>();
        for (JsonElement itemEl : items) {
            JsonObject item = itemEl.getAsJsonObject();
            JsonObject snippet = item.getAsJsonObject("snippet");
            String videoId = item.getAsJsonObject("id").get("videoId").getAsString();

            String title = snippet.get("title").getAsString();
            String description = snippet.get("description").getAsString();
            String videoUrl = "https://www.youtube.com/watch?v=" + videoId;

            videos.add(new YouTubeVideo(title, description, videoUrl));
        }
        return videos;
    }
}