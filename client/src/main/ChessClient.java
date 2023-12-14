import Server.DataAccessing.GameData;
import Server.Results.CreateGameResult;
import Server.Results.JoinGameResult;
import Server.Results.ListGamesResult;
import Server.Results.UserAccessResult;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class ChessClient {

    private String currentUser = null;
    String myToken = null;
    private final ServerFacade server;
    private final String serverUrl;
    private State state = State.LOGGEDOUT;

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var command = (tokens.length > 0) ? tokens[0] : "help";
            var inputReq = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (command) {
                case "register" -> register(inputReq);
                case "login" -> logIn(inputReq);
                case "create" -> createGame(inputReq[0]);
                case "list" -> listGames();
                case "logout" -> logout();
                case "join" -> joinGame(inputReq);
                case "observe" -> observe(inputReq[0]);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public String register(String[] inputReq) {
        if(inputReq.length == 3) {
            try {
                UserAccessResult res = server.facadeRegister(inputReq);
                String userName = res.getUsername();
                myToken = res.getAuthToken();
                state = State.LOGGEDIN;
                return "Logged in as " + userName;
            }
            catch(Exception e) {
                return e.getMessage();
            }
        }
        return "Invalid input. \n Format: \"register <USERNAME> <PASSWORD> <EMAIL>\"";
    }

    public String logIn(String[] inputReq) {
        if (inputReq.length == 2) {
            try {
                UserAccessResult res = server.facadeLogin(inputReq);
                String userName = res.getUsername();
                myToken = res.getAuthToken();
                state = State.LOGGEDIN;
                return "Logged in as " + userName;
            }
            catch (Exception e) {
                return e.getMessage();
            }
        }
        return "Invalid input. \n Format: \"login <USERNAME> <PASSWORD>\"";
    }

    public String createGame(String inputReq) {
        try {
            assertLoggedIn();
        }
        catch (Exception ex) {
            return ex.getMessage();
        }
        if (!inputReq.isEmpty()) {
            try {
                int id = server.facadeCreateGame(inputReq, myToken).getGameID();
                return "Game " + inputReq + " created. \n Assigned ID: " + id;
            } catch (Exception e) {
                return e.getMessage();
            }
        }
        return "Invalid input. \n Format: \"create <NAME>\"";
    }

    public String listGames() {
        try {
            assertLoggedIn();
        }
        catch (Exception ex) {
            //System.out.println("catch on assertLoggedIn");
            return ex.getMessage();
        }
        try {
            Gson gson = new Gson();
            ListGamesResult[] allGames = server.facadeListGames(myToken);
            System.out.println("allGames length = " + allGames.length);
            for(int l = 0; l < allGames.length - 1; l++) {
                System.out.println(allGames[l].toString());
            }
            StringBuilder showGames = new StringBuilder();
            for(int i = 0; i < allGames.length - 1; i++) {
                showGames.append("(" + allGames[i].getGameID() + ") " + allGames[i].getGameName() + "\n");
                showGames.append("White Player: " + allGames[i].getWhiteUsername() + "\n");
                showGames.append("Black Player: " + allGames[i].getBlackUsername() + "\n\n");
            }
            System.out.println("cp 4");
            return showGames.toString();
        }
        catch (Exception e) {
            //System.out.println("catch on json");
            return e.getMessage();
        }

    }

    public String logout() {
        try {
            assertLoggedIn();
        }
        catch (Exception ex) {
            return ex.getMessage();
        }
        try {
            server.facadeLogout(myToken);
            currentUser = null;
            state = State.LOGGEDOUT;
            return "Logged out";
        }
        catch (Exception e) {
            return e.getMessage();
        }

    }

    public String joinGame(String[] inputReq) {
        try {
            assertLoggedIn();
        }
        catch (Exception ex) {
            return ex.getMessage();
        }
        if (inputReq.length >= 1) {
            try {
                JoinGameResult res = server.facadeJoinGame(inputReq, myToken);
                int joinId = res.getGameID();
                String joinColor;
                if(inputReq.length > 1) joinColor = inputReq[1];
                else joinColor = "OBSERVER";
                return "Joined game with ID \'" + joinId + "\' as " + joinColor.toUpperCase();
            }
            catch (Exception e) {
                return e.getMessage();
            }
        }
        return "Invalid input. \n Format: \"join <ID> [WHITE|BLACK|<empty>]\"";
    }

    public String observe(String inputReq) {
        try {
            assertLoggedIn();
        }
        catch (Exception ex) {
            return ex.getMessage();
        }
        if(!inputReq.isEmpty()) {
            try {
                JoinGameResult res = server.facadeObserve(inputReq, myToken);
                int joinName = res.getGameID();
                return "Joined game \'" + joinName + "\' as observer";
            } catch (Exception e) {
                return e.getMessage();
            }
        }
        return "Invalid input. \n Format: \"observe <ID>\"";
    }

    public String help() {
        if (state == State.LOGGEDOUT) {
            return """
                    register <USERNAME> <PASWORD> <EMAIL> - to create an account
                    login <USERNAME> <PASSWORD> to play chess
                    quit - playing chess
                    help - with possible commands
                    """;
        }
        return """
                create <NAME> - a game
                list - games
                join <ID> [WHITE|BLACK|<empty>] - a game
                observe <ID> - a game
                logout - when you are done
                quit - playing chess
                help - with possible commands
                """;
    }

    private void assertLoggedIn() throws Exception {
        if (state == State.LOGGEDOUT) {
            throw new Exception("Error, you must be logged in to use this command");
        }
    }

    public String getState() {
        return "[" + state.toString() + "]";
    }
}
