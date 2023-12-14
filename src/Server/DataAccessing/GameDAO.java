package Server.DataAccessing;

import java.util.Collection;

public class GameDAO extends BaseDAO {

    private static DataAccess myDatabase;

    private static int gameNumInc = 0;

    public GameDAO(DataAccess database) {
        myDatabase = database;
    }

    /**
     * deletes all data from the database
     */
    public void clear() {
        myDatabase.clear();
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
            //System.out.println("throw: can't find game");
            throw(new DataAccessException("Unable to find game " + gameID));
        }
        String roleDefine;
        if(role.equalsIgnoreCase("white")) roleDefine = "WHITE";
        else if(role.equalsIgnoreCase("black")) roleDefine = "BLACK";
        else if(role.equalsIgnoreCase("observer")) roleDefine = "OBSERVER";
        else throw(new DataAccessException("Unrecognized role"));

        switch (roleDefine) {
            case "WHITE":
                if(gameChanging.getWhiteUserName() == null) gameChanging.setWhiteUserName(username);
                //System.out.println("white changed to " + username);
                break;
            case "BLACK":
                if(gameChanging.getBlackUserName() == null) gameChanging.setBlackUserName(username);
                //System.out.println("black changed to " + username);
                break;
            case "OBSERVER":
                gameChanging.addObserver(username);
                break;
            default:
                throw (new DataAccessException("Unrecognized role"));
        }

        //System.out.println("updatedGame: w=" + gameChanging.getWhiteUserName()+ ", b="+ gameChanging.getBlackUserName() + ", id=" + gameChanging.getGameID());
        myDatabase.updateGame(gameChanging);
        GameData checkTwice = myDatabase.readGame(gameChanging);
        //System.out.println("2x check: w=" + checkTwice.getWhiteUserName()+ ", b="+ checkTwice.getBlackUserName() + ", id=" + checkTwice.getGameID());

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
        while(myDatabase.readGame(gameNumInc) != null) gameNumInc++;
        return gameNumInc;
    }

}
