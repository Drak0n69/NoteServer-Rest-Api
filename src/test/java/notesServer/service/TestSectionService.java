package notesServer.service;

import notesServer.database.dao.iface.SectionDao;
import notesServer.database.dao.iface.SessionDao;
import notesServer.dto.request.section.SectionActionRequestDto;
import notesServer.dto.response.section.SectionInfoResponseDto;
import notesServer.erros.ServerException;
import notesServer.model.Section;
import notesServer.model.Session;
import notesServer.model.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@SpringBootTest
public class TestSectionService {

    @MockBean
    private SessionDao sessionDao;
    @MockBean
    private SectionDao sectionDao;
    private SectionService sectionService;

    @Autowired
    public TestSectionService(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    private User getUser(long id, String login) {
        return new User(id, "firstname", "lastname", "patronymic", login, "password123");
    }

    @Test
    void createSection() throws ServerException {
        User user = getUser(1, "login");
        SectionActionRequestDto createDto = new SectionActionRequestDto("books");
        Section books = new Section("books", user);
        when(sessionDao.getSessionByCookie("cookie")).thenReturn(new Session(user, "cookie", LocalDateTime.now()));
        when(sectionDao.insertSection(books)).thenReturn(new Section(1, "books", user));
        SectionInfoResponseDto infoDto = sectionService.createSection(createDto, "cookie");
        assertEquals(createDto.getName(), infoDto.getName());
        verify(sectionDao, times(1)).insertSection(books);
    }

    @Test
    void editSection() throws ServerException {
        User user = getUser(1, "login");
        Section books = new Section(1, "books", user);
        SectionActionRequestDto changeDto = new SectionActionRequestDto("animals");
        when(sessionDao.getSessionByCookie("cookie")).thenReturn(new Session(user, "cookie", LocalDateTime.now()));
        when(sectionDao.getSectionInfo(user, books.getId())).thenReturn(books);
        SectionInfoResponseDto infoDto = sectionService.editSection(changeDto, books.getId(), "cookie");
        assertEquals(changeDto.getName(), infoDto.getName());
        verify(sectionDao, times(1)).renameSection(user, books);
    }

    @Test
    void failChangeSectionNameWrongCreator() throws ServerException {
        User creator = getUser(1, "creator");
        User user = getUser(2, "user");
        Section books = new Section(1, "books", creator);
        SectionActionRequestDto changeDto = new SectionActionRequestDto("animals");
        when(sessionDao.getSessionByCookie("cookie")).thenReturn(new Session(user, "cookie", LocalDateTime.now()));
        when(sectionDao.getSectionInfo(user, books.getId())).thenReturn(books);
        assertThrows(ServerException.class, () -> sectionService.editSection(changeDto, books.getId(), "cookie"));
    }

    @Test
    void deleteSection() throws ServerException {
        User user = getUser(1, "user");
        Section section = new Section(1, "section", user);
        when(sessionDao.getSessionByCookie("cookie")).thenReturn(new Session(user, "cookie", LocalDateTime.now()));
        when(sectionDao.getSectionInfo(user, section.getId())).thenReturn(section);
        sectionService.deleteSection(section.getId(), "cookie");
        verify(sectionDao, times(1)).deleteSection(user, section.getId());
    }

    @Test
    void deleteSectionIsSuper() throws ServerException {
        User creator = getUser(1, "creator");
        User user = getUser(2, "user");
        user.setSuper(true);
        Section section = new Section(1, "section", creator);
        when(sessionDao.getSessionByCookie("cookie")).thenReturn(new Session(user, "cookie", LocalDateTime.now()));
        when(sectionDao.getSectionInfo(user, section.getId())).thenReturn(section);
    }

    @Test
    void failDeleteSectionWrongCreator() throws ServerException {
        User creator = getUser(1, "creator");
        User user = getUser(2, "user");
        Section section = new Section(1, "section", creator);
        when(sessionDao.getSessionByCookie("cookie")).thenReturn(new Session(user, "cookie", LocalDateTime.now()));
        when(sectionDao.getSectionInfo(user, section.getId())).thenReturn(section);
        assertThrows(ServerException.class, () -> sectionService.deleteSection(section.getId(), "cookie"));
    }

    @Test
    void getSectionInfo() throws ServerException {
        User user = getUser(1, "user");
        Section section = new Section(1, "section", user);
        when(sessionDao.getSessionByCookie("cookie")).thenReturn(new Session(user, "cookie", LocalDateTime.now()));
        when(sectionDao.getSectionInfo(user, section.getId())).thenReturn(section);
        SectionInfoResponseDto infoDto = sectionService.getSectionInfo(section.getId(), "cookie");
        assertEquals(section.getName(), infoDto.getName());
    }

    @Test
    void failGerSectionInfoDeletedSection() throws ServerException {
        User user = getUser(1, "user");
        when(sessionDao.getSessionByCookie("cookie")).thenReturn(new Session(user, "cookie", LocalDateTime.now()));
        when(sectionDao.getSectionInfo(user, 1)).thenReturn(null);
        assertThrows(ServerException.class, () -> sectionService.getSectionInfo(1, "cookie"));
    }

    @Test
    void getAllSections() throws ServerException {
        User user = getUser(1, "user");
        List<Section> sections = new ArrayList<>();
        Section s1 = new Section("1", user);
        Section s2 = new Section("2", user);
        Section s3 = new Section("3", user);
        Section s4 = new Section("4", user);
        sections.add(s1);
        sections.add(s2);
        sections.add(s3);
        sections.add(s4);
        when(sessionDao.getSessionByCookie("cookie")).thenReturn(new Session(user, "cookie", LocalDateTime.now()));
        when(sectionDao.getAllSections(user)).thenReturn(sections);
        List<SectionInfoResponseDto> sectionsDto = sectionService.getAllSections("cookie");
        assertEquals(4, sectionsDto.size());
    }
}
