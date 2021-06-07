package notesServer.database.dao.iface;

import notesServer.erros.ServerException;
import notesServer.model.User;

import java.util.List;

public interface AccountDao {

    User insertUser(User user) throws ServerException;

    void deleteUser(User user) throws ServerException;

    void changePassword(User user) throws ServerException;

    User getUserById(long id) throws ServerException;

    void makeUserSuper(long id, User user) throws ServerException;

    List<User> getUsersByHighRating(long id, long from, long count) throws ServerException;

    List<User> getUsersByLowRating(long id, long from, long count) throws ServerException;

    List<User> getUserFollowingsList(User user, String sortBy, long from, long count) throws ServerException;

    List<User> getUserFollowersList(User user, String sortBy, long from, long count) throws ServerException;

    List<User> getUserIgnoreList(User user, String sortBy, long from, long count) throws ServerException;

    List<User> getIgnoredByUsersList(User user, String sortBy, long from, long count) throws ServerException;

    List<User> getDeletedUsersList(long id, String sortBy, long from, long count) throws ServerException;

    List<User> getSuperUsersList(long id, String sortBy, long from, long count) throws ServerException;

    List<User> getAllUsers(long id, String sortBy, long from, long count) throws ServerException;

    void addToFollowing(User user, String login) throws ServerException;

    void addToIgnore(User user, String login) throws ServerException;

    void deleteFromFollowings(User user, String login) throws ServerException;

    void deleteFromIgnore(User user, String login) throws ServerException;

    User getUserByLogin(String login) throws ServerException;

    Integer getUserRating(User user) throws ServerException;

    void testSetRating(User user, int rating) throws ServerException;
}
