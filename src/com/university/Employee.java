package com.university;

public abstract class Employee extends User {
    protected double salary;

    public Employee(String id, String firstName, String lastName, String email, String password, double salary) {
        super(id, firstName, lastName, email, password);
        this.salary = salary;
    }

    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }

    public void sendMessage(User to, String text) {
        Message m = new Message(this.getFullName(), to.getFullName(), text);
        to.receiveMessage(m);
    }

    public void sendComplaint(String topic, String content) {
        Complaint c = new Complaint(this.getFullName(), topic, content);
        DataStore.getInstance().getComplaints().add(c);
    }
}
