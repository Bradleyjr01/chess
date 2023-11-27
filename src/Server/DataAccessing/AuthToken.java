package Server.DataAccessing;

import java.util.Objects;
import java.util.UUID;

public class AuthToken {

    private String authToken;
    private String userID;

    public AuthToken(){}

    public AuthToken(String token, String user){
        authToken = token;
        userID = user;
    }

    public String getUserID() {
        return userID;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthToken authToken1 = (AuthToken) o;
        return Objects.equals(authToken, authToken1.authToken) && Objects.equals(userID, authToken1.userID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authToken, userID);
    }

    public String generateToken(String userID){
        String uniqueID = UUID.randomUUID().toString();
        return uniqueID + userID.hashCode() * hashCode();
    }
}
