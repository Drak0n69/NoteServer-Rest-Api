package notesServer.dto.request.comment;

import javax.validation.constraints.*;
import java.util.Objects;

public class CommentActionRequestDto {

    @NotBlank @NotEmpty @NotNull @Max(500)
    private String body;
    @Min(1)
    private long noteId;

    public CommentActionRequestDto() {
    }

    public CommentActionRequestDto(String body) {
        this.body = body;
    }

    public CommentActionRequestDto(String body, long noteId) {
        this(body);
        this.noteId = noteId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommentActionRequestDto)) return false;
        CommentActionRequestDto that = (CommentActionRequestDto) o;
        return noteId == that.noteId && Objects.equals(body, that.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(body, noteId);
    }

    @Override
    public String toString() {
        return "CommentActionRequestDto{" +
                "body='" + body + '\'' +
                ", noteId=" + noteId +
                '}';
    }
}
