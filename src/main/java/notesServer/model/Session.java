package notesServer.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Session {

    private User user;
    private String cookie;
    private LocalDateTime date;

    public Session() {
    }

    public Session(User user, String cookie) {
        this.user = user;
        this.cookie = cookie;
    }

    public Session(User user, String cookie, LocalDateTime date) {
        this(user, cookie);
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Session)) return false;
        Session session = (Session) o;
        return Objects.equals(user, session.user) &&
                Objects.equals(cookie, session.cookie) &&
                Objects.equals(date, session.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, cookie, date);
    }

    @Override
    public String toString() {
        return "Session{" +
                "user=" + user +
                ", cookie='" + cookie + '\'' +
                ", date=" + date +
                '}';
    }
}
