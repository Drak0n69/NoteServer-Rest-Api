package notesServer.dto.response.comment;

import java.time.LocalDateTime;

public class CommentInfoResponseDto {

    private long id;
    private String body;
    private long noteId;
    private long authorId;
    private int revisionId;
    private LocalDateTime created;

    public CommentInfoResponseDto(long id, String body, long noteId, long authorId, int revisionId, LocalDateTime created) {
        this.id = id;
        this.body = body;
        this.noteId = noteId;
        this.authorId = authorId;
        this.revisionId = revisionId;
        this.created = created;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getNoteId() {
        return noteId;
    }

    public void setNoteId(long noteId) {
        this.noteId = noteId;
    }

    public long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }

    public int getRevisionId() {
        return revisionId;
    }

    public void setRevisionId(int revisionId) {
        this.revisionId = revisionId;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "CommentInfoResponseDto{" +
                "id=" + id +
                ", body='" + body + '\'' +
                ", noteId=" + noteId +
                ", authorId=" + authorId +
                ", revisionId=" + revisionId +
                ", created=" + created +
                '}';
    }
}
