package webSocketMessages.userCommands;

public class JoinObserverCommand extends UserGameCommand{

    public JoinObserverCommand(String authToken, CommandType command, String user, int id) {
        super(authToken);
        commandType = command;
        myUsername = user;
        gameID = id;
    }

}
