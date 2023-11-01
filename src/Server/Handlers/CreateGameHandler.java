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

                ///TODO get header and body to fill request object
                String fullRequest = req.headers("authorization") + req.body();
                CreateGameRequest request = gson.fromJson(fullRequest, CreateGameRequest.class);
                //request.setAuthToken(req.headers("authorization"));
                //request.setGameName(req.body());

                //pass request to CreateGameService and get result
                CreateGameService service = new CreateGameService();
                CreateGameResult result = service.CreateGame(request);

                //valid AuthToken
                if(result.getGameID() != null) {
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