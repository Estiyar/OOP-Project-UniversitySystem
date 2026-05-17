package com.university;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Main {

    static Scanner sc = new Scanner(System.in);
    static DataStore store = DataStore.getInstance();
    static Logger log = Logger.getInstance();

    public static void main(String[] args) {
        if (store.getUsers().isEmpty()) {
            seedData();
        }

        System.out.println("=== KBTU University Information System ===");

        while (true) {
            User u = login();
            if (u == null) break;

            log.log(u.getId(), "LOGIN");

            if (u instanceof Admin) adminMenu((Admin) u);
            else if (u instanceof Teacher) teacherMenu((Teacher) u);
            else if (u instanceof Student) studentMenu((Student) u);
            else if (u instanceof Manager) managerMenu((Manager) u);
            else if (u instanceof ResearcherEmployee) researcherEmployeeMenu((ResearcherEmployee) u);

            log.log(u.getId(), "LOGOUT");
            store.save();
        }

        System.out.println("Bye!");
    }

    // ============ LOGIN ============
    static User login() {
        while (true) {
            System.out.print("\nLogin (id, empty to exit): ");
            String id = sc.nextLine().trim();
            if (id.isEmpty()) return null;
            System.out.print("Password: ");
            String pwd = sc.nextLine();

            User u = store.getUsers().get(id);
            if (u != null && u.getPassword().equals(pwd)) {
                System.out.println("Welcome, " + u.getFullName());
                return u;
            }
            System.out.println("Wrong id or password.");
        }
    }

    // ============ ADMIN ============
    static void adminMenu(Admin a) {
        while (true) {
            a.showMenu();
            System.out.print("> ");
            String c = sc.nextLine().trim();
            if (c.equals("0")) return;
            else if (c.equals("1")) addUserFlow();
            else if (c.equals("2")) removeUserFlow();
            else if (c.equals("3")) updateUserFlow();
            else if (c.equals("4")) listUsers();
            else if (c.equals("5")) System.out.println(log.readLog());
            else System.out.println("Invalid choice.");
        }
    }

    static void addUserFlow() {
        System.out.print("Type (admin/manager/teacher/student/researcher): ");
        String type = sc.nextLine().trim();
        System.out.print("ID: "); String id = sc.nextLine().trim();
        if (store.getUsers().containsKey(id)) {
            System.out.println("User with this ID already exists.");
            return;
        }
        System.out.print("First name: "); String fn = sc.nextLine();
        System.out.print("Last name: "); String ln = sc.nextLine();
        System.out.print("Email: "); String em = sc.nextLine();
        System.out.print("Password: "); String pw = sc.nextLine();
        User u = UserFactory.createUser(type, id, fn, ln, em, pw);
        if (u == null) {
            System.out.println("Unknown user type.");
            return;
        }
        store.getUsers().put(id, u);
        log.log("admin", "ADD_USER " + id);
        System.out.println("User added: " + u);
    }

    static void removeUserFlow() {
        System.out.print("User ID to remove: ");
        String id = sc.nextLine().trim();
        if (store.getUsers().remove(id) != null) {
            log.log("admin", "REMOVE_USER " + id);
            System.out.println("Removed.");
        } else {
            System.out.println("Not found.");
        }
    }

    static void updateUserFlow() {
        System.out.print("User ID to update: ");
        User u = store.getUsers().get(sc.nextLine().trim());
        if (u == null) { System.out.println("Not found."); return; }
        System.out.print("New first name (empty=keep): ");
        String fn = sc.nextLine();
        if (!fn.isEmpty()) u.setFirstName(fn);
        System.out.print("New last name (empty=keep): ");
        String ln = sc.nextLine();
        if (!ln.isEmpty()) u.setLastName(ln);
        System.out.print("New email (empty=keep): ");
        String em = sc.nextLine();
        if (!em.isEmpty()) u.setEmail(em);
        log.log("admin", "UPDATE_USER " + u.getId());
        System.out.println("Updated: " + u);
    }

    static void listUsers() {
        List<User> all = new ArrayList<>(store.getUsers().values());
        java.util.Collections.sort(all);
        for (User u : all) System.out.println(u);
    }

    // ============ TEACHER ============
    static void teacherMenu(Teacher t) {
        while (true) {
            t.showMenu();
            System.out.print("> ");
            String c = sc.nextLine().trim();
            if (c.equals("0")) return;
            else if (c.equals("1")) viewMyCourses(t);
            else if (c.equals("2")) viewMyStudents(t);
            else if (c.equals("3")) putMarksFlow(t);
            else if (c.equals("4")) sendMessageFlow(t);
            else if (c.equals("5")) sendComplaintFlow(t);
            else if (c.equals("6") && t.isResearcher()) researchMenu(t);
            else if (c.equals("7")) viewNewsFeed(t);
            else System.out.println("Invalid choice.");
        }
    }

    static void viewMyCourses(Teacher t) {
        if (t.getCourseIds().isEmpty()) {
            System.out.println("No courses assigned.");
            return;
        }
        for (String cid : t.getCourseIds()) {
            Course c = store.getCourses().get(cid);
            if (c != null) {
                System.out.println(c);
                for (Lesson l : c.getLessons()) System.out.println("    " + l);
            }
        }
    }

    static void viewMyStudents(Teacher t) {
        java.util.Set<String> ids = new java.util.HashSet<>();
        for (String cid : t.getCourseIds()) {
            Course c = store.getCourses().get(cid);
            if (c != null) ids.addAll(c.getStudentIds());
        }
        if (ids.isEmpty()) {
            System.out.println("No students.");
            return;
        }
        for (String sid : ids) {
            User u = store.getUsers().get(sid);
            if (u != null) System.out.println(u);
        }
    }

    static void putMarksFlow(Teacher t) {
        System.out.print("Course ID: ");
        String cid = sc.nextLine().trim();
        Course c = store.getCourses().get(cid);
        if (c == null || !c.getTeacherIds().contains(t.getId())) {
            System.out.println("This is not your course.");
            return;
        }
        System.out.print("Student ID: ");
        String sid = sc.nextLine().trim();
        if (!c.getStudentIds().contains(sid)) {
            System.out.println("Student is not enrolled in this course.");
            return;
        }
        double a1 = readDouble("1st attestation (0..30): ", 0, 30);
        double a2 = readDouble("2nd attestation (0..30): ", 0, 30);
        double fe = readDouble("Final exam (0..40): ", 0, 40);

        String key = DataStore.markKey(sid, cid);
        Mark m = store.getMarks().get(key);
        boolean newMark = (m == null);
        if (newMark) m = new Mark(sid, cid);
        m.setFirstAttestation(a1);
        m.setSecondAttestation(a2);
        m.setFinalExam(fe);
        store.getMarks().put(key, m);

        // update student GPA + fails (only if new mark, to avoid double counting on edit)
        if (newMark) {
            Student s = (Student) store.getUsers().get(sid);
            recomputeGpa(s);
            if (m.isFailed()) s.addFail();
        } else {
            // re-edit: just recompute GPA
            Student s = (Student) store.getUsers().get(sid);
            recomputeGpa(s);
        }

        log.log(t.getId(), "PUT_MARK " + sid + "/" + cid + " total=" + m.getTotal());
        System.out.println("Saved: " + m);
    }

    static void recomputeGpa(Student s) {
        double totalPoints = 0;
        int totalCredits = 0;
        for (Mark m : store.getMarks().values()) {
            if (!m.getStudentId().equals(s.getId())) continue;
            Course c = store.getCourses().get(m.getCourseId());
            if (c == null) continue;
            totalPoints += m.getGpaPoints() * c.getCredits();
            totalCredits += c.getCredits();
        }
        if (totalCredits > 0) s.setGpa(totalPoints / totalCredits);
    }

    static void sendMessageFlow(Employee from) {
        System.out.print("To user ID: ");
        User to = store.getUsers().get(sc.nextLine().trim());
        if (to == null) { System.out.println("User not found."); return; }
        System.out.print("Text: ");
        String text = sc.nextLine();
        from.sendMessage(to, text);
        log.log(from.getId(), "MESSAGE -> " + to.getId());
        System.out.println("Sent.");
    }

    static void sendComplaintFlow(Employee from) {
        System.out.print("Topic: "); String topic = sc.nextLine();
        System.out.print("Content: "); String content = sc.nextLine();
        from.sendComplaint(topic, content);
        log.log(from.getId(), "COMPLAINT");
        System.out.println("Complaint sent.");
    }

    // ============ STUDENT ============
    static void studentMenu(Student s) {
        // 4th year supervisor check
        if (s.getYear() == 4 && s.getSupervisor() == null) {
            System.out.println("[!] You are a 4th-year student. You must have a supervisor.");
        }
        while (true) {
            s.showMenu();
            System.out.print("> ");
            String c = sc.nextLine().trim();
            if (c.equals("0")) return;
            else if (c.equals("1")) {
                for (Course co : store.getCourses().values()) {
                    System.out.println(co);
                    for (Lesson l : co.getLessons()) System.out.println("    " + l);
                }
            }
            else if (c.equals("2")) registerCourseFlow(s);
            else if (c.equals("3")) viewTeacherInfoFlow();
            else if (c.equals("4")) viewMyMarks(s);
            else if (c.equals("5")) viewTranscript(s);
            else if (c.equals("6")) rateTeacherFlow(s);
            else if (c.equals("7") && s.isResearcher()) researchMenu(s);
            else if (c.equals("8")) viewNewsFeed(s);
            else if (c.equals("9")) setSupervisorFlow(s);
            else System.out.println("Invalid choice.");
        }
    }

    static void setSupervisorFlow(Student s) {
        // list researchers with h-index >= 3
        List<Researcher> ok = new ArrayList<>();
        for (User u : store.getUsers().values()) {
            if (u instanceof Researcher) {
                Researcher r = (Researcher) u;
                if (r.getHIndex() >= 3) ok.add(r);
            }
        }
        if (ok.isEmpty()) {
            System.out.println("No eligible researchers (need h-index >= 3).");
            return;
        }
        for (int i = 0; i < ok.size(); i++) {
            System.out.println(i + ") " + ok.get(i).getResearcherName() + " h=" + ok.get(i).getHIndex());
        }
        System.out.print("Pick index: ");
        try {
            Researcher r = ok.get(Integer.parseInt(sc.nextLine().trim()));
            s.setSupervisor(r);
            System.out.println("Supervisor set: " + r.getResearcherName());
        } catch (LowHIndexException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Invalid index.");
        }
    }

    static void registerCourseFlow(Student s) {
        // 4th year must have supervisor (enforced)
        if (s.getYear() == 4 && s.getSupervisor() == null) {
            System.out.println("Error: 4th-year students must have a research supervisor before registering for courses.");
            return;
        }
        System.out.print("Course ID: ");
        String cid = sc.nextLine().trim();
        Course c = store.getCourses().get(cid);
        if (c == null) { System.out.println("Course not found."); return; }

        try {
            if (s.getFails() >= Student.MAX_FAILS) {
                throw new TooManyFailsException("You have failed " + s.getFails() + " times, cannot register.");
            }
            if (s.getCurrentCredits() + c.getCredits() > Student.MAX_CREDITS) {
                throw new CreditLimitException("Cannot register: would exceed "
                        + Student.MAX_CREDITS + " credits.");
            }
            RegistrationRequest req = new RegistrationRequest(s.getId(), cid);
            store.getRequests().add(req);
            log.log(s.getId(), "REG_REQUEST " + cid);
            System.out.println("Request submitted, waiting for manager approval.");
        } catch (TooManyFailsException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (CreditLimitException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void viewTeacherInfoFlow() {
        System.out.print("Course ID: ");
        Course c = store.getCourses().get(sc.nextLine().trim());
        if (c == null) { System.out.println("Course not found."); return; }
        for (String tid : c.getTeacherIds()) {
            User u = store.getUsers().get(tid);
            if (u != null) System.out.println(u);
        }
    }

    static void viewNewsFeed(User u) {
        if (u.getNewsFeed().isEmpty()) {
            System.out.println("No news yet.");
            return;
        }
        for (News n : u.getNewsFeed()) System.out.println(n);
    }

    static void viewMyMarks(Student s) {
        boolean found = false;
        for (Mark m : store.getMarks().values()) {
            if (m.getStudentId().equals(s.getId())) {
                System.out.println(m);
                found = true;
            }
        }
        if (!found) System.out.println("No marks yet.");
    }

    static void viewTranscript(Student s) {
        System.out.println("=== Transcript for " + s.getFullName() + " ===");
        double totalPoints = 0;
        int totalCredits = 0;
        for (Mark m : store.getMarks().values()) {
            if (!m.getStudentId().equals(s.getId())) continue;
            Course c = store.getCourses().get(m.getCourseId());
            if (c == null) continue;
            System.out.println("  " + c.getCourseId() + " " + c.getName()
                    + " (" + c.getCredits() + " cr) -> " + m.getLetterGrade()
                    + " (" + m.getGpaPoints() + ")");
            totalPoints += m.getGpaPoints() * c.getCredits();
            totalCredits += c.getCredits();
        }
        double gpa = totalCredits == 0 ? 0 : totalPoints / totalCredits;
        System.out.printf("GPA: %.3f%n", gpa);
    }

    static void rateTeacherFlow(Student s) {
        System.out.print("Teacher ID: ");
        User u = store.getUsers().get(sc.nextLine().trim());
        if (!(u instanceof Teacher)) { System.out.println("Not a teacher."); return; }
        int r = (int) readDouble("Rating (1..5): ", 1, 5);
        ((Teacher) u).rate(s.getId(), r);
        log.log(s.getId(), "RATE_TEACHER " + u.getId() + " " + r);
        System.out.println("Rated.");
    }

    // ============ MANAGER ============
    static void managerMenu(Manager m) {
        while (true) {
            m.showMenu();
            System.out.print("> ");
            String c = sc.nextLine().trim();
            if (c.equals("0")) return;
            else if (c.equals("1")) approveRegistrationFlow();
            else if (c.equals("2")) addCourseFlow();
            else if (c.equals("3")) assignTeacherFlow();
            else if (c.equals("4")) statisticalReport();
            else if (c.equals("5")) addNewsFlow(m);
            else if (c.equals("6")) for (News n : store.getNews()) System.out.println(n);
            else if (c.equals("7")) viewStudentsByGpa();
            else if (c.equals("8")) viewStudentsAlphabetically();
            else if (c.equals("9")) viewTeachers();
            else if (c.equals("10")) viewComplaintsFlow();
            else if (c.equals("11")) sendMessageFlow(m);
            else if (c.equals("12")) addLessonFlow();
            else System.out.println("Invalid choice.");
        }
    }

    static void approveRegistrationFlow() {
        List<RegistrationRequest> pending = new ArrayList<>();
        for (RegistrationRequest r : store.getRequests()) {
            if (!r.isProcessed()) pending.add(r);
        }
        if (pending.isEmpty()) {
            System.out.println("No pending requests.");
            return;
        }
        for (int i = 0; i < pending.size(); i++) {
            System.out.println(i + ") " + pending.get(i));
        }
        System.out.print("Approve index (or empty to cancel): ");
        String idx = sc.nextLine().trim();
        if (idx.isEmpty()) return;
        try {
            int i = Integer.parseInt(idx);
            RegistrationRequest r = pending.get(i);
            Course c = store.getCourses().get(r.getCourseId());
            Student s = (Student) store.getUsers().get(r.getStudentId());
            if (c == null || s == null) { System.out.println("Invalid."); return; }
            if (c.getStudentIds().contains(s.getId())) {
                r.approve();
                System.out.println("Student is already enrolled in this course.");
                return;
            }
            if (s.getFails() >= Student.MAX_FAILS) {
                System.out.println("Cannot approve: student has failed " + s.getFails() + " times.");
                return;
            }
            if (s.getCurrentCredits() + c.getCredits() > Student.MAX_CREDITS) {
                System.out.println("Cannot approve: would exceed " + Student.MAX_CREDITS + " credits.");
                return;
            }

            c.addStudent(s.getId());
            s.addCourse(c.getCourseId());
            s.addCredits(c.getCredits());
            r.approve();
            log.log("manager", "APPROVE_REG " + s.getId() + "/" + c.getCourseId());
            System.out.println("Approved.");
        } catch (Exception e) {
            System.out.println("Invalid index.");
        }
    }

    static void addCourseFlow() {
        System.out.print("Course ID (e.g. CSCI 3160): ");
        String id = sc.nextLine().trim();
        if (store.getCourses().containsKey(id)) {
            System.out.println("Course already exists.");
            return;
        }
        System.out.print("Name: "); String name = sc.nextLine();
        int credits = (int) readDouble("Credits: ", 1, 30);
        Major major = readEnum("Major", Major.values());
        int year = (int) readDouble("Year (1..4): ", 1, 4);
        Course c = new Course(id, name, credits, major, year);
        store.getCourses().put(id, c);
        log.log("manager", "ADD_COURSE " + id);
        System.out.println("Course added.");
    }

    static void addLessonFlow() {
        System.out.print("Course ID: ");
        Course c = store.getCourses().get(sc.nextLine().trim());
        if (c == null) { System.out.println("Course not found."); return; }
        LessonType type = readEnum("Lesson type", LessonType.values());
        String lid = "L-" + (c.getLessons().size() + 1) + "-" + c.getCourseId().replace(" ", "");
        c.addLesson(new Lesson(lid, type, c.getCourseId()));
        log.log("manager", "ADD_LESSON " + lid);
        System.out.println("Lesson added.");
    }

    static void assignTeacherFlow() {
        System.out.print("Course ID: ");
        Course c = store.getCourses().get(sc.nextLine().trim());
        if (c == null) { System.out.println("Course not found."); return; }
        System.out.print("Teacher ID: ");
        User u = store.getUsers().get(sc.nextLine().trim());
        if (!(u instanceof Teacher)) { System.out.println("Not a teacher."); return; }
        c.addTeacher(u.getId());
        ((Teacher) u).addCourse(c.getCourseId());
        log.log("manager", "ASSIGN " + u.getId() + " -> " + c.getCourseId());
        System.out.println("Assigned.");
    }

    static void statisticalReport() {
        System.out.println("=== STATISTICAL REPORT ===");
        java.util.Map<String, List<Mark>> byCourse = new java.util.HashMap<>();
        for (Mark m : store.getMarks().values()) {
            if (!byCourse.containsKey(m.getCourseId())) {
                byCourse.put(m.getCourseId(), new ArrayList<Mark>());
            }
            byCourse.get(m.getCourseId()).add(m);
        }
        if (byCourse.isEmpty()) {
            System.out.println("No marks yet.");
            return;
        }
        for (String cid : byCourse.keySet()) {
            List<Mark> list = byCourse.get(cid);
            double sum = 0;
            int passed = 0, failed = 0;
            for (Mark m : list) {
                sum += m.getTotal();
                if (m.isFailed()) failed++;
                else passed++;
            }
            double avg = sum / list.size();
            System.out.printf("  %s: students=%d avg=%.2f passed=%d failed=%d%n",
                    cid, list.size(), avg, passed, failed);
        }
    }

    static void addNewsFlow(Manager m) {
        System.out.print("Title: "); String title = sc.nextLine();
        System.out.print("Body: "); String body = sc.nextLine();
        News n = new News(title, body, m.getFullName());
        store.getNews().add(n);
        // notify all users (Observer)
        for (User u : store.getUsers().values()) {
            u.onNews(n);
        }
        log.log(m.getId(), "ADD_NEWS");
        System.out.println("News added and " + store.getUsers().size() + " users notified.");
    }

    static void viewStudentsByGpa() {
        List<Student> list = new ArrayList<>();
        for (User u : store.getUsers().values()) {
            if (u instanceof Student) list.add((Student) u);
        }
        list.sort(new Comparator<Student>() {
            @Override
            public int compare(Student a, Student b) {
                return Double.compare(b.getGpa(), a.getGpa());
            }
        });
        for (Student s : list) System.out.println(s);
    }

    static void viewStudentsAlphabetically() {
        List<Student> list = new ArrayList<>();
        for (User u : store.getUsers().values()) {
            if (u instanceof Student) list.add((Student) u);
        }
        java.util.Collections.sort(list); // uses User.compareTo (by last name)
        for (Student s : list) System.out.println(s);
    }

    static void viewTeachers() {
        for (User u : store.getUsers().values()) {
            if (u instanceof Teacher) System.out.println(u);
        }
    }

    static void viewComplaintsFlow() {
        if (store.getComplaints().isEmpty()) {
            System.out.println("No complaints.");
            return;
        }
        for (int i = 0; i < store.getComplaints().size(); i++) {
            System.out.println(i + ") " + store.getComplaints().get(i));
        }
        System.out.print("Sign index (empty to skip): ");
        String idx = sc.nextLine().trim();
        if (idx.isEmpty()) return;
        try {
            store.getComplaints().get(Integer.parseInt(idx)).sign();
            System.out.println("Signed.");
        } catch (Exception e) {
            System.out.println("Invalid index.");
        }
    }

    // ============ RESEARCHER EMPLOYEE ============
    static void researcherEmployeeMenu(ResearcherEmployee r) {
        while (true) {
            r.showMenu();
            System.out.print("> ");
            String c = sc.nextLine().trim();
            if (c.equals("0")) return;
            else if (c.equals("1")) researchMenu(r);
            else if (c.equals("2")) sendMessageFlow(r);
            else System.out.println("Invalid choice.");
        }
    }

    // ============ RESEARCH MENU (shared) ============
    static void researchMenu(Researcher r) {
        while (true) {
            System.out.println("\n[RESEARCH MENU]");
            System.out.println("1. View my papers (by date)");
            System.out.println("2. View my papers (by citations)");
            System.out.println("3. View my papers (by pages)");
            System.out.println("4. Add new paper");
            System.out.println("5. View all papers in university");
            System.out.println("6. Top cited researcher of school");
            System.out.println("7. Top cited researcher of the year");
            System.out.println("8. Create research project");
            System.out.println("9. Join research project");
            System.out.println("10. View research projects");
            System.out.println("11. Add paper to research project");
            System.out.println("0. Back");
            System.out.print("> ");
            String c = sc.nextLine().trim();
            if (c.equals("0")) return;
            else if (c.equals("1")) r.printPapers(PaperComparator.BY_DATE);
            else if (c.equals("2")) r.printPapers(PaperComparator.BY_CITATIONS);
            else if (c.equals("3")) r.printPapers(PaperComparator.BY_PAGES);
            else if (c.equals("4")) addPaperFlow(r);
            else if (c.equals("5")) printAllUniversityPapers();
            else if (c.equals("6")) topCitedOfSchool();
            else if (c.equals("7")) topCitedOfYear();
            else if (c.equals("8")) createProjectFlow(r);
            else if (c.equals("9")) joinProjectFlow(r);
            else if (c.equals("10")) for (ResearchProject p : store.getProjects()) System.out.println(p);
            else if (c.equals("11")) addPaperToProjectFlow(r);
            else System.out.println("Invalid choice.");
        }
    }

    static void addPaperFlow(Researcher r) {
        System.out.print("DOI: "); String doi = sc.nextLine();
        System.out.print("Title: "); String title = sc.nextLine();
        System.out.print("Authors (comma separated): "); String aut = sc.nextLine();
        System.out.print("Journal: "); String j = sc.nextLine();
        int pages = (int) readDouble("Pages: ", 1, 10000);
        int citations = (int) readDouble("Citations: ", 0, 1000000);
        List<String> authors = Arrays.asList(aut.split("\\s*,\\s*"));
        ResearchPaper p = new ResearchPaper(doi, title, authors, j, pages, LocalDate.now(), citations);
        r.addPaper(p);
        System.out.println("Paper added.");
    }

    static void addPaperToProjectFlow(Researcher r) {
        if (r.getPapers().isEmpty()) {
            System.out.println("You have no papers to add. Add a paper first (option 4).");
            return;
        }
        if (store.getProjects().isEmpty()) {
            System.out.println("No projects. Create one first (option 8).");
            return;
        }
        for (int i = 0; i < store.getProjects().size(); i++) {
            System.out.println(i + ") " + store.getProjects().get(i));
        }
        System.out.print("Project index: ");
        ResearchProject p;
        try {
            p = store.getProjects().get(Integer.parseInt(sc.nextLine().trim()));
        } catch (Exception e) {
            System.out.println("Invalid index.");
            return;
        }
        for (int i = 0; i < r.getPapers().size(); i++) {
            System.out.println(i + ") " + r.getPapers().get(i));
        }
        System.out.print("Paper index: ");
        try {
            ResearchPaper rp = r.getPapers().get(Integer.parseInt(sc.nextLine().trim()));
            p.addPaper(rp);
            System.out.println("Paper added to project.");
        } catch (Exception e) {
            System.out.println("Invalid index.");
        }
    }

    static void printAllUniversityPapers() {
        System.out.println("Sort: 1.date 2.citations 3.pages");
        String c = sc.nextLine().trim();
        Comparator<ResearchPaper> cmp;
        if (c.equals("2")) cmp = PaperComparator.BY_CITATIONS;
        else if (c.equals("3")) cmp = PaperComparator.BY_PAGES;
        else cmp = PaperComparator.BY_DATE;

        List<ResearchPaper> all = collectAllPapers();
        all.sort(cmp);
        System.out.println("=== All papers in university (" + all.size() + ") ===");
        for (ResearchPaper p : all) System.out.println("  " + p);
    }

    static List<ResearchPaper> collectAllPapers() {
        List<ResearchPaper> all = new ArrayList<>();
        java.util.Set<String> seenDois = new java.util.HashSet<>();
        for (User u : store.getUsers().values()) {
            if (u instanceof Researcher) {
                for (ResearchPaper p : ((Researcher) u).getPapers()) {
                    if (seenDois.add(p.getDoi())) all.add(p);
                }
            }
        }
        return all;
    }

    static void topCitedOfSchool() {
        School s = readEnum("School", School.values());
        Researcher top = null;
        for (User u : store.getUsers().values()) {
            if (!(u instanceof Researcher)) continue;
            School us = null;
            if (u instanceof Teacher) us = ((Teacher) u).getSchool();
            else if (u instanceof Student) us = ((Student) u).getSchool();
            if (us != s) continue;
            Researcher r = (Researcher) u;
            if (top == null || r.getTotalCitations() > top.getTotalCitations()) top = r;
        }
        if (top == null) System.out.println("No researchers in this school.");
        else System.out.println("Top cited in " + s + ": " + top.getResearcherName()
                + " citations=" + top.getTotalCitations());
    }

    static void topCitedOfYear() {
        Researcher top = null;
        for (User u : store.getUsers().values()) {
            if (!(u instanceof Researcher)) continue;
            Researcher r = (Researcher) u;
            if (top == null || r.getTotalCitations() > top.getTotalCitations()) top = r;
        }
        if (top == null) System.out.println("No researchers.");
        else System.out.println("Top cited researcher of the year: " + top.getResearcherName()
                + " citations=" + top.getTotalCitations());
    }

    static void createProjectFlow(Researcher r) {
        System.out.print("Topic: ");
        String topic = sc.nextLine();
        String pid = "RP-" + (store.getProjects().size() + 1);
        ResearchProject p = new ResearchProject(pid, topic);
        try {
            p.addParticipant(r);
        } catch (NotResearcherException e) {
            System.out.println(e.getMessage());
            return;
        }
        store.getProjects().add(p);
        log.log(r.getResearcherId(), "NEW_PROJECT " + pid);
        System.out.println("Created: " + p);
    }

    static void joinProjectFlow(Researcher r) {
        if (store.getProjects().isEmpty()) {
            System.out.println("No projects.");
            return;
        }
        for (int i = 0; i < store.getProjects().size(); i++) {
            System.out.println(i + ") " + store.getProjects().get(i));
        }
        System.out.print("Project index: ");
        try {
            ResearchProject p = store.getProjects().get(Integer.parseInt(sc.nextLine().trim()));
            p.addParticipant(r);
            System.out.println("Joined.");
        } catch (NotResearcherException e) {
            System.out.println("Cannot join: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Invalid index.");
        }
    }

    // ============ HELPERS ============
    static double readDouble(String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                double v = Double.parseDouble(line);
                if (v < min || v > max) {
                    System.out.println("Must be between " + min + " and " + max);
                    continue;
                }
                return v;
            } catch (NumberFormatException e) {
                System.out.println("Enter a number.");
            }
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    static <T extends Enum<T>> T readEnum(String name, T[] values) {
        while (true) {
            System.out.println(name + " options: " + Arrays.toString(values));
            System.out.print(name + ": ");
            String s = sc.nextLine().trim().toUpperCase();
            for (T v : values) {
                if (v.name().equals(s)) return v;
            }
            System.out.println("Invalid " + name + ".");
        }
    }

    // ============ SEED DATA ============
    static void seedData() {
        System.out.println("Seeding initial data...");

        Admin admin = new Admin("A001", "Aigerim", "Sultan", "admin@kbtu.kz", "admin", 500000);
        Manager mgr = new Manager("M001", "Bauyrzhan", "Erlanov", "mgr@kbtu.kz", "mgr", 400000, ManagerType.OR);

        Teacher prof = new Teacher("T001", "Khaled", "Mohamad", "khaled@kbtu.kz", "prof",
                700000, TeacherTitle.PROFESSOR, School.SITE);
        Teacher senior = new Teacher("T002", "Madina", "Asanova", "madina@kbtu.kz", "snr",
                500000, TeacherTitle.SENIOR_LECTURER, School.SITE);
        Teacher tutor = new Teacher("T003", "Daniyar", "Bekov", "daniyar@kbtu.kz", "tutor",
                350000, TeacherTitle.TUTOR, School.SITE);
        senior.makeResearcher();  // senior lecturer who is also researcher

        Student s1 = new Student("S001", "Alibek", "Ali", "estiyar@kbtu.kz", "s1",
                Major.IS, School.SITE, 4);
        Student s2 = new Student("S002", "Aigerim", "Tlegenova", "aigerim@kbtu.kz", "s2",
                Major.SE, School.SITE, 3);
        Student s3 = new Student("S003", "Nursultan", "Imanov", "nursultan@kbtu.kz", "s3",
                Major.CS, School.SITE, 2);
        s1.makeResearcher();

        ResearcherEmployee re = new ResearcherEmployee("R001", "Yerlan", "Kaisar",
                "yerlan@kbtu.kz", "res", 600000);

        store.getUsers().put(admin.getId(), admin);
        store.getUsers().put(mgr.getId(), mgr);
        store.getUsers().put(prof.getId(), prof);
        store.getUsers().put(senior.getId(), senior);
        store.getUsers().put(tutor.getId(), tutor);
        store.getUsers().put(s1.getId(), s1);
        store.getUsers().put(s2.getId(), s2);
        store.getUsers().put(s3.getId(), s3);
        store.getUsers().put(re.getId(), re);

        // Papers for professor (high h-index)
        prof.addPaper(new ResearchPaper("10.1/p1", "Deep Learning for Edge Computing",
                Arrays.asList("Khaled Mohamad", "Yerlan Kaisar"), "IEEE Access",
                15, LocalDate.of(2022, 5, 20), 45));
        prof.addPaper(new ResearchPaper("10.1/p2", "Federated Learning Survey",
                Arrays.asList("Khaled Mohamad"), "IEEE TPAMI",
                28, LocalDate.of(2023, 3, 10), 120));
        prof.addPaper(new ResearchPaper("10.1/p3", "Transformer Optimization",
                Arrays.asList("Khaled Mohamad", "Madina Asanova"), "IEEE TNNLS",
                20, LocalDate.of(2024, 9, 1), 30));
        senior.addPaper(new ResearchPaper("10.1/p3", "Transformer Optimization",
                Arrays.asList("Khaled Mohamad", "Madina Asanova"), "IEEE TNNLS",
                20, LocalDate.of(2024, 9, 1), 30));
        re.addPaper(new ResearchPaper("10.1/p4", "Cloud Architectures for Universities",
                Arrays.asList("Yerlan Kaisar"), "IEEE Access",
                12, LocalDate.of(2021, 7, 4), 60));
        s1.addPaper(new ResearchPaper("10.1/p5", "Recommendation Systems",
                Arrays.asList("Alibek Ali"), "IEEE SIVR",
                8, LocalDate.of(2024, 11, 15), 2));

        // Assign supervisor for s1 (4th year)
        try {
            s1.setSupervisor(prof);
        } catch (LowHIndexException e) {
            System.out.println("Could not set supervisor: " + e.getMessage());
        }

        // Courses
        Course c1 = new Course("CSCI 3160", "Object-Oriented Programming", 6, Major.IS, 2);
        Course c2 = new Course("CSCI 4160", "Distributed Systems", 6, Major.IS, 4);
        Course c3 = new Course("CSCI 3120", "Databases", 6, Major.SE, 3);
        store.getCourses().put(c1.getCourseId(), c1);
        store.getCourses().put(c2.getCourseId(), c2);
        store.getCourses().put(c3.getCourseId(), c3);

        c1.addTeacher("T001"); prof.addCourse("CSCI 3160");
        c1.addTeacher("T002"); senior.addCourse("CSCI 3160");
        c2.addTeacher("T001"); prof.addCourse("CSCI 4160");
        c3.addTeacher("T003"); tutor.addCourse("CSCI 3120");

        // lessons
        c1.addLesson(new Lesson("L-1-3160", LessonType.LECTURE, "CSCI 3160"));
        c1.addLesson(new Lesson("L-2-3160", LessonType.PRACTICE, "CSCI 3160"));
        c2.addLesson(new Lesson("L-1-4160", LessonType.LECTURE, "CSCI 4160"));
        c2.addLesson(new Lesson("L-2-4160", LessonType.PRACTICE, "CSCI 4160"));
        c3.addLesson(new Lesson("L-1-3120", LessonType.LECTURE, "CSCI 3120"));

        store.save();
        System.out.println("Seed complete. Logins:");
        System.out.println("  A001/admin  M001/mgr  T001/prof  T002/snr  T003/tutor");
        System.out.println("  S001/s1  S002/s2  S003/s3  R001/res");
    }
}
