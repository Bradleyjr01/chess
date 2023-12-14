package webSocketMessages.userCommands;

import Resources.Move;
import StartCode.ChessMove;

public class MakeMoveCommand extends UserGameCommand {

    Move move;
    public MakeMoveCommand(String authToken, CommandType command, String user, Move toMake, int id) {
        super(authToken);
        commandType = command;
        myUsername = user;
        move = toMake;
        gameID = id;
    }

    public Move getMove() {
        return move;
    }

    public void setMove(Move move) {
        move = move;
    }


}
