package notesServer.database.dao.iface;

import notesServer.erros.ServerException;
import notesServer.model.Comment;
import notesServer.model.Note;
import notesServer.model.NoteHistory;
import notesServer.model.User;

import java.util.List;

public interface NoteDao {

    Note insertNote(User user, Note note) throws ServerException;

    Note getNoteById(User user, long noteId) throws ServerException;

    Comment insertComment(User user, Comment comment) throws ServerException;

    Comment getComment(User user, long id) throws ServerException;

    List<Comment> getComments(User user, long noteId) throws ServerException;

    Note editNote(User user, Note note, NoteHistory noteHistory) throws ServerException;

    void deleteNote(User user, long noteId) throws ServerException;

    Comment editComment(User user, Comment comment) throws ServerException;

    void deleteComment(User user, long commentId) throws ServerException;

    void deleteAllComments(User user, long noteId) throws ServerException;

    List<NoteHistory> getNoteHistory(User user, long noteId) throws ServerException;

    void ratingNotes(User user, long noteId, int rating) throws ServerException;

    Integer getNoteRating(User user, long noteId) throws ServerException;
}
