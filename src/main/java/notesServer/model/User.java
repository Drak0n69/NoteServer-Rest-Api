package notesServer.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class User {

    private long id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String login;
    private String password;
    private LocalDateTime timeRegistered;
    private boolean online;
    private boolean deleted;
    private boolean isSuper;
    private int rating;

    public User() {
    }

    public User(String firstName, String lastName, String patronymic, String login, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.login = login;
        this.password = password;
        this.timeRegistered = LocalDateTime.now().withNano(0);
        this.online = true;
    }

    public User(long id, String firstName, String lastName, String patronymic, String login, String password) {
        this(firstName, lastName, patronymic, login, password);
        this.id = id;
    }

    public User(long id, String firstName, String lastName, String patronymic, String login, String password,
                LocalDateTime timeRegistered, boolean online, boolean deleted, boolean isSuper, int rating) {
        this(id, firstName, lastName, patronymic, login, password);
        this.timeRegistered = timeRegistered;
        this.online = online;
        this.deleted = deleted;
        this.isSuper = isSuper;
        this.rating = rating;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getTimeRegistered() {
        return timeRegistered;
    }

    public void setTimeRegistered(LocalDateTime timeRegistered) {
        this.timeRegistered = timeRegistered;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public boolean isSuper() {
        return isSuper;
    }

    public void setSuper(boolean aSuper) {
        isSuper = aSuper;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return id == user.id &&
                online == user.online &&
                deleted == user.deleted &&
                isSuper == user.isSuper &&
                rating == user.rating &&
                Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName) &&
                Objects.equals(patronymic, user.patronymic) &&
                Objects.equals(login, user.login) &&
                Objects.equals(password, user.password) &&
                Objects.equals(timeRegistered, user.timeRegistered);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, patronymic, login, password, timeRegistered, online, deleted, isSuper, rating);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", timeRegistered=" + timeRegistered +
                ", online=" + online +
                ", deleted=" + deleted +
                ", isSuper=" + isSuper +
                ", rating=" + rating +
                '}';
    }
}
