package Server.Handlers;

import Server.DataAccessing.DataAccessException;
import Server.Services.CreateGameService;
import Server.Requests.CreateGameRequest;
import Server.Results.CreateGameResult;
import com.google.gson.*;
import spark.*;

import java.net.HttpURLConnection;

public class CreateGameHandler implements Route {

    public Object handle(spark.Request req, spark.Response res) {

        try {
            //create new request
            Gson gson = new Gson();

            CreateGameRequest request = gson.fromJson(req.body(), CreateGameRequest.class);
            request.setAuthorization(req.headers("authorization"));
            //System.out.println("token: " + request.getAuthorization() + "[" + req.headers("authorization") + "]" + ", name: " + request.getGameName());

            //pass request to CreateGameService and get result
            CreateGameService service = new CreateGameService();
            CreateGameResult result = service.CreateGame(request);

            //valid AuthToken
            if(result.getGameID() != 0) {
                String respData = "{ \"gameID\":\" " + result.getGameID() + "\" }";
                res.status(HttpURLConnection.HTTP_OK);
                return respData;
            }
            //no GameID provided
            else {
                res.status(HttpURLConnection.HTTP_BAD_REQUEST);
                return "{ \"message\": \"Error: " + result.getMessage() + "\" }";
            }
        }
        //Invalid AuthToken
        catch (DataAccessException e){
            res.status(HttpURLConnection.HTTP_UNAUTHORIZED);
            e.printStackTrace();

            return "{ \"message\": \"Error: " + e.getMessage() + "\" }";
        }
    }
}