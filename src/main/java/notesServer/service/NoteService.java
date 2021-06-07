package notesServer.service;

import notesServer.database.dao.iface.NoteDao;
import notesServer.database.dao.iface.SectionDao;
import notesServer.database.dao.iface.SessionDao;
import notesServer.dto.request.comment.CommentActionRequestDto;
import notesServer.dto.request.note.NoteActionRequestDto;
import notesServer.dto.response.comment.CommentInfoResponseDto;
import notesServer.dto.response.note.NoteInfoResponseDto;
import notesServer.dto.response.user.EmptyResponseDto;
import notesServer.erros.ServerError;
import notesServer.erros.ServerException;
import notesServer.model.*;
import notesServer.util.Settings;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NoteService extends BaseService{

    private NoteDao noteDao;
    private SectionDao sectionDao;

    public NoteService(SessionDao sessionDao, Settings settings, NoteDao noteDao, SectionDao sectionDao) {
        super(sessionDao, settings);
        this.noteDao = noteDao;
        this.sectionDao = sectionDao;
    }

    public NoteInfoResponseDto postNote(NoteActionRequestDto postDto, String cookie) throws ServerException {
        User user = getSessionByCookie(cookie).getUser();
        Section section = sectionDao.getSectionInfo(user, postDto.getSectionId());
        Note note = new Note(postDto.getSubject(), postDto.getBody(), user, section, 1);
        noteDao.insertNote(user, note);
        return noteInfoToDto(note);
    }

    public NoteInfoResponseDto getNoteInfo(long noteId, String cookie) throws ServerException {
        User user = getSessionByCookie(cookie).getUser();
        Note note = noteDao.getNoteById(user, noteId);
        return noteInfoToDto(note);
    }

    public NoteInfoResponseDto editNote(NoteActionRequestDto editDto, long noteId, String cookie)
            throws ServerException {
        User user = getSessionByCookie(cookie).getUser();
        Note note = noteDao.getNoteById(user, noteId);
        NoteHistory noteHistory = new NoteHistory(noteId, note.getBody(), note.getRevisionId());
        if (editDto.getBody() != null) {
            if (note.getAuthor() != user) {
                throw new ServerException(ServerError.NOT_NOTE_CREATOR);
            }
            note.setBody(editDto.getBody());
            note.setRevisionId(note.getRevisionId() + 1);
        }
        if (editDto.getSectionId() != 0) {
            if (!user.isSuper() || note.getAuthor() != user) {
                throw new ServerException(ServerError.NOT_NOTE_CREATOR);
            }
            Section section = sectionDao.getSectionInfo(user, editDto.getSectionId());
                note.setSection(section);
        }
        note = noteDao.editNote(user, note, noteHistory);
        return noteInfoToDto(note);
    }

    public EmptyResponseDto deleteNote(long noteId, String cookie) throws ServerException {
        User user = getSessionByCookie(cookie).getUser();
        Note note = noteDao.getNoteById(user, noteId);
        if (!user.isSuper() || note.getAuthor() != user) {
            throw new ServerException(ServerError.NOT_NOTE_CREATOR);
        }
        noteDao.deleteNote(user, noteId);
        return new EmptyResponseDto();
    }

    public CommentInfoResponseDto postComment(CommentActionRequestDto postDto, String cookie) throws ServerException {
        User user = getSessionByCookie(cookie).getUser();
        Comment comment = new Comment(postDto.getBody(), postDto.getNoteId(), user);
        comment = noteDao.insertComment(user, comment);
        return commentInfoToDto(comment);
    }

    public List<CommentInfoResponseDto> getAllComments(long noteId, String cookie) throws ServerException {
        User user = getSessionByCookie(cookie).getUser();
        List<Comment> comments = noteDao.getComments(user, noteId);
        return commentsInfoToDto(comments);
    }

    public CommentInfoResponseDto editComment(CommentActionRequestDto editDto, long commentId, String cookie)
        throws ServerException {
        User user = getSessionByCookie(cookie).getUser();
        Comment comment = noteDao.getComment(user, commentId);
        if (comment.getAuthor() != user) {
            throw new ServerException(ServerError.NOT_COMMENT_CREATOR);
        }
        comment.setBody(editDto.getBody());
        comment = noteDao.editComment(user, comment);
        return commentInfoToDto(comment);
    }

    public EmptyResponseDto deleteComment(long commentId, String cookie) throws ServerException {
        User user = getSessionByCookie(cookie).getUser();
        Comment comment = noteDao.getComment(user, commentId);
        if (!user.isSuper() || comment.getAuthor() != user) {
            throw new ServerException(ServerError.NOT_COMMENT_CREATOR);
        }
        noteDao.deleteComment(user, commentId);
        return new EmptyResponseDto();
    }

    public EmptyResponseDto deleteAllComments(long noteId, String cookie) throws ServerException {
        User user = getSessionByCookie(cookie).getUser();
        Note note = noteDao.getNoteById(user, noteId);
        if (!user.isSuper() || note.getAuthor() != user) {
            throw new ServerException(ServerError.NOT_COMMENT_CREATOR);
        }
        noteDao.deleteAllComments(user, noteId);
        return new EmptyResponseDto();
    }

    public EmptyResponseDto ratingNotes(NoteActionRequestDto ratingDto, long noteId, String cookie)
        throws ServerException {
        User user = getSessionByCookie(cookie).getUser();
        Note note = noteDao.getNoteById(user, noteId);
        if (user == note.getAuthor()) {
            throw new ServerException(ServerError.USER_NOTE_CREATOR);
        }
        noteDao.ratingNotes(user, note.getId(), ratingDto.getRating());
        return new EmptyResponseDto();
    }



    private CommentInfoResponseDto commentInfoToDto(Comment comment) throws ServerException {
        if (comment == null) {
            throw new ServerException(ServerError.COMMENT_IS_EMPTY);
        }
        return new CommentInfoResponseDto(comment.getId(), comment.getBody(), comment.getNoteId(),
                comment.getAuthor().getId(), comment.getRevisionId(), comment.getCreated());
    }

    private List<CommentInfoResponseDto> commentsInfoToDto(List<Comment> comments) {
        List<CommentInfoResponseDto> commentsDto = new ArrayList<>();
        comments.forEach(i -> commentsDto.add(new CommentInfoResponseDto(i.getId(), i.getBody(), i.getNoteId(),
                i.getAuthor().getId(), i.getRevisionId(), i.getCreated())));
        return commentsDto;
    }

    private NoteInfoResponseDto noteInfoToDto(Note note) throws ServerException {
        if (note == null) {
            throw new ServerException(ServerError.NOTE_IS_EMPTY);
        }
        return new NoteInfoResponseDto(note.getId(), note.getSubject(), note.getBody(), note.getSection().getId(),
                note.getAuthor().getId(), note.getCreated(), note.getRevisionId());
    }
}
