package notesServer.database.dao.impl;

import notesServer.database.dao.iface.SectionDao;
import notesServer.database.mappers.SectionMapper;
import notesServer.erros.ServerError;
import notesServer.erros.ServerException;
import notesServer.model.Section;
import notesServer.model.User;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SectionDaoImpl implements SectionDao {

    private final Logger LOGGER = LoggerFactory.getLogger(SectionDaoImpl.class);

    private SectionMapper sectionMapper;

    @Autowired
    public SectionDaoImpl(SectionMapper sectionMapper) {
        this.sectionMapper = sectionMapper;
    }

    @Override
    public Section insertSection(Section section) throws ServerException {
        LOGGER.debug("Insert new section {}", section);
        try {
            sectionMapper.updateSessionLifetime(section.getCreator().getId());
            sectionMapper.insertSection(section);
        } catch (DataAccessException e) {
            if (e.getClass() == DuplicateKeyException.class) {
                ServerError er = ServerError.SECTION_SAME_NAME;
                er.setMessage(String.format(er.getMessage(), section.getCreator().getLogin()));
                throw new ServerException(ServerError.SECTION_SAME_NAME);
            }
            LOGGER.info("Can't insert this section {}! Cause: {}", section, ExceptionUtils.getStackTrace(e));
            throw new ServerException(ServerError.SQL_SERVER_ERROR);
        }
        return section;
    }

    @Override
    public void renameSection(User user, Section section) throws ServerException {
        LOGGER.debug("Rename section {}", section);
        try {
            sectionMapper.updateSessionLifetime(user.getId());
            sectionMapper.renameSection(section);
        } catch (DataAccessException e) {
            LOGGER.info("Can't rename section {}! Cause: {}", section, ExceptionUtils.getStackTrace(e));
            throw new ServerException(ServerError.SQL_SERVER_ERROR);
        }
    }

    @Override
    public void deleteSection(User user, long sectionId) throws ServerException {
        LOGGER.debug("Delete section with id = {}", sectionId);
        try {
            sectionMapper.deleteSection(user.getId(), sectionId);
            sectionMapper.updateSessionLifetime(user.getId());
        } catch (DataAccessException e) {
            LOGGER.info("Can't delete section with id = {}! Cause: {}", sectionId, ExceptionUtils.getStackTrace(e));
            throw new ServerException(ServerError.SQL_SERVER_ERROR);
        }
    }

    @Override
    public Section getSectionInfo(User user, long sectionId) throws ServerException {
        LOGGER.debug("Get section's info with id = {}", sectionId);
        try {
            sectionMapper.updateSessionLifetime(user.getId());
            return sectionMapper.getSection(sectionId);
        } catch (DataAccessException e) {
            LOGGER.info("Can't get section's info with id = {}! Cause: {}", sectionId, ExceptionUtils.getStackTrace(e));
            throw new ServerException(ServerError.SQL_SERVER_ERROR);
        }
    }

    @Override
    public List<Section> getAllSections(User user) throws ServerException {
        LOGGER.debug("Get all sections");
        try {
            sectionMapper.updateSessionLifetime(user.getId());
            return sectionMapper.getAllSections();
        } catch (DataAccessException e) {
            LOGGER.info("Can't get all sections! Cause: {}", ExceptionUtils.getStackTrace(e));
            throw new ServerException(ServerError.SQL_SERVER_ERROR);
        }
    }
}
