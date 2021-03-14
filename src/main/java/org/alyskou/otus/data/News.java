package org.alyskou.otus.data;

import org.springframework.jdbc.core.RowMapper;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Objects;

public class News implements Serializable {
    @NotNull
    @NotEmpty
    private String authorEmail;

    private Instant timestamp;

    @NotNull
    @NotEmpty
    private String text;

    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        News news = (News) o;
        return timestamp == news.timestamp && authorEmail.equals(news.authorEmail) && text.equals(news.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorEmail, timestamp, text);
    }

    public static class NewsRowMapper implements RowMapper<News> {
        @Override
        public News mapRow(ResultSet resultSet, int i) throws SQLException {
            News news = new News();
            news.setAuthorEmail(resultSet.getString("author_email"));
            news.setTimestamp(resultSet.getTimestamp("timestamp").toInstant());
            news.setText(resultSet.getString("text"));
            return news;
        }
    }
}
