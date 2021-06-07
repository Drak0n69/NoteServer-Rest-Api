package notesServer.database.dao.impl;

import notesServer.database.dao.iface.NoteDao;
import notesServer.database.mappers.NoteMapper;
import notesServer.erros.ServerError;
import notesServer.erros.ServerException;
import notesServer.model.Comment;
import notesServer.model.Note;
import notesServer.model.NoteHistory;
import notesServer.model.User;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NoteDaoImpl implements NoteDao {

    private final Logger LOGGER = LoggerFactory.getLogger(NoteDaoImpl.class);

    private NoteMapper noteMapper;

    @Autowired
    public NoteDaoImpl(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    @Override
    public Note insertNote(User user, Note note) throws ServerException {
        LOGGER.debug("Insert new note {}", note);
        try {
            noteMapper.updateSessionLifetime(user.getId());
            noteMapper.insertNote(note);
        } catch (DataAccessException e) {
            if (e.getClass() == DuplicateKeyException.class) {
                ServerError er = ServerError.SUBJECT_SAME_NAME;
                er.setMessage(String.format(er.getMessage(), note.getSubject()));
                throw new ServerException(ServerError.SUBJECT_SAME_NAME);
            }
            LOGGER.info("Can't insert this note {}! Cause: {}", note, ExceptionUtils.getStackTrace(e));
            throw new ServerException(ServerError.SQL_SERVER_ERROR);
        }
        return note;
    }

    @Override
    public Note getNoteById(User user, long noteId) throws ServerException {
        LOGGER.debug("Get note by id = {}", noteId);
        try {
            noteMapper.updateSessionLifetime(user.getId());
            return noteMapper.getNoteById(noteId);
        } catch (DataAccessException e) {
            LOGGER.info("Can't get note by id = {}! Cause: {}", noteId, ExceptionUtils.getStackTrace(e));
            throw new ServerException(ServerError.SQL_SERVER_ERROR);
        }
    }

    @Override
    public Note editNote(User user, Note note, NoteHistory noteHistory) throws ServerException {
        LOGGER.debug("Edit note {}", note);
        try {
            noteMapper.updateSessionLifetime(user.getId());
            if (note.getRevisionId() > noteHistory.getRevisionId()) {
                noteMapper.insertNoteHistory(noteHistory);
            }
            noteMapper.updateNote(note);
        } catch (DataAccessException e) {
            LOGGER.info("Can't edit note {}!, Cause: {}", note, ExceptionUtils.getStackTrace(e));
            throw new ServerException(ServerError.SQL_SERVER_ERROR);
        }
        return note;
    }

    @Override
    public void deleteNote(User user, long noteId) throws ServerException {
        LOGGER.debug("Delete note by id = {}", noteId);
        try {
            noteMapper.updateSessionLifetime(user.getId());
            noteMapper.deleteNote(noteId);
        } catch (DataAccessException e) {
            LOGGER.info("Can't delete note by id = {}! Cause: {}", noteId, ExceptionUtils.getStackTrace(e));
            throw new ServerException(ServerError.SQL_SERVER_ERROR);
        }
    }

    @Override
    public Comment editComment(User user, Comment comment) throws ServerException {
        LOGGER.debug("Edit comment body {}", comment);
        try {
            noteMapper.updateSessionLifetime(user.getId());
            noteMapper.editComment(comment);
        } catch (DataAccessException e) {
            LOGGER.info("Can't edit comment {}! Cause: {}", comment, ExceptionUtils.getStackTrace(e));
            throw new ServerException(ServerError.SQL_SERVER_ERROR);
        }
        return comment;
    }

    @Override
    public Comment insertComment(User user, Comment comment) throws ServerException {
        LOGGER.debug("Insert new comment {}", comment);
        try {
            noteMapper.updateSessionLifetime(user.getId());
            noteMapper.insertComment(comment);
        } catch (DataAccessException e) {
            LOGGER.info("Can't insert new comment {}! Cause: {}", comment, ExceptionUtils.getStackTrace(e));
            throw new ServerException(ServerError.SQL_SERVER_ERROR);
        }
        return comment;
    }

    @Override
    public Comment getComment(User user, long id) throws ServerException {
        LOGGER.debug("Get comment by id = {}", id);
        try {
            noteMapper.updateSessionLifetime(user.getId());
            return noteMapper.getComment(id);
        } catch (DataAccessException e) {
            LOGGER.info("Can't get comment by id = {}! Cause: {}", id, ExceptionUtils.getStackTrace(e));
            throw new ServerException(ServerError.SQL_SERVER_ERROR);
        }
    }

    @Override
    public List<Comment> getComments(User user, long noteId) throws ServerException {
        LOGGER.debug("Get note comments by id = {}", noteId);
        try {
            noteMapper.updateSessionLifetime(user.getId());
            return noteMapper.getComments(noteId);
        } catch (DataAccessException e) {
            LOGGER.info("Can't get note comments by id = {}! Cause: {}", noteId, ExceptionUtils.getStackTrace(e));
            throw new ServerException(ServerError.SQL_SERVER_ERROR);
        }
    }

    @Override
    public void deleteComment(User user, long commentId) throws ServerException {
        LOGGER.debug("Delete comment by id = {}", commentId);
        try {
            noteMapper.updateSessionLifetime(user.getId());
            noteMapper.deleteComment(commentId);
        } catch (DataAccessException e) {
            LOGGER.info("Can't delete comment by id = {}! Cause: {}", commentId, ExceptionUtils.getStackTrace(e));
            throw new ServerException(ServerError.SQL_SERVER_ERROR);
        }
    }

    @Override
    public void deleteAllComments(User user, long noteId) throws ServerException {
        LOGGER.debug("Delete all comments of note's id = {}", noteId);
        try {
            noteMapper.updateSessionLifetime(user.getId());
            noteMapper.deleteAllComments(noteId);
        } catch (DataAccessException e) {
            LOGGER.info("Can't delete all comments of note's id = {}! Cause; {}", noteId, ExceptionUtils.getStackTrace(e));
            throw new ServerException(ServerError.SQL_SERVER_ERROR);
        }
    }

    @Override
    public List<NoteHistory> getNoteHistory(User user, long noteId) throws ServerException {
        LOGGER.debug("Get note history by id = {}", noteId);
        try {
            noteMapper.updateSessionLifetime(user.getId());
            return noteMapper.getNoteHistory(noteId);
        } catch (DataAccessException e) {
            LOGGER.info("Can't get note history by id = {}! Cause: {}", noteId, ExceptionUtils.getStackTrace(e));
            throw new ServerException(ServerError.SQL_SERVER_ERROR);
        }
    }

    @Override
    public void ratingNotes(User user, long noteId, int rating) throws ServerException {
        LOGGER.debug("Rating notes by id = {}", noteId);
        try {
            noteMapper.updateSessionLifetime(user.getId());
            noteMapper.ratingNotes(noteId, rating);
        } catch (DataAccessException e) {
            LOGGER.info("Can't rating note by id = {}! Cause: {}", noteId, ExceptionUtils.getStackTrace(e));
            throw new ServerException(ServerError.SQL_SERVER_ERROR);
        }
    }

    @Override
    public Integer getNoteRating(User user, long noteId) throws ServerException {
        LOGGER.debug("Get rating of note by id = {}", noteId);
        try {
            noteMapper.updateSessionLifetime(user.getId());
            return noteMapper.getRating(noteId);
        } catch (DataAccessException e) {
            LOGGER.info("Can't get rating of note by id = {}! Cause: {}", noteId, ExceptionUtils.getStackTrace(e));
            throw new ServerException(ServerError.SQL_SERVER_ERROR);
        }
    }
}
