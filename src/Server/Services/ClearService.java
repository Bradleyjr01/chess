package Server.Services;

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
        Server.MEMORY_DATA_ACCESS.clear();

        return new MessageResult();
    }
}
