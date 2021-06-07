package notesServer.service;

import notesServer.database.dao.iface.AccountDao;
import notesServer.database.dao.iface.SessionDao;
import notesServer.dto.request.user.UserChangePasswordRequestDto;
import notesServer.dto.request.user.UserDeleteRequestDto;
import notesServer.dto.request.user.UserLoginRequestDto;
import notesServer.dto.request.user.UserRegisterRequestDto;
import notesServer.dto.response.user.UserInfoResponseDto;
import notesServer.erros.ServerException;
import notesServer.model.Session;
import notesServer.model.User;
import notesServer.util.Settings;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest
public class TestAccountService {

    @MockBean
    private AccountDao accountDao;
    @MockBean
    private SessionDao sessionDao;
    private AccountService accountService;
    private Settings settings;

    @Autowired
    public TestAccountService(AccountService accountService, Settings settings) {
        this.accountService = accountService;
        this.settings = settings;
    }

    private String getUIID() {
        return UUID.randomUUID().toString();
    }

    private User getUser() {
        return new User("firstname", "lastname", "secondname", "login",
                "password");
    }

    private User getUser(String login) {
        return new User("firstname", "lastname", "secondname", login,
                "password");
    }

    @Test
    void registerUser() throws ServerException {
        User user = getUser();
        String cookie = getUIID();
        when(accountDao.insertUser(any(User.class))).thenReturn(user);
        UserRegisterRequestDto urrdto = new UserRegisterRequestDto("fName", "lName",
                "patr", "login", "pass");
        UserInfoResponseDto uirDto = accountService.registerUser(urrdto, cookie);
        assertEquals(user.getLogin(), uirDto.getLogin());
        verify(accountDao, times(1)).insertUser(any(User.class));
        verify(sessionDao, times(1)).insertSession(any(Session.class));
    }

    @Test
    void loginUser() throws ServerException {
        User user = getUser();
        UserLoginRequestDto ulrDto = new UserLoginRequestDto("login", "password");
        when(accountDao.getUserByLogin(user.getLogin())).thenReturn(user);
        accountService.login(ulrDto, "cookie");
        verify(sessionDao, times(1)).insertSession(any(Session.class));
    }

    @Test
    void logoutUser() throws ServerException {
        accountService.logout("cookie");
        verify(sessionDao, times(1)).deleteSession(anyString());
    }

    @Test
    void failLoginUserWrongLogin() throws ServerException {
        User user = getUser();
        when(accountDao.getUserByLogin(anyString())).thenReturn(user);
        UserLoginRequestDto ulrDto = new UserLoginRequestDto("wrong", "password");
        assertThrows(ServerException.class, () -> accountService.login(ulrDto, "cookie"));
    }

    @Test
    void failLoginUserWrongPassword() throws ServerException {
        User user = getUser();
        when(accountDao.getUserByLogin(user.getLogin())).thenReturn(user);
        UserLoginRequestDto ulrDto = new UserLoginRequestDto("login", "wrong");
        assertThrows(ServerException.class, () -> accountService.login(ulrDto, "cookie"));
    }

    @Test
    void failLoginUserIsDeleted() throws ServerException {
        User user = getUser();
        when(accountDao.getUserByLogin(anyString())).thenReturn(user);
        UserLoginRequestDto ulrDto = new UserLoginRequestDto("login", "password");
        user.setDeleted(true);
        assertThrows(ServerException.class, () -> accountService.login(ulrDto, "cookie"));
    }

    @Test
    void getUserInfo() throws ServerException {
        User user = getUser();
        when(sessionDao.getSessionByCookie("cookie")).thenReturn(new Session(user, "cookie", LocalDateTime.now()));
        UserInfoResponseDto uirDto = accountService.getUserInfo("cookie");
        assertEquals(user.getPatronymic(), uirDto.getPatronymic());
    }

    @Test
    void deleteUser() throws ServerException {
        User user = getUser();
        when(sessionDao.getSessionByCookie("cookie")).thenReturn(new Session(user, "cookie", LocalDateTime.now()));
        UserDeleteRequestDto udrDto = new UserDeleteRequestDto("password");
        accountService.deleteUser(udrDto, "cookie");
        verify(accountDao, times(1)).deleteUser(user);
        verify(sessionDao, times(1)).deleteSession("cookie");
    }

    @Test
    void failDeleteUserIsDeleted() throws ServerException {
        User user = getUser();
        user.setDeleted(true);
        UserDeleteRequestDto udrDto = new UserDeleteRequestDto("password");
        when(sessionDao.getSessionByCookie("cookie")).thenReturn(new Session(user, "cookie", LocalDateTime.now()));
        assertThrows(ServerException.class, () -> accountService.deleteUser(udrDto, "cookie"));
    }

    @Test
    void failDeleteUserWrongPassword() throws ServerException {
        User user = getUser();
        UserDeleteRequestDto udrDto = new UserDeleteRequestDto("wrong");
        when(sessionDao.getSessionByCookie("cookie")).thenReturn(new Session(user, "cookie", LocalDateTime.now()));
        assertThrows(ServerException.class, () -> accountService.deleteUser(udrDto, "cookie"));
    }

    @Test
    void changePassword() throws ServerException {
        User user = getUser();
        UserChangePasswordRequestDto ucprDto = new UserChangePasswordRequestDto("firstname", "lastname",
                "secondname", "password", "newpass");
        when(sessionDao.getSessionByCookie("cookie")).thenReturn(new Session(user, "cookie", LocalDateTime.now()));
        UserInfoResponseDto uirDto = accountService.changePassword(ucprDto, "cookie");
        assertEquals(user.getFirstName(), uirDto.getFirstName());
        verify(accountDao, times(1)).changePassword(any(User.class));
    }

    @Test
    void failChangePassword() throws ServerException {
        User user = getUser();
        UserChangePasswordRequestDto ucprDto = new UserChangePasswordRequestDto("firstname", "lastname",
                "secondname", "wrong", "newpass");
        when(sessionDao.getSessionByCookie("cookie")).thenReturn(new Session(user, "cookie", LocalDateTime.now()));
        assertThrows(ServerException.class, () -> accountService.changePassword(ucprDto, "cookie"));

    }

    @Test
    void updateUserToSuper() throws ServerException {
        User user = getUser();
        user.setSuper(true);
        when(sessionDao.getSessionByCookie("cookie")).thenReturn(new Session(user, "cookie", LocalDateTime.now()));
        accountService.updateToSuper(1, "cookie");
        verify(accountDao, times(1)).makeUserSuper(1, user);
    }

    @Test
    void failUpdateUserToSuper() throws ServerException {
        User user = getUser();
        when(sessionDao.getSessionByCookie("cookie")).thenReturn(new Session(user, "cookie", LocalDateTime.now()));
        assertThrows(ServerException.class, () -> accountService.updateToSuper(1, "cookie"));
    }

    @Test
    void getAllUserInfo() throws ServerException {
        User user = getUser();
        user.setId(1);
        User userFromDb1 = getUser("1");
        User userFromDb2 = getUser("2");
        User userFromDb3 = getUser("3");
        List<User> users = new ArrayList<>();
        users.add(userFromDb1);
        users.add(userFromDb2);
        users.add(userFromDb3);
        when(sessionDao.getSessionByCookie("cookie")).thenReturn(new Session(user, "cookie", LocalDateTime.now()));
        when(accountDao.getAllUsers(user.getId(), "desc", 0, 3)).thenReturn(users);
        List<UserInfoResponseDto> uirDto = accountService.getAllUsersInfo("desc", "default", 0, 3, "cookie");
        assertEquals(3, uirDto.size());
    }


}
