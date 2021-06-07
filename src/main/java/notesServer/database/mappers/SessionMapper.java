package notesServer.database.mappers;

import notesServer.model.Session;
import notesServer.model.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

@Mapper
public interface SessionMapper {

    @Insert("INSERT INTO session(userId, cookie, date) VALUES (#{user.id}, #{cookie}, now()) " +
            "ON DUPLICATE KEY UPDATE userId = #{user.id}, cookie = #{cookie}, date = now()")
    void insert(Session session);

    @Update("UPDATE user SET online = 0 WHERE id = (SELECT userId FROM session WHERE cookie = #{cookie})")
    @Delete("DELETE FROM session WHERE cookie = #{cookie}")
    void delete(String cookie);

    @Select("SELECT userId, cookie, date FROM session WHERE cookie = #{cookie}")
    @Results({
            @Result(property = "cookie", column = "cookie"),
            @Result(property = "date", column = "date"),
            @Result(property = "user", column = "userId", javaType = User.class,
                one = @One(select = "notesServer.database.mappers.AccountMapper.getUserById", fetchType = FetchType.LAZY))
    })
    Session getByCookie(String cookie);
}
