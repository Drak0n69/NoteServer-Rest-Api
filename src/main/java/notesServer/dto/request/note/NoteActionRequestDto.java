package notesServer.dto.request.note;

import notesServer.dto.validator.SectionName;

import javax.validation.constraints.*;
import java.util.Objects;

public class NoteActionRequestDto {

    @SectionName
    private String subject;
    @NotBlank @NotEmpty @NotNull @Max(500)
    private String body;
    @Min(1)
    private long sectionId;
    @Min(1) @Max(5)
    private short rating;

    public NoteActionRequestDto() {
    }

    public NoteActionRequestDto(short rating) {
        this.rating = rating;
    }

    public NoteActionRequestDto(String body, long sectionId) {
        this.body = body;
        this.sectionId = sectionId;
    }

    public NoteActionRequestDto(String subject, String body, long sectionId) {
        this(body, sectionId);
        this.subject = subject;
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

    public short getRating() {
        return rating;
    }

    public void setRating(short rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NoteActionRequestDto)) return false;
        NoteActionRequestDto that = (NoteActionRequestDto) o;
        return sectionId == that.sectionId && rating == that.rating && Objects.equals(subject, that.subject) && Objects.equals(body, that.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subject, body, sectionId, rating);
    }

    @Override
    public String toString() {
        return "NoteActionRequestDto{" +
                "subject='" + subject + '\'' +
                ", body='" + body + '\'' +
                ", sectionId=" + sectionId +
                ", rating=" + rating +
                '}';
    }
}
