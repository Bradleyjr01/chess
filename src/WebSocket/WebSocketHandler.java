package WebSocket;
import Resources.Game;
import Resources.InvalidMoveException;
import Resources.Move;
import Server.DataAccessing.*;
import Server.Server;
import StartCode.ChessSerialization;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;
import java.sql.SQLException;

import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import static java.sql.DriverManager.getConnection;


@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();
    //public DataAccess myDatabase = Server.MEMORY_DATA_ACCESS;

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);

        //var conn = getConnection(action.getAuthString(), session);
        if(session != null) {
            switch (action.getCommandType()) {
                case JOIN_PLAYER -> player(message, session);
                case JOIN_OBSERVER -> observer(message, session);
                case MAKE_MOVE -> move(message, session);
                case RESIGN -> resign(message, session);
                case LEAVE -> leave(message, session);
            }
        }
        //else {
        //    Connection.sendError(session.getRemote(), "unknown user");
        //}
    }

    private void player(String message, Session session) throws IOException {
        JoinPlayerCommand command = new Gson().fromJson(message, JoinPlayerCommand.class);
        String username = command.getMyUsername();
        String authToken = command.getAuthString();

        AuthDAO token = new AuthDAO(Server.MEMORY_DATA_ACCESS);
        AuthToken myAuth = new AuthToken();
        try {
            myAuth = token.findAuth(authToken);
        } catch (DataAccessException e) {
            connections.add(username, session);
            String outMsg = "Error: invalid authorization";
            var errMsg = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, outMsg);
            connections.singleSend(username, errMsg);
            connections.remove(username);
        }
        if (myAuth != null) {
            connections.add(username, session);
            var loadGame = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, new Game());
            connections.singleSend(username, loadGame);
            String outMsg = username + " has joined as " + command.getPlayerColor().toString();
            var notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, outMsg);
            connections.broadcast(username, notification);
        }
    }

    private void observer(String message, Session session) throws IOException {
        JoinObserverCommand command = new Gson().fromJson(message, JoinObserverCommand.class);
        String username = command.getMyUsername();
        String authToken = command.getAuthString();

        AuthDAO token = new AuthDAO(Server.MEMORY_DATA_ACCESS);
        AuthToken myAuth = new AuthToken();
        try {
            myAuth = token.findAuth(authToken);
        } catch (DataAccessException e) {
            connections.add(username, session);
            String outMsg = "Error: invalid authorization";
            var errMsg = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, outMsg);
            connections.singleSend(username, errMsg);
            connections.remove(username);
        }
        if (myAuth != null) {
            connections.add(username, session);
            var loadGame = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, new Game());
            connections.singleSend(username, loadGame);
            String outMsg = username + " is now watching";
            var notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, outMsg);
            connections.broadcast(username, notification);
        }
    }

    private void move(String message, Session session) throws IOException {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(MakeMoveCommand.class, new MakeMoveCommandAdapter());
        Gson gson2 = builder.create();

        MakeMoveCommand command = gson2.fromJson(message, MakeMoveCommand.class);
        String username = command.getMyUsername();
        String authToken = command.getAuthString();
        int gameID = command.getGameID();

        AuthDAO token = new AuthDAO(Server.MEMORY_DATA_ACCESS);
        AuthToken myAuth = new AuthToken();
        try {
            myAuth = token.findAuth(authToken);
        } catch (DataAccessException e) {
            String outMsg = "Error: invalid authorization. Received: " + e.getMessage();
            var errMsg = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, outMsg);
            connections.singleSend(username, errMsg);
            System.out.println(e.getMessage());
        }
        if (myAuth != null) {
            GameDAO gameDAO = new GameDAO(Server.MEMORY_DATA_ACCESS);
            GameData myGame = new GameData();
            try {
                myGame = gameDAO.findGame(gameID);
            } catch (DataAccessException ex) {
                String outMsg = "Error: invalid game ID. Received:" + ex.getMessage();
                var errMsg = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, outMsg);
                connections.singleSend(username, errMsg);
                System.out.println(ex.getMessage());
            }
            if(myGame.isGameEnded()) {
                String outMsg = "Error: game has already ended.";
                var errMsg = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, outMsg);
                connections.singleSend(username, errMsg);
            }
            else {
                try {
                    myGame.getGame().makeMove(command.getMove());
                    try {
                        gameDAO.updateGame(myGame, myGame);
                        var loadGame = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, new Game());
                        connections.broadcast("", loadGame);
                        String outMsg = username + " moved " + command.getMove().toString();
                        var notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, outMsg);
                        connections.broadcast(username, notification);
                    } catch (DataAccessException ec) {
                        String outMsg = "Error: Something went wrong. Received: " + ec.getMessage();
                        var errMsg = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, outMsg);
                        connections.singleSend(username, errMsg);
                        System.out.println(ec.getMessage());
                    }
                } catch (InvalidMoveException ep) {
                    String outMsg = "Error: invalid move. Received: " + ep.getMessage();
                    var errMsg = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, outMsg);
                    connections.singleSend(username, errMsg);
                    System.out.println(ep.getMessage());
                }
            }
        }
    }

    private void leave(String message, Session session) throws IOException {
        LeaveCommand command = new Gson().fromJson(message, LeaveCommand.class);
        String username = command.getMyUsername();
        String authToken = command.getAuthString();
        int gameID = command.getGameID();

        AuthDAO token = new AuthDAO(Server.MEMORY_DATA_ACCESS);
        AuthToken myAuth = new AuthToken();
        try {
            myAuth = token.findAuth(authToken);
        } catch (DataAccessException e) {
            String outMsg = "Error: invalid authorization. Received: " + e.getMessage();
            var errMsg = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, outMsg);
            connections.singleSend(username, errMsg);
            System.out.println(e.getMessage());
        }
        if (myAuth != null) {
            GameDAO gameDAO = new GameDAO(Server.MEMORY_DATA_ACCESS);
            GameData myGame;
            try {
                myGame = gameDAO.findGame(gameID);
                if(myGame.getWhiteUserName().equalsIgnoreCase(username)) myGame.setWhiteUserName(null);
                else if(myGame.getBlackUserName().equalsIgnoreCase(username)) myGame.setBlackUserName(null);
                gameDAO.updateGame(myGame, myGame);
            } catch (DataAccessException ex) {
                String outMsg = "Error: invalid game ID. Received:" + ex.getMessage();
                var errMsg = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, outMsg);
                connections.singleSend(username, errMsg);
                System.out.println(ex.getMessage());
            }
            connections.remove(username);
            String outMsg = username + " has left the game";
            var notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, outMsg);
            connections.broadcast(username, notification);
        }
    }

    private void resign(String message, Session session) throws IOException {
        ResignCommand command = new Gson().fromJson(message, ResignCommand.class);
        String username = command.getMyUsername();
        String authToken = command.getAuthString();
        int gameID = command.getGameID();

        AuthDAO token = new AuthDAO(Server.MEMORY_DATA_ACCESS);
        AuthToken myAuth = new AuthToken();
        try {
            myAuth = token.findAuth(authToken);
        } catch (DataAccessException e) {
            String outMsg = "Error: invalid authorization. Received: " + e.getMessage();
            var errMsg = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, outMsg);
            connections.singleSend(username, errMsg);
            System.out.println(e.getMessage());
        }
        if (myAuth != null) {
            GameDAO gameDAO = new GameDAO(Server.MEMORY_DATA_ACCESS);
            GameData myGame = new GameData();
            try {
                myGame = gameDAO.findGame(gameID);
            } catch (DataAccessException ex) {
                String outMsg = "Error: invalid game ID. Received:" + ex.getMessage();
                var errMsg = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, outMsg);
                connections.singleSend(username, errMsg);
                System.out.println(ex.getMessage());
            }
            try {
                myGame.setGameEnded(true);
                gameDAO.updateGame(myGame, myGame);
                String outMsg = username + " has resigned";
                var notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, outMsg);
                connections.broadcast("", notification);
                System.out.println("    complete");

            } catch (DataAccessException ec) {
                String outMsg = "Error: Something went wrong. Received: " + ec.getMessage();
                var errMsg = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, outMsg);
                connections.singleSend(username, errMsg);
                System.out.println(ec.getMessage());
            }
        }
    }

    class MakeMoveCommandAdapter extends TypeAdapter<MakeMoveCommand> {
        @Override
        public MakeMoveCommand read(JsonReader reader) throws IOException {
            MakeMoveCommand moveCommand = new MakeMoveCommand(null, null, null, null, -1);
            reader.beginObject();
            String fieldname = null;

            while (reader.hasNext()) {
                JsonToken token = reader.peek();
                if (token.equals(JsonToken.NAME)) {
                    fieldname = reader.nextName();
                }
                if ("authToken".equals(fieldname)) {
                    token = reader.peek();
                    moveCommand.setAuthToken(reader.nextString());
                }
                if("commandType".equals(fieldname)) {
                    token = reader.peek();
                    String type = reader.nextString();
                    moveCommand.setCommandType(UserGameCommand.CommandType.MAKE_MOVE);
                }
                if("myUsername".equals(fieldname)) {
                    token = reader.peek();
                    moveCommand.setMyUsername(reader.nextString());
                }
                /*if("move".equals(fieldname)) {
                    token = reader.peek();
                    Gson gson2 = ChessSerialization.createGsonSerializer();
                    gson2.fromJson(reader.beginObject(), Move.class);
                }*/
                if("gameID".equals(fieldname)) {
                    token = reader.peek();
                    moveCommand.setGameID(reader.nextInt());
                }
            }
            reader.endObject();
            return moveCommand;
        }

        @Override
        public void write(JsonWriter writer, MakeMoveCommand moveCommand) throws IOException {
            writer.beginObject();
            writer.name("authToken");
            writer.value(moveCommand.getAuthString());
            writer.name("commandType");
            writer.value(moveCommand.getCommandType().toString());
            writer.name("myUsername");
            writer.value(moveCommand.getMyUsername());
            writer.name("move");
            writer.value(moveCommand.getMove().toString());
            writer.name("gameID");
            writer.value(moveCommand.getGameID());
            writer.endObject();
        }
    }
}
