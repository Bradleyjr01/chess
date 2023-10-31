package Server.Results;


public class JoinGameResult {

    String message;
    String gameID;
    String role;

    public JoinGameResult(String msg) {
        message = msg;
        role = null;
        gameID = null;
    }

    public JoinGameResult(String r, String id) {
        message = null;
        role = r;
        gameID = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getGameID() {
        return gameID;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
