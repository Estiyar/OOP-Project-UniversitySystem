package com.university;

// Factory pattern
public class UserFactory {

    public static User createUser(String type, String id, String firstName, String lastName,
                                  String email, String password) {
        if (type.equalsIgnoreCase("admin")) {
            return new Admin(id, firstName, lastName, email, password, 500000);
        }
        if (type.equalsIgnoreCase("manager")) {
            return new Manager(id, firstName, lastName, email, password, 400000, ManagerType.OR);
        }
        if (type.equalsIgnoreCase("teacher")) {
            return new Teacher(id, firstName, lastName, email, password, 500000,
                    TeacherTitle.LECTURER, School.SITE);
        }
        if (type.equalsIgnoreCase("student")) {
            return new Student(id, firstName, lastName, email, password, Major.IS, School.SITE, 1);
        }
        if (type.equalsIgnoreCase("researcher")) {
            return new ResearcherEmployee(id, firstName, lastName, email, password, 600000);
        }
        return null;
    }
}
