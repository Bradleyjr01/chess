package Server.Handlers;

import Server.Results.MessageResult;
import Server.Services.LogoutService;
import com.google.gson.Gson;
import spark.Route;

import java.net.HttpURLConnection;

public class LogoutHandler implements Route {

    public Object handle(spark.Request req, spark.Response res) {

        //create new request
        Gson gson = new Gson();
        //System.out.println("auth: " + req.headers("authorization"));


        //pass request to LogoutService and get result
        LogoutService service = new LogoutService();
        //System.out.println("auth: " + req.headers("authorization"));
        if(req.headers("authorization") != null) {
            MessageResult result = service.logout(req.headers("authorization"));

            //valid AuthToken
            if (result.getMessage() == null) {
                res.status(HttpURLConnection.HTTP_OK);
                return "{}";
            }
            else if(result.getMessage().equals("unauthorized")) {
                res.status(HttpURLConnection.HTTP_UNAUTHORIZED);
                return "{ \"message\": \"Error: unauthorized\" }";
            }
            //something went wrong
            else {
                res.status(HttpURLConnection.HTTP_INTERNAL_ERROR);
                return "{ \"message\": \"Error: internal error\" }";
            }
        }
        else {
            res.status(HttpURLConnection.HTTP_UNAUTHORIZED);
            return "{ \"message\": \"Error: unauthorized\" }";
        }
    }
}