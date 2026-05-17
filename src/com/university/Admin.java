package com.university;

public class Admin extends Employee {

    public Admin(String id, String firstName, String lastName, String email, String password,
                 double salary) {
        super(id, firstName, lastName, email, password, salary);
    }

    @Override
    public void showMenu() {
        System.out.println("\n[ADMIN MENU]");
        System.out.println("1. Add user");
        System.out.println("2. Remove user");
        System.out.println("3. Update user");
        System.out.println("4. View all users");
        System.out.println("5. View log file");
        System.out.println("0. Logout");
    }

    @Override
    public String toString() {
        return "Admin " + super.toString();
    }
}
