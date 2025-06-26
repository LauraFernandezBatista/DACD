package com.youtubefeeder;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("¿Qué tema quieres buscar en YouTube?: ");
            String query = scanner.nextLine().trim();
            if (query.isEmpty()) {
                System.out.println("Tema vacío, finalizando.");
                return;
            }

            YouTubeFetcher fetcher = new YouTubeFetcher();
            List<YouTubeVideo> videos = fetcher.fetchVideos(query);

            YouTubePublisher publisher = new YouTubePublisher();
            publisher.publishVideos(videos);
            publisher.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
