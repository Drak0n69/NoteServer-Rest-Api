package notesServer.dto.response.user;

import java.time.LocalDateTime;

public class UserInfoResponseDto {

    private long id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String login;
    private LocalDateTime timeRegistered;
    private boolean online;
    private boolean deleted;
    private int rating;
    private boolean isSuper;

    public UserInfoResponseDto(String firstName, String lastName, String patronymic, String login) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.login = login;
    }

    public UserInfoResponseDto(long id, String firstName, String lastName, String patronymic, String login) {
        this(firstName, lastName, patronymic, login);
        this.id = id;
    }

    public UserInfoResponseDto(long id, String firstName, String lastName, String patronymic, String login,
                               LocalDateTime timeRegistered, boolean online, boolean deleted, int rating) {
        this(id, firstName, lastName, patronymic, login);
        this.timeRegistered = timeRegistered;
        this.online = online;
        this.deleted = deleted;
        this.rating = rating;
    }

    public UserInfoResponseDto(long id, String firstName, String lastName, String patronymic, String login,
                               LocalDateTime timeRegistered, boolean online, boolean deleted, int rating, boolean isSuper) {
        this(id, firstName, lastName, patronymic, login, timeRegistered, online, deleted, rating);
        this.isSuper = isSuper;
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
    public String toString() {
        return "UserInfoResponseDto{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", login='" + login + '\'' +
                ", timeRegistered=" + timeRegistered +
                ", online=" + online +
                ", deleted=" + deleted +
                ", rating=" + rating +
                ", isSuper=" + isSuper +
                '}';
    }
}
