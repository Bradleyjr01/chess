package Server.Services;

import Server.DataAccessing.*;
import Server.Results.ListGamesResult;
import Server.Requests.AuthTokenOnlyRequest;
import Server.Server;

import java.util.ArrayList;
import java.util.Collection;

public class ListGamesService {
    public ListGamesResult[] listGames(AuthTokenOnlyRequest request) {
        AuthDAO tokenAccess = new AuthDAO(Server.MEMORY_DATA_ACCESS);
        AuthToken token = new AuthToken();
        //System.out.println("listing games service");
        try {
            token = tokenAccess.findAuth(request.getAuthorization());
        }
        catch(DataAccessException e) {
            //System.out.println("unauthorized");
            ListGamesResult messageRes = new ListGamesResult("unauthorized");
            ListGamesResult[] returnVal = new ListGamesResult[1];
            returnVal[0] = messageRes;
            return returnVal;
        }
        if(token != null){
            //System.out.println("authorized");
            GameDAO gameAccess = new GameDAO(Server.MEMORY_DATA_ACCESS);
            Collection<GameData> activeGames = gameAccess.findAllGames();
            ListGamesResult[] allGames = new ListGamesResult[activeGames.size()];
            int i = 0;
            for(GameData g : activeGames) {
                ListGamesResult myResult = new ListGamesResult(g.getGameID(), g.getWhiteUserName(), g.getBlackUserName(), g.getGameName());
                allGames[i] = myResult;
                i++;
            }

            return allGames;
        }
        ListGamesResult messageRes = new ListGamesResult("an error has occurred");
        ListGamesResult[] returnVal = new ListGamesResult[1];
        returnVal[0] = messageRes;
        return returnVal;
        //return new ListGamesResult("an error has occurred");
    }
}
