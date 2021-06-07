package notesServer.dto.request.user;

import notesServer.dto.validator.Password;
import notesServer.dto.validator.UserName;

import java.util.Objects;

public class UserChangePasswordRequestDto {

    @UserName
    private String firstName;
    @UserName
    private String lastName;
    @UserName
    private String patronymic;
    @Password
    private String oldPassword;
    @Password
    private String newPassword;

    public UserChangePasswordRequestDto() {
    }

    public UserChangePasswordRequestDto(String firstName, String lastName, String patronymic, String oldPassword,
                                        String newPassword) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
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

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserChangePasswordRequestDto)) return false;
        UserChangePasswordRequestDto that = (UserChangePasswordRequestDto) o;
        return Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(patronymic, that.patronymic) && Objects.equals(oldPassword, that.oldPassword) && Objects.equals(newPassword, that.newPassword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, patronymic, oldPassword, newPassword);
    }

    @Override
    public String toString() {
        return "UserChangePasswordRequestDto{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", oldPassword='" + oldPassword + '\'' +
                ", newPassword='" + newPassword + '\'' +
                '}';
    }
}
