import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ServerFacade {
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public String facadeRegister(String[] bodyInfo) throws Exception {
        String path = "/user";
        try {
            return this.makeRequest("POST", path, bodyInfo, String.class);
        }
        catch(Exception e) {
            throw new Exception("Unable to complete command: \"register\"");
        }
    }

    public String facadeLogin(String[] bodyInfo) throws Exception {
        String path = "/session";
        try {
            return this.makeRequest("POST", path, bodyInfo, String.class);
        }
        catch(Exception e) {
            throw new Exception("Unable to complete command: \"login\"");
        }
    }

    public void facadeLogout() throws Exception {
        String path = "/session";
        try {
            this.makeRequest("DELETE", path, null, null);
        }
        catch(Exception e) {
            throw new Exception("Unable to complete command: \"logout\"");
        }
    }

    public String facadeListGames() throws Exception {
        String path = "/game";
        try {
            return this.makeRequest("GET", path, null, String.class);
        }
        catch(Exception e) {
            throw new Exception("Unable to complete command: \"list\"");
        }
    }

    public String facadeCreateGame(String bodyInfo) throws Exception {
        String path = "/game";
        try {
        return this.makeRequest("POST", path, bodyInfo, String.class);
        }
        catch(Exception e) {
            throw new Exception("Unable to complete command: \"create\"");
        }
    }

    public String facadeJoinGame(String[] bodyInfo) throws Exception {
        String path = "/game";
        try {
            return this.makeRequest("PUT", path, bodyInfo, String.class);
        }
        catch(Exception e) {
            throw new Exception("Unable to complete command: \"join\"");
        }
    }

    public String facadeObserve(String bodyInfo) throws Exception {
        String path = "/game";
        try {
            return this.makeRequest("PUT", path, bodyInfo, String.class);
        }
        catch(Exception e) {
            throw new Exception("Unable to complete command: \"observe\"");
        }
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws Exception{
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new Exception("Exception:" + ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException{
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new IOException("failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    Gson gson = new Gson();
                    response = gson.fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
