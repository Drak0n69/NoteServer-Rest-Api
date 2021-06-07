package notesServer.dto.request.user;

import notesServer.dto.validator.Login;

public class AddUserByLoginRequestDto {

    @Login
    private String login;

    public AddUserByLoginRequestDto(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String toString() {
        return "UserAddFollowingsRequestDto{" +
                "login='" + login + '\'' +
                '}';
    }
}
