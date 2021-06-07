package notesServer.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Comment {

    private long id;
    private String body;
    private long noteId;
    private User author;
    private int revisionId;
    private LocalDateTime created;

    public Comment() {
    }

    public Comment(String body, long noteId, User author) {
        this.body = body;
        this.noteId = noteId;
        this.author = author;
        this.created = LocalDateTime.now().withNano(0);
    }

    public Comment(long id, String body, long noteId, User author, int revisionId, LocalDateTime created) {
        this(body, noteId, author);
        this.id = id;
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

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;
        Comment comment = (Comment) o;
        return id == comment.id && noteId == comment.noteId && revisionId == comment.revisionId && Objects.equals(body, comment.body) && Objects.equals(author, comment.author) && Objects.equals(created, comment.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, body, noteId, author, revisionId, created);
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", body='" + body + '\'' +
                ", noteId=" + noteId +
                ", author=" + author +
                ", revisionId=" + revisionId +
                ", created=" + created +
                '}';
    }
}
