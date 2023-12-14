package Server.Requests;


public class JoinGameRequest extends HTTPRequest {
    private int gameID;
    private String playerColor;
    private String authorization;

    public JoinGameRequest(){}
    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public String getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(String playerColor) {
        this.playerColor = playerColor;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authToken) {
        this.authorization = authToken;
    }
}
