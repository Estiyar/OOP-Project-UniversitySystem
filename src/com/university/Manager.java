package com.university;

public class Manager extends Employee {
    private ManagerType type;

    public Manager(String id, String firstName, String lastName, String email, String password,
                   double salary, ManagerType type) {
        super(id, firstName, lastName, email, password, salary);
        this.type = type;
    }

    public ManagerType getType() { return type; }

    @Override
    public void showMenu() {
        System.out.println("\n[MANAGER MENU]");
        System.out.println("1. Approve student registration");
        System.out.println("2. Add course");
        System.out.println("3. Assign course to teacher");
        System.out.println("4. Statistical report (marks)");
        System.out.println("5. Add news");
        System.out.println("6. View news");
        System.out.println("7. View students sorted by GPA");
        System.out.println("8. View students alphabetically");
        System.out.println("9. View teachers");
        System.out.println("10. View complaints (sign)");
        System.out.println("11. Send message");
        System.out.println("12. Add lesson to course");
        System.out.println("0. Logout");
    }

    @Override
    public String toString() {
        return "Manager " + super.toString() + " | " + type;
    }
}
