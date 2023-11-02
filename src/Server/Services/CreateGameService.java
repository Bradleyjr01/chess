package Server.Services;

import Server.Requests.CreateGameRequest;
import Server.Results.CreateGameResult;
import Server.DataAccessing.*;
import Server.Server;

public class CreateGameService {
    /**
     * Attempts to log in user specified in request
     *
     * @param request - the create request to check
     * @return gameID if Token is valid
     *         or error message if invalid Token or other Error
     *
     */
    public CreateGameResult CreateGame(CreateGameRequest request) throws DataAccessException {

        //if no gameID given
        if(request.getGameName() == null) return new CreateGameResult(-1, "bad request");

        //verify AuthToken
        AuthDAO tokenAccess = new AuthDAO(Server.MEMORY_DATA_ACCESS);
        try{
            tokenAccess.findAuth(request.getAuthorization());
        }
        catch (DataAccessException e) {
            throw new DataAccessException("unauthorized");
        }

        //Create the game
        GameDAO gameAccess = new GameDAO(Server.MEMORY_DATA_ACCESS);
        GameData myGame = new GameData(request.getGameName());
        gameAccess.addGame(myGame);
        return new CreateGameResult(myGame.getGameID(), null);
    }
}
