package notesServer.database.mappers;

import notesServer.model.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface AccountMapper {

    @Insert("INSERT INTO user(firstName, lastName, patronymic, login, password, online) VALUES " +
            "(#{user.firstName}, #{user.lastName}, #{user.patronymic}, #{user.login}, #{user.password}, #{user.online})")
    @Options(useGeneratedKeys = true, keyProperty = "user.id")
    void insertUser(@Param("user") User user);

    @Update("UPDATE user SET deleted = 1, online = 0 WHERE id = #{user.id}")
    void deleteUser(@Param("user") User user);

    @Update("UPDATE user SET password = #{user.password} WHERE id = #{user.id}")
    void changeUserPassword(@Param("user") User user);

    @Select("SELECT * FROM user WHERE id = #{id}")
    @Results(id = "getUserById", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "firstName", column = "firstName"),
            @Result(property = "lastName", column = "lastName"),
            @Result(property = "patronymic", column = "patronymic"),
            @Result(property = "login", column = "login"),
            @Result(property = "password", column = "password"),
            @Result(property = "timeRegistered", column = "timeRegistered"),
            @Result(property = "online", column = "online"),
            @Result(property = "deleted", column = "deleted"),
            @Result(property = "isSuper", column = "super"),
            @Result(property = "rating", column = "id", javaType = Integer.class,
                    one = @One(select = "getUserRating", fetchType = FetchType.LAZY))
    })
    User getUserById(long id);

    @Update("UPDATE user SET super = 1 WHERE id = #{id}")
    void makeSuperUser(long id);

    @Select("<script>" +
            "SELECT * FROM user " +
            "WHERE user.rating = 5 " +
            "GROUP BY user.id " +
            "ORDER BY rating DESC " +
            "<if test=\"count gt 0\"> LIMIT #{from}, #{count} </if> " +
            "</script>")
    List<User> getUsersByHighRating(long from, long count);

    @Select("<script>" +
            "SELECT * FROM user " +
            "WHERE user.rating = 1 " +
            "GROUP BY user.id " +
            "ORDER BY rating DESC " +
            "<if test=\"count gt 0\"> LIMIT #{from}, #{count} </if> " +
            "</script>")
    List<User> getUsersByLowRating(long from, long count);

    @Select("<script>" +
            "SELECT * FROM user " +
            "WHERE user.id IN (SELECT userFollowId FROM follow WHERE userId = #{user.id}) " +
            "GROUP BY user.id " +
            "<if test=\" sortBy eq 'asc'\"> ORDER BY user.rating ASC </if> " +
            "<if test=\" sortBy eq 'desc'\"> ORDER BY user.rating DESC </if> " +
            "<if test=\" count gt 0\"> LIMIT #{from}, #{count} </if> " +
            "</script>")
    List<User> getUserFollowings(@Param("user") User user, String sortBy, long from, long count);

    @Select("<script>" +
            "SELECT * FROM user " +
            "WHERE user.id IN (SELECT userId FROM follow WHERE userFollowId = #{user.id}) " +
            "GROUP BY user.id " +
            "<if test=\" sortBy eq 'asc'\"> ORDER BY user.rating ASC </if> " +
            "<if test=\" sortBy eq 'desc'\"> ORDER BY user.rating DESC </if> " +
            "<if test=\" count gt 0\"> LIMIT #{from}, #{count} </if> " +
            "</script>")
    List<User> getUserFollowers(@Param("user") User user, String sortBy, long from, long count);

    @Select("<script>" +
            "SELECT * FROM user " +
            "WHERE user.id IN (SELECT userIgnorId FROM ignor WHERE userId = #{user.id}) " +
            "GROUP BY user.id " +
            "<if test=\" sortBy eq 'asc'\"> ORDER BY user.rating ASC </if> " +
            "<if test=\" sortBy eq 'desc'\"> ORDER BY user.rating DESC </if> " +
            "<if test=\" count gt 0\"> LIMIT #{from}, #{count} </if> " +
            "</script>")
    List<User> getUserIgnore(@Param("user") User user, String sortBy, long from, long count);

    @Select("<script>" +
            "SELECT * FROM user " +
            "WHERE user.id IN (SELECT userId FROM ignor WHERE userIgnorId = #{user.id}) " +
            "GROUP BY user.id " +
            "<if test=\" sortBy eq 'asc'\"> ORDER BY user.rating ASC </if> " +
            "<if test=\" sortBy eq 'desc'\"> ORDER BY user.rating DESC </if> " +
            "<if test=\" count gt 0\"> LIMIT #{from}, #{count} </if> " +
            "</script>")
    List<User> getIgnoredByUser(@Param("user") User user, String sortBy, long from, long count);

    @Select("<script>" +
            "SELECT * FROM user " +
            "WHERE user.deleted = 1 " +
            "GROUP BY user.id " +
            "<if test=\" sortBy eq 'asc'\"> ORDER BY user.rating ASC </if> " +
            "<if test=\" sortBy eq 'desc'\"> ORDER BY user.rating DESC </if> " +
            "<if test=\"count gt 0\"> LIMIT #{from}, #{count} </if> " +
            "</script>")
    List<User> getDeletedUsers(String sortBy, long from, long count);

    @Select("<script>" +
            "SELECT * FROM user " +
            "WHERE user.super = 1 " +
            "GROUP BY user.id " +
            "<if test=\" sortBy eq 'asc'\"> ORDER BY user.rating ASC </if> " +
            "<if test=\" sortBy eq 'desc'\"> ORDER BY user.rating DESC </if> " +
            "<if test=\"count gt 0\"> LIMIT #{from}, #{count} </if> " +
            "</script>")
    List<User> getSuperUsers(String sortBy, long from, long count);

    @Select("<script>" +
            "SELECT * FROM user " +
            "GROUP BY user.id " +
            "<if test=\" sortBy eq 'asc'\"> ORDER BY user.rating ASC </if> " +
            "<if test=\" sortBy eq 'desc'\"> ORDER BY user.rating DESC </if> " +
            "<if test=\" count gt 0\"> LIMIT #{from}, #{count} </if> " +
            "</script>")
    List<User> getAllUsers(String sortBy, long from, long count);

    @Insert("INSERT INTO follow (userId, userFollowId) VALUES " +
            "(#{user.id}, (SELECT id FROM user WHERE login = #{login}))")
    void addToFollowing(String login, @Param("user") User user);

    @Insert("INSERT INTO ignor (userId, userIgnorId) VALUES " +
            "(#{user.id}, (SELECT id FROM user WHERE login = #{login}))")
    void addToIgnore(String login, @Param("user") User user);

    @Delete("DELETE FROM follow " +
            "WHERE userId = #{user.id} " +
            "AND userFollowId = (SELECT id FROM user WHERE login = #{login})")
    void deleteFromFollowing(String login, @Param("user") User user);

    @Delete("DELETE FROM ignor " +
            "WHERE userId = #{user.id} " +
            "AND userIgnorId = (SELECT id FROM user WHERE login = #{login})")
    void deleteFromIgnore(String login, @Param("user") User user);

    @Select("SELECT * FROM user WHERE login = #{login}")
    User getUserByLogin(String login);

    @Update("UPDATE session SET date = now() WHERE userId = #{id}")
    void updateSessionLifetime(long id);

    @Select("SELECT AVG(noteRating.rating) AS rating FROM noteRating " +
            "LEFT JOIN note ON (note.id = noteRating.noteId) " +
            "LEFT JOIN user ON (user.id = note.authorId) " +
            "WHERE user.id = #{id} " +
            "GROUP bY user.id")
    Integer getUserRating(long id);

    @Update("UPDATE user SET user.rating = #{rating} WHERE id = #{user.id}")
    void testSetUserRating(@Param("user") User user, int rating);
}
