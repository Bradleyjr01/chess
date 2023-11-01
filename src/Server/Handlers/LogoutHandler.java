package Server.Handlers;

import Server.DataAccessing.DataAccessException;
import Server.Requests.AuthTokenOnlyRequest;
import Server.Results.MessageResult;
import Server.Results.UserAccessResult;
import Server.Services.LogoutService;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import spark.Route;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

public class LogoutHandler implements Route {

    public Object handle(spark.Request req, spark.Response res) {

        try {
            //create new request
            Gson gson = new Gson();
            System.out.println("cp 1");
            //System.out.println("auth: " + req.headers("authorization"));
            //AuthTokenOnlyRequest request = gson.fromJson(req.headers("authorization"), AuthTokenOnlyRequest.class);
            //TODO: format request based on headers of req


            //pass request to LogoutService and get result
            LogoutService service = new LogoutService();
            System.out.println("auth: " + req.headers("authorization"));
            if(req.headers("authorization") != null) {
                MessageResult result = service.logout(req.headers("authorization"));

                //valid AuthToken
                if (result.getMessage() == null) {
                    res.status(HttpURLConnection.HTTP_OK);
                    return "{}";
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
        }//Invalid token
        catch(DataAccessException e){
                res.status(HttpURLConnection.HTTP_UNAUTHORIZED);
                e.printStackTrace();

                return "{ \"message\": \"Error: " + e.getMessage() + "\" }";
        }
    }
}