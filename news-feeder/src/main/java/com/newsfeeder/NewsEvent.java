package com.newsfeeder;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class NewsEvent {
    public String ts;
    public String ss;
    public String title;
    public String description;
    public String url;

    public NewsEvent(String ss, NewsArticle art) {
        this.ss  = ss;
        this.ts  = ZonedDateTime.now(ZoneOffset.UTC).toString();
        this.title       = art.title;
        this.description = art.description;
        this.url         = art.url;
    }

}
