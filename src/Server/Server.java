package Server;
import Server.DataAccessing.MemoryDataAccess;
import Server.Handlers.*;
import spark.*;

public class Server {

    public static MemoryDataAccess MEMORY_DATA_ACCESS = new MemoryDataAccess();
    public static void main(String[] args) {
        //try {
            //int port = Integer.parseInt(args[0]);
        Spark.port(8080);
        Spark.staticFiles.location("/public");

            //createRoutes();
        Spark.delete("/db", new ClearHandler());

        Spark.post("/user", new RegisterHandler());

        Spark.post("/session", new LoginHandler());

        Spark.delete("/session", new LogoutHandler());

        Spark.get("/game", new ListGamesHandler());

        Spark.post("/game", new CreateGameHandler());

        Spark.put("/game", new JoinGameHandler());

        Spark.awaitInitialization();
        System.out.println("Listening on port 8080");
        //} catch(ArrayIndexOutOfBoundsException | NumberFormatException ex) {
        //    System.err.println("Specify the port number as a command line parameter");
        //}

    }

        /*private String readString(InputStream in) throws IOException {
        StringBuilder builder = new StringBuilder();
        InputStreamReader reader = new InputStreamReader(in);
        char[] buffer = new char[1024];
        int length;
        while((length = reader.read(buffer)) > 0) {
            builder.append(buffer, 0, length);
        }
        return builder.toString();
    }

    private void writeString(String in, OutputStream out) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(out);
        writer.write(in);
        writer.flush();
    }*/

}
