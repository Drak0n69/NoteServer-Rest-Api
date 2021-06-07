package notesServer.database.dao.iface;

import notesServer.erros.ServerException;
import notesServer.model.Section;
import notesServer.model.User;

import java.util.List;

public interface SectionDao {

    Section insertSection(Section section) throws ServerException;

    void renameSection(User user, Section section) throws ServerException;

    void deleteSection(User user, long sectionId) throws ServerException;

    Section getSectionInfo(User user, long sectionId) throws ServerException;

    List<Section> getAllSections(User user) throws ServerException;
}
