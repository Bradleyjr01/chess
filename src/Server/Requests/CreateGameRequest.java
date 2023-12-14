package Server.Requests;

public class CreateGameRequest extends HTTPRequest {
    private String authorization;
    private String gameName;
    public CreateGameRequest() { }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
}
