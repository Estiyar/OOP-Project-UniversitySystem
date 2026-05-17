package com.university;

import java.io.Serializable;

public class RegistrationRequest implements Serializable {
    private String studentId;
    private String courseId;
    private boolean approved;
    private boolean processed;

    public RegistrationRequest(String studentId, String courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.approved = false;
        this.processed = false;
    }

    public String getStudentId() { return studentId; }
    public String getCourseId() { return courseId; }
    public boolean isApproved() { return approved; }
    public boolean isProcessed() { return processed; }

    public void approve() {
        this.approved = true;
        this.processed = true;
    }

    public void reject() {
        this.approved = false;
        this.processed = true;
    }

    @Override
    public String toString() {
        String status;
        if (!processed) status = "PENDING";
        else if (approved) status = "APPROVED";
        else status = "REJECTED";
        return "Request: student=" + studentId + " course=" + courseId + " [" + status + "]";
    }
}
