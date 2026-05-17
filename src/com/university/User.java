package com.university;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class User implements Serializable, Comparable<User>, NewsListener {
    protected String id;
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String password;
    protected List<Message> inbox;
    protected List<News> newsFeed;

    public User(String id, String firstName, String lastName, String email, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.inbox = new ArrayList<>();
        this.newsFeed = new ArrayList<>();
    }

    @Override
    public void onNews(News news) {
        newsFeed.add(news);
    }

    public List<News> getNewsFeed() { return newsFeed; }

    public abstract void showMenu();

    public String getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getFullName() { return firstName + " " + lastName; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public List<Message> getInbox() { return inbox; }

    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }

    public void receiveMessage(Message m) {
        inbox.add(m);
    }

    @Override
    public int compareTo(User o) {
        return this.lastName.compareToIgnoreCase(o.lastName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User u = (User) o;
        return Objects.equals(id, u.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return id + " " + getFullName() + " <" + email + ">";
    }
}
