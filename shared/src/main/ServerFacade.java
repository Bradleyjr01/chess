import Server.Requests.*;
import Server.Results.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collection;

public class ServerFacade {
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public UserAccessResult facadeRegister(String[] bodyInfo) throws Exception {
        String path = "/user";
        try {
            RegisterRequest req = new RegisterRequest();
            req.setUsername(bodyInfo[0]);
            req.setPassword(bodyInfo[1]);
            req.setEmail(bodyInfo[2]);
            //return this.makeRequest("POST", path, req, UserAccessResult.class, false);
            try {
                URL url = (new URI(serverUrl + path)).toURL();
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("POST");
                http.setDoOutput(true);

                //System.out.println("Writing body...");
                writeBody(req, http);
                //System.out.println("Connecting...");
                http.connect();
                //System.out.println("Checking success...");
                return readBody(http, UserAccessResult.class);
            } catch (Exception ex) {
                System.out.println("Exception: " + ex.getMessage());
                throw new Exception("Exception: " + ex.getMessage());
            }
        }
        catch(Exception e) {
            throw new Exception("Unable to complete command: \"register\"");
        }
    }

    public UserAccessResult facadeLogin(String[] bodyInfo) throws Exception {
        String path = "/session";
        try {
            LoginRequest req = new LoginRequest();
            req.setUsername(bodyInfo[0]);
            req.setPassword(bodyInfo[1]);
            //return this.makeRequest("POST", path, req, UserAccessResult.class, false);
            try {
                URL url = (new URI(serverUrl + path)).toURL();
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("POST");
                http.setDoOutput(true);

                //System.out.println("Writing body...");
                writeBody(req, http);
                //System.out.println("Connecting...");
                http.connect();
                //System.out.println("Checking success...");
                return readBody(http, UserAccessResult.class);
            } catch (Exception ex) {
                System.out.println("Exception: " + ex.getMessage());
                throw new Exception("Exception: " + ex.getMessage());
            }
        }
        catch(Exception e) {
            throw new Exception("Unable to complete command: \"login\"");
        }
    }

    public void facadeLogout(String auth) throws Exception {
        String path = "/session";
        try {
            AuthTokenOnlyRequest req = new AuthTokenOnlyRequest();
            //req.setAuthorization(auth);
            //this.makeRequest("DELETE", path, req, MessageResult.class, true);
            try {
                URL url = (new URI(serverUrl + path)).toURL();
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestProperty("Authorization", auth);
                http.setRequestMethod("DELETE");
                http.setDoOutput(true);

                //System.out.println("Writing body...");
                writeBody(req, http);
                //System.out.println("Connecting...");
                http.connect();
                //System.out.println("Checking success...");
                readBody(http, MessageResult.class);
            } catch (Exception ex) {
                System.out.println("Exception: " + ex.getMessage());
                throw new Exception("Exception: " + ex.getMessage());
            }
        }
        catch(Exception e) {
            throw new Exception("Unable to complete command: \"logout\"");
        }
    }

    public ListGamesResult[] facadeListGames(String auth) throws Exception {
        String path = "/game";
        try {
            AuthTokenOnlyRequest req = new AuthTokenOnlyRequest();
            //req.setAuthorization(auth);
            //return this.makeRequest("GET", path, req, ListGamesResult.class, true);
            try {
                URL url = (new URI(serverUrl + path)).toURL();
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                record listGameResponse(ListGamesResult[] games){

                }
                http.setRequestProperty("Authorization", auth);
                http.setRequestMethod("GET");
                http.setDoOutput(true);

                //System.out.println("Writing body...");
                //writeBody(req, http);
                http.connect();
                //String response = readBody(http, String.class);

                ListGamesResult[] response = new ListGamesResult[100];
                if (http.getContentLength() < 0) {
                    try (InputStream respBody = http.getInputStream()) {
                        InputStreamReader reader = new InputStreamReader(respBody);
                            Gson gson = new Gson();
                            response = gson.fromJson(reader, ListGamesResult[].class);
                        }
                    }

                Gson gson = new Gson();
                return response;
            } catch (Exception ex) {
                System.out.println("Exception: " + ex.getMessage());
                throw new Exception("Exception: " + ex.getMessage());
            }
        }
        catch(Exception e) {
            throw new Exception("Unable to complete command: \"list\"");
        }
    }

    public CreateGameResult facadeCreateGame(String bodyInfo, String auth) throws Exception {
        String path = "/game";
        try {
            CreateGameRequest req = new CreateGameRequest();
            //req.setAuthorization(auth);
            req.setGameName(bodyInfo);
            //return this.makeRequest("POST", path, req, CreateGameResult.class, true);
            try {
                URL url = (new URI(serverUrl + path)).toURL();
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestProperty("Authorization", auth);
                http.setRequestMethod("POST");
                http.setDoOutput(true);

                //System.out.println("Writing body...");
                writeBody(req, http);
                //System.out.println("Connecting...");
                http.connect();
                //System.out.println("Checking success...");
                return readBody(http, CreateGameResult.class);
            } catch (Exception ex) {
                System.out.println("Exception: " + ex.getMessage());
                throw new Exception("Exception: " + ex.getMessage());
            }
        }
        catch(Exception e) {
            throw new Exception("Unable to complete command: \"create\"");
        }
    }

    public JoinGameResult facadeJoinGame(String[] bodyInfo, String auth) throws Exception {
        String path = "/game";
        try {
            JoinGameRequest req = new JoinGameRequest();
            //req.setAuthorization(auth);
            req.setGameID(Integer.parseInt(bodyInfo[0]));
            if(bodyInfo.length > 1) req.setPlayerColor(bodyInfo[1]);
            else req.setPlayerColor("OBSERVER");
            //return this.makeRequest("PUT", path, req, JoinGameResult.class, true);
            try {
                URL url = (new URI(serverUrl + path)).toURL();
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestProperty("Authorization", auth);
                http.setRequestMethod("PUT");
                http.setDoOutput(true);

                //System.out.println("Writing body...");
                writeBody(req, http);
                //System.out.println("Connecting...");
                http.connect();
                //System.out.println("Checking success...");
                return readBody(http, JoinGameResult.class);
            } catch (Exception ex) {
                System.out.println("Exception: " + ex.getMessage());
                throw new Exception("Exception: " + ex.getMessage());
            }
        }
        catch(Exception e) {
            throw new Exception("Unable to complete command: \"join\"");
        }
    }

    public JoinGameResult facadeObserve(String bodyInfo, String auth) throws Exception {
        String path = "/game";
        try {
            JoinGameRequest req = new JoinGameRequest();
            req.setAuthorization(auth);
            req.setGameID(Integer.parseInt(bodyInfo));
            req.setPlayerColor("OBSERVER");
            //return this.makeRequest("PUT", path, req, JoinGameResult.class, true);
            try {
                URL url = (new URI(serverUrl + path)).toURL();
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestProperty("Authorization", auth);
                http.setRequestMethod("PUT");
                http.setDoOutput(true);

                //System.out.println("Writing body...");
                writeBody(req, http);
                //System.out.println("Connecting...");
                http.connect();
                //System.out.println("Checking success...");
                return readBody(http, JoinGameResult.class);
            } catch (Exception ex) {
                System.out.println("Exception: " + ex.getMessage());
                throw new Exception("Exception: " + ex.getMessage());
            }
        }
        catch(Exception e) {
            throw new Exception("Unable to complete command: \"observe\"");
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
                //System.out.println("wrote to body: " + reqBody.toString());
            }
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
