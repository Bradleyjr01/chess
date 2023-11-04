package Server.Handlers;

import Server.DataAccessing.DataAccessException;
import Server.Requests.AuthTokenOnlyRequest;
import Server.Results.ListGamesResult;
import Server.Results.MessageResult;
import Server.Services.ListGamesService;
import com.google.gson.Gson;
import spark.Route;
import java.nio.charset.StandardCharsets.*;


import java.net.HttpURLConnection;

public class ListGamesHandler implements Route {

    public Object handle(spark.Request req, spark.Response res) {

        //create new request
        Gson gson = new Gson();
        AuthTokenOnlyRequest request = new AuthTokenOnlyRequest();
        request.setAuthorization(req.headers("authorization"));

        //pass request to ListGamesService and get result
        ListGamesService service = new ListGamesService();
        ListGamesResult result = service.listGames(request);

        //valid AuthToken
        if (result.getMessage() == null) {
            System.out.println("valid in list");
            res.status(HttpURLConnection.HTTP_OK);
            System.out.println("{ \"games\": " + result.getGames() + " }");
            return "{ \"games\": " + result.getGames() + " }";
        }
        else if(result.getMessage().equals("unauthorized")) {
            res.status(HttpURLConnection.HTTP_UNAUTHORIZED);
            return "{ \"message\": \"Error: unauthorized\" }";
        }
        //something went wrong
        else {
            res.status(HttpURLConnection.HTTP_INTERNAL_ERROR);
            return "{ \"message\": \"Error: " + result.getMessage() + "\" }";
        }
    }
}