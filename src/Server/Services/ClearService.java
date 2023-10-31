package Server.Services;

import Server.DataAccessing.*;
import Server.Requests.HTTPRequest;
import Server.Results.MessageResult;
import Server.Server;

public class ClearService {

    /**
     * clears all UserData, GameData, and AuthTokens from the database
     *
     * @param request - the request
     * @return MessageResult containing status of request
     */
    public MessageResult clear(HTTPRequest request) {
        //TODO: add check for admin privilege
        AuthDAO tokenAccess = new AuthDAO(Server.MEMORY_DATA_ACCESS);
        tokenAccess.clear();

        UserDAO userAccess = new UserDAO(Server.MEMORY_DATA_ACCESS);
        userAccess.clear();

        GameDAO gameAccess = new GameDAO(Server.MEMORY_DATA_ACCESS);
        gameAccess.clear();

        return new MessageResult();
    }
}
