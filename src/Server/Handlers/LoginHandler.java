package Server.Handlers;

import Server.DataAccessing.DataAccessException;
import Server.Services.LoginService;
import Server.Requests.LoginRequest;
import Server.Results.UserAccessResult;
import com.google.gson.*;
import spark.*;

import java.net.HttpURLConnection;

public class LoginHandler implements Route{

    public Object handle(spark.Request req, spark.Response res) {

            //create new request
            Gson gson = new Gson();
            LoginRequest request = gson.fromJson(req.body(), LoginRequest.class);

            //pass request to LoginService and get result
            LoginService service = new LoginService();
            UserAccessResult result = service.login(request);

            //correct password for user
            if(result.getAuthToken() != null && result.getUsername() != null) {
                String respData = "{ \"username\":\"" + result.getUsername()
                        + "\", \"authToken\":\"" + result.getAuthToken() + "\" }";

                res.status(HttpURLConnection.HTTP_OK);
                return respData;
            }
            //Invalid username or password
            else if(result.getMessage().equals("unauthorized")) {
                res.status(HttpURLConnection.HTTP_UNAUTHORIZED);
                return "{ \"message\": \"Error: unauthorized\" }";
            }
            //something weird happened
            else {
                res.status(HttpURLConnection.HTTP_INTERNAL_ERROR);
                return "{ \"message\": \"Error: Internal Error\" }";

            }
    }

}