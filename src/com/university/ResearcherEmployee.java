package com.university;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ResearcherEmployee extends Employee implements Researcher {
    private List<ResearchPaper> papers;

    public ResearcherEmployee(String id, String firstName, String lastName, String email,
                              String password, double salary) {
        super(id, firstName, lastName, email, password, salary);
        this.papers = new ArrayList<>();
    }

    @Override
    public String getResearcherId() { return id; }

    @Override
    public String getResearcherName() { return getFullName(); }

    @Override
    public List<ResearchPaper> getPapers() { return papers; }

    @Override
    public void addPaper(ResearchPaper p) { papers.add(p); }

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
        List<ResearchPaper> copy = new ArrayList<>(papers);
        copy.sort(c);
        System.out.println("--- Papers of " + getFullName() + " (h-index=" + getHIndex() + ") ---");
        for (ResearchPaper p : copy) {
            System.out.println("  " + p);
        }
    }

    @Override
    public void showMenu() {
        System.out.println("\n[RESEARCHER MENU]");
        System.out.println("1. Research menu");
        System.out.println("2. Send message");
        System.out.println("0. Logout");
    }

    @Override
    public String toString() {
        return "ResearcherEmployee " + super.toString() + " | h=" + getHIndex();
    }
}
