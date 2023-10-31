package Server.Results;

import Server.Handlers.CreateGameHandler;

public class CreateGameResult {
    private String gameID;
    private String message;
    public CreateGameResult() {}

    public CreateGameResult(String id, String msg) {
        gameID = id;
        message = msg;
    }

    public String getGameID() {
        return gameID;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
