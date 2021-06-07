package notesServer.database.dao.impl;

import notesServer.database.dao.iface.SessionDao;
import notesServer.database.mappers.SessionMapper;
import notesServer.erros.ServerError;
import notesServer.erros.ServerException;
import notesServer.model.Session;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

@Component
public class SessionDaoImpl implements SessionDao {

    private final Logger LOGGER = LoggerFactory.getLogger(SessionDaoImpl.class);

    private SessionMapper sessionMapper;

    @Autowired
    public SessionDaoImpl(SessionMapper sessionMapper) {
        this.sessionMapper = sessionMapper;
    }

    @Override
    public void insertSession(Session session) throws ServerException {
        LOGGER.debug("DAO insert new session {}", session);
        try {
            sessionMapper.insert(session);
        } catch (DataAccessException e) {
            LOGGER.info("Can't insert session {}! Cause: {}", session, ExceptionUtils.getStackTrace(e));
            throw new ServerException(ServerError.SQL_SERVER_ERROR);
        }
    }

    @Override
    public void deleteSession(String cookie) throws ServerException {
        LOGGER.debug("DAO delete session with cookie {}", cookie);
        try {
            sessionMapper.delete(cookie);
        } catch (DataAccessException e) {
            LOGGER.info("Can't delete session with cookie {}! Cause: {}", cookie, ExceptionUtils.getStackTrace(e));
            throw new ServerException(ServerError.SQL_SERVER_ERROR);
        }
    }

    @Override
    public Session getSessionByCookie(String cookie) throws ServerException {
        LOGGER.debug("DAO select session by cookie {}", cookie);
        try {
            return sessionMapper.getByCookie(cookie);
        } catch (DataAccessException e) {
            LOGGER.info("Can't select session by cookie {}! Cause: {}", cookie, ExceptionUtils.getStackTrace(e));
            throw new ServerException(ServerError.SQL_SERVER_ERROR);
        }
    }
}
