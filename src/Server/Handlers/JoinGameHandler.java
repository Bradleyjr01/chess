package Server.Handlers;

import Server.DataAccessing.DataAccessException;
import Server.Requests.CreateGameRequest;
import Server.Requests.JoinGameRequest;
import Server.Results.JoinGameResult;
import Server.Services.JoinGameService;
import com.google.gson.Gson;
import spark.Route;

import java.net.HttpURLConnection;

public class JoinGameHandler implements Route {

    public Object handle(spark.Request req, spark.Response res) {

        //create new request
        Gson gson = new Gson();
        JoinGameRequest request = gson.fromJson(req.body(), JoinGameRequest.class);
        request.setAuthorization(req.headers("authorization"));
        System.out.println("req head - join game - authorization:" + req.headers("authorization"));
        System.out.println("req body - " + req.body());

        //pass request to JoinGameService and get result
        JoinGameService service = new JoinGameService();
        JoinGameResult result = service.joinGame(request);
        if(result.getRole() != null) System.out.println("res join = " + result.getGameID() + ", " + result.getRole());
        else System.out.println("msg = " + result.getMessage());

        //user added as preferred role
        if(result.getMessage() == null) {
            String respData = "{ \"playerColor\":\"" + result.getRole()
                    + "\", \"gameID\":\"" + result.getGameID() + "\" }";
            System.out.println("response: " + respData);

            res.status(HttpURLConnection.HTTP_OK);
            return respData;
        }
        //invalid request
        else if(result.getMessage().equals("unauthorized")){
            System.out.println("Join Message: " + result.getMessage());
            res.status(HttpURLConnection.HTTP_UNAUTHORIZED);
            return "{ \"message\": \"Error: " + result.getMessage() + "\" }";
        }
        //invalid request
        else if(result.getMessage().equals("bad request")){
            System.out.println("Join Message: " + result.getMessage());
            res.status(HttpURLConnection.HTTP_BAD_REQUEST);
            return "{ \"message\": \"Error: " + result.getMessage() + "\" }";
        }
        //invalid request
        else if(result.getMessage().equals("already taken")){
            System.out.println("Join Message: " + result.getMessage());
            res.status(HttpURLConnection.HTTP_FORBIDDEN);
            return "{ \"message\": \"Error: " + result.getMessage() + "\" }";
        }
        //invalid request
        /*else {
            res.status(HttpURLConnection.HTTP_INTERNAL_ERROR);
            return "{ \"message\": \"Error: " + result.getMessage() + "\" }";
        }*/
        System.out.println("Join Message: found nothing");
        res.status(HttpURLConnection.HTTP_INTERNAL_ERROR);
        return "{ \"message\": \"Error: internal error\" }";
    }

}