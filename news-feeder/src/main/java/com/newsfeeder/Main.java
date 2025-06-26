package com.newsfeeder;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("¿Qué tema de noticias quieres buscar?: ");
            String query = scanner.nextLine().trim();

            if (query.isEmpty()) {
                System.out.println("Tema vacío. Finalizando.");
                return;
            }

            NewsFetcher fetcher = new NewsFetcher();
            List<NewsArticle> articles = fetcher.fetchArticles(query);

            if (articles.isEmpty()) {
                System.out.println("No se encontraron artículos.");
                return;
            }

            NewsPublisher publisher = new NewsPublisher();
            publisher.publishArticles(articles);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
