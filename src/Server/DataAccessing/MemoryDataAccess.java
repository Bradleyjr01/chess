package Server.DataAccessing;

import java.util.ArrayList;

public class MemoryDataAccess implements DataAccess {

    ArrayList<UserData> allUsers = new ArrayList<>();
    ArrayList<AuthToken> allTokens = new ArrayList<>();
    ArrayList<GameData> allGames = new ArrayList<>();

    /**
     * Delete all items in the database
     * Only usable with admin account
     */
    @Override
    public void clear() {
        allUsers.clear();
        allTokens.clear();
        allGames.clear();
    }

    /**
     * Creates a new user and adds it to the database
     *
     * @param myData - the user to add
     */
    @Override
    public void writeUser(UserData myData) {
        allUsers.add(myData);
    }

    /**
     * Retrieves a user from the database
     *
     * @param myData - the user to retrieve
     * @return the user if found, else returns null
     */
    @Override
    public UserData readUser(UserData myData) {
        for(UserData u : allUsers) {
            if (myData == u) {
                return u;
            }
        }

        return null;
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
        for(UserData u : allUsers) {
            if (username.equals(u.getUsername())) {
                return u;
            }
        }

        return null;
    }

    /**
     * Creates and adds an AuthToken to the database
     *
     * @param myToken - the AuthToken to add
     */
    @Override
    public void writeAuthToken(AuthToken myToken) {
        allTokens.add(myToken);
    }

    /**
     * Retrieves an AuthToken's verifiability from the database
     *
     * @param myToken - the token to read
     * @return the AuthToken if found, else return null
     */
    @Override
    public AuthToken readAuthToken(AuthToken myToken) {
        for(AuthToken t : allTokens) {
            if (myToken == t) {
                return t;
            }
        }

        return null;
    }

    /**
     * Retrieves an AuthToken's verifiability from the database
     *
     * @param stringToken - the id of the token to read
     * @return the AuthToken if found, else return null
     */
    @Override
    public AuthToken readAuthToken(String stringToken) {
        if(stringToken == null) return null;
        for(AuthToken t : allTokens) {
            if (stringToken.equals(t.getAuthToken())) {
                return t;
            }
        }
        return null;
    }

    /**
     * Removes an AuthToken from the database
     *
     * @param myToken - the AuthToken to delete
     */
    @Override
    public void deleteAuthToken(AuthToken myToken) {
        for(int i = allTokens.size() - 1; i >= 0; i--) {
            if (myToken == allTokens.get(i)) {
                allTokens.remove(i);
                break;
            }
        }
    }

    /**
     * Creates a new game and adds it to the database
     *
     * @param myGame - the GameData to add
     * @return theGame added
     */
    @Override
    public GameData newGame(GameData myGame) {
        allGames.add(myGame);
        return myGame;
    }

    /**
     * Updates the state of a game in the database
     *
     * @param myGame - the GameData to update
     * @return the updated GameData
     */
    @Override
    public GameData updateGame(GameData myGame) {
        for(GameData g : allGames) {
            if (myGame == g) {
                g = newGame(myGame);
                return g;
            }
        }
        return null;
    }

    /**
     * Lists all active games in the database
     *
     * @return all games in the database
     */
    @Override
    public ArrayList<GameData> listAllGames() {
        return allGames;
    }

    /**
     * Retrieve a single game from the database
     *
     * @param myGame - the GameData to retrieve
     * @return the GameData if it is present, else null
     */
    @Override
    public GameData readGame(GameData myGame) {
        for(GameData g : allGames) {
            if (myGame == g) {
                return g;
            }
        }
        return null;
    }

    /**
     * Retrieve a single game from the database
     *
     * @param gameID - the ID of the game to retrieve
     * @return the GameData if it is present, else null
     */
    public GameData readGame(int gameID) {
        for(GameData g : allGames) {
            if (gameID == g.getGameID()) {
                return g;
            }
        }
        return null;
    }

    /**
     * Removes a user from the database
     *
     * @param deleteMe - the UserData to delete
     */
    @Override
    public void deleteUser(UserData deleteMe) {
        for(int i = allUsers.size() - 1; i >= 0; i--) {
            if (deleteMe == allUsers.get(i)) {
                allUsers.remove(i);
                break;
            }
        }
    }

    /**
     * Removes a game from the database
     *
     * @param deleteMe - the GameData to delete
     */
    @Override
    public void deleteGame(GameData deleteMe) {
        for(int i = allGames.size() - 1; i >= 0; i--) {
            if (deleteMe == allGames.get(i)) {
                allGames.remove(i);
                break;
            }
        }
    }

    @Override
    public ArrayList<UserData> getUsers() {
        return allUsers;
    }

    @Override
    public ArrayList<GameData> getGames() {
        return allGames;
    }

    @Override
    public ArrayList<AuthToken> getTokens() {
        return allTokens;
    }

    public void setAllUsers(ArrayList<UserData> allUsers) {
        this.allUsers = allUsers;
    }
    public void setAllTokens(ArrayList<AuthToken> allTokens) {
        this.allTokens = allTokens;
    }
    public void setAllGames(ArrayList<GameData> allGames) {
        this.allGames = allGames;
    }

}