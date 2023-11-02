package Server.Services;

import Server.DataAccessing.*;
import Server.Requests.AuthTokenOnlyRequest;
import Server.Results.MessageResult;
import Server.Server;

public class LogoutService {
    /**
     * Attempts to log in user specified in request
     * throws DataAccessException if user is not found
     *
     * @param request - the login request to check
     * @return LoginResult with new AuthToken and current username if password is valid
     *         or Login result with message "unauthorized" if password is not valid
     *
     */
    public MessageResult logout(AuthTokenOnlyRequest request) throws DataAccessException {
        AuthDAO tokenAccess = new AuthDAO(Server.MEMORY_DATA_ACCESS);
        AuthToken token = new AuthToken();
        try{
            token = tokenAccess.findAuth(request.getAuthorization());
        }
        catch(DataAccessException e) {
            throw new DataAccessException("unauthorized");
        }
        if(token != null){
            tokenAccess.deleteAuth(token);
            return new MessageResult();
        }
        return new MessageResult("an error has occurred");
    }

    public MessageResult logout(String myToken) throws DataAccessException {
        AuthDAO tokenAccess = new AuthDAO(Server.MEMORY_DATA_ACCESS);
        AuthToken token = new AuthToken();
        System.out.println("token: " + myToken);
        try{
            token = tokenAccess.findAuth(myToken);
        }
        catch(DataAccessException e) {
            throw new DataAccessException("unauthorized");
        }
        if(token != null){
            tokenAccess.deleteAuth(token);
            return new MessageResult();
        }
        return new MessageResult("an error has occurred");
    }
}
