package WebSocket;

import Resources.Game;
import Resources.Move;
import StartCode.ChessGame;
import StartCode.ChessMove;
import com.google.gson.Gson;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@ServerEndpoint(value = "/connect")
public class WebSocketFacade extends Endpoint {

    Session session;
    MessageHandler notificationHandler;


    public WebSocketFacade(String url, MessageHandler messageHandler) throws Exception {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            this.notificationHandler = messageHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);
            Gson gson = new Gson();

            //set message handler
            this.session.addMessageHandler(new javax.websocket.MessageHandler.Whole<String>() {
                @Override
                @OnMessage
                public void onMessage(String message) {
                    ServerMessage sm = gson.fromJson(message, ServerMessage.class);
                    switch (sm.getServerMessageType()) {
                        case LOAD_GAME:
                            notificationHandler.updateBoard(gson.fromJson(message, LoadGameMessage.class).getGame());
                        case NOTIFICATION:
                            notificationHandler.message(gson.fromJson(message, Notification.class).getMessage());
                        case ERROR:
                            notificationHandler.error(gson.fromJson(message, ErrorMessage.class).getErrorMessage());
                    }
                    Notification notification = new Gson().fromJson(message, Notification.class);
                    notificationHandler.notify(notification);
                    System.out.println(message);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new Exception(ex.getMessage());
        }
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void joinGame(String auth, String username, ChessGame.TeamColor color, int gameID) throws Exception {
        try {
            var action = new JoinPlayerCommand(auth, UserGameCommand.CommandType.JOIN_PLAYER, username, color, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            throw new Exception(ex.getMessage());
        }
    }

    public void observeGame(String auth, String username, int gameID) throws Exception {
        try {
            var action = new JoinObserverCommand(auth, UserGameCommand.CommandType.JOIN_OBSERVER, username, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            throw new Exception(ex.getMessage());
        }
    }

    public void makeMove(String auth, String username, Move move, int gameID) throws Exception {
        try {
            var action = new MakeMoveCommand(auth, UserGameCommand.CommandType.MAKE_MOVE, username, move, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            throw new Exception(ex.getMessage());
        }
    }

    public void leaveGame(String auth, String username, int gameID) throws Exception {
        try {
            var action = new LeaveCommand(auth, UserGameCommand.CommandType.LEAVE, username, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            throw new Exception(ex.getMessage());
        }
    }

    public void resignGame(String auth, String username, int gameID) throws Exception {
        try {
            var action = new ResignCommand(auth, UserGameCommand.CommandType.RESIGN, username, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            throw new Exception(ex.getMessage());
        }
    }

}
