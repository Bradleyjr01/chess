package Server.Handlers;

import Server.Requests.AuthTokenOnlyRequest;
import Server.Results.ListGamesResult;
import Server.Services.ListGamesService;
import com.google.gson.Gson;
import spark.Route;


import java.net.HttpURLConnection;

public class ListGamesHandler implements Route {

    public Object handle(spark.Request req, spark.Response res) {

        //create new request
        //System.out.println("creating request in handler");
        Gson gson = new Gson();
        AuthTokenOnlyRequest request = new AuthTokenOnlyRequest();
        request.setAuthorization(req.headers("authorization"));

        //pass request to ListGamesService and get result
        //System.out.println("passing request to service");
        ListGamesService service = new ListGamesService();
        ListGamesResult[] result = service.listGames(request);

        //valid AuthToken
        if (result[0].getMessage() == null) {
            //System.out.println("valid in list");
            res.status(HttpURLConnection.HTTP_OK);
            StringBuilder library = new StringBuilder("[");
            for(int i = 0; i < result.length; i++) {
                library.append(gson.toJson(result[i]) + ",");
            }
            library.append("]");
            System.out.print(library.toString());
            return library.toString();
        }
        else if(result[0].getMessage().equals("unauthorized")) {
            res.status(HttpURLConnection.HTTP_UNAUTHORIZED);
            return "{ \"message\": \"Error: unauthorized\" }";
        }
        //something went wrong
        else {
            res.status(HttpURLConnection.HTTP_INTERNAL_ERROR);
            return "{ \"message\": \"Error: " + result[0].getMessage() + "\" }";
        }
    }
}