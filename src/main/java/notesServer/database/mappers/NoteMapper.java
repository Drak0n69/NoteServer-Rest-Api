package notesServer.database.mappers;

import notesServer.model.*;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface NoteMapper {

    @Insert("INSERT INTO note(subject, sectionId, body, authorId, created, revisionId) VALUES " +
            "(#{note.subject}, #{note.section.id}, #{note.body}, #{note.author.id}, #{note.created}, #{note.revisionId})")
    @Options(useGeneratedKeys = true, keyProperty = "note.id")
    void insertNote(@Param("note") Note note);

    @Select("SELECT * FROM note WHERE id = #{id} ")
    @Results(id = "getNoteById", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "subject", column = "subject"),
            @Result(property = "body", column = "body"),
            @Result(property = "created", column = "created"),
            @Result(property = "revisionId", column = "revisionId"),
            @Result(property = "author", column = "authorId", javaType = User.class,
                    one = @One(select = "notesServer.database.mappers.AccountMapper.getUserById", fetchType = FetchType.LAZY)),
            @Result(property = "section", column = "sectionId", javaType = Section.class,
                    one = @One(select = "notesServer.database.mappers.SectionMapper.getSection", fetchType = FetchType.LAZY)),
            @Result(property = "comments", column = "id", javaType = List.class,
                    many = @Many(select = "notesServer.database.mappers.NoteMapper.getComments", fetchType = FetchType.LAZY)),
            @Result(property = "rating", column = "id", javaType = Integer.class,
                    one = @One(select = "getRating", fetchType = FetchType.LAZY))
    })
    Note getNoteById(long id);

    @Update("UPDATE note SET " +
            "sectionId = #{note.section.id}, body = #{note.body}, revisionId = #{note.revisionId} " +
            "WHERE id = #{note.id}")
    void updateNote(@Param("note") Note note);

    @Delete("DELETE FROM note WHERE id = #{noteId}")
    void deleteNote(long noteId);

    @Insert("INSERT INTO comment(body, noteId, authorId, revisionId) VALUES " +
            "(#{comment.body}, #{comment.noteId}, #{comment.author.id}, " +
            "(SELECT revisionId FROM note WHERE id = #{comment.noteId}))")
    @Options(useGeneratedKeys = true, keyProperty = "comment.id")
    void insertComment(@Param("comment") Comment comment);

    @Select("SELECT * FROM comment WHERE id = #{id}")
    @Results(id = "getComment", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "body", column = "body"),
            @Result(property = "noteId", column = "noteId"),
            @Result(property = "revisionId", column = "revisionId"),
            @Result(property = "created", column = "created"),
            @Result(property = "author", column = "authorId", javaType = User.class,
                    one = @One(select = "notesServer.database.mappers.AccountMapper.getUserById", fetchType = FetchType.LAZY))
    })
    Comment getComment(long id);

    @Select("SELECT * FROM comment WHERE noteId = #{id}")
    @ResultMap("getComment")
    List<Comment> getComments(long id);

    @Update("UPDATE comment SET body = #{comment.body} WHERE id = #{comment.id}")
    void editComment(@Param("comment") Comment comment);

    @Delete("DELETE FROM comment WHERE id = #{id}")
    void deleteComment(long id);

    @Delete("DELETE FROM comment WHERE noteId = #{noteId}")
    void deleteAllComments(long noteId);

    @Insert("INSERT INTO noteHistory(noteId, revisionId, body) VALUES " +
            "(#{noteHistory.noteId}, #{noteHistory.revisionId}, #{noteHistory.body})")
    @Options(useGeneratedKeys = true, keyProperty = "noteHistory.id")
    void insertNoteHistory(@Param("noteHistory") NoteHistory noteHistory);

    @Select("SELECT * FROM noteHistory WHERE noteId = #{noteId}")
    List<NoteHistory> getNoteHistory(long noteId);

    @Insert("INSERT INTO noteRating(noteId, rating) VALUES(#{noteId}, #{rating})")
    void ratingNotes(long noteId, int rating);

    @Select("SELECT avg(rating) FROM noteRating WHERE noteId = #{id}")
    Integer getRating(long id);

    @Update("UPDATE session SET date = now() WHERE userId = #{id}")
    void updateSessionLifetime(long id);
}
