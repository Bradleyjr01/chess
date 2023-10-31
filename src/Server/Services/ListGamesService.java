package Server.Services;

import Server.DataAccessing.*;
import Server.Results.ListGamesResult;
import Server.Requests.AuthTokenOnlyRequest;
import Server.Results.MessageResult;
import Server.Server;

import java.util.ArrayList;
import java.util.Collection;

public class ListGamesService {
    public ListGamesResult listGames(AuthTokenOnlyRequest request) throws DataAccessException{
        AuthDAO tokenAccess = new AuthDAO(Server.MEMORY_DATA_ACCESS);
        AuthToken token = new AuthToken();
        try{
            token = tokenAccess.findAuth(request.getAuthToken());
        }
        catch(DataAccessException e) {
            throw new DataAccessException("unauthorized");
        }
        if(token != null){
            GameDAO gameAccess = new GameDAO(Server.MEMORY_DATA_ACCESS);
            Collection<GameData> activeGames = gameAccess.findAllGames();

            return new ListGamesResult(activeGames);
        }
        return new ListGamesResult("an error has occurred");
    }
}
