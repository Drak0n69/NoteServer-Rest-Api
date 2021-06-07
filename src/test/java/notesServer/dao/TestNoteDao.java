package notesServer.dao;

import notesServer.database.dao.iface.AccountDao;
import notesServer.database.dao.iface.NoteDao;
import notesServer.database.dao.iface.SectionDao;
import notesServer.database.dao.impl.AccountDaoImpl;
import notesServer.database.dao.impl.NoteDaoImpl;
import notesServer.database.dao.impl.SectionDaoImpl;
import notesServer.erros.ServerException;
import notesServer.model.*;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({AccountDaoImpl.class, SectionDaoImpl.class, NoteDaoImpl.class})
public class TestNoteDao {

    private AccountDao accountDao;
    private SectionDao sectionDao;
    private NoteDao noteDao;

    @Autowired
    public TestNoteDao(AccountDao accountDao, SectionDao sectionDao, NoteDao noteDao) {
        this.accountDao = accountDao;
        this.sectionDao = sectionDao;
        this.noteDao = noteDao;
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

    Note getNote(User user, Section section)
            throws ServerException{
        return noteDao.insertNote(user, new Note("subject", "body", user, section, 1));
    }

    @Test
    void insertNote() throws ServerException {
        User user = getUser();
        Section section = getSection(user, "cars");
        Note note = getNote(user, section);
        assertTrue(note.getId() > 0);
    }

    @Test
    void insertSameNote() throws ServerException {
        User user = getUser();
        Section section = getSection(user, "cars");
        Note note = getNote(user, section);
        assertThrows(ServerException.class, () -> noteDao.insertNote(user, note));
    }

    @Test
    void getNoteById() throws ServerException {
        User user = getUser();
        Section section = getSection(user, "111");
        Note note = getNote(user, section);
        Note fromDb = noteDao.getNoteById(user, note.getId());
        assertEquals(note.getBody(), fromDb.getBody());
    }

    @Test
    void editNoteBody() throws ServerException {
        User user = getUser();
        Section section = getSection(user, "111");
        Note note = getNote(user, section);
        NoteHistory noteHistory = new NoteHistory(note.getId(), note.getBody(), note.getRevisionId());
        String newBody = "qwerty";
        note.setBody(newBody);
        note.setRevisionId(2);
        note = noteDao.editNote(user, note, noteHistory);
        assertEquals(note.getBody(), newBody);
    }

    @Test
    void editNoteSection() throws ServerException {
        User user = getUser();
        Section section = getSection(user, "s1");
        Note note = getNote(user, section);
        NoteHistory notWrite = new NoteHistory(note.getId(), note.getBody(), note.getRevisionId());
        Section newSection = getSection(user, "newS");
        note.setSection(newSection);
        note = noteDao.editNote(user, note, notWrite);
        Note fromDb = noteDao.getNoteById(user, note.getId());
        List<NoteHistory> histories = noteDao.getNoteHistory(user, note.getId());
        assertEquals(note.getSection().getName(), fromDb.getSection().getName());
        assertEquals(0, histories.size());
    }

    @Test
    void getNoteHistory() throws ServerException {
        User user = getUser();
        Section section = getSection(user, "111");
        Note note = getNote(user, section);
        NoteHistory noteHistory = new NoteHistory(note.getId(), note.getBody(), note.getRevisionId());
        String newBody = "qwerty";
        note.setBody(newBody);
        note.setRevisionId(2);
        note = noteDao.editNote(user, note, noteHistory);
        NoteHistory noteHistory2 = new NoteHistory(note.getId(), note.getBody(), note.getRevisionId());
        String newBody2 = "ytrewq";
        note.setBody(newBody2);
        note.setRevisionId(3);
        note = noteDao.editNote(user, note, noteHistory2);
        List<NoteHistory> histories = noteDao.getNoteHistory(user, note.getId());
        assertEquals(2, histories.size());
    }

    @Test
    void deleteNote() throws ServerException {
        User user = getUser();
        Section section = getSection(user, "123");
        Note note = getNote(user, section);
        noteDao.deleteNote(user, note.getId());
        assertNull(noteDao.getNoteById(user, note.getId()));
    }

    @Test
    void insertComment() throws ServerException {
        User user = getUser();
        Section section = getSection(user, "books");
        Note note = getNote(user, section);
        Comment comment = new Comment("body", note.getId(), user);
        comment = noteDao.insertComment(user, comment);
        assertTrue(comment.getId() > 0);
    }

    @Test
    void getComments() throws ServerException {
        User user = getUser();
        Section section = getSection(user, "books");
        Note note = getNote(user, section);
        Comment comment1 = new Comment("body", note.getId(), user);
        Comment comment2 = new Comment("body2", note.getId(), user);
        Comment comment3 = new Comment("body3", note.getId(), user);
        noteDao.insertComment(user, comment1);
        noteDao.insertComment(user, comment2);
        noteDao.insertComment(user, comment3);
        List<Comment> comments = noteDao.getComments(user, note.getId());
        assertEquals(3, comments.size());
    }

    @Test
    void editComment() throws ServerException {
        User user = getUser();
        Section section = getSection(user, "books");
        Note note = getNote(user, section);
        Comment comment = new Comment("body", note.getId(), user);
        noteDao.insertComment(user, comment);
        String newBody = "123qwe123";
        comment.setBody(newBody);
        comment = noteDao.editComment(user, comment);
        Comment fromDb = noteDao.getComment(user, comment.getId());
        assertEquals(comment.getBody(), fromDb.getBody());
    }

    @Test
    void deleteComment() throws ServerException {
        User user = getUser();
        Section section = getSection(user, "books");
        Note note = getNote(user, section);
        Comment comment = new Comment("body", note.getId(), user);
        noteDao.insertComment(user, comment);
        noteDao.deleteComment(user, comment.getId());
        Comment fromDb = noteDao.getComment(user, comment.getId());
        assertNull(fromDb);
    }

    @Test
    void deleteAllNoteComments() throws ServerException {
        User user = getUser();
        Section section = getSection(user, "books");
        Note note = getNote(user, section);
        Comment comment1 = new Comment("body", note.getId(), user);
        Comment comment2 = new Comment("body2", note.getId(), user);
        Comment comment3 = new Comment("body3", note.getId(), user);
        noteDao.insertComment(user, comment1);
        noteDao.insertComment(user, comment2);
        noteDao.insertComment(user, comment3);
        List<Comment> comments = noteDao.getComments(user, note.getId());
        assertEquals(3, comments.size());
        noteDao.deleteAllComments(user, note.getId());
        comments = noteDao.getComments(user, note.getId());
        assertEquals(0, comments.size());
    }

    @Test
    void ratingNote() throws ServerException {
        User user = getUser();
        Section section = getSection(user, "books");
        Note note = getNote(user, section);
        noteDao.ratingNotes(user, note.getId(), 1);
        noteDao.ratingNotes(user, note.getId(), 1);
        noteDao.ratingNotes(user, note.getId(), 2);
        noteDao.ratingNotes(user, note.getId(), 4);
        noteDao.ratingNotes(user, note.getId(), 5);
        int rating = noteDao.getNoteRating(user, note.getId());
        assertEquals(2, rating);
    }

    @Test
    void getUserRating() throws ServerException {
        User user = getUser();
        User user2 = getUser("qwertrertet");
        Section section = getSection(user, "books");
        Note note1 = getNote(user, section);
        noteDao.ratingNotes(user, note1.getId(), 2);
        noteDao.ratingNotes(user, note1.getId(), 2);
        noteDao.ratingNotes(user, note1.getId(), 2);
        Note note2 = noteDao.insertNote(user, new Note("sub", "body2", user, section, 1));
        noteDao.ratingNotes(user, note2.getId(), 4);
        noteDao.ratingNotes(user, note2.getId(), 4);
        noteDao.ratingNotes(user, note2.getId(), 4);
        Note note3 = noteDao.insertNote(user2, new Note("subbub", "body3", user2, section, 1));
        noteDao.ratingNotes(user, note3.getId(), 5);
        noteDao.ratingNotes(user, note3.getId(), 5);
        noteDao.ratingNotes(user, note3.getId(), 5);
        User userFromDb = accountDao.getUserById(user.getId());
        assertEquals(3, userFromDb.getRating());
    }

}
