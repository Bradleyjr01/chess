package Server.Results;

public class UserAccessResult {
    private String message;
    private String authToken;
    private String username;
    public UserAccessResult() {}
    public UserAccessResult(String token, String name){
        authToken = token;
        username = name;
        message = "";
    }

    public UserAccessResult(String msg) {
        authToken = null;
        username = null;
        message = msg;
    }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getAuthToken() { return authToken; }
    public void setAuthToken(String authToken) { this.authToken = authToken; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }
}