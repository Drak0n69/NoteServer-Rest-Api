package notesServer.dao;

import notesServer.database.dao.iface.AccountDao;
import notesServer.database.dao.iface.SectionDao;
import notesServer.database.dao.impl.AccountDaoImpl;
import notesServer.database.dao.impl.SectionDaoImpl;
import notesServer.erros.ServerException;
import notesServer.model.Section;
import notesServer.model.User;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({AccountDaoImpl.class, SectionDaoImpl.class})
public class TestSectionDao {

    private AccountDao accountDao;
    private SectionDao sectionDao;

    @Autowired
    public TestSectionDao(AccountDao accountDao, SectionDao sectionDao) {
        this.accountDao = accountDao;
        this.sectionDao = sectionDao;
    }

    User getUser() throws ServerException {
        return accountDao.insertUser(new User("firstname", "lastname", "patronymic",
                "login", "password1234"));
    }

    User getUser(String login) throws ServerException {
        return accountDao.insertUser(new User("firstname", "lastname", "patronymic",
                login, "password1234"));
    }

    Section getSection(User user, String name) throws ServerException {
        return sectionDao.insertSection(new Section(name, user));
    }

    @Test
    void insertSection() throws ServerException {
        User user = getUser();
        Section section = new Section("books", user);
        sectionDao.insertSection(section);
    }

    @Test
    void insertSameSection() throws ServerException {
        User user = getUser();
        Section animals = getSection(user, "animals");
        assertThrows(ServerException.class, () -> getSection(user, "animals"));
    }

    @Test
    void renameSection() throws ServerException {
        User user = getUser();
        Section cars = getSection(user, "cars");
        String newName = "old cars";
        cars.setName(newName);
        sectionDao.renameSection(user, cars);
        cars = sectionDao.getSectionInfo(user, cars.getId());
        assertEquals(newName, cars.getName());
    }

    @Test
    void deleteSection() throws ServerException {
        User creator = getUser("creator");
        Section weather = new Section("weather", creator);
        sectionDao.insertSection(weather);
        sectionDao.deleteSection(creator, weather.getId());
        assertNull(sectionDao.getSectionInfo(creator, weather.getId()));
    }

    @Test
    void getAllSections() throws ServerException {
        User user = getUser();
        Section books = getSection(user, "books");
        Section cars = getSection(user, "cars");
        Section animals = getSection(user, "animals");
        List<Section> sections = sectionDao.getAllSections(user);
        assertEquals(3, sections.size());
    }
}
