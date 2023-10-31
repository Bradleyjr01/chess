package Server.Results;

import Server.DataAccessing.GameData;
import com.google.gson.Gson;

import java.util.Collection;

public class ListGamesResult {
    private String message;
    private String games;
    public ListGamesResult() { }

    public ListGamesResult(Collection<GameData> list) {
        Gson gson = new Gson();
        games = gson.toJson(list);
        message = null;
    }
    public ListGamesResult(String msg) {
        message = msg;
        games = null;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getGames() {
        return games;
    }

    public void setGames(String games) {
        this.games = games;
    }
}
