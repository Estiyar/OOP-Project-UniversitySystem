package com.university;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Singleton pattern + serialization
public class DataStore implements Serializable {
    private static DataStore instance;
    private static final String FILE = "data/state.bin";

    private Map<String, User> users;
    private Map<String, Course> courses;
    private Map<String, Mark> marks;             // key = studentId + "|" + courseId
    private List<ResearchProject> projects;
    private List<Complaint> complaints;
    private List<News> news;
    private List<RegistrationRequest> requests;

    private DataStore() {
        users = new HashMap<>();
        courses = new HashMap<>();
        marks = new HashMap<>();
        projects = new ArrayList<>();
        complaints = new ArrayList<>();
        news = new ArrayList<>();
        requests = new ArrayList<>();
    }

    public static DataStore getInstance() {
        if (instance == null) {
            instance = load();
            if (instance == null) {
                instance = new DataStore();
            }
        }
        return instance;
    }

    public static String markKey(String studentId, String courseId) {
        return studentId + "|" + courseId;
    }

    public Map<String, User> getUsers() { return users; }
    public Map<String, Course> getCourses() { return courses; }
    public Map<String, Mark> getMarks() { return marks; }
    public List<ResearchProject> getProjects() { return projects; }
    public List<Complaint> getComplaints() { return complaints; }
    public List<News> getNews() { return news; }
    public List<RegistrationRequest> getRequests() { return requests; }

    public void save() {
        File dir = new File("data");
        if (!dir.exists()) dir.mkdirs();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE));
            oos.writeObject(this);
            oos.close();
        } catch (IOException e) {
            System.out.println("Save error: " + e.getMessage());
        }
    }

    private static DataStore load() {
        File f = new File(FILE);
        if (!f.exists()) return null;
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
            DataStore d = (DataStore) ois.readObject();
            ois.close();
            return d;
        } catch (Exception e) {
            System.out.println("Load error: " + e.getMessage() + ", starting fresh.");
            return null;
        }
    }
}
