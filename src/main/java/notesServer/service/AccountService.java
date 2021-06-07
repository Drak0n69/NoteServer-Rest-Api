package notesServer.service;

import notesServer.database.dao.iface.AccountDao;
import notesServer.database.dao.iface.SessionDao;
import notesServer.dto.request.user.*;
import notesServer.dto.response.user.EmptyResponseDto;
import notesServer.dto.response.user.UserInfoResponseDto;
import notesServer.erros.ServerError;
import notesServer.erros.ServerException;
import notesServer.model.Session;
import notesServer.model.User;
import notesServer.util.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(rollbackFor = DataAccessException.class)
public class AccountService extends BaseService{

    private AccountDao accountDao;

    @Autowired
    public AccountService(SessionDao sessionDao, Settings settings, AccountDao accountDao) {
        super(sessionDao, settings);
        this.accountDao = accountDao;
    }

    public UserInfoResponseDto registerUser(UserRegisterRequestDto dto, String cookie) throws ServerException {
        User user = new User(dto.getFirstName(), dto.getLastName(), dto.getPatronymic(), dto.getLogin(), dto.getPassword());
        addSession(accountDao.insertUser(user), cookie);
        return new UserInfoResponseDto(user.getFirstName(), user.getLastName(), user.getPatronymic(), user.getLogin());
    }

    public EmptyResponseDto login(UserLoginRequestDto loginDto, String newCookie) throws ServerException {
        User user = getUserFromDB(loginDto.getLogin(), loginDto.getPassword());
        addSession(user, newCookie);
        return new EmptyResponseDto();
    }

    public EmptyResponseDto logout(String cookie) throws ServerException {
        sessionDao.deleteSession(cookie);
        return new EmptyResponseDto();
    }

    public UserInfoResponseDto getUserInfo(String cookie) throws ServerException {
        User user = getSessionByCookie(cookie).getUser();
        return new UserInfoResponseDto(user.getFirstName(), user.getLastName(), user.getPatronymic(), user.getLogin());
    }

    public EmptyResponseDto deleteUser(UserDeleteRequestDto deleteDto, String cookie) throws ServerException {
        Session session = getSessionByCookie(cookie);
        checkPassword(session.getUser(), deleteDto.getPassword());
        checkDeleted(session.getUser());
        accountDao.deleteUser(session.getUser());
        sessionDao.deleteSession(cookie);
        return new EmptyResponseDto();
    }

    public UserInfoResponseDto changePassword(UserChangePasswordRequestDto changeDto , String cookie) throws ServerException {
        Session session = getSessionByCookie(cookie);
        checkPassword(session.getUser(), changeDto.getOldPassword());
        if (!changeDto.getFirstName().equalsIgnoreCase(session.getUser().getFirstName()) ||
            !changeDto.getLastName().equalsIgnoreCase(session.getUser().getLastName())) {
            throw new ServerException(ServerError.WRONG_USER_INFO);
        }
        User user = session.getUser();
        user.setPassword(changeDto.getNewPassword());
        accountDao.changePassword(user);
        return new UserInfoResponseDto(user.getId(), user.getFirstName(), user.getLastName(), user.getPatronymic(),
                user.getLogin());
    }

    public EmptyResponseDto updateToSuper(long id, String cookie) throws ServerException {
        Session session = getSessionByCookie(cookie);
        checkSuperUser(session.getUser());
        accountDao.makeUserSuper(id, session.getUser());
        return new EmptyResponseDto();
    }

    public List<UserInfoResponseDto> getAllUsersInfo(String sortBy, String type, long from, long count, String cookie
        ) throws ServerException {
        Session session = getSessionByCookie(cookie);
        User user = session.getUser();
        List<User> users;

        switch (type.toLowerCase()) {
            case "highrating" :
                users = accountDao.getUsersByHighRating(user.getId(), from, count);
                break;
            case "lowrating" :
                users = accountDao.getUsersByLowRating(user.getId(), from, count);
                break;
            case "followings" :
                users = accountDao.getUserFollowingsList(user, sortBy.toLowerCase(), from, count);
                break;
            case "followers" :
                users = accountDao.getUserFollowersList(user, sortBy.toLowerCase(), from, count);
                break;
            case "ignore" :
                users = accountDao.getUserIgnoreList(user, sortBy.toLowerCase(), from, count);
                break;
            case "ignoredby" :
                users = accountDao.getIgnoredByUsersList(user, sortBy.toLowerCase(), from, count);
                break;
            case "deleted" :
                users = accountDao.getDeletedUsersList(user.getId(), sortBy.toLowerCase(), from, count);
                break;
            case "super" :
                checkSuperUser(user);
                users = accountDao.getSuperUsersList(user.getId(), sortBy.toLowerCase(), from, count);
                break;
            default :
                users = accountDao.getAllUsers(user.getId(), sortBy.toLowerCase(), from, count);
        }

        if (users.size() > 0) {
            return usersInfoToDto(user, users);
        }
        return new ArrayList<>();
    }

    public EmptyResponseDto addUserToFollowingList(AddUserByLoginRequestDto loginDto, String cookie) throws ServerException {
        Session session = getSessionByCookie(cookie);
        accountDao.addToFollowing(session.getUser(), loginDto.getLogin());
        return new EmptyResponseDto();
    }

    public EmptyResponseDto addUserToIgnoreList(AddUserByLoginRequestDto loginDto, String cookie) throws ServerException {
        Session session = getSessionByCookie(cookie);
        accountDao.addToIgnore(session.getUser(), loginDto.getLogin());
        return new EmptyResponseDto();
    }

    public EmptyResponseDto deleteUserFromFollowingList(String login, String cookie) throws ServerException {
        Session session = getSessionByCookie(cookie);
        accountDao.deleteFromFollowings(session.getUser(), login);
        return new EmptyResponseDto();
    }

    public EmptyResponseDto deleteUserFromIgnoreList(String login, String cookie) throws ServerException {
        Session session = getSessionByCookie(cookie);
        accountDao.deleteFromIgnore(session.getUser(), login);
        return new EmptyResponseDto();
    }

    private void addSession(User user, String cookie) throws ServerException {
        Session session = new Session(user, cookie);
        sessionDao.insertSession(session);
    }

    private User getUserFromDB(String login, String password) throws ServerException {
        User userFromDB = accountDao.getUserByLogin(login);
        checkLogin(userFromDB, login);
        checkPassword(userFromDB, password);
        checkDeleted(userFromDB);
        return userFromDB;
    }

    private void checkPassword(User user, String password) throws ServerException {
        if (!user.getPassword().equalsIgnoreCase(password)) {
            throw new ServerException(ServerError.WRONG_USER_PASSWORD);
        }
    }

    private void checkLogin(User user, String login) throws ServerException {
        if (!user.getLogin().equals(login)) {
            throw new ServerException(ServerError.WRONG_USER_LOGIN);
        }
    }

    private void checkDeleted(User user) throws ServerException {
        if (user.isDeleted()) {
            throw new ServerException(ServerError.USER_DELETED);
        }
    }

    private void checkSuperUser(User user) throws ServerException {
        if (!user.isSuper()) {
            throw new ServerException(ServerError.USER_NOT_SUPER);
        }
    }

    private List<UserInfoResponseDto> usersInfoToDto(User usver, List<User> userList) {
        List<UserInfoResponseDto> listDto = new ArrayList<>();
        if (usver.isSuper()) {
            userList.forEach(i -> listDto.add(new UserInfoResponseDto(i.getId(), i.getFirstName(), i.getLastName(),
                    i.getPatronymic(), i.getLogin(), i.getTimeRegistered(), i.isOnline(), i.isDeleted(), i.getRating(),
                    i.isSuper())));
        } else {
            userList.forEach(i -> listDto.add(new UserInfoResponseDto(i.getId(), i.getFirstName(), i.getLastName(),
                    i.getPatronymic(), i.getLogin(), i.getTimeRegistered(), i.isOnline(), i.isDeleted(), i.getRating())));
        }
        return listDto;
    }
}
