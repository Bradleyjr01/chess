package Server.Results;


import Server.DataAccessing.GameData;

public class JoinGameResult {

    String message;
    int gameID;
    String role;
    GameData joined;

    public JoinGameResult() {}

    public JoinGameResult(String msg) {
        message = msg;
        role = null;
        gameID = -1;
    }

    public JoinGameResult(String r, int id, GameData game) {
        message = null;
        role = r;
        gameID = id;
        joined = game;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public GameData getJoined() {
        return joined;
    }

    public void setJoined(GameData joined) {
        this.joined = joined;
    }

}
