package com.wukong.common.model;

import lombok.Data;
import java.io.Serializable;
import java.util.Objects;

@Data
public class NewsBO implements Serializable {

    private String url;

    private String title;

    private String time;

    private String source;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewsBO newsBO = (NewsBO) o;
        return title.equals(newsBO.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }
}
