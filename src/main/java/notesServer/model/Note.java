package notesServer.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Note {

    private long id;
    private String subject;
    private String body;
    private Section section;
    private User author;
    private LocalDateTime created;
    private int revisionId;
    private List<Comment> comments;
    private int rating;

    public Note() {
    }

    public Note(String subject, String body, User author, Section section, int revisionId) {
        this.subject = subject;
        this.body = body;
        this.author = author;
        this.section = section;
        this.revisionId = revisionId;
        this.created = LocalDateTime.now().withNano(0);
    }

    public Note(long id, String subject, String body, Section section, User author, LocalDateTime created,
                int revisionId) {
        this(subject, body, author, section, revisionId);
        this.created = created;
        this.id = id;
    }

    public Note(long id, String subject, String body, Section section, User author, LocalDateTime created,
                int revisionId, List<Comment> comments, short rating) {
        this(id, subject, body, section, author, created, revisionId);
        this.comments = comments;
        this.rating = rating;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public int getRevisionId() {
        return revisionId;
    }

    public void setRevisionId(int revisionId) {
        this.revisionId = revisionId;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Note)) return false;
        Note note = (Note) o;
        return id == note.id && revisionId == note.revisionId && rating == note.rating &&
                Objects.equals(subject, note.subject) && Objects.equals(body, note.body) &&
                Objects.equals(section, note.section) && Objects.equals(author, note.author) &&
                Objects.equals(created, note.created) && Objects.equals(comments, note.comments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, subject, body, section, author, created, revisionId, comments, rating);
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", subject='" + subject + '\'' +
                ", body='" + body + '\'' +
                ", section=" + section +
                ", author=" + author +
                ", created=" + created +
                ", revisionId=" + revisionId +
                ", comments=" + comments +
                ", rating=" + rating +
                '}';
    }
}
