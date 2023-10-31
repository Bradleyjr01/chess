package Server.Handlers;

import Server.DataAccessing.DataAccessException;
import Server.Requests.JoinGameRequest;
import Server.Results.JoinGameResult;
import Server.Services.JoinGameService;
import com.google.gson.Gson;
import spark.Route;

import java.net.HttpURLConnection;

public class JoinGameHandler implements Route {

    public Object handle(spark.Request req, spark.Response res) {

        try {
            //create new request
            Gson gson = new Gson();
            JoinGameRequest request = gson.fromJson(req.body(), JoinGameRequest.class);

            //pass request to JoinGameService and get result
            JoinGameService service = new JoinGameService();
            JoinGameResult result = service.joinGame(request);

            //user added as preferred role
            if(result.getMessage() == null) {
                String respData = "{ \"playerColor\":\" " + result.getRole()
                        + "\", \"gameID\":\"" + result.getGameID() + "\" }";

                res.status(HttpURLConnection.HTTP_OK);
                return respData;
            }
            //invalid request
            else if(result.getMessage().equals("bad request")){
                res.status(HttpURLConnection.HTTP_BAD_REQUEST);
                return "{ \"message\": \"Error: " + result.getMessage() + "\" }";
            }
            //invalid request
            else if(result.getMessage().equals("unauthorized")){
                res.status(HttpURLConnection.HTTP_UNAUTHORIZED);
                return "{ \"message\": \"Error: " + result.getMessage() + "\" }";
            }
            //invalid request
            else if(result.getMessage().equals("already taken")){
                res.status(HttpURLConnection.HTTP_FORBIDDEN);
                return "{ \"message\": \"Error: " + result.getMessage() + "\" }";
            }

            //invalid request
            else {
                res.status(HttpURLConnection.HTTP_INTERNAL_ERROR);
                return "{ \"message\": \"Error: " + result.getMessage() + "\" }";
            }
        }
        //Invalid username
        catch (DataAccessException e){
            res.status(HttpURLConnection.HTTP_INTERNAL_ERROR);
            e.printStackTrace();

            return "{ \"message\": \"Error: " + e.getMessage() + "\" }";
        }
    }

}