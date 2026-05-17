package com.university;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Complaint implements Serializable {
    private String from;
    private String topic;
    private String content;
    private LocalDateTime sentAt;
    private boolean signed;

    public Complaint(String from, String topic, String content) {
        this.from = from;
        this.topic = topic;
        this.content = content;
        this.sentAt = LocalDateTime.now();
        this.signed = false;
    }

    public void sign() {
        this.signed = true;
    }

    public String getFrom() { return from; }
    public String getTopic() { return topic; }
    public String getContent() { return content; }
    public boolean isSigned() { return signed; }

    @Override
    public String toString() {
        return "Complaint from " + from + ": " + topic
                + (signed ? " [SIGNED]" : " [NOT SIGNED]")
                + "\n  " + content;
    }
}
