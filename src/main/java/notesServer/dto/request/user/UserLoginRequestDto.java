package notesServer.dto.request.user;

import notesServer.dto.validator.Login;
import notesServer.dto.validator.Password;

import java.util.Objects;

public class UserLoginRequestDto {

    @Login
    private String login;
    @Password
    private String password;

    public UserLoginRequestDto() {
    }

    public UserLoginRequestDto(String login, String password) {
        this.login = login;
        this.password = password;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserLoginRequestDto)) return false;
        UserLoginRequestDto that = (UserLoginRequestDto) o;
        return Objects.equals(login, that.login) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, password);
    }

    @Override
    public String toString() {
        return "UserLoginRequestDto{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
