package notesServer.service;

import notesServer.database.dao.iface.SessionDao;
import notesServer.erros.ServerError;
import notesServer.erros.ServerException;
import notesServer.model.Session;
import notesServer.util.Settings;

import java.time.LocalDateTime;

public abstract class BaseService {

    protected SessionDao sessionDao;
    protected Settings settings;

    public BaseService(SessionDao sessionDao, Settings settings) {
        this.sessionDao = sessionDao;
        this.settings = settings;
    }

    protected Session getSessionByCookie(String cookie) throws ServerException {
        Session session = sessionDao.getSessionByCookie(cookie);
        if (session == null || session.getDate().plusSeconds(settings.getUserIdleTimeout()).isBefore(LocalDateTime.now())) {
            sessionDao.deleteSession(cookie);
            throw new ServerException(ServerError.NO_ACTUAL_SESSION);
        }
        if (session.getUser().isDeleted()) {
            throw new ServerException(ServerError.USER_DELETED);
        }
        return session;
    }
}
