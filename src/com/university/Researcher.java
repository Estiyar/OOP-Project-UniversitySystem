package com.university;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

public interface Researcher extends Serializable {
    String getResearcherId();
    String getResearcherName();
    List<ResearchPaper> getPapers();
    void addPaper(ResearchPaper p);
    int getHIndex();
    int getTotalCitations();
    void printPapers(Comparator<ResearchPaper> c);
}
