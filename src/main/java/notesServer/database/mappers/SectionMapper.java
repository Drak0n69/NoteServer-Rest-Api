package notesServer.database.mappers;

import notesServer.model.Section;
import notesServer.model.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface SectionMapper {

    @Insert("INSERT INTO section(name, creatorId) VALUES (#{section.name}, #{section.creator.id})")
    @Options(useGeneratedKeys = true, keyProperty = "section.id")
    void insertSection(@Param("section") Section section);

    @Update("UPDATE section SET name = #{section.name} WHERE id = #{section.id}")
    void renameSection(@Param("section") Section section);

    @Delete("DELETE FROM section WHERE id = #{sectionId} AND creatorId = #{userId}")
    void deleteSection(long userId, long sectionId);

    @Select("SELECT * FROM section WHERE id = #{sectionId}")
    @Results(id = "getSection", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "creator", column = "creatorId", javaType = User.class,
                    one = @One(select = "notesServer.database.mappers.AccountMapper.getUserById", fetchType = FetchType.LAZY))
    })
    Section getSection(long sectionId);

    @Select("SELECT * FROM section")
    @ResultMap("getSection")
    List<Section> getAllSections();

    @Update("UPDATE session SET date = now() WHERE userId = #{id}")
    void updateSessionLifetime(long id);
}
