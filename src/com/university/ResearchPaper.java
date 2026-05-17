package com.university;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class ResearchPaper implements Serializable, Comparable<ResearchPaper> {
    private String doi;
    private String title;
    private List<String> authors;
    private String journal;
    private int pages;
    private LocalDate datePublished;
    private int citations;

    public ResearchPaper(String doi, String title, List<String> authors, String journal,
                         int pages, LocalDate datePublished, int citations) {
        this.doi = doi;
        this.title = title;
        this.authors = authors;
        this.journal = journal;
        this.pages = pages;
        this.datePublished = datePublished;
        this.citations = citations;
    }

    public String getDoi() { return doi; }
    public String getTitle() { return title; }
    public List<String> getAuthors() { return authors; }
    public String getJournal() { return journal; }
    public int getPages() { return pages; }
    public LocalDate getDatePublished() { return datePublished; }
    public int getCitations() { return citations; }

    @Override
    public int compareTo(ResearchPaper o) {
        // default: by date
        return this.datePublished.compareTo(o.datePublished);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResearchPaper)) return false;
        ResearchPaper p = (ResearchPaper) o;
        return Objects.equals(doi, p.doi);
    }

    @Override
    public int hashCode() {
        return Objects.hash(doi);
    }

    @Override
    public String toString() {
        return "\"" + title + "\" by " + authors + " | " + journal
                + " | pp." + pages + " | " + datePublished + " | citations=" + citations
                + " | DOI: " + doi;
    }
}
