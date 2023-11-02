package Server;

import Server.DataAccessing.AuthToken;
import Server.DataAccessing.GameData;
import Server.DataAccessing.UserData;
import Server.Requests.HTTPRequest;
import Server.Requests.JoinGameRequest;
import Server.Requests.UserAccessRequest;

public class HTTPServer {

    private AuthToken myAccess;
    private UserData myUser;
    private GameData currentGame;

    /**
     * Attempts to log a user in
     *
     * @param myRequest - the login request to parse
     * @return AuthToken for the user's session or null if invalid login
     */
    AuthToken logIn(UserAccessRequest myRequest){
        return null;
    }

    /**
     * Attempts to log a user out
     *
     * @param myRequest - the logout request to parse
     */
    void logOut(HTTPRequest myRequest){
        //TODO
    }

    /**
     * Register a new user and log them in
     *
     * @param myRequest - the user information request
     * @return the registered user
     */
    UserData registerUser(UserAccessRequest myRequest) {
        return null;
    }

    /**
     * Join selected game
     *
     * @param myRequest - the game information request
     * @return the game to join
     */
    GameData joinGame(JoinGameRequest myRequest) {
        return null;
    }

    public AuthToken getMyAccess() {
        return myAccess;
    }

    public void setMyAccess(AuthToken myAccess) {
        this.myAccess = myAccess;
    }

    public UserData getMyUser() {
        return myUser;
    }

    public void setMyUser(UserData myUser) {
        this.myUser = myUser;
    }

    public GameData getCurrentGame() {
        return currentGame;
    }

    public void setCurrentGame(GameData currentGame) {
        this.currentGame = currentGame;
    }

}
