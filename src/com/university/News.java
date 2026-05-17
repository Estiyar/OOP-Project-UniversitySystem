package com.university;

import java.io.Serializable;
import java.time.LocalDateTime;

public class News implements Serializable {
    private String title;
    private String body;
    private String author;
    private LocalDateTime publishedAt;

    public News(String title, String body, String author) {
        this.title = title;
        this.body = body;
        this.author = author;
        this.publishedAt = LocalDateTime.now();
    }

    public String getTitle() { return title; }
    public String getBody() { return body; }
    public String getAuthor() { return author; }
    public LocalDateTime getPublishedAt() { return publishedAt; }

    @Override
    public String toString() {
        return "[NEWS by " + author + " at " + publishedAt + "]\n  " + title + "\n  " + body;
    }
}
