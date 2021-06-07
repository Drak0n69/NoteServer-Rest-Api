package notesServer.dto.request.user;

import notesServer.dto.validator.Password;

import java.util.Objects;

public class UserDeleteRequestDto {

    @Password
    private String password;

    public UserDeleteRequestDto() {
    }

    public UserDeleteRequestDto(String password) {
        this.password = password;
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
        if (!(o instanceof UserDeleteRequestDto)) return false;
        UserDeleteRequestDto that = (UserDeleteRequestDto) o;
        return Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password);
    }

    @Override
    public String toString() {
        return "UserDeleteRequestDto{" +
                "password='" + password + '\'' +
                '}';
    }
}
