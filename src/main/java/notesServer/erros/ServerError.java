package notesServer.erros;

public enum ServerError {

    NO_VALID_SESSION("cookie", "User has no opened session"),
    INVALID_USER_LOGIN("login", "Invalid user login"),
    INVALID_USER_PASSWORD("password", "Invalid user password"),
    INVALID_USER_NAME("name", "Invalid user name"),
    WRONG_USER_INFO("dto", "User information does not match valid"),
    NO_ACTUAL_SESSION("session", "This user has no open sessions"),
    WRONG_USER_PASSWORD("password", "Wrong user password"),
    WRONG_USER_LOGIN("login", "Wrong user login"),
    USER_DELETED("user", "This user has been deleted"),
    USER_NOT_SUPER("user", "This user is not a superuser"),
    USER_SAME_LOGIN("login", "This login already exists"),
    SQL_SERVER_ERROR("", "Error in SQL syntax!"),
    SECTION_SAME_NAME("name", "This section name already exists"),
    SECTION_IS_DELETED("", "This section has been deleted"),
    NOT_SECTION_CREATOR("user", "This user is not the creator of the section"),
    SUBJECT_SAME_NAME("name", "This subject name already exists"),
    NOTE_IS_EMPTY("note", "This note is empty or missing"),
    NOT_NOTE_CREATOR("author", "This user is not the author of the note or the superuser"),
    USER_NOTE_CREATOR("user", "This user is a note creator"),
    COMMENT_IS_EMPTY("comment", "This comment is empty or missing"),
    NOT_COMMENT_CREATOR("author", "This user is not the author of the comment");


    private String field;
    private String message;

    ServerError(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
