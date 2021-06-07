package notesServer.model;

import java.util.Objects;

public class NoteHistory {

    private long id;
    private long noteId;
    private String body;
    private int revisionId;

    public NoteHistory() {
    }

    public NoteHistory(long noteId, String body, int revisionId) {
        this.noteId = noteId;
        this.body = body;
        this.revisionId = revisionId;
    }

    public NoteHistory(long id, long noteId, String body, int revisionId) {
        this(noteId, body, revisionId);
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getNoteId() {
        return noteId;
    }

    public void setNoteId(long noteId) {
        this.noteId = noteId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getRevisionId() {
        return revisionId;
    }

    public void setRevisionId(int revisionId) {
        this.revisionId = revisionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NoteHistory)) return false;
        NoteHistory that = (NoteHistory) o;
        return id == that.id && noteId == that.noteId && revisionId == that.revisionId && Objects.equals(body, that.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, noteId, body, revisionId);
    }

    @Override
    public String toString() {
        return "NoteHistory{" +
                "id=" + id +
                ", noteId=" + noteId +
                ", body='" + body + '\'' +
                ", revisionId=" + revisionId +
                '}';
    }
}
