package Server.Handlers;

import Server.DataAccessing.DataAccessException;
import Server.Requests.HTTPRequest;
import Server.Results.MessageResult;
import Server.Results.UserAccessResult;
import Server.Services.ClearService;
import com.google.gson.Gson;
import spark.Route;

import java.net.HttpURLConnection;

public class ClearHandler implements Route {

    public Object handle(spark.Request req, spark.Response res) {
        System.out.println("req " + req.body());

        //create new request
        System.out.println("before gson");
        Gson gson = new Gson();
        System.out.println("before gson");
        String myGson = gson.toJson(req.body(), HTTPRequest.class);
        HTTPRequest request = gson.fromJson(req.body(), HTTPRequest.class);
        System.out.println("aft gson");

        //pass request to ClearService and get result
        ClearService service = new ClearService();
        MessageResult result = service.clear(request);

        res.status(HttpURLConnection.HTTP_OK);
        return res;
    }
}
