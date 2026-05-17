package com.university;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Course implements Serializable {
    private String courseId;
    private String name;
    private int credits;
    private Major major;
    private int year;
    private List<String> teacherIds;       // many instructors per course
    private List<String> studentIds;
    private List<Lesson> lessons;

    public Course(String courseId, String name, int credits, Major major, int year) {
        this.courseId = courseId;
        this.name = name;
        this.credits = credits;
        this.major = major;
        this.year = year;
        this.teacherIds = new ArrayList<>();
        this.studentIds = new ArrayList<>();
        this.lessons = new ArrayList<>();
    }

    public String getCourseId() { return courseId; }
    public String getName() { return name; }
    public int getCredits() { return credits; }
    public Major getMajor() { return major; }
    public int getYear() { return year; }
    public List<String> getTeacherIds() { return teacherIds; }
    public List<String> getStudentIds() { return studentIds; }
    public List<Lesson> getLessons() { return lessons; }

    public void addTeacher(String teacherId) {
        if (!teacherIds.contains(teacherId)) teacherIds.add(teacherId);
    }

    public void addStudent(String studentId) {
        if (!studentIds.contains(studentId)) studentIds.add(studentId);
    }

    public void addLesson(Lesson l) {
        lessons.add(l);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course)) return false;
        Course c = (Course) o;
        return Objects.equals(courseId, c.courseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId);
    }

    @Override
    public String toString() {
        return "Course " + courseId + " " + name + " (" + credits + " cr, "
                + major + ", year " + year + ") teachers=" + teacherIds.size()
                + " students=" + studentIds.size() + " lessons=" + lessons.size();
    }
}
