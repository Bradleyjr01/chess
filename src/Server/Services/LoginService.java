package Server.Services;

import Server.DataAccessing.*;
import Server.Requests.LoginRequest;
import Server.Results.UserAccessResult;
import Server.Server;

public class LoginService {

    /**
     * Attempts to log in user specified in request
     * throws DataAccessException if user is not found
     *
     * @param request - the login request to check
     * @return LoginResult with new AuthToken and current username if password is valid
     *         or Login result with message "unauthorized" if password is not valid
     *
     */
    public UserAccessResult login(LoginRequest request) throws DataAccessException {
        UserDAO userAccess = new UserDAO(Server.MEMORY_DATA_ACCESS);
        UserData myUser;
        try{
            myUser = userAccess.findUser(request.getUsername());
        }
        catch(DataAccessException e) {
            throw new DataAccessException("User " + request.getUsername() + " not found");
        }
        if(myUser != null){
            if(request.getPassword().equals(myUser.getPassword())) {
                AuthToken token = new AuthToken();
                String tokenID = token.generateToken(request.getUsername());
                token = new AuthToken(tokenID, request.getUsername());

                AuthDAO tokenAccess = new AuthDAO(Server.MEMORY_DATA_ACCESS);
                tokenAccess.addAuth(token);

                return new UserAccessResult(tokenID, request.getUsername());
            }
        }
        throw new DataAccessException("unauthorized");
    }

}
