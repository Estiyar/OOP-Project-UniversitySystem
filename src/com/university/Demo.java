package com.university;

import java.time.LocalDate;
import java.util.Arrays;

public class Demo {

    public static void main(String[] args) {
        System.out.println("============= DEMO =============");

        DataStore store = DataStore.getInstance();
        // clear any previous data
        store.getUsers().clear();
        store.getCourses().clear();
        store.getMarks().clear();
        store.getProjects().clear();
        store.getRequests().clear();
        store.getComplaints().clear();
        store.getNews().clear();

        // ---------- 1. Create users (using Factory pattern indirectly + direct ctors) ----------
        System.out.println("\n1) Create users");

        Teacher prof = new Teacher("T001", "Khaled", "Mohamad", "k@kbtu.kz", "p",
                700000, TeacherTitle.PROFESSOR, School.SITE);
        Teacher tutor = new Teacher("T002", "Daniyar", "Bekov", "d@kbtu.kz", "t",
                350000, TeacherTitle.TUTOR, School.SITE);
        Student s1 = new Student("S001", "Alibek", "Ali", "e@kbtu.kz", "s",
                Major.IS, School.SITE, 4);
        Student s2 = new Student("S002", "Aigerim", "Tlegenova", "a@kbtu.kz", "s",
                Major.SE, School.SITE, 2);
        Manager mgr = new Manager("M001", "Bauyrzhan", "Erlanov", "m@kbtu.kz", "m",
                400000, ManagerType.OR);
        ResearcherEmployee re = new ResearcherEmployee("R001", "Yerlan", "Kaisar",
                "y@kbtu.kz", "r", 600000);
        s1.makeResearcher();

        store.getUsers().put(prof.getId(), prof);
        store.getUsers().put(tutor.getId(), tutor);
        store.getUsers().put(s1.getId(), s1);
        store.getUsers().put(s2.getId(), s2);
        store.getUsers().put(mgr.getId(), mgr);
        store.getUsers().put(re.getId(), re);
        System.out.println("Created " + store.getUsers().size() + " users.");

        // ---------- 2. Add papers ----------
        System.out.println("\n2) Add research papers");
        prof.addPaper(new ResearchPaper("10.1/a", "Paper A",
                Arrays.asList("Khaled"), "IEEE", 10, LocalDate.of(2022, 1, 1), 50));
        prof.addPaper(new ResearchPaper("10.1/b", "Paper B",
                Arrays.asList("Khaled"), "IEEE", 20, LocalDate.of(2023, 1, 1), 100));
        prof.addPaper(new ResearchPaper("10.1/c", "Paper C",
                Arrays.asList("Khaled"), "IEEE", 15, LocalDate.of(2024, 1, 1), 5));
        s1.addPaper(new ResearchPaper("10.1/d", "Student Paper",
                Arrays.asList("Alibek Ali"), "Conf", 8, LocalDate.now(), 2));
        re.addPaper(new ResearchPaper("10.1/e", "Researcher Paper",
                Arrays.asList("Yerlan"), "Journal", 12, LocalDate.of(2021, 6, 1), 30));

        // ---------- 3. PrintPapers with Comparator (Strategy) ----------
        System.out.println("\n3) Print professor's papers sorted by citations:");
        prof.printPapers(PaperComparator.BY_CITATIONS);

        System.out.println("\nPrint professor's papers sorted by pages:");
        prof.printPapers(PaperComparator.BY_PAGES);

        // ---------- 4. Custom exception: LowHIndexException ----------
        System.out.println("\n4) Try to assign tutor (h=0) as supervisor:");
        tutor.makeResearcher();  // now researcher but no papers
        try {
            s1.setSupervisor(tutor);
        } catch (LowHIndexException e) {
            System.out.println("  Caught: " + e.getMessage());
        }

        System.out.println("\nNow assign professor (h>=3) as supervisor:");
        try {
            s1.setSupervisor(prof);
            System.out.println("  Supervisor set: " + s1.getSupervisor().getResearcherName());
        } catch (LowHIndexException e) {
            System.out.println("  Caught: " + e.getMessage());
        }

        // ---------- 5. ResearchProject + NotResearcherException ----------
        System.out.println("\n5) Create research project, try non-researcher join:");
        ResearchProject rp = new ResearchProject("RP-1", "AI in Education");
        try {
            rp.addParticipant(prof);
            rp.addParticipant(re);
            System.out.println("  Added 2 researchers: " + rp);
            rp.addParticipant(mgr);    // manager is not a Researcher
        } catch (NotResearcherException e) {
            System.out.println("  Caught: " + e.getMessage());
        }
        store.getProjects().add(rp);

        // ---------- 6. Add courses ----------
        System.out.println("\n6) Add courses");
        Course c1 = new Course("CSCI 3160", "OOP", 6, Major.IS, 2);
        Course c2 = new Course("CSCI 4160", "Distributed Sys", 6, Major.IS, 4);
        Course big = new Course("BIG 9999", "Heavy", 18, Major.IS, 4);
        store.getCourses().put(c1.getCourseId(), c1);
        store.getCourses().put(c2.getCourseId(), c2);
        store.getCourses().put(big.getCourseId(), big);
        c1.addTeacher("T001"); prof.addCourse("CSCI 3160");
        c2.addTeacher("T001"); prof.addCourse("CSCI 4160");

        // ---------- 7. Register and approve ----------
        System.out.println("\n7) Register student for course");
        try {
            if (s1.getFails() >= Student.MAX_FAILS) {
                throw new TooManyFailsException("Too many fails");
            }
            if (s1.getCurrentCredits() + c2.getCredits() > Student.MAX_CREDITS) {
                throw new CreditLimitException("Too many credits");
            }
            // manager approves directly
            c2.addStudent(s1.getId());
            s1.addCourse(c2.getCourseId());
            s1.addCredits(c2.getCredits());
            System.out.println("  Approved. Current credits=" + s1.getCurrentCredits());
        } catch (Exception e) {
            System.out.println("  Caught: " + e.getMessage());
        }

        // ---------- 8. Credit limit exception ----------
        System.out.println("\n8) Try to exceed 21-credit limit");
        try {
            if (s1.getCurrentCredits() + big.getCredits() > Student.MAX_CREDITS) {
                throw new CreditLimitException("Would exceed " + Student.MAX_CREDITS + " credits.");
            }
        } catch (CreditLimitException e) {
            System.out.println("  Caught: " + e.getMessage());
        }

        // ---------- 9. Put marks ----------
        System.out.println("\n9) Put marks");
        Mark m = new Mark(s1.getId(), c2.getCourseId());
        m.setFirstAttestation(25);
        m.setSecondAttestation(27);
        m.setFinalExam(35);
        store.getMarks().put(DataStore.markKey(s1.getId(), c2.getCourseId()), m);
        System.out.println("  " + m);

        // ---------- 10. Statistical report ----------
        System.out.println("\n10) Statistical report");
        double sum = 0;
        int passed = 0, failed = 0;
        for (Mark mm : store.getMarks().values()) {
            sum += mm.getTotal();
            if (mm.isFailed()) failed++;
            else passed++;
        }
        int total = store.getMarks().size();
        if (total > 0) {
            System.out.printf("  Marks=%d avg=%.2f passed=%d failed=%d%n",
                    total, sum / total, passed, failed);
        }

        // ---------- 11. Print all university papers ----------
        System.out.println("\n11) All university papers sorted by date:");
        java.util.List<ResearchPaper> all = new java.util.ArrayList<>();
        java.util.Set<String> seen = new java.util.HashSet<>();
        for (User u : store.getUsers().values()) {
            if (u instanceof Researcher) {
                for (ResearchPaper p : ((Researcher) u).getPapers()) {
                    if (seen.add(p.getDoi())) all.add(p);
                }
            }
        }
        all.sort(PaperComparator.BY_DATE);
        for (ResearchPaper p : all) System.out.println("  " + p);

        // ---------- 12. Top cited researcher ----------
        System.out.println("\n12) Top cited researcher of the year:");
        Researcher top = null;
        for (User u : store.getUsers().values()) {
            if (!(u instanceof Researcher)) continue;
            Researcher r = (Researcher) u;
            if (top == null || r.getTotalCitations() > top.getTotalCitations()) top = r;
        }
        if (top != null) {
            System.out.println("  " + top.getResearcherName()
                    + " citations=" + top.getTotalCitations() + " h-index=" + top.getHIndex());
        }

        // ---------- 13. Authentication ----------
        System.out.println("\n13) Authentication test");
        User auth = store.getUsers().get("T001");
        if (auth != null && auth.getPassword().equals("p")) {
            System.out.println("  Login OK for T001");
        }
        auth = store.getUsers().get("T001");
        if (auth != null && !auth.getPassword().equals("wrong")) {
            System.out.println("  Login FAILED for T001/wrong (as expected)");
        }

        // ---------- 14. Serialization ----------
        System.out.println("\n14) Save state to disk");
        store.save();
        System.out.println("  Saved to data/state.bin");

        // ---------- 15. Logger ----------
        System.out.println("\n15) Logger test");
        Logger.getInstance().log("T001", "Demo log entry");
        System.out.println("  Log written to logs/system.log");

        System.out.println("\n============= DEMO COMPLETE =============");
    }
}
