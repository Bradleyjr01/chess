package Server;
import Server.DataAccessing.DataAccess;
import Server.DataAccessing.SQLDataAccess;
import Server.Handlers.*;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import spark.*;

public class Server {

    private static final WebSocket.WebSocketHandler webSocketHandler = new WebSocket.WebSocketHandler();

    //public static DataAccess MEMORY_DATA_ACCESS = new MemoryDataAccess();
    public static DataAccess MEMORY_DATA_ACCESS = new SQLDataAccess();

    public static void main(String[] args) {
        Spark.port(8080);
        Spark.externalStaticFileLocation("web");

        Spark.webSocket("/connect", webSocketHandler);

        //create routes
        Spark.delete("/db", new ClearHandler());
        Spark.post("/user", new RegisterHandler());
        Spark.post("/session", new LoginHandler());
        Spark.delete("/session", new LogoutHandler());
        Spark.get("/game", new ListGamesHandler());
        Spark.post("/game", new CreateGameHandler());
        Spark.put("/game", new JoinGameHandler());

        Spark.awaitInitialization();

        System.out.println("Initializing database...");
        Database.Database.main(new String[0]);

        System.out.println("Listening on port 8080...");

    }


}
