import Resources.Game;
import Server.DataAccessing.*;
import Server.Results.*;
import Server.Server;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ServerFacadeTests {
    private static UserDAO myUserDAO = new UserDAO(Server.MEMORY_DATA_ACCESS);
    private static GameDAO myGameDAO = new GameDAO(Server.MEMORY_DATA_ACCESS);
    private static AuthDAO myAuthDAO = new AuthDAO(Server.MEMORY_DATA_ACCESS);
    private static UserData myUser = new UserData();
    private static GameData myGame = new GameData();
    private AuthToken myToken = new AuthToken();
    private ServerFacade server;
    @BeforeEach
    public void setup() {
        myUser.setUsername("me");
        myUser.setPassword("password");
        myUser.setEmail("memail@email.com");

        myToken.setAuthToken("token");
        myToken.setUserID("me");
        myAuthDAO.addAuth(myToken);

        myGame.setGame(new Game());
        myGame.setGameName("game");
        myGame.setGameID(1);
        myGameDAO.addGame(myGame);
    }

    @Test
    public void facadeRegisterTestGood() {
        String[] registerTest = new String[3];
        registerTest[0] = myUser.getUsername();
        registerTest[1] = myUser.getPassword();
        registerTest[2] = myUser.getEmail();

        try {
            UserAccessResult res = server.facadeRegister(registerTest);
            Assertions.assertNotNull(res.getAuthToken(), "No Auth Token");
            Assertions.assertNull(res.getMessage(), "Something went wrong");
            Assertions.assertEquals(res.getUsername(), myUser.getUsername(), "wrong user");
        }
        catch (Exception e) {
            String s = "Failed";
        }
    }

    @Test
    public void facadeRegisterTestBad() {
        String[] registerTest = new String[3];
        registerTest[0] = myUser.getUsername();
        registerTest[1] = myUser.getPassword();

        try {
            UserAccessResult res = server.facadeRegister(registerTest);
            Assertions.assertNull(res.getAuthToken());

        }
        catch (Exception e) {
            String s = "Success";
        }
    }

    @Test
    public void facadeLoginTestGood() {
        myUserDAO.addUser(myUser);
        String[] loginTest = new String[2];
        loginTest[0] = myUser.getUsername();
        loginTest[1] = myUser.getPassword();

        try {
            UserAccessResult res = server.facadeLogin(loginTest);
            Assertions.assertNotNull(res.getAuthToken(), "No token");
            Assertions.assertNull(res.getMessage(), "An error occured");
        } catch (Exception e) {
            String s = "Failed";
        }
    }

    @Test
    public void facadeLoginTestBad() {
        String[] loginTest = new String[2];
        loginTest[0] = myUser.getUsername();
        loginTest[1] = "badpassword";

        try {
            UserAccessResult res = server.facadeLogin(loginTest);
            Assertions.assertNull(res.getAuthToken(), "Token returned");
            Assertions.assertNotNull(res.getMessage(), "No error");
        } catch (Exception e) {
            String s = "Success";
        }
    }
    @Test
    public void facadeLogoutTestGood() {
        String auth = myToken.getAuthToken();

        try {
            server.facadeLogout(auth);
            Assertions.assertNull(myAuthDAO.findAuth(auth));
        } catch (Exception e){
            String s = "Failed";
        }
    }

    @Test
    public void facadeLogoutTestBad() {
        String auth = "bad auth token";

        try {
            server.facadeLogout(auth);
            Assertions.assertNull(myAuthDAO.findAuth(auth));
        } catch (Exception e){
            String s = "Success";
        }
    }

    @Test
    public void facadeListTestGood() {
        myGameDAO.addGame(myGame);
        String auth = myToken.getAuthToken();

        try {
            ListGamesResult[] res = server.facadeListGames(auth);
            Assertions.assertNotEquals(res.length, 0,"No games");
        } catch (Exception e) {
            String s = "Failed";
        }
    }

    @Test
    public void facadeListTestBad() {
        myGameDAO.addGame(myGame);
        String auth = "badAuth";

        try {
            ListGamesResult[] res = server.facadeListGames(auth);
            Assertions.assertEquals(res.length, 0,"Games were found");
        } catch (Exception e) {
            String s = "Success";
        }
    }

    @Test
    public void facadeCreateTestGood() {
        String name = "newGame2";
        String auth = myToken.getAuthToken();

        try {
            CreateGameResult res = server.facadeCreateGame(name, auth);
            Assertions.assertNotEquals(res.getGameID(), -1, "no ID");
            Assertions.assertNull(res.getMessage(), "something stared back");

        } catch (Exception e) {
            String s = "Failed";
        }

    }

    @Test
    public void facadeCreateTestBad() {
        String name = "newGame2";
        String auth = "badToken";

        try {
            CreateGameResult res = server.facadeCreateGame(name, auth);
            Assertions.assertNotNull(res.getMessage(), "something was made");

        } catch (Exception e) {
            String s = "Success";
        }
    }

    @Test
    public void facadeJoinTestGood() {
        int id = myGame.getGameID();
        String team = "WHITE";
        String auth = myToken.getAuthToken();

        String[] reqs = new String[2];
        reqs[0] = "" + id;
        reqs[1] = team;

        try {
            JoinGameResult res = server.facadeJoinGame(reqs, auth);
            Assertions.assertNotEquals(res.getGameID(), -1, "Not joined");
            Assertions.assertNull(res.getMessage(), "*explosion noises*");
            Assertions.assertNotNull(res.getJoined(), "we've lost the game");

        } catch (Exception e) {
            String s = "Failed";
        }

    }

    @Test
    public void facadeJoinTestBad() {
        myGame.setWhiteUserName("not_you");
        int id = myGame.getGameID();
        String team = "WHITE";
        String auth = myToken.getAuthToken();

        String[] reqs = new String[2];
        reqs[0] = "" + id;
        reqs[1] = team;

        try {
            JoinGameResult res = server.facadeJoinGame(reqs, auth);
            Assertions.assertNotNull(res.getMessage(), "everything went well which is bad");
            Assertions.assertNull(res.getJoined(), "we weren't supposed to have mail");

        } catch (Exception e) {
            String s = "Success";
        }
    }

    @Test
    public void facadeObserveTestGood() {
        String id = "" + myGame.getGameID();
        String auth = myToken.getAuthToken();


        try {
            JoinGameResult res = server.facadeObserve(id, auth);
            Assertions.assertNotEquals(res.getGameID(), -1, "Not joined");
            Assertions.assertNull(res.getMessage(), "*explosion noises*");
            Assertions.assertNotNull(res.getJoined(), "we've lost the game");

        } catch (Exception e) {
            String s = "Failed";
        }
    }

    @Test
    public void facadeObserveTestBad() {
        String id = "" + myGame.getGameID();
        String auth = "worstAuth";

        try {
            JoinGameResult res = server.facadeObserve(id, auth);
            Assertions.assertNotNull(res.getMessage(), "everything went well which is bad");
            Assertions.assertNull(res.getJoined(), "we weren't supposed to have mail");

        } catch (Exception e) {
            String s = "Success";
        }
    }
}
