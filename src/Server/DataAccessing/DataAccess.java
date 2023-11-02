package Server.DataAccessing;

import java.util.ArrayList;

public interface DataAccess {
    /**
     * Delete all items in the database
     * Only usable with admin account
     */
    public void clear();

    /**
     * Creates a new user and adds it to the database
     *
     * @param myData - the user to add
     */
    public void writeUser(UserData myData);

    /**
     * Retrieves a user from the database
     *
     * @param myData - the user to retrieve
     * @return the user if found, else returns null
     */
    public UserData readUser(UserData myData);

    /**
     * Creates and adds an AuthToken to the database
     *
     * @param myToken - the AuthToken to add
     */
    public void writeAuthToken(AuthToken myToken);

    /**
     * Retrieves an AuthToken's verifiability from the database
     *
     * @param myToken - the token to read
     * @return the AuthToken if found, else return null
     */
    public AuthToken readAuthToken(AuthToken myToken);

    /**
     * Retrieves an AuthToken's verifiability from the database
     *
     * @param stringToken - the token to read
     * @return the AuthToken if found, else return null
     */
    public AuthToken readAuthToken(String stringToken);

    /**
     * Removes an AuthToken from the database
     *
     * @param myToken - the AuthToken to delete
     */
    public void deleteAuthToken(AuthToken myToken);

    /**
     * Creates a new game and adds it to the database
     *
     * @param myGame - the GameData to add
     * @return theGame added
     */
    public GameData newGame(GameData myGame);

    /**
     * Updates the state of a game in the database
     *
     * @param myGame - the GameData to update
     * @return the updated GameData
     */
    public GameData updateGame(GameData myGame);

    /**
     * Lists all active games in the database
     *
     * @return all games in the database
     */
    public ArrayList<GameData> listAllGames();

    /**
     * Retrieve a single game from the database
     *
     * @param myGame - the GameData to retrieve
     * @return the GameData if it is present, else null
     */
    public GameData readGame(GameData myGame);

    /**
     * Retrieve a single game from the database
     *
     * @param gameID - the ID of the game to retrieve
     * @return the GameData if it is present, else null
     */
    public GameData readGame(int gameID);

    /**
     * Removes a user from the database
     *
     * @param deleteMe - the UserData to delete
     */
    public void deleteUser(UserData deleteMe);

    /**
     * Removes a game from the database
     *
     * @param deleteMe - the GameData to delete
     */
    public void deleteGame(GameData deleteMe);

    /**
     * Retrieves a user from the database
     * search by username
     *
     * @param username - the user to retrieve
     * @return the user if found, else returns null
     */
    public UserData readUser(String username);


    public ArrayList<UserData> getUsers();
    public ArrayList<GameData> getGames();
    public ArrayList<AuthToken> getTokens();
}
