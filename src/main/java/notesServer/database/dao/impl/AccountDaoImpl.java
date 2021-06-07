package notesServer.database.dao.impl;

import notesServer.database.dao.iface.AccountDao;
import notesServer.database.mappers.AccountMapper;
import notesServer.erros.ServerError;
import notesServer.erros.ServerException;
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
public class AccountDaoImpl implements AccountDao {

    private final Logger LOGGER = LoggerFactory.getLogger(AccountDaoImpl.class);

    private AccountMapper accountMapper;

    @Autowired
    public AccountDaoImpl(AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }

    @Override
    public User insertUser(User user) throws ServerException {
        LOGGER.debug("Insert new user {}", user);
        try {
            accountMapper.insertUser(user);
        } catch (DataAccessException e) {
            if (e.getClass() == DuplicateKeyException.class) {
                ServerError er = ServerError.USER_SAME_LOGIN;
                er.setMessage(String.format(er.getMessage(), user.getLogin()));
                throw new ServerException(ServerError.USER_SAME_LOGIN);
            }
            LOGGER.info("Can't insert this user {}! Cause: {}", user, ExceptionUtils.getStackTrace(e));
            throw new ServerException(ServerError.SQL_SERVER_ERROR);
        }
        return user;
    }

    @Override
    public void deleteUser(User user) throws ServerException {
        LOGGER.debug("Delete user {}", user);
        try {
            accountMapper.deleteUser(user);
        } catch (DataAccessException e) {
            LOGGER.info("Can't delete user {}! Cause: {}", user, ExceptionUtils.getStackTrace(e));
            throw new ServerException(ServerError.SQL_SERVER_ERROR);
        }
    }

    @Override
    public void changePassword(User user) throws ServerException {
        LOGGER.debug("Change user's password {}", user);
        try {
            accountMapper.updateSessionLifetime(user.getId());
            accountMapper.changeUserPassword(user);
        } catch (DataAccessException e) {
            LOGGER.info("Can't change user's password {}! Cause: {}", user, ExceptionUtils.getStackTrace(e));
            throw new ServerException(ServerError.SQL_SERVER_ERROR);
        }
    }

    @Override
    public User getUserById(long id) throws ServerException {
        LOGGER.debug("Select user by id = {}", id);
        try {
            return accountMapper.getUserById(id);
        } catch (DataAccessException e) {
            LOGGER.info("Can't select user by id = {}! Cause: {}", id, ExceptionUtils.getStackTrace(e));
            throw new ServerException(ServerError.SQL_SERVER_ERROR);
        }
    }

    @Override
    public void makeUserSuper(long id, User user) throws ServerException {
        LOGGER.debug("Make user to super id = {}", id);
        try {
            accountMapper.updateSessionLifetime(user.getId());
            accountMapper.makeSuperUser(id);
        } catch (DataAccessException e) {
            LOGGER.info("Can't make super user id = {}! Cause: {}", id, ExceptionUtils.getStackTrace(e));
            throw new ServerException(ServerError.SQL_SERVER_ERROR);
        }
    }

    @Override
    public List<User> getUsersByHighRating(long id, long from, long count) throws ServerException {
        LOGGER.debug("Get users by high rating");
        try {
            accountMapper.updateSessionLifetime(id);
            return accountMapper.getUsersByHighRating(from, count);
        } catch (DataAccessException e) {
            LOGGER.info("Can't get users by high rating! Cause: {}", ExceptionUtils.getStackTrace(e));
            throw new ServerException(ServerError.SQL_SERVER_ERROR);
        }
    }

    @Override
    public List<User> getUsersByLowRating(long id, long from, long count) throws ServerException {
        LOGGER.debug("Get users by low rating");
        try {
            accountMapper.updateSessionLifetime(id);
            return accountMapper.getUsersByLowRating(from, count);
        } catch (DataAccessException e) {
            LOGGER.info("Can't get users by low rating! Cause: {}", ExceptionUtils.getStackTrace(e));
            throw new ServerException(ServerError.SQL_SERVER_ERROR);
        }
    }

    @Override
    public List<User> getUserFollowingsList(User user, String sortBy, long from, long count) throws ServerException {
        LOGGER.debug("Get user's followings {}", user);
        try {
            accountMapper.updateSessionLifetime(user.getId());
            return accountMapper.getUserFollowings(user, sortBy, from, count);
        } catch (DataAccessException e) {
            LOGGER.info("Can't get user's followings {}! Cause: {}", user, ExceptionUtils.getStackTrace(e));
            throw new ServerException(ServerError.SQL_SERVER_ERROR);
        }
    }

    @Override
    public List<User> getUserFollowersList(User user, String sortBy, long from, long count) throws ServerException {
        LOGGER.debug("Get user's followers {}", user);
        try {
            accountMapper.updateSessionLifetime(user.getId());
            return accountMapper.getUserFollowers(user, sortBy, from, count);
        } catch (DataAccessException e) {
            LOGGER.info("Can't get user's followers {}! Cause: {}", user, ExceptionUtils.getStackTrace(e));
            throw new ServerException(ServerError.SQL_SERVER_ERROR);
        }
    }

    @Override
    public List<User> getUserIgnoreList(User user, String sortBy, long from, long count) throws ServerException {
        LOGGER.debug("Get user's ignore list {}", user);
        try {
            accountMapper.updateSessionLifetime(user.getId());
            return accountMapper.getUserIgnore(user, sortBy, from, count);
        } catch (DataAccessException e) {
            LOGGER.info("Can't get user's ignore list {}! Cause: {}", user, ExceptionUtils.getStackTrace(e));
            throw new ServerException(ServerError.SQL_SERVER_ERROR);
        }
    }

    @Override
    public List<User> getIgnoredByUsersList(User user, String sortBy, long from, long count) throws ServerException {
        LOGGER.debug("Get ignored by users list {}", user);
        try {
            accountMapper.updateSessionLifetime(user.getId());
            return accountMapper.getIgnoredByUser(user, sortBy, from, count);
        } catch (DataAccessException e) {
            LOGGER.info("Can't get ignored by user list {}! Cause: {}", user, ExceptionUtils.getStackTrace(e));
            throw new ServerException(ServerError.SQL_SERVER_ERROR);
        }
    }

    @Override
    public List<User> getDeletedUsersList(long id, String sortBy, long from, long count) throws ServerException {
        LOGGER.debug("Get deleted users");
        try {
            accountMapper.updateSessionLifetime(id);
            return accountMapper.getDeletedUsers(sortBy, from, count);
        } catch (DataAccessException e) {
            LOGGER.info("Can't get deleted users! Cause: {}", ExceptionUtils.getStackTrace(e));
            throw new ServerException(ServerError.SQL_SERVER_ERROR);
        }
    }

    @Override
    public List<User> getSuperUsersList(long id, String sortBy, long from, long count) throws ServerException {
        LOGGER.debug("Get super users");
        try {
            accountMapper.updateSessionLifetime(id);
            return accountMapper.getSuperUsers(sortBy, from, count);
        } catch (DataAccessException e) {
            LOGGER.info("Can't get super users! Cause: {}", ExceptionUtils.getStackTrace(e));
            throw new ServerException(ServerError.SQL_SERVER_ERROR);
        }
    }

    @Override
    public List<User> getAllUsers(long id, String sortBy, long from, long count) throws ServerException {
        LOGGER.debug("Get all users");
        try {
            accountMapper.updateSessionLifetime(id);
            return accountMapper.getAllUsers(sortBy, from, count);
        } catch (DataAccessException e) {
            LOGGER.info("Can't get all users! Cause: {}", ExceptionUtils.getStackTrace(e));
            throw new ServerException(ServerError.SQL_SERVER_ERROR);
        }
    }

    @Override
    public void addToFollowing(User user, String login) throws ServerException {
        LOGGER.debug("Add login = {} to user's {} following", login, user);
        try {
            accountMapper.addToFollowing(login, user);
            accountMapper.updateSessionLifetime(user.getId());
        } catch (DataAccessException e) {
            LOGGER.info("Can't add login = {} to user's {} following! Cause: {}", login, user,
                    ExceptionUtils.getStackTrace(e));
            throw new ServerException(ServerError.SQL_SERVER_ERROR);
        }
    }

    @Override
    public void addToIgnore(User user, String login) throws ServerException {
        LOGGER.debug("Add login = {} to user's {} ignore", login, user);
        try {
            accountMapper.addToIgnore(login, user);
            accountMapper.updateSessionLifetime(user.getId());
        } catch (DataAccessException e) {
            LOGGER.info("Can't add login = {} to user's {} ignore! Cause: {}", login, user,
                    ExceptionUtils.getStackTrace(e));
            throw new ServerException(ServerError.SQL_SERVER_ERROR);
        }
    }

    @Override
    public void deleteFromFollowings(User user, String login) throws ServerException {
        LOGGER.debug("Delete login = {} from user's {} following", login, user);
        try {
            accountMapper.deleteFromFollowing(login, user);
            accountMapper.updateSessionLifetime(user.getId());
        } catch (DataAccessException e) {
            LOGGER.info("Can't delete login = {} from user's {} following! Cause: {}", login, user,
                    ExceptionUtils.getStackTrace(e));
            throw new ServerException(ServerError.SQL_SERVER_ERROR);
        }
    }

    @Override
    public void deleteFromIgnore(User user, String login) throws ServerException {
        LOGGER.debug("Delete login = {} from user's {} ignore", login, user);
        try {
            accountMapper.deleteFromIgnore(login, user);
            accountMapper.updateSessionLifetime(user.getId());
        } catch (DataAccessException e) {
            LOGGER.info("Can't delete login = {} from user's {} ignore! Cause: {}", login, user,
                    ExceptionUtils.getStackTrace(e));
            throw new ServerException(ServerError.SQL_SERVER_ERROR);
        }
    }

    @Override
    public User getUserByLogin(String login) throws ServerException {
        LOGGER.debug("Select user by login = {}", login);
        try {
            return accountMapper.getUserByLogin(login);
        } catch (DataAccessException e) {
            LOGGER.info("Can't select user by login = {}! Cause: {}", login, ExceptionUtils.getStackTrace(e));
            throw new ServerException(ServerError.SQL_SERVER_ERROR);
        }
    }

    @Override
    public Integer getUserRating(User user) throws ServerException {
        try {
            return accountMapper.getUserRating(user.getId());
        } catch (DataAccessException e) {
            LOGGER.info("Cause: {}", ExceptionUtils.getStackTrace(e));
            throw new ServerException(ServerError.SQL_SERVER_ERROR);
        }
    }

    @Override
    public void testSetRating(User user, int rating) throws ServerException {
        LOGGER.debug("Set user {} rating {}", user, rating);
        try {
            accountMapper.testSetUserRating(user, rating);
        } catch (DataAccessException e) {
            LOGGER.info("Can't set user {} rating! Cause: {}", user, ExceptionUtils.getStackTrace(e));
            throw new ServerException(ServerError.SQL_SERVER_ERROR);
        }
    }
}
