package Server.Services;

import Server.DataAccessing.*;
import Server.Results.ListGamesResult;
import Server.Requests.AuthTokenOnlyRequest;
import Server.Server;

import java.util.ArrayList;
import java.util.Collection;

public class ListGamesService {
    public ListGamesResult listGames(AuthTokenOnlyRequest request) throws DataAccessException{
        AuthDAO tokenAccess = new AuthDAO(Server.MEMORY_DATA_ACCESS);
        AuthToken token = new AuthToken();
        try{
            token = tokenAccess.findAuth(request.getAuthorization());
        }
        catch(DataAccessException e) {
            throw new DataAccessException("unauthorized");
        }
        if(token != null){
            GameDAO gameAccess = new GameDAO(Server.MEMORY_DATA_ACCESS);
            Collection<GameData> activeGames = gameAccess.findAllGames();
            ArrayList<ListGamesResult> allGames = new ArrayList<>();
            for(GameData g : activeGames) {
                ListGamesResult myResult = new ListGamesResult(g.getGameID(), g.getWhiteUserName(), g.getBlackUserName(), g.getGameName());
                allGames.add(myResult);
            }

            return new ListGamesResult(allGames);
        }
        return new ListGamesResult("an error has occurred");
    }
}
