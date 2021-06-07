package notesServer.dto.response.note;

import java.time.LocalDateTime;
import java.util.Objects;

public class NoteInfoResponseDto {

    private long id;
    private String subject;
    private String body;
    private long sectionId;
    private long authorId;
    private LocalDateTime created;
    private long revisionId;

    public NoteInfoResponseDto(long id, String subject, String body, long sectionId, long authorId,
                               LocalDateTime created, long revisionId) {
        this.id = id;
        this.subject = subject;
        this.body = body;
        this.sectionId = sectionId;
        this.authorId = authorId;
        this.created = created;
        this.revisionId = revisionId;
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

    public long getSectionId() {
        return sectionId;
    }

    public void setSectionId(long sectionId) {
        this.sectionId = sectionId;
    }

    public long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public long getRevisionId() {
        return revisionId;
    }

    public void setRevisionId(long revisionId) {
        this.revisionId = revisionId;
    }

    @Override
    public String toString() {
        return "NoteInfoResponseDto{" +
                "id=" + id +
                ", subject='" + subject + '\'' +
                ", body='" + body + '\'' +
                ", sectionId=" + sectionId +
                ", authorId=" + authorId +
                ", created=" + created +
                ", revisionId=" + revisionId +
                '}';
    }
}
