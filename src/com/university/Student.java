package com.university;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Student extends User implements Researcher {
    public static final int MAX_CREDITS = 21;
    public static final int MAX_FAILS = 3;

    private Major major;
    private School school;
    private int year;
    private double gpa;
    private int fails;
    private int currentCredits;
    private List<String> courseIds;
    private Researcher supervisor;

    // researcher fields
    private boolean isResearcher;
    private List<ResearchPaper> papers;

    public Student(String id, String firstName, String lastName, String email, String password,
                   Major major, School school, int year) {
        super(id, firstName, lastName, email, password);
        this.major = major;
        this.school = school;
        this.year = year;
        this.gpa = 0;
        this.fails = 0;
        this.currentCredits = 0;
        this.courseIds = new ArrayList<>();
        this.papers = new ArrayList<>();
        this.isResearcher = false;
    }

    public Major getMajor() { return major; }
    public School getSchool() { return school; }
    public int getYear() { return year; }
    public double getGpa() { return gpa; }
    public int getFails() { return fails; }
    public int getCurrentCredits() { return currentCredits; }
    public List<String> getCourseIds() { return courseIds; }
    public Researcher getSupervisor() { return supervisor; }

    public void setGpa(double gpa) { this.gpa = gpa; }
    public void setYear(int year) { this.year = year; }
    public void addCredits(int credits) { this.currentCredits += credits; }
    public void resetCredits() { this.currentCredits = 0; }
    public void addFail() { this.fails++; }

    public void addCourse(String courseId) {
        if (!courseIds.contains(courseId)) courseIds.add(courseId);
    }

    public void setSupervisor(Researcher r) throws LowHIndexException {
        if (r.getHIndex() < 3) {
            throw new LowHIndexException("Supervisor " + r.getResearcherName()
                    + " has h-index " + r.getHIndex() + " (< 3). Cannot be supervisor.");
        }
        this.supervisor = r;
    }

    public void makeResearcher() {
        this.isResearcher = true;
    }

    public boolean isResearcher() {
        return isResearcher;
    }

    // ---- Researcher ----
    @Override
    public String getResearcherId() { return id; }

    @Override
    public String getResearcherName() { return getFullName(); }

    @Override
    public List<ResearchPaper> getPapers() { return papers; }

    @Override
    public void addPaper(ResearchPaper p) {
        if (!isResearcher) {
            System.out.println("This student is not a researcher.");
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
        System.out.println("\n[STUDENT MENU]");
        System.out.println("1. View courses");
        System.out.println("2. Register for course");
        System.out.println("3. View teacher info");
        System.out.println("4. View marks");
        System.out.println("5. View transcript");
        System.out.println("6. Rate teacher");
        if (isResearcher) System.out.println("7. Research menu");
        System.out.println("8. View news feed");
        System.out.println("9. Set research supervisor");
        System.out.println("0. Logout");
    }

    @Override
    public String toString() {
        return "Student " + super.toString() + " | " + major + " | " + school
                + " | year=" + year + " | GPA=" + String.format("%.2f", gpa)
                + " | fails=" + fails + " | credits=" + currentCredits
                + (isResearcher ? " | researcher (h=" + getHIndex() + ")" : "");
    }
}
