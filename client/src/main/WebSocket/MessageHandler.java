package WebSocket;

import Resources.Game;
import webSocketMessages.serverMessages.*;

public interface MessageHandler {
    void notify(ServerMessage notification);

    void updateBoard(Game load);

    void message(String notification);

    void error(String err);
}