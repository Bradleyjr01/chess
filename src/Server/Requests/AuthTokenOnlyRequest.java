package Server.Requests;

public class AuthTokenOnlyRequest {
    private String authToken;

    public void translateJSON() {

    }
    public AuthTokenOnlyRequest() {}

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
