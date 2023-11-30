import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class ChessClient {

    private String currentUser = null;
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
                String regRes = server.facadeRegister(inputReq);
                state = State.LOGGEDIN;
                return "Logged in as " + regRes;
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
                String myUser = server.facadeLogin(inputReq);
                state = State.LOGGEDIN;
                currentUser = inputReq[0];
                return "Logged in as " + currentUser;
            }
            catch (Exception e) {
                return e.getMessage();
            }
        }
        return "Invalid input. \n Format: \"login <USERNAME> <PASSWORD>\"";
    }

    public String createGame(String inputReq) throws Exception {
        try {
            assertLoggedIn();
        }
        catch (Exception ex) {
            return ex.getMessage();
        }
        if (!inputReq.isEmpty()) {
            try {
                String gameID = server.facadeCreateGame(inputReq);
                return "Resources.Game " + inputReq + " created. \n Assigned ID: " + gameID;
            } catch (Exception e) {
                return e.getMessage();
            }
        }
        return "Invalid input. \n Format: \"create <NAME>\"";
    }

    public String listGames() throws Exception {
        try {
            assertLoggedIn();
        }
        catch (Exception ex) {
            return ex.getMessage();
        }
        try {
            return server.facadeListGames().replace(" ", "\n");
        }
        catch (Exception e) {
            return e.getMessage();
        }

    }

    public String logout() throws Exception {
        try {
            assertLoggedIn();
        }
        catch (Exception ex) {
            return ex.getMessage();
        }
        server.facadeLogout();
        currentUser = null;
        state = State.LOGGEDOUT;
        return "Logged out";

    }

    public String joinGame(String[] inputReq) throws Exception {
        try {
            assertLoggedIn();
        }
        catch (Exception ex) {
            return ex.getMessage();
        }
        if (inputReq.length >= 1) {
            try {
                String joinName = server.facadeJoinGame(inputReq);
                String joinMessage = "Joined game \'" + joinName + "\' as ";
                if(inputReq.length > 1) joinMessage += inputReq[1];
                else joinMessage += "observer";
                return joinMessage;
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
                String joinName = server.facadeObserve(inputReq);
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
