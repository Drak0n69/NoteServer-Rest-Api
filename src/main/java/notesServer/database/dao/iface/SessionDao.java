package notesServer.database.dao.iface;

import notesServer.erros.ServerException;
import notesServer.model.Session;

public interface SessionDao {

    void insertSession(Session session) throws ServerException;

    void deleteSession(String cookie) throws ServerException;

    Session getSessionByCookie(String cookie) throws ServerException;
}
