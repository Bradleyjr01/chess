package Server.Services;

import Server.DataAccessing.*;
import Server.Requests.JoinGameRequest;
import Server.Results.JoinGameResult;
import Server.Server;

public class JoinGameService {

    /**
     * Attempts to join a game
     * role specified in request
     * throws DataAccessException if user is not found, or if game is not found
     *
     * @param request - the JoinGame request to check
     * @return JoinGameResult with new AuthToken and current username if password is valid
     *         or JoinGame result with message "unauthorized" if password is not valid
     *
     */
    public JoinGameResult joinGame(JoinGameRequest request) {
        //verify AuthToken
        AuthDAO tokenAccess = new AuthDAO(Server.MEMORY_DATA_ACCESS);
        AuthToken myToken = new AuthToken();
        try{
            myToken = tokenAccess.findAuth(request.getAuthorization());
        }
        catch (DataAccessException e) {
            System.out.println("invalid auth");
            return new JoinGameResult("unauthorized");
        }

        //verify request
        //if(request.getPlayerColor() == null) {
        //    System.out.println("Team is null -");
        //    return new JoinGameResult("bad request");
        //}

        //verify game
        GameDAO gameAccess = new GameDAO(Server.MEMORY_DATA_ACCESS);
        GameData myGame = new GameData();
        try{
            //System.out.println("invalid game");
            myGame = gameAccess.findGame(request.getGameID());
        }
        catch(DataAccessException e) {
            return new JoinGameResult("bad request");
        }

        //verify color empty
        String requestedColor;
        if(request.getPlayerColor() != null) {
            requestedColor = request.getPlayerColor();
        }
        else {
            requestedColor = "OBSERVER";
        }
        if(requestedColor.equalsIgnoreCase("WHITE")) {
            //System.out.println("check white");
            if(myGame.getWhiteUserName() != null) {
                System.out.println("white taken");
                return new JoinGameResult("already taken");
            }
        }
        else if(requestedColor.equalsIgnoreCase("BLACK")) {
            //System.out.println("check black");
            if(myGame.getBlackUserName() != null) {
                System.out.println("black taken");
                return new JoinGameResult("already taken");
            }
        }

        //add the player to the game
        try {
            System.out.println("added");
            gameAccess.addPlayer(myToken.getUserID(), requestedColor, myGame.getGameID());
        }
        catch(DataAccessException e) {
            return new JoinGameResult("can't find something");
        }
        System.out.println("sending result");
        return new JoinGameResult(requestedColor, myGame.getGameID());
    }

}
