package com.university;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable {
    private String from;
    private String to;
    private String text;
    private LocalDateTime sentAt;

    public Message(String from, String to, String text) {
        this.from = from;
        this.to = to;
        this.text = text;
        this.sentAt = LocalDateTime.now();
    }

    public String getFrom() { return from; }
    public String getTo() { return to; }
    public String getText() { return text; }
    public LocalDateTime getSentAt() { return sentAt; }

    @Override
    public String toString() {
        return "[" + sentAt + "] from " + from + " to " + to + ": " + text;
    }
}
