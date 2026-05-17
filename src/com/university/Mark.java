package com.university;

import java.io.Serializable;

public class Mark implements Serializable {
    private String studentId;
    private String courseId;
    private double firstAttestation;   // 0..30
    private double secondAttestation;  // 0..30
    private double finalExam;          // 0..40

    public Mark(String studentId, String courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
    }

    public String getStudentId() { return studentId; }
    public String getCourseId() { return courseId; }
    public double getFirstAttestation() { return firstAttestation; }
    public double getSecondAttestation() { return secondAttestation; }
    public double getFinalExam() { return finalExam; }

    public void setFirstAttestation(double v) { this.firstAttestation = v; }
    public void setSecondAttestation(double v) { this.secondAttestation = v; }
    public void setFinalExam(double v) { this.finalExam = v; }

    public double getTotal() {
        return firstAttestation + secondAttestation + finalExam;
    }

    public String getLetterGrade() {
        double t = getTotal();
        if (t >= 90) return "A";
        if (t >= 85) return "A-";
        if (t >= 80) return "B+";
        if (t >= 75) return "B";
        if (t >= 70) return "B-";
        if (t >= 65) return "C+";
        if (t >= 60) return "C";
        if (t >= 55) return "C-";
        if (t >= 50) return "D";
        return "F";
    }

    public double getGpaPoints() {
        String g = getLetterGrade();
        if (g.equals("A"))  return 4.0;
        if (g.equals("A-")) return 3.67;
        if (g.equals("B+")) return 3.33;
        if (g.equals("B"))  return 3.0;
        if (g.equals("B-")) return 2.67;
        if (g.equals("C+")) return 2.33;
        if (g.equals("C"))  return 2.0;
        if (g.equals("C-")) return 1.67;
        if (g.equals("D"))  return 1.0;
        return 0;
    }

    public boolean isFailed() {
        return getTotal() < 50;
    }

    @Override
    public String toString() {
        return "Mark " + courseId + ": 1st=" + firstAttestation
                + " 2nd=" + secondAttestation + " final=" + finalExam
                + " total=" + getTotal() + " grade=" + getLetterGrade();
    }
}
