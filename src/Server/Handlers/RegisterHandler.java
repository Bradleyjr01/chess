package Server.Handlers;

import Server.Requests.RegisterRequest;
import Server.Services.RegisterService;
import Server.Results.UserAccessResult;
import com.google.gson.Gson;
import spark.Route;

import java.net.HttpURLConnection;

public class RegisterHandler implements Route {

    public Object handle(spark.Request req, spark.Response res) {

        //create new request
        Gson gson = new Gson();
        RegisterRequest request = gson.fromJson(req.body(), RegisterRequest.class);

        //pass request to RegisterService and get result
        RegisterService service = new RegisterService();
        UserAccessResult result = service.register(request);

        //successfully registered
        if(result.getAuthToken() != null && result.getUsername() != null) {
            String respData = "{ \"username\":\"" + result.getUsername()
                    + "\", \"authToken\":\"" + result.getAuthToken() + "\" }";

            res.status(HttpURLConnection.HTTP_OK);
            System.out.println("   >> In handler: OK 200");
            return respData;
        }
        //invalid request
        else if(result.getMessage().equals("bad request")) {
            res.status(HttpURLConnection.HTTP_BAD_REQUEST);
            System.out.println("   >> In handler: Bad Request 400");
            return "{ \"message\": \"Error: " + result.getMessage() + "\" }";
        }
        //username already taken
        else if(result.getMessage().equals("already taken")) {
            res.status(HttpURLConnection.HTTP_FORBIDDEN);
            System.out.println("   >> In handler: Forbidden 403");
            return "{ \"message\": \"Error: " + result.getMessage() + "\" }";
        }
        //something weird happens
        else {
            res.status(HttpURLConnection.HTTP_INTERNAL_ERROR);
            System.out.println("   >> In handler: Internal 500");
            return "{ \"message\": \"Error: internal error }";
        }
    }

}