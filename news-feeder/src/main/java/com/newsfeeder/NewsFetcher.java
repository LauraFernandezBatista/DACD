package com.newsfeeder;

import com.google.gson.*;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NewsFetcher {
    private static final String API_KEY = "29ee668ea1e3452487efcf707588897f";
    private static final String BASE_URL = "https://newsapi.org/v2/everything?q=";

    public List<NewsArticle> fetchArticles(String query) throws Exception {
        String endpoint = BASE_URL + java.net.URLEncoder.encode(query, "UTF-8") + "&apiKey=" + API_KEY;
        HttpURLConnection conn = (HttpURLConnection) new URL(endpoint).openConnection();
        conn.setRequestMethod("GET");

        JsonObject response = JsonParser.parseReader(
                        new InputStreamReader(conn.getInputStream()))
                .getAsJsonObject();

        JsonArray articlesJson = response.getAsJsonArray("articles");
        List<NewsArticle> articles = new ArrayList<>();

        for (int i = 0; i < Math.min(5, articlesJson.size()); i++) {
            JsonObject a = articlesJson.get(i).getAsJsonObject();
            String title = a.get("title").getAsString();
            String description = a.get("description").isJsonNull() ? "" : a.get("description").getAsString();
            String url = a.get("url").getAsString();
            articles.add(new NewsArticle(title, description, url));
        }

        return articles;
    }
}
