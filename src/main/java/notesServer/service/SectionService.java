package notesServer.service;

import notesServer.database.dao.iface.SectionDao;
import notesServer.database.dao.iface.SessionDao;
import notesServer.dto.request.section.SectionActionRequestDto;
import notesServer.dto.response.section.SectionInfoResponseDto;
import notesServer.dto.response.user.EmptyResponseDto;
import notesServer.erros.ServerError;
import notesServer.erros.ServerException;
import notesServer.model.Section;
import notesServer.model.User;
import notesServer.util.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackFor = DataAccessException.class)
public class SectionService extends BaseService{

    private SectionDao sectionDao;

    @Autowired
    public SectionService(SessionDao sessionDao, Settings settings, SectionDao sectionDao) {
        super(sessionDao, settings);
        this.sectionDao = sectionDao;
    }

    public SectionInfoResponseDto createSection(SectionActionRequestDto createDto, String cookie)
            throws ServerException {
        User user = getSessionByCookie(cookie).getUser();
        Section section = new Section(createDto.getName(), user);
        sectionDao.insertSection(section);
        return new SectionInfoResponseDto(section.getId(), section.getName());
    }

    public SectionInfoResponseDto editSection(SectionActionRequestDto changeDto, long sectionId, String cookie)
        throws ServerException {
        User user = getSessionByCookie(cookie).getUser();
        Section section = sectionDao.getSectionInfo(user, sectionId);
        checkSection(section);
        if (user.getId() != section.getCreator().getId()) {
            throw new ServerException(ServerError.NOT_SECTION_CREATOR);
        }
        section.setName(changeDto.getName());
        sectionDao.renameSection(user, section);
        return new SectionInfoResponseDto(section.getId(), section.getName());
    }

    public EmptyResponseDto deleteSection(long sectionId, String cookie) throws ServerException {
        User user = getSessionByCookie(cookie).getUser();
        Section section = sectionDao.getSectionInfo(user, sectionId);
        checkSection(section);
        if (user.getId() != section.getCreator().getId() || user.isSuper()) {
            throw new ServerException(ServerError.NOT_SECTION_CREATOR);
        }
        sectionDao.deleteSection(user, sectionId);
        return new EmptyResponseDto();
    }

    public SectionInfoResponseDto getSectionInfo(long sectionId, String cookie) throws ServerException {
        User user = getSessionByCookie(cookie).getUser();
        Section section = sectionDao.getSectionInfo(user, sectionId);
        checkSection(section);
        return new SectionInfoResponseDto(section.getId(), section.getName());
    }

    public List<SectionInfoResponseDto> getAllSections(String cookie) throws ServerException {
        List<SectionInfoResponseDto> sectionsDto = new ArrayList<>();
        User user = getSessionByCookie(cookie).getUser();
        List<Section> sections = sectionDao.getAllSections(user);
        sections.forEach(i -> sectionsDto.add(new SectionInfoResponseDto(i.getId(), i.getName())));
        return sectionsDto;
    }

    private void checkSection(Section section) throws ServerException {
        if(section == null || section.getCreator() == null) {
            throw new ServerException(ServerError.SECTION_IS_DELETED);
        }
    }
}
