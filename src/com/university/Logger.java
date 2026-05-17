package com.university;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

// Singleton pattern
public class Logger {
    private static Logger instance;
    private static final String FILE = "logs/system.log";

    private Logger() {}

    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public void log(String userId, String action) {
        String line = "[" + LocalDateTime.now() + "] " + userId + ": " + action;
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(FILE, true));
            pw.println(line);
            pw.close();
        } catch (IOException e) {
            System.out.println("Logger error: " + e.getMessage());
        }
    }

    public String readLog() {
        StringBuilder sb = new StringBuilder();
        try {
            java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(FILE));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            br.close();
        } catch (IOException e) {
            return "(log is empty)";
        }
        return sb.toString();
    }
}
