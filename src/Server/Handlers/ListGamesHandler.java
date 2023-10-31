package Server.Handlers;

import Server.DataAccessing.DataAccessException;
import Server.Requests.AuthTokenOnlyRequest;
import Server.Results.ListGamesResult;
import Server.Services.ListGamesService;
import com.google.gson.Gson;
import spark.Route;

import java.net.HttpURLConnection;

public class ListGamesHandler implements Route {

    public Object handle(spark.Request req, spark.Response res) {

        try {
            //create new request
            Gson gson = new Gson();
            AuthTokenOnlyRequest request = gson.fromJson(req.body(), AuthTokenOnlyRequest.class);

            //pass request to ListGamesService and get result
            ListGamesService service = new ListGamesService();
            ListGamesResult result = service.listGames(request);

            //valid AuthToken
            if (result.getMessage() == null) {
                res.status(HttpURLConnection.HTTP_OK);
                return result.getGames();
            }
            //something went wrong
            else {
                res.status(HttpURLConnection.HTTP_INTERNAL_ERROR);
                return "{ \"message\": \"Error: " + result.getMessage() + "\" }";
            }
        }
        //Invalid token
        catch (DataAccessException e) {
            res.status(HttpURLConnection.HTTP_UNAUTHORIZED);
            e.printStackTrace();

            return "{ \"message\": \"Error: " + e.getMessage() + "\" }";
        }
    }
}