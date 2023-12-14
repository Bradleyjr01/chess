package Server.DataAccessing;

import Database.Database;
import com.google.gson.Gson;

import java.sql.SQLException;
import java.util.ArrayList;

public class SQLDataAccess implements DataAccess {

    public static Database DATABASE = new Database();
    public static void main(String[] args) {
        try(var conn = DATABASE.getConnection()) {
            conn.setCatalog("chess");

            try (var preparedStatement = conn.prepareStatement("CREATE TABLE IF NOT EXISTS users (" +
                    "    username VARCHAR(255)," +
                    "    password VARCHAR(255) NOT NULL," +
                    "    email VARCHAR(255) NOT NULL)")) {
                preparedStatement.executeUpdate();
            }

            try (var preparedStatement = conn.prepareStatement("CREATE TABLE IF NOT EXISTS games (" +
                    "    gameID INT," +
                    "    gameName VARCHAR(255) NOT NULL," +
                    "    whiteUserName VARCHAR(255)," +
                    "    blackUserName VARCHAR(255)," +
                    "    game TEXT NOT NULL," +
                    "    gameEnded boolean)")) {
                preparedStatement.executeUpdate();
            }

            try (var preparedStatement = conn.prepareStatement("CREATE TABLE IF NOT EXISTS tokens (" +
                    "    authToken VARCHAR(255)," +
                    "    userID VARCHAR(255) NOT NULL)")) {
                preparedStatement.executeUpdate();
            }
        }
        catch(DataAccessException d) {
            System.out.println("Error: Data exception");
            d.printStackTrace();
        }
        catch(SQLException s) {
            System.out.println("Error: SQL exception");
            s.printStackTrace();
        }
    }

    /**
     * Delete all items in the database
     * Only usable with admin account
     */
    @Override
    public void clear() {
        try(var conn = DATABASE.getConnection()) {
            conn.setCatalog("chess");

            try (var preparedStatement = conn.prepareStatement("TRUNCATE users")) {
                preparedStatement.executeUpdate();
            }
            try (var preparedStatement = conn.prepareStatement("TRUNCATE games")) {
                preparedStatement.executeUpdate();
            }
            try (var preparedStatement = conn.prepareStatement("TRUNCATE tokens")) {
                preparedStatement.executeUpdate();
            }
        }
        catch(DataAccessException d) {
            System.out.println("Error: Data exception");
            d.printStackTrace();
        }
        catch(SQLException s) {
            System.out.println("Error: SQL exception");
            s.printStackTrace();
        }
    }

    /**
     * Creates a new user and adds it to the database
     *
     * @param myData - the user to add
     */
    @Override
    public void writeUser(UserData myData) {
        try(var conn = DATABASE.getConnection()) {
            conn.setCatalog("chess");

            try (var preparedStatement = conn.prepareStatement("INSERT INTO users (username, password, email) VALUES(?, ?, ?)")) {
                preparedStatement.setString(1, myData.getUsername());
                preparedStatement.setString(2, myData.getPassword());
                preparedStatement.setString(3, myData.getEmail());

                preparedStatement.executeUpdate();
            }
        }
        catch(DataAccessException d) {
            System.out.println("Error: Data exception");
            d.printStackTrace();
        }
        catch(SQLException s) {
            System.out.println("Error: SQL exception");
            s.printStackTrace();
        }
    }

    /**
     * Retrieves a user from the database
     *
     * @param myData - the user to retrieve
     * @return the user if found, else returns null
     */
    @Override
    public UserData readUser(UserData myData) {
        if(myData == null) return null;
        try(var conn = DATABASE.getConnection()) {
            conn.setCatalog("chess");

            try (var preparedStatement = conn.prepareStatement("SELECT * FROM users WHERE username=?")) {
                preparedStatement.setString(1, myData.getUsername());
                try (var rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        var usr = rs.getString("username");
                        var pwd = rs.getString("password");
                        var email = rs.getString("email");

                        Gson gson = new Gson();
                        String json = "{\"username\":\"" + usr + "\", \"password\":\""
                                        + pwd + "\", \"email\":" + email + "\" }";

                        UserData returnUser = gson.fromJson(json, UserData.class);
                        return  returnUser;
                    }
                }
            }
        }
        catch(DataAccessException d) {
            System.out.println("Error: Data exception");
            d.printStackTrace();
        }
        catch(SQLException s) {
            System.out.println("Error: SQL exception");
            s.printStackTrace();
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
        try(var conn = DATABASE.getConnection()) {
            conn.setCatalog("chess");

            try (var preparedStatement = conn.prepareStatement("INSERT INTO tokens (authToken, userID) VALUES(?, ?)")) {
                preparedStatement.setString(1, myToken.getAuthToken());
                preparedStatement.setString(2, myToken.getUserID());

                preparedStatement.executeUpdate();
            }
        }
        catch(DataAccessException d) {
            System.out.println("Error: Data exception");
            d.printStackTrace();
        }
        catch(SQLException s) {
            System.out.println("Error: SQL exception");
            s.printStackTrace();
        }
    }

    /**
     * Retrieves an AuthToken's verifiability from the database
     *
     * @param myToken - the token to read
     * @return the AuthToken if found, else return null
     */
    @Override
    public AuthToken readAuthToken(AuthToken myToken) {
        if(myToken == null) return null;
        try(var conn = DATABASE.getConnection()) {
            conn.setCatalog("chess");

            try (var preparedStatement = conn.prepareStatement("SELECT * FROM tokens WHERE authToken=?")) {
                preparedStatement.setString(1, myToken.getAuthToken());
                try (var rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        var token = rs.getString("authToken");
                        var id = rs.getString("userID");

                        Gson gson = new Gson();
                        String json = "{\"authToken\":\"" + token + "\", \"userID\":\"" + id + "\" }";

                        AuthToken returnToken = gson.fromJson(json, AuthToken.class);
                        return  returnToken;
                    }
                }
            }
        }
        catch(DataAccessException d) {
            System.out.println("Error: Data exception");
            d.printStackTrace();
        }
        catch(SQLException s) {
            System.out.println("Error: SQL exception");
            s.printStackTrace();
        }
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
        if(stringToken == null) return null;
        try(var conn = DATABASE.getConnection()) {
            conn.setCatalog("chess");

            try (var preparedStatement = conn.prepareStatement("SELECT * FROM tokens WHERE authToken=?")) {
                preparedStatement.setString(1, stringToken);
                try (var rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        var token = rs.getString("authToken");
                        var id = rs.getString("userID");

                        Gson gson = new Gson();
                        String json = "{\"authToken\":\"" + token + "\", \"userID\":\"" + id + "\" }";

                        AuthToken returnToken = gson.fromJson(json, AuthToken.class);
                        return  returnToken;
                    }
                }
            }
        }
        catch(DataAccessException d) {
            System.out.println("Error: Data exception");
            d.printStackTrace();
        }
        catch(SQLException s) {
            System.out.println("Error: SQL exception");
            s.printStackTrace();
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
        try(var conn = DATABASE.getConnection()) {
            conn.setCatalog("chess");

            try (var preparedStatement = conn.prepareStatement("DELETE FROM tokens WHERE authToken=?")) {
                preparedStatement.setString(1, myToken.getAuthToken());

                preparedStatement.executeUpdate();
            }
        }
        catch(DataAccessException d) {
            System.out.println("Error: Data exception");
            d.printStackTrace();
        }
        catch(SQLException s) {
            System.out.println("Error: SQL exception");
            s.printStackTrace();
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
        if(myGame == null) return null;
        try(var conn = DATABASE.getConnection()) {
            conn.setCatalog("chess");

            try (var preparedStatement = conn.prepareStatement("INSERT INTO games (gameID, gameName, whiteUserName, blackUserName, game) VALUES(?, ?, ?, ?, ?)")) {
                preparedStatement.setInt(1, myGame.getGameID());
                preparedStatement.setString(2, myGame.getGameName());
                if(myGame.getWhiteUserName() != null) preparedStatement.setString(3, myGame.getWhiteUserName());
                else preparedStatement.setNull(3, 0);
                if(myGame.getBlackUserName() != null) preparedStatement.setString(4, myGame.getBlackUserName());
                else preparedStatement.setNull(4, 0);
                myGame.getGame().getBoard().resetBoard();
                preparedStatement.setString(5, myGame.getGameString());
                //System.out.println("getGameString: " + myGame.getGameString());

                preparedStatement.executeUpdate();
            }
        }
        catch(DataAccessException d) {
            System.out.println("Error: Data exception");
            d.printStackTrace();
        }
        catch(SQLException s) {
            System.out.println("Error: SQL exception");
            s.printStackTrace();
        }
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
        if(myGame == null) return null;
        //System.out.println("id=" + myGame.getGameID());
        try(var conn = DATABASE.getConnection()) {
            conn.setCatalog("chess");

            GameData updateMe = readGame(myGame);
            try (var preparedStatement = conn.prepareStatement("UPDATE games SET game=? WHERE gameID=?")) {
                preparedStatement.setString(1, myGame.getGameString());
                preparedStatement.setInt(2, myGame.getGameID());

                preparedStatement.executeUpdate();
            }

            if(myGame.getWhiteUserName() != null) {
                try (var preparedStatement = conn.prepareStatement("UPDATE games SET whiteUserName=? WHERE gameID=?")) {
                    preparedStatement.setString(1, myGame.getWhiteUserName());
                    preparedStatement.setInt(2, myGame.getGameID());

                    preparedStatement.executeUpdate();

                    //System.out.println("update white: " + myGame.getWhiteUserName());
                }
            }
            if(myGame.getBlackUserName() != null) {
                try (var preparedStatement = conn.prepareStatement("UPDATE games SET blackUserName=? WHERE gameID=?")) {
                    preparedStatement.setString(1, myGame.getBlackUserName());
                    preparedStatement.setInt(2, myGame.getGameID());

                    preparedStatement.executeUpdate();
                    //System.out.println("update black: " + myGame.getBlackUserName());
                }
            }
            try(var preparedStatement = conn.prepareStatement("UPDATE games SET gameEnded=? WHERE gameID=?")) {
                preparedStatement.setBoolean(1, myGame.isGameEnded());
                preparedStatement.setInt(2, myGame.getGameID());

                preparedStatement.executeUpdate();
                //System.out.println("update game board: " + myGame.getBlackUserName());

            }

            try(var preparedStatement = conn.prepareStatement("UPDATE games SET game=? WHERE gameID=?")) {
                preparedStatement.setString(1, myGame.getGameString());
                preparedStatement.setInt(2, myGame.getGameID());

                preparedStatement.executeUpdate();
                //System.out.println("update game status: " + myGame.getBlackUserName());

            }

            return updateMe;
        }
        catch(DataAccessException d) {
            System.out.println("Error: Data exception");
            d.printStackTrace();
        }
        catch(SQLException s) {
            System.out.println("Error: SQL exception");
            s.printStackTrace();
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
        return getGames();
    }

    /**
     * Retrieve a single game from the database
     *
     * @param myGame - the GameData to retrieve
     * @return the GameData if it is present, else null
     */
    @Override
    public GameData readGame(GameData myGame) {
        if(myGame == null) return null;
        try(var conn = DATABASE.getConnection()) {
            conn.setCatalog("chess");

            try (var preparedStatement = conn.prepareStatement("SELECT * FROM games WHERE gameID=?")) {
                preparedStatement.setInt(1, myGame.getGameID());
                try (var rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        var id = rs.getInt("gameID");
                        var name = rs.getString("gameName");
                        String white;
                        if(rs.getString("whiteUserName") != null) {
                            white = rs.getString("whiteUserName");
                            //System.out.println("white:" + white);
                        }
                        else white = myGame.getWhiteUserName();
                        String black;
                        if(rs.getString("blackUserName") != null) {
                            black = rs.getString("blackUserName");
                            //System.out.println("black:" + black);
                        }
                        else black = myGame.getBlackUserName();
                        var game = rs.getString("game");

                        Gson gson = new Gson();
                        StringBuilder json = new StringBuilder();
                        json.append("{\"gameID\":\"" + id + "\", \"gameName\":\"" + name + "\", ");
                        if(white != null) json.append("\"whiteUserName\":\"" + white + "\", ");
                        else json.append("\"whiteUserName\": null, ");
                        if(black != null) json.append("\"blackUserName\":\"" + black + "\", ");
                        else json.append("\"blackUserName\": null, ");
                        json.append("\"game\": " + game + " }");

                        GameData returnGame = gson.fromJson(json.toString(), GameData.class);
                        //System.out.println("return game GameData: w=" + returnGame.getWhiteUserName() + ", b=" + returnGame.getBlackUserName());
                        return returnGame;
                    }
                }
            }
        }
        catch(DataAccessException d) {
            System.out.println("Error: Data exception");
            d.printStackTrace();
        }
        catch(SQLException s) {
            System.out.println("Error: SQL exception");
            s.printStackTrace();
        }
        return null;
    }

    @Override
    public GameData readGame(int gameID) {
        try(var conn = DATABASE.getConnection()) {
            conn.setCatalog("chess");

            try (var preparedStatement = conn.prepareStatement("SELECT * FROM games WHERE gameID=?")) {
                preparedStatement.setInt(1, gameID);
                try (var rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        var id = rs.getInt("gameID");
                        var name = rs.getString("gameName");
                        String white;
                        if(rs.getString("whiteUserName") != null) {
                        white = rs.getString("whiteUserName");
                        //System.out.println("white:" + white);
                        }
                        else white = null;
                        String black;
                        if(rs.getString("blackUserName") != null) {
                            black = rs.getString("blackUserName");
                            //System.out.println("black:" + black);
                        }
                        else black = null;
                        var game = rs.getString("game");

                        Gson gson = new Gson();
                        StringBuilder json = new StringBuilder();
                        json.append("{\"gameID\":\"" + id + "\", \"gameName\":\"" + name + "\", ");
                        if(white != null) json.append("\"whiteUserName\":\"" + white + "\", ");
                        else json.append("\"whiteUserName\": null, ");
                        if(black != null) json.append("\"blackUserName\":\"" + black + "\", ");
                        else json.append("\"blackUserName\": null, ");
                        json.append("\"game\": " + game + " }");

                        GameData returnGame = gson.fromJson(json.toString(), GameData.class);
                        //System.out.println("return game GameID: w=" + returnGame.getWhiteUserName() + ", b=" + returnGame.getBlackUserName());
                        return returnGame;
                    }
                }
            }
        }
        catch(DataAccessException d) {
            System.out.println("Error: Data exception");
            d.printStackTrace();
        }
        catch(SQLException s) {
            System.out.println("Error: SQL exception");
            s.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteUser(UserData deleteMe) {
        try(var conn = DATABASE.getConnection()) {
            conn.setCatalog("chess");

            try (var preparedStatement = conn.prepareStatement("DELETE FROM users WHERE userName=?")) {
                preparedStatement.setString(1, deleteMe.getUsername());

                preparedStatement.executeUpdate();
            }
        }
        catch(DataAccessException d) {
            System.out.println("Error: Data exception");
            d.printStackTrace();
        }
        catch(SQLException s) {
            System.out.println("Error: SQL exception");
            s.printStackTrace();
        }
    }

    @Override
    public void deleteGame(GameData deleteMe) {
        try(var conn = DATABASE.getConnection()) {
            conn.setCatalog("chess");

            try (var preparedStatement = conn.prepareStatement("DELETE FROM games WHERE gameID=?")) {
                preparedStatement.setInt(1, deleteMe.getGameID());

                preparedStatement.executeUpdate();
            }
        }
        catch(DataAccessException d) {
            System.out.println("Error: Data exception");
            d.printStackTrace();
        }
        catch(SQLException s) {
            System.out.println("Error: SQL exception");
            s.printStackTrace();
        }
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
        if(username == null) return null;
        try(var conn = DATABASE.getConnection()) {
            conn.setCatalog("chess");

            try (var preparedStatement = conn.prepareStatement("SELECT * FROM users WHERE username=?")) {
                preparedStatement.setString(1, username);
                try (var rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        var usr = rs.getString("username");
                        var pwd = rs.getString("password");
                        var email = rs.getString("email");

                        Gson gson = new Gson();
                        String json = "{\"username\":\"" + usr + "\", \"password\":\""
                                + pwd + "\", \"email\":" + email + "\" }";

                        UserData returnUser = gson.fromJson(json, UserData.class);
                        return returnUser;
                    }
                }
            }
        }
        catch(DataAccessException d) {
            System.out.println("Error: Data exception");
            d.printStackTrace();
        }
        catch(SQLException s) {
            System.out.println("Error: SQL exception");
            s.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<UserData> getUsers() {
        try(var conn = DATABASE.getConnection()) {
            conn.setCatalog("chess");

            var users = new ArrayList<UserData>();
            try (var preparedStatement = conn.prepareStatement("SELECT * FROM users")) {
                try (var rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        var usr = rs.getString("username");
                        var pwd = rs.getString("password");
                        var email = rs.getString("email");

                        Gson gson = new Gson();
                        String json = "{\"username\":\"" + usr + "\", \"password\":\""
                                + pwd + "\", \"email\":" + email + "\" }";

                        UserData returnUser = gson.fromJson(json, UserData.class);
                        users.add(returnUser);
                    }
                }
            }
            return users;
        }
        catch(DataAccessException d) {
            System.out.println("Error: Data exception");
            d.printStackTrace();
        }
        catch(SQLException s) {
            System.out.println("Error: SQL exception");
            s.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<GameData> getGames() {
        try(var conn = DATABASE.getConnection()) {
            conn.setCatalog("chess");

            var allGames = new ArrayList<GameData>();
            try (var preparedStatement = conn.prepareStatement("SELECT * FROM games")) {
                try (var rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        var id = rs.getInt("gameID");
                        var name = rs.getString("gameName");
                        String white;
                        if(rs.getString("whiteUserName") != null) {
                            white = rs.getString("whiteUserName");
                            //System.out.println("white:" + white);
                        }
                        else white = null;
                        String black;
                        if(rs.getString("blackUserName") != null) {
                            black = rs.getString("blackUserName");
                            //System.out.println("black:" + black);
                        }
                        else black = null;
                        var game = rs.getString("game");

                        Gson gson = new Gson();
                        StringBuilder json = new StringBuilder();
                        json.append("{\"gameID\":\"" + id + "\", \"gameName\":\"" + name + "\", ");
                        if(white != null) json.append("\"whiteUserName\":\"" + white + "\", ");
                        else json.append("\"whiteUserName\":null, ");
                        if(black != null) json.append("\"blackUserName\":\"" + black + "\", ");
                        else json.append("\"blackUserName\":null, ");
                        json.append("\"game\": " + game + " }");
                        //System.out.println("gameData: " + json);
                        GameData returnGame = gson.fromJson(json.toString(), GameData.class);
                        allGames.add(returnGame);
                    }
                }
            }
            return allGames;
        }
        catch(DataAccessException d) {
            System.out.println("Error: Data exception");
            d.printStackTrace();
        }
        catch(SQLException s) {
            System.out.println("Error: SQL exception");
            s.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<AuthToken> getTokens() {
        try(var conn = DATABASE.getConnection()) {
            conn.setCatalog("chess");

            var tokens = new ArrayList<AuthToken>();
            try (var preparedStatement = conn.prepareStatement("SELECT * FROM tokens")) {
                try (var rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        var token = rs.getString("authToken");
                        var id = rs.getString("userID");

                        Gson gson = new Gson();
                        String json = "{\"authToken\":\"" + token + "\", \"userID\":\"" + id + "\" }";

                        AuthToken returnToken = gson.fromJson(json, AuthToken.class);
                        tokens.add(returnToken);
                    }
                }
            }
            return tokens;
        }
        catch(DataAccessException d) {
            System.out.println("Error: Data exception");
            d.printStackTrace();
        }
        catch(SQLException s) {
            System.out.println("Error: SQL exception");
            s.printStackTrace();
        }
        return null;
    }
}