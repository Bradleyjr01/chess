package webSocketMessages.serverMessages;

import Resources.Game;

public class LoadGameMessage extends ServerMessage {

    Game game;
    public LoadGameMessage(ServerMessageType type, Game gameBoard) {
        super(type);
        game = gameBoard;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
