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

import java.util.UUID;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({AccountDaoImpl.class, SessionDaoImpl.class})
public class TestSessionDao {

    private SessionDao sessionDao;
    private AccountDao accountDao;

    @Autowired
    public TestSessionDao(SessionDao sessionDao, AccountDao accountDao) {
        this.sessionDao = sessionDao;
        this.accountDao = accountDao;
    }

    private String getUUID() {
        return UUID.randomUUID().toString();
    }

    private User getUser() throws ServerException {
        return accountDao.insertUser(new User("denis", "vyrezkov", "olegovich", "den6969",
                                                "passlord123"));
    }

    @Test
    void testInsertSession() throws ServerException {
        User user = getUser();
        String cookie = getUUID();
        sessionDao.insertSession(new Session(user, cookie));
    }

    @Test
    void testDeleteSession() throws ServerException {
        User user = getUser();
        String cookie = getUUID();
        sessionDao.insertSession(new Session(user, cookie));
        sessionDao.deleteSession(cookie);
    }

    @Test
    void testUpdateSession() throws ServerException {
        User user = getUser();
        String cookie = getUUID();
        sessionDao.insertSession(new Session(user, cookie));
        String newCookie = getUUID();
        sessionDao.insertSession(new Session(user, newCookie));
    }

    @Test
    void testGetSession() throws ServerException {
        User user = getUser();
        String cookie = getUUID();
        sessionDao.insertSession(new Session(user, cookie));
        Session session = sessionDao.getSessionByCookie(cookie);
        assertEquals(user.getLogin(), session.getUser().getLogin());
    }
}
