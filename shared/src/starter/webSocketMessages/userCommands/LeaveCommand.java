package webSocketMessages.userCommands;

public class LeaveCommand extends UserGameCommand {

    public LeaveCommand(String authToken, CommandType command, String user, int id) {
        super(authToken);
        commandType = command;
        myUsername = user;
        gameID = id;
    }

}
