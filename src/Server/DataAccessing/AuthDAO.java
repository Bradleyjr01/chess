package Server.DataAccessing;

import java.util.Collection;

public class AuthDAO extends BaseDAO {

    private DataAccess myDatabase;

    public AuthDAO(DataAccess database) {
        myDatabase = database;
    }

    /**
     * add a new AuthToken to the database.
     * @param addMe - the AuthToken to add to the database
     */
    public void addAuth(AuthToken addMe) {
        myDatabase.writeAuthToken(addMe);
    }

    /**
     * retrieve an AuthToken from the database
     * @param findMe - the AuthToken to find
     * @return the AuthToken if found else null
     */
    public AuthToken findAuth(AuthToken findMe) throws DataAccessException {
        if(myDatabase.readAuthToken(findMe) != null) return findMe;
        throw(new DataAccessException("Token not valid"));
    }

    /**
     * retrieve an AuthToken from the database by tokenID
     * @param stringToken - the id of the AuthToken to find
     * @return the AuthToken if found else null
     */
    public AuthToken findAuth(String stringToken) throws DataAccessException {
        AuthToken token = myDatabase.readAuthToken(stringToken);
        if(token != null) System.out.println("AuthDAO: " + token.getAuthToken());
        if(myDatabase.readAuthToken(token) != null) return myDatabase.readAuthToken(stringToken);
        throw(new DataAccessException("Token not valid"));
    }

    /**
     * retrieve all AuthTokens from the database
     * @return collection of all AuthTokens
     */
    public Collection<AuthToken> findAllAuths() {
        return myDatabase.getTokens();
    }

    /**
     * deletes an AuthToken from the database
     * throws DataAccessException if AuthToken is not found
     * @param deleteMe - the AuthToken to delete
     */
    public void deleteAuth(AuthToken deleteMe) throws DataAccessException {
        myDatabase.deleteAuthToken(deleteMe);
    }

}
