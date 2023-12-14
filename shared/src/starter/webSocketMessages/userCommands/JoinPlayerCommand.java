package webSocketMessages.userCommands;

import StartCode.ChessGame;

public class JoinPlayerCommand extends UserGameCommand{
    private ChessGame.TeamColor playerColor;

    public JoinPlayerCommand(String authToken, CommandType command, String user, ChessGame.TeamColor color, int id) {
        super(authToken);
        commandType = command;
        myUsername = user;
        playerColor = color;
        gameID = id;
    }

    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(ChessGame.TeamColor playerColor) {
        this.playerColor = playerColor;
    }
}
