package com.youtubefeeder;

import java.time.Instant;

public class VideoEvent {
    public String ts;
    public String ss;
    public String title;
    public String description;
    public String videoUrl;

    public VideoEvent(String ss, String title, String description, String videoUrl) {
        this.ts = Instant.now().toString();
        this.ss = ss;
        this.title = title;
        this.description = description;
        this.videoUrl = videoUrl;
    }

}
