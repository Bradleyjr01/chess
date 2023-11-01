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

        //create new request
        Gson gson = new Gson();
        HTTPRequest request = gson.fromJson(req.body(), HTTPRequest.class);

        //pass request to ClearService and get result
        ClearService service = new ClearService();
        MessageResult result = service.clear(request);

        res.status(HttpURLConnection.HTTP_OK);
        return "{}";
    }
}
