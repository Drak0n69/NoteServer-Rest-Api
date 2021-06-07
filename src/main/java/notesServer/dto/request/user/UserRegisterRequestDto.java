package notesServer.dto.request.user;

import notesServer.dto.validator.Login;
import notesServer.dto.validator.Password;
import notesServer.dto.validator.UserName;
import org.hibernate.validator.constraints.Length;

import java.util.Objects;

public class UserRegisterRequestDto {

    @UserName
    private String firstName;
    @UserName
    private String lastName;
    @UserName
    private String patronymic;
    @Login
    private String login;
    @Password
    private String password;

    public UserRegisterRequestDto() {
    }

    public UserRegisterRequestDto(String firstName, String lastName, @Length(max = 20) String patronymic,
                                  String login, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.login = login;
        this.password = password;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRegisterRequestDto)) return false;
        UserRegisterRequestDto that = (UserRegisterRequestDto) o;
        return Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(patronymic, that.patronymic) && Objects.equals(login, that.login) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, patronymic, login, password);
    }

    @Override
    public String toString() {
        return "UserRegisterRequestDto{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
