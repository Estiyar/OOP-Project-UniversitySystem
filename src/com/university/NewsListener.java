package com.university;

import java.io.Serializable;

// Observer pattern
public interface NewsListener extends Serializable {
    void onNews(News news);
}
