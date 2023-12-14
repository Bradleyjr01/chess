package webSocketMessages.userCommands;

public class ResignCommand extends UserGameCommand{
    public ResignCommand(String authToken, CommandType command, String user, int id) {
        super(authToken);
        commandType = command;
        myUsername = user;
        gameID = id;
    }
}
