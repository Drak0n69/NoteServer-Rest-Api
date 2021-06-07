package notesServer.dao;

import notesServer.database.dao.iface.AccountDao;
import notesServer.database.dao.iface.SessionDao;
import notesServer.database.dao.impl.AccountDaoImpl;
import notesServer.database.dao.impl.SessionDaoImpl;
import notesServer.erros.ServerException;
import notesServer.model.Session;
import notesServer.model.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({AccountDaoImpl.class, SessionDaoImpl.class})
public class TestAccountDao {

    private SessionDao sessionDao;
    private AccountDao accountDao;

    @Autowired
    public TestAccountDao(SessionDao sessionDao, AccountDao accountDao) {
        this.sessionDao = sessionDao;
        this.accountDao = accountDao;
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
    void insertUser() throws ServerException {
        User user = getUser();
        user = accountDao.insertUser(user);
        assertNotEquals(0, user.getId());
    }

    @Test
    void insertSameUser() throws ServerException {
        User user = getUser();
        accountDao.insertUser(user);
        assertThrows(ServerException.class, () -> accountDao.insertUser(user));
    }

    @Test
    void deleteUser() throws ServerException {
        User user = getUser();
        accountDao.insertUser(user);
        accountDao.deleteUser(user);
        User deletedUser = accountDao.getUserById(user.getId());
        assertTrue(deletedUser.isDeleted());
    }

    @Test
    void changePassword() throws ServerException {
        User user = getUser();
        String cookie = getUIID();
        accountDao.insertUser(user);
        sessionDao.insertSession(new Session(user, cookie));
        user.setPassword("newPassword");
        accountDao.changePassword(user);
        User userFromDb = accountDao.getUserById(user.getId());
        assertEquals(user.getPassword(), userFromDb.getPassword());
    }

    @Test
    void makeUserToSuper() throws ServerException {
        User admin = getUser("admin2");
        String cookie = getUIID();
        accountDao.insertUser(admin);
        sessionDao.insertSession(new Session(admin, cookie));
        User noSuperUser = getUser("noSuper");
        accountDao.insertUser(noSuperUser);
        accountDao.makeUserSuper(noSuperUser.getId(), admin);
        User superUser = accountDao.getUserById(noSuperUser.getId());
        assertTrue(superUser.isSuper());
    }

    @Test
    void getUsersByHighRating() throws ServerException {
        User user = getUser("user");
        String cookie = getUIID();
        accountDao.insertUser(user);
        sessionDao.insertSession(new Session(user, cookie, LocalDateTime.now()));
        User u1 = getUser("u1");
        User u2 = getUser("u2");
        User u3 = getUser("u3");
        User u4 = getUser("u4");
        accountDao.insertUser(u1);
        accountDao.insertUser(u2);
        accountDao.insertUser(u3);
        accountDao.insertUser(u4);
        accountDao.testSetRating(u1, 5);
        accountDao.testSetRating(u2, 2);
        accountDao.testSetRating(u3, 5);
        accountDao.testSetRating(u4, 4);
        List<User> highRatingList = accountDao.getUsersByHighRating(user.getId(), 0, 10);
        assertAll(
                () -> assertEquals(2, highRatingList.size()),
                () -> assertEquals(5, highRatingList.get(0).getRating())
        );
    }

    @Test
    void getUsersByLowRating() throws ServerException {
        User usver = getUser("usver");
        accountDao.insertUser(usver);
        User us1 = getUser("us1");
        User us2 = getUser("us2");
        User us3 = getUser("us3");
        User us4 = getUser("us4");
        accountDao.insertUser(us1);
        accountDao.insertUser(us2);
        accountDao.insertUser(us3);
        accountDao.insertUser(us4);
        accountDao.testSetRating(us1, 1);
        accountDao.testSetRating(us2, 4);
        accountDao.testSetRating(us3, 1);
        accountDao.testSetRating(us4, 1);
        List<User> lowRatingList = accountDao.getUsersByLowRating(usver.getId(), 0, 10);
        assertAll(
                () -> assertEquals(3, lowRatingList.size()),
                () -> assertEquals(1, lowRatingList.get(0).getRating())
        );
    }

    @Test
    void getSuperUserList() throws ServerException {
        User user = getUser();
        User super1 = getUser("sup1");
        User super2 = getUser("sup2");
        User noSuper = getUser("noSup");
        accountDao.insertUser(user);
        accountDao.insertUser(super1);
        accountDao.insertUser(super2);
        accountDao.insertUser(noSuper);
        accountDao.makeUserSuper(super1.getId(), user);
        accountDao.makeUserSuper(super2.getId(), user);
        List<User> superUserList = accountDao.getSuperUsersList(user.getId(), "DESC", 0, 3);
        assertEquals(3, superUserList.size());
    }

    @Test
    void getAllUsers() throws ServerException {
        User user1 = getUser("1");
        User user2 = getUser("2");
        User user3 = getUser("3");
        accountDao.insertUser(user1);
        accountDao.insertUser(user2);
        accountDao.insertUser(user3);
        List<User> allUserList = accountDao.getAllUsers(user1.getId(), "DESC", 0, 4);
        assertEquals(4, allUserList.size());
    }

    @Test
    void getFollowingsOfUser() throws ServerException {
        User user = getUser("user");
        User follow1 = getUser("fol1");
        User follow2 = getUser("fol2");
        accountDao.insertUser(user);
        accountDao.insertUser(follow1);
        accountDao.insertUser(follow2);
        accountDao.addToFollowing(user, follow1.getLogin());
        accountDao.addToFollowing(user, follow2.getLogin());
        List<User> usersFollowList = accountDao.getUserFollowingsList(user, "DESC", 0, 2);
        assertEquals(2, usersFollowList.size());
    }

    @Test
    void getFollowersOfUser() throws ServerException {
        User user = getUser("user");
        User follower1 = getUser("fer1");
        User follower2 = getUser("fer2");
        accountDao.insertUser(user);
        accountDao.insertUser(follower1);
        accountDao.insertUser(follower2);
        accountDao.addToFollowing(follower1, user.getLogin());
        accountDao.addToFollowing(follower2, user.getLogin());
        List<User> usersFollowerList = accountDao.getUserFollowersList(user, "DESC", 0, 2);
        assertEquals(2, usersFollowerList.size());
    }

    @Test
    void getIgnoredByUser() throws ServerException {
        User user = getUser("user");
        User ignored1 = getUser("ed1");
        User ignored2 = getUser("ed2");
        accountDao.insertUser(user);
        accountDao.insertUser(ignored1);
        accountDao.insertUser(ignored2);
        accountDao.addToIgnore(user, ignored1.getLogin());
        accountDao.addToIgnore(user, ignored2.getLogin());
        List<User> userIgnoreList = accountDao.getUserIgnoreList(user, "DESC", 0, 2);
        assertEquals(2, userIgnoreList.size());
    }

    @Test
    void getIgnoringTheUser() throws ServerException {
        User user = getUser("user");
        User ignoring1 = getUser("ing1");
        User ignoring2 = getUser("ing2");
        accountDao.insertUser(user);
        accountDao.insertUser(ignoring1);
        accountDao.insertUser(ignoring2);
        accountDao.addToIgnore(ignoring1, user.getLogin());
        accountDao.addToIgnore(ignoring2, user.getLogin());
        List<User> ignoringTheUserList = accountDao.getIgnoredByUsersList(user, "DESC", 0, 2);
        assertEquals(2, ignoringTheUserList.size());
    }

    @Test
    void deleteFromFollow() throws ServerException {
        User user = getUser();
        User follow = getUser("fol");
        accountDao.insertUser(user);
        accountDao.insertUser(follow);
        accountDao.addToFollowing(user, follow.getLogin());
        List<User> followList = accountDao.getUserFollowingsList(user, "DESC", 0, 1);
        assertEquals(followList.get(0).getLogin(), follow.getLogin());

        accountDao.deleteFromFollowings(user, follow.getLogin());
        followList = accountDao.getUserFollowingsList(user, "DESC", 0, 1);
        assertEquals(0, followList.size());
    }

    @Test
    void deleteFromIgnor() throws ServerException {
        User user = getUser();
        User ignored = getUser("ied");
        accountDao.insertUser(user);
        accountDao.insertUser(ignored);
        accountDao.addToIgnore(user, ignored.getLogin());
        List<User> userIgnorList = accountDao.getUserIgnoreList(user, "DESC", 0, 1);
        assertEquals(userIgnorList.get(0).getLogin(), ignored.getLogin());

        accountDao.deleteFromIgnore(user, ignored.getLogin());
        userIgnorList = accountDao.getUserIgnoreList(user, "DESC", 0, 1);
        assertEquals(0, userIgnorList.size());
    }



    @Test
    void getUserByLogin() throws ServerException {
        User user = getUser();
        String cookie = getUIID();
        accountDao.insertUser(user);
        sessionDao.insertSession(new Session(user, cookie));
        User userFromDb = accountDao.getUserByLogin(user.getLogin());
        assertEquals(user.getLogin(), userFromDb.getLogin());
    }
}
