package Server.Requests;

public class AuthTokenOnlyRequest {
    private String authorization;

    public AuthTokenOnlyRequest() {}
    public String getAuthorization() {
        return authorization;
    }
    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }
}
