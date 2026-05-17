package com.university;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Teacher extends Employee implements Researcher {
    private TeacherTitle title;
    private School school;
    private List<String> courseIds;        // courses this teacher teaches
    private Map<String, Integer> ratings;  // studentId -> rating 1..5

    // researcher fields
    private boolean isResearcher;
    private List<ResearchPaper> papers;

    public Teacher(String id, String firstName, String lastName, String email, String password,
                   double salary, TeacherTitle title, School school) {
        super(id, firstName, lastName, email, password, salary);
        this.title = title;
        this.school = school;
        this.courseIds = new ArrayList<>();
        this.ratings = new HashMap<>();
        this.papers = new ArrayList<>();
        // professors are always researchers
        this.isResearcher = (title == TeacherTitle.PROFESSOR);
    }

    public TeacherTitle getTitle() { return title; }
    public School getSchool() { return school; }
    public List<String> getCourseIds() { return courseIds; }

    public void addCourse(String courseId) {
        if (!courseIds.contains(courseId)) courseIds.add(courseId);
    }

    public void rate(String studentId, int score) {
        if (score < 1 || score > 5) {
            System.out.println("Rating must be 1..5");
            return;
        }
        ratings.put(studentId, score);
    }

    public double getAverageRating() {
        if (ratings.isEmpty()) return 0;
        int sum = 0;
        for (int r : ratings.values()) sum += r;
        return (double) sum / ratings.size();
    }

    public void makeResearcher() {
        this.isResearcher = true;
    }

    public boolean isResearcher() {
        return isResearcher;
    }

    // ---- Researcher interface ----
    @Override
    public String getResearcherId() { return id; }

    @Override
    public String getResearcherName() { return getFullName(); }

    @Override
    public List<ResearchPaper> getPapers() { return papers; }

    @Override
    public void addPaper(ResearchPaper p) {
        if (!isResearcher) {
            System.out.println("This teacher is not a researcher.");
            return;
        }
        papers.add(p);
    }

    @Override
    public int getTotalCitations() {
        int sum = 0;
        for (ResearchPaper p : papers) sum += p.getCitations();
        return sum;
    }

    @Override
    public int getHIndex() {
        // sort citations descending, h is the largest i such that papers[i-1].citations >= i
        List<Integer> cits = new ArrayList<>();
        for (ResearchPaper p : papers) cits.add(p.getCitations());
        cits.sort((a, b) -> b - a);
        int h = 0;
        for (int i = 0; i < cits.size(); i++) {
            if (cits.get(i) >= i + 1) h = i + 1;
            else break;
        }
        return h;
    }

    @Override
    public void printPapers(Comparator<ResearchPaper> c) {
        if (!isResearcher) {
            System.out.println(getFullName() + " is not a researcher.");
            return;
        }
        List<ResearchPaper> copy = new ArrayList<>(papers);
        copy.sort(c);
        System.out.println("--- Papers of " + getFullName() + " (h-index=" + getHIndex() + ") ---");
        for (ResearchPaper p : copy) {
            System.out.println("  " + p);
        }
    }

    @Override
    public void showMenu() {
        System.out.println("\n[TEACHER MENU]");
        System.out.println("1. View my courses");
        System.out.println("2. View my students");
        System.out.println("3. Put marks");
        System.out.println("4. Send message");
        System.out.println("5. Send complaint");
        if (isResearcher) System.out.println("6. Research menu");
        System.out.println("7. View news feed");
        System.out.println("0. Logout");
    }

    @Override
    public String toString() {
        return "Teacher " + super.toString() + " | " + title + " | " + school
                + (isResearcher ? " | researcher (h=" + getHIndex() + ")" : "");
    }
}
