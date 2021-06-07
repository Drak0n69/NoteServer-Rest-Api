package notesServer.controller;

import notesServer.dto.request.comment.CommentActionRequestDto;
import notesServer.dto.request.note.NoteActionRequestDto;
import notesServer.dto.response.comment.CommentInfoResponseDto;
import notesServer.dto.response.note.NoteInfoResponseDto;
import notesServer.dto.response.user.EmptyResponseDto;
import notesServer.erros.ServerException;
import notesServer.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
@Validated
public class NoteController {

    private NoteService noteService;

    @Autowired
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping(path = "/notes", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public NoteInfoResponseDto postNote(
            @CookieValue("JAVASESSIONID") String cookie,
            @RequestBody @Valid NoteActionRequestDto postDto
    ) throws ServerException {
        return noteService.postNote(postDto, cookie);
    }

    @GetMapping(path = "/notes/{noteId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public NoteInfoResponseDto getNoteInfo(
            @CookieValue("JAVASESSIONID") String cookie,
            @PathVariable("noteId") @Min(1) long noteId
    ) throws ServerException {
        return noteService.getNoteInfo(noteId, cookie);
    }

    @PutMapping(path = "/notes/{noteId}", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public NoteInfoResponseDto editNote(
            @CookieValue("JAVASESSIONID") String cookie,
            @RequestBody @Valid NoteActionRequestDto editDto,
            @PathVariable("noteId") @Min(1) long noteId
    ) throws ServerException {
        return noteService.editNote(editDto, noteId, cookie);
    }

    @DeleteMapping(path = "/notes/{noteId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EmptyResponseDto deleteNote(
            @CookieValue("JAVASESSIONID") String cookie,
            @PathVariable("noteId") @Min(1) long noteId
    ) throws ServerException {
        return noteService.deleteNote(noteId, cookie);
    }

    @PostMapping(path = "/comments", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommentInfoResponseDto postComment(
            @CookieValue("JAVASESSIONID") String cookie,
            @RequestBody @Valid CommentActionRequestDto postDto
    ) throws ServerException {
        return noteService.postComment(postDto, cookie);
    }

    @GetMapping(path = "/notes/{noteId}/comments", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CommentInfoResponseDto> getAllCommentsOfNote(
            @CookieValue("JAVASESSIONID") String cookie,
            @PathVariable("noteId") @Min(1) long noteId
    ) throws ServerException {
        return noteService.getAllComments(noteId, cookie);
    }

    @PutMapping(path = "/comments/{commentId}", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommentInfoResponseDto editComment(
            @CookieValue("JAVASESSIONID") String cookie,
            @RequestBody @Valid CommentActionRequestDto editDto,
            @PathVariable("commentId") @Min(1) long commentId
    ) throws ServerException {
        return noteService.editComment(editDto, commentId, cookie);
    }

    @DeleteMapping(path = "/comments/{commentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EmptyResponseDto deleteComment(
            @CookieValue("JAVASESSIONID") String cookie,
            @PathVariable("commentId") @Min(1) long commentId
    ) throws ServerException {
        return noteService.deleteComment(commentId, cookie);
    }

    @DeleteMapping(path = "/notes/{noteId}/comments", produces = MediaType.APPLICATION_JSON_VALUE)
    public EmptyResponseDto deleteAllCommentsOfNote(
            @CookieValue("JAVASESSIONID") String cookie,
            @PathVariable("noteId") @Min(1) long noteId
    ) throws ServerException {
        return noteService.deleteAllComments(noteId, cookie);
    }

    @PostMapping(path = "/notes/{noteId}/rating", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public EmptyResponseDto ratingNotes(
            @CookieValue("JAVASESSIONID") String cookie,
            @RequestBody @Valid NoteActionRequestDto ratingDto,
            @PathVariable("noteId") long noteId
    ) throws ServerException {
        return noteService.ratingNotes(ratingDto, noteId, cookie);
    }

    @GetMapping(path = "/notes", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<NoteInfoResponseDto> getAllNotes(
            @CookieValue("JAVASESSIONID") String cookie,
            @RequestParam(name = "sectionId", required = false) long sectionId,
            @RequestParam(name = "sortByRating", required = false) String sortByRating,
            @RequestParam(name = "tags", required = false) List<String> tags,
            @RequestParam(name = "allTags", defaultValue = "false") boolean allTags,
            @RequestParam(name = "timeFrom", defaultValue = "#{T(java.time.LocalDateTime).now().minusMonths(2)}")
                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime timeFrom,
            @RequestParam(name = "timeTo", defaultValue = "#{T(java.time.LocalDateTime).now()}")
                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime timeTo,
            @RequestParam(name = "user", required = false) long userId,
            @RequestParam(name = "include", required = false) String include,
            @RequestParam(name = "comments", defaultValue = "false") boolean comments,
            @RequestParam(name = "allVersion", defaultValue = "false") boolean allVersion,
            @RequestParam(name = "commentVersion", defaultValue = "false") boolean commentVersion,
            @RequestParam(name = "from", defaultValue = "0") long from,
            @RequestParam(name = "count", defaultValue = "1844674407370955165") long count
    ) throws ServerException {
        return null;
//                noteService.getAllNotes(cookie, sectionId, sortByRating, tags, allTags, timeFrom, timeTo, userId,
//                include, comments, allVersion, commentVersion, from, count);
    }
}
