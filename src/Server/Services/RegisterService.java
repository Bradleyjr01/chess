package Server.Services;

import Server.DataAccessing.*;
import Server.Requests.RegisterRequest;
import Server.Requests.RegisterRequest;
import Server.Results.UserAccessResult;
import Server.Server;

public class RegisterService {

    /**
     * Attempts to register user specified in request
     * throws DataAccessException if user is not found
     *
     * @param request - the Register request to check
     * @return RegisterResult with new AuthToken and current username if unused
     *         or Register result with message "unauthorized" if password is not valid
     *
     */
    public UserAccessResult register(RegisterRequest request) {
        UserDAO userAccess = new UserDAO(Server.MEMORY_DATA_ACCESS);
        UserData myUser = new UserData();

        //verify request
        if(request.getUsername() == null || request.getEmail() == null || request.getPassword() == null) {
            return new UserAccessResult("bad request");
        }

        //check if username is taken
        try {
            //username is taken
            myUser = userAccess.findUser(request.getUsername());
            return new UserAccessResult("already taken");
        }
        //username is not taken
        catch(DataAccessException e) {
            //create user with provided data
            myUser.setUsername(request.getUsername());
            myUser.setPassword(request.getPassword());
            myUser.setEmail(request.getEmail());

            //add user to databaseregiste
            userAccess.addUser(myUser);

            //generate AuthToken
            AuthToken token = new AuthToken();
            String tokenID = token.generateToken(request.getUsername());
            token = new AuthToken(tokenID, request.getUsername());

            AuthDAO tokenAccess = new AuthDAO(Server.MEMORY_DATA_ACCESS);
            tokenAccess.addAuth(token);

            //TODO: add auto login here?

            return new UserAccessResult(tokenID, request.getUsername());
        }
    }

}