package com.university;

import java.io.Serializable;

public class Lesson implements Serializable {
    private String lessonId;
    private LessonType type;
    private String courseId;

    public Lesson(String lessonId, LessonType type, String courseId) {
        this.lessonId = lessonId;
        this.type = type;
        this.courseId = courseId;
    }

    public String getLessonId() { return lessonId; }
    public LessonType getType() { return type; }
    public String getCourseId() { return courseId; }

    @Override
    public String toString() {
        return "Lesson " + lessonId + " " + type + " for " + courseId;
    }
}
