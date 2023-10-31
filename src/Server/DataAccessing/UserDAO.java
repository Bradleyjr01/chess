package Server.DataAccessing;

import java.util.Collection;

public class UserDAO extends BaseDAO {

    private DataAccess myDatabase;

    public UserDAO(DataAccess database) {
        myDatabase = database;
    }

    /**
     * add a new user to the database.
     * @param addMe - the user to add to the database
     */
    public void addUser(UserData addMe) {
        myDatabase.writeUser(addMe);
    }

    /**
     * retrieve a user from the database
     * @param findMe - the user to find
     * @return the user if found else throws DataAccessException
     */
    public UserData findUser(UserData findMe) throws DataAccessException {
        if(myDatabase.readUser(findMe) != null) return findMe;
        throw(new DataAccessException("User not found"));
    }

    public UserData findUser(String username) throws DataAccessException {
        UserData userSession = myDatabase.readUser(username);
        if(myDatabase.readUser(username) != null) return myDatabase.readUser(username);
        throw(new DataAccessException("User not found"));
    }

    /**
     * retrieve all users from the database
     * @return collection of all users
     */
    public Collection<UserData> findAllUsers() {
        return myDatabase.getUsers();
    }

    /**
     * update a chess user
     * replace username, password, or email
     * throws DataAccessException if user is not found
     * @param updateMe - the user to update
     * @param updateTo - the data values to update
     */
    public void updateUser(UserData updateMe, UserData updateTo) throws DataAccessException{
        UserData updated = findUser(updateMe);
        updated = new UserData(updateTo);
        myDatabase.deleteUser(updateMe);
        myDatabase.writeUser(updated);
    }

    /**
     * deletes a user from the database
     *
     * @param deleteMe - the user to delete
     */
    public void removeUser(UserData deleteMe) throws DataAccessException {
        myDatabase.deleteUser(deleteMe);
    }

}
