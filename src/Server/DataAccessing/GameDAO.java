package Server.DataAccessing;

import Server.DataAccessing.BaseDAO;
import Server.DataAccessing.DataAccess;
import Server.DataAccessing.DataAccessException;
import Server.DataAccessing.GameData;

import java.util.Collection;

public class GameDAO extends BaseDAO {

    private DataAccess myDatabase;

    private static int gameNumInc = 0;

    public GameDAO(DataAccess database) {
        myDatabase = database;
    }

    /**
     * add a new game to the database.
     * @param addMe - the game to add to the database
     */
    public GameData addGame(GameData addMe) {
        return myDatabase.newGame(addMe);
    }

    /**
     * retrieve a game from the database
     * @param findMe - the game to find
     * @return the game if found else throws DataAccessException
     */
    public GameData findGame(GameData findMe) throws DataAccessException {
        if(myDatabase.readGame(findMe) != null) return findMe;
        throw(new DataAccessException("game not found"));
    }

    /**
     * retrieve a game from the database
     * @param gameID - the id of the game to find
     * @return the game if found else throws DataAccessException
     */
    public GameData findGame(int gameID) throws DataAccessException {
        if(myDatabase.readGame(gameID) != null) return myDatabase.readGame(gameID);
        throw(new DataAccessException("game not found"));
    }

    /**
     * retrieve all games from the database
     * @return collection of all games
     */
    public Collection<GameData> findAllGames() {
        return myDatabase.getGames();
    }

    /**
     * adds a player to a game.
     * player's username is added as a player in the database.
     * If specified role is taken, assigned to the other color
     * If all colors are taken, assigned as an observer
     * @param username - the user to add
     * @param role - the role the player will take - WHITE, BLACK, OBSERVER
     * @param gameID - the game to join
     */
    public void addPlayer(String username, String role, int gameID) throws DataAccessException{
        GameData gameChanging = myDatabase.readGame(gameID);
        if(gameChanging == null) {
            System.out.println("throw: can't find game");
            throw(new DataAccessException("Unable to find game " + gameID));
        }
        String roleDefine;
        if(role.equalsIgnoreCase("white")) roleDefine = "WHITE";
        else if(role.equalsIgnoreCase("black")) roleDefine = "BLACK";
        else if(role.equalsIgnoreCase("observer")) roleDefine = "OBSERVER";
        else throw(new DataAccessException("Unrecognized role"));

        switch (roleDefine) {
            case "WHITE":
                /*if(gameChanging.whiteUserName != null)*/ gameChanging.setWhiteUserName(username);
                /*else {
                    System.out.println("throw: white taken");
                    throw (new DataAccessException("WHITE is already taken"));
                }*/
                break;
            case "BLACK":
                /*if(gameChanging.blackUserName != null)*/ gameChanging.setBlackUserName(username);
                /*else {
                    System.out.println("throw: black taken");
                    throw (new DataAccessException("BLACK is already taken"));
                }*/
                break;
            case "OBSERVER":
                gameChanging.addObserver(username);
                break;
            default:
                System.out.println("throw: default");
                throw (new DataAccessException("Unrecognized role"));
        }
    }

    /**
     * update a chess game
     * replace gameID, board, black player, or white player
     * throws DataAccessException if game is not found
     * @param updateMe - the game to update
     * @param updateTo - the data values to update
    */
    public void updateGame(GameData updateMe, GameData updateTo) throws DataAccessException{
        GameData updated = findGame(updateMe);
        updated = new GameData(updateTo);
        myDatabase.deleteGame(updateMe);
        myDatabase.newGame(updated);
    }

    /**
     * deletes a game from the database
     *
     * @param deleteMe - the game to delete
    */
    public void removeGame(GameData deleteMe) throws DataAccessException {
        myDatabase.deleteGame(deleteMe);
    }

    public static int createGameID() {
        gameNumInc++;
        return gameNumInc;
    }

}
