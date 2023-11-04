package Server.Results;


public class JoinGameResult {

    String message;
    int gameID;
    String role;

    public JoinGameResult() {}

    public JoinGameResult(String msg) {
        message = msg;
        role = null;
        gameID = -1;
    }

    public JoinGameResult(String r, int id) {
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
}
