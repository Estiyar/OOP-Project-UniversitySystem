package com.university;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ResearchProject implements Serializable {
    private String projectId;
    private String topic;
    private List<ResearchPaper> papers;
    private List<Researcher> participants;

    public ResearchProject(String projectId, String topic) {
        this.projectId = projectId;
        this.topic = topic;
        this.papers = new ArrayList<>();
        this.participants = new ArrayList<>();
    }

    public void addParticipant(Object candidate) throws NotResearcherException {
        if (!(candidate instanceof Researcher)) {
            throw new NotResearcherException("This user is not a Researcher and cannot join the project.");
        }
        if (candidate instanceof Teacher && !((Teacher) candidate).isResearcher()) {
            throw new NotResearcherException("This teacher is not marked as a Researcher.");
        }
        if (candidate instanceof Student && !((Student) candidate).isResearcher()) {
            throw new NotResearcherException("This student is not marked as a Researcher.");
        }
        Researcher r = (Researcher) candidate;
        if (!participants.contains(r)) {
            participants.add(r);
        }
    }

    public void addPaper(ResearchPaper p) {
        papers.add(p);
    }

    public String getProjectId() { return projectId; }
    public String getTopic() { return topic; }
    public List<ResearchPaper> getPapers() { return papers; }
    public List<Researcher> getParticipants() { return participants; }

    @Override
    public String toString() {
        return "ResearchProject[" + projectId + "] topic=\"" + topic
                + "\" papers=" + papers.size() + " participants=" + participants.size();
    }
}
