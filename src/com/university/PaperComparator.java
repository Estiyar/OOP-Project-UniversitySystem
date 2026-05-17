package com.university;

import java.util.Comparator;

public class PaperComparator {

    public static final Comparator<ResearchPaper> BY_DATE = new Comparator<ResearchPaper>() {
        @Override
        public int compare(ResearchPaper a, ResearchPaper b) {
            return b.getDatePublished().compareTo(a.getDatePublished()); // newest first
        }
    };

    public static final Comparator<ResearchPaper> BY_CITATIONS = new Comparator<ResearchPaper>() {
        @Override
        public int compare(ResearchPaper a, ResearchPaper b) {
            return b.getCitations() - a.getCitations(); // most cited first
        }
    };

    public static final Comparator<ResearchPaper> BY_PAGES = new Comparator<ResearchPaper>() {
        @Override
        public int compare(ResearchPaper a, ResearchPaper b) {
            return b.getPages() - a.getPages();
        }
    };
}
