package Server.Results;

import Server.Handlers.CreateGameHandler;

public class CreateGameResult {
    private int gameID;
    private String message;
    public CreateGameResult() {}

    public CreateGameResult(int id, String msg) {
        gameID = id;
        message = msg;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
