package Server.Results;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;

public class ListGamesResult {
    private String message;
    private int gameID = -1;
    private String whiteUsername = "";
    private String blackUsername = "";
    private String gameName = "";
    private String games = null;

    //private Collection<ListGamesResult> allGamesResult;

    public ListGamesResult() { }

    public ListGamesResult(int game, String white, String black, String name){
        message = null;
        gameID = game;
        whiteUsername = white;
        blackUsername = black;
        gameName = name;
    }

    public ListGamesResult(ArrayList<ListGamesResult> list) {
        //games = myToJson(list);
        Gson gson = new Gson();
        games = gson.toJson(list);
        message = null;
    }
    public ListGamesResult(String msg) {
        message = msg;
        gameID = -1;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getWhiteUsername() {
        return whiteUsername;
    }

    public void setWhiteUsername(String whiteUsername) {
        this.whiteUsername = whiteUsername;
    }

    public String getBlackUsername() {
        return blackUsername;
    }

    public void setBlackUsername(String blackUsername) {
        this.blackUsername = blackUsername;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public String getGames() {
        return games;
    }

    public void setGames(String games) {
        this.games = games;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public static String myToJson(ArrayList<ListGamesResult> list) {
        StringBuilder string = new StringBuilder();
        string.append("[");
        for(ListGamesResult l : list) {
            string.append("{\"gameID\": " + l.gameID
                    + ", \"whiteUsername\":\"" + l.whiteUsername
                    + "\", \"blackUsername\":\"" + l.blackUsername +
                    "\", " + "\"gameName\":\"" + l.gameName + "\"} ");
        }
        string.append("]");
        return string.toString();
    }
}
