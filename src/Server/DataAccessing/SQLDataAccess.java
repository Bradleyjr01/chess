package Server.DataAccessing;

import java.util.ArrayList;

public class SQLDataAccess implements DataAccess {

    //SQL Database: var here

    /**
     * Delete all items in the database
     * Only usable with admin account
     */
    @Override
    public void clear() {
        //TODO
    }

    /**
     * Creates a new user and adds it to the database
     *
     * @param myData - the user to add
     */
    @Override
    public void writeUser(UserData myData) {
        //TODO
    }

    /**
     * Retrieves a user from the database
     *
     * @param myData - the user to retrieve
     * @return the user if found, else returns null
     */
    @Override
    public UserData readUser(UserData myData) {
        return null;
    }

    /**
     * Creates and adds an AuthToken to the database
     *
     * @param myToken - the AuthToken to add
     */
    @Override
    public void writeAuthToken(AuthToken myToken) {
        //TODO
    }

    /**
     * Retrieves an AuthToken's verifiability from the database
     *
     * @param myToken - the token to read
     * @return the AuthToken if found, else return null
     */
    @Override
    public AuthToken readAuthToken(AuthToken myToken) {
        return null;
    }

    /**
     * Retrieves an AuthToken's verifiability from the database
     *
     * @param stringToken - the token to read
     * @return the AuthToken if found, else return null
     */
    @Override
    public AuthToken readAuthToken(String stringToken) {
        return null;
    }

    /**
     * Removes an AuthToken from the database
     *
     * @param myToken - the AuthToken to delete
     */
    @Override
    public void deleteAuthToken(AuthToken myToken) {
        //TODO
    }

    /**
     * Creates a new game and adds it to the database
     *
     * @param myGame - the GameData to add
     * @return theGame added
     */
    @Override
    public GameData newGame(GameData myGame) {
        return null;
    }

    /**
     * Updates the state of a game in the database
     *
     * @param myGame - the GameData to update
     * @return the updated GameData
     */
    @Override
    public GameData updateGame(GameData myGame) {
        return null;
    }

    /**
     * Lists all active games in the database
     *
     * @return all games in the database
     */
    @Override
    public ArrayList<GameData> listAllGames() {
        return null;
    }

    /**
     * Retrieve a single game from the database
     *
     * @param myGame - the GameData to retrieve
     * @return the GameData if it is present, else null
     */
    @Override
    public GameData readGame(GameData myGame) {
        return null;
    }

    @Override
    public GameData readGame(int gameID) {
        return null;
    }

    @Override
    public void deleteUser(UserData deleteMe) {

    }

    @Override
    public void deleteGame(GameData deleteMe) {

    }

    /**
     * Retrieves a user from the database
     * search by username
     *
     * @param username - the user to retrieve
     * @return the user if found, else returns null
     */
    @Override
    public UserData readUser(String username) {
        return null;
    }

    @Override
    public ArrayList<UserData> getUsers() {
        return null;
    }

    @Override
    public ArrayList<GameData> getGames() {
        return null;
    }

    @Override
    public ArrayList<AuthToken> getTokens() {
        return null;
    }
}
