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
    public UserAccessResult login(LoginRequest request) {
        UserDAO userAccess = new UserDAO(Server.MEMORY_DATA_ACCESS);
        UserData myUser;
        if(request.getUsername() == null || request.getPassword() == null) return new UserAccessResult("unauthorized");
        try {
            myUser = userAccess.findUser(request.getUsername());
        }
        catch(DataAccessException e) {
            return new UserAccessResult("unauthorized");
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
        return new UserAccessResult("unauthorized");
    }

}
