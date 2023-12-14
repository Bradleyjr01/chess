package passoffTests;
import Server.DataAccessing.*;
import Server.Services.*;
import Server.Results.*;
import Server.Requests.*;
import org.junit.jupiter.api.*;
import Server.Server;

public class ServiceTests {

    private static UserData myUser = new UserData();
    private static GameData myGame = new GameData();
    private AuthToken myToken = new AuthToken();

    @BeforeEach
    public void setup() {
        myUser.setUsername("me");
        myUser.setPassword("password");
        myUser.setEmail("memail@email.com");

        myToken.setAuthToken("token");
        myToken.setUserID("me");
        Server.MEMORY_DATA_ACCESS.writeAuthToken(myToken);

        //myGame.setGame(new Resources.Game());
        myGame.setGameName("game");
        myGame.setGameID(1);
        Server.MEMORY_DATA_ACCESS.newGame(myGame);
    }

    @Test
    public void testClear() {
        ClearService myClear = new ClearService();

        //good request
        HTTPRequest goodRequest = new HTTPRequest();

        MessageResult clearResult = myClear.clear(goodRequest);
        Assertions.assertNull(clearResult.getMessage(), "An error occured");
    }

    @Test
    public void testRegisterGood() {
        RegisterService myRegister = new RegisterService();

        //good request
        RegisterRequest goodRequest = new RegisterRequest();
        goodRequest.setUsername("me2");
        goodRequest.setPassword("p@$$w0rd");
        goodRequest.setEmail("2mail@email.com");

        UserAccessResult registerResult = myRegister.register(goodRequest);
        Assertions.assertEquals(registerResult.getMessage(), "", "An error occured");
        Assertions.assertEquals(registerResult.getUsername(), goodRequest.getUsername(), "Username is not the same");
        Assertions.assertNotNull(registerResult.getAuthToken(), "No authToken returned");
    }

    @Test
    public void testRegisterBad() {
    RegisterService myRegister = new RegisterService();
    //bad requests
    RegisterRequest badRequest = new RegisterRequest();
    UserAccessResult registerResult = myRegister.register(badRequest);
    Assertions.assertNotNull(registerResult.getMessage(), "No error returned with no parameters");

    badRequest.setUsername(myUser.getUsername());
    registerResult = myRegister.register(badRequest);
    Assertions.assertNotNull(registerResult.getMessage(), "No error returned with missing parameters");

    badRequest.setPassword(myUser.getPassword());
    registerResult = myRegister.register(badRequest);
    Assertions.assertNotNull(registerResult.getMessage(), "No error returned with missing parameters");
    }

    @Test
    public void testLoginGood() {
        LoginService myLogin = new LoginService();
        myUser.setUsername("me");
        myUser.setPassword("password");
        myUser.setEmail("memail@email.com");
        Server.MEMORY_DATA_ACCESS.writeUser(myUser);

        //good request
        LoginRequest goodRequest = new LoginRequest();
        goodRequest.setUsername(myUser.getUsername());
        goodRequest.setPassword(myUser.getPassword());

        UserAccessResult loginResult = new UserAccessResult();

        loginResult = myLogin.login(goodRequest);
        Assertions.assertEquals(myUser.getUsername(), loginResult.getUsername(), "wrong username");
        Assertions.assertNotNull(loginResult.getAuthToken(), "No authToken returned");
    }

    @Test
    public void testLoginBad() {
        LoginService myLogin = new LoginService();

        //bad requests
        LoginRequest badRequest = new LoginRequest();
        UserAccessResult loginResult = myLogin.login(badRequest);
        Assertions.assertNotNull(loginResult.getMessage(), "No error returned with no parameters");
    }

    @Test
    public void testLogoutGood() {
        AuthToken token = new AuthToken(myToken.getAuthToken(), myUser.getUsername());

        LogoutService myLogout = new LogoutService();

        //good request
        AuthTokenOnlyRequest goodRequest = new AuthTokenOnlyRequest();
        goodRequest.setAuthorization(token.getAuthToken());

        MessageResult logoutResult = new MessageResult();

        logoutResult = myLogout.logout(goodRequest);
        Assertions.assertNull(logoutResult.getMessage(), "An error occured");
    }

    @Test
    public void testLogoutBad() {
        AuthToken token = new AuthToken(myToken.getAuthToken(), myUser.getUsername());
        LogoutService myLogout = new LogoutService();
        MessageResult logoutResult = new MessageResult();

        //bad requests
        AuthTokenOnlyRequest badRequest = new AuthTokenOnlyRequest();
        logoutResult = myLogout.logout(badRequest);
        Assertions.assertNotNull(logoutResult.getMessage(), "No error returned with no token");
    }

    @Test
    public void testListGamesGood() {
        AuthToken token = new AuthToken(myToken.getAuthToken(), myUser.getUsername());
        ListGamesService myList = new ListGamesService();

        //good request
        AuthTokenOnlyRequest goodRequest = new AuthTokenOnlyRequest();
        goodRequest.setAuthorization(token.getAuthToken());

        ListGamesResult[] listResult = new ListGamesResult[10];

        listResult = myList.listGames(goodRequest);
        Assertions.assertNull(listResult[0].getMessage(),"An error occured");
        Assertions.assertNotNull(listResult[0].getGames(), "returned no games");
    }

    @Test
    public void testListGamesBad() {
        AuthToken token = new AuthToken(myToken.getAuthToken(), myUser.getUsername());
        ListGamesService myList = new ListGamesService();

        ListGamesResult[] listResult = new ListGamesResult[10];

        //bad requests
        AuthTokenOnlyRequest badRequest = new AuthTokenOnlyRequest();
        listResult = myList.listGames(badRequest);
        Assertions.assertNotNull(listResult[0].getMessage(), "No error returned with no token");
    }

    @Test
    public void testCreateGameGood() {
        AuthToken token = new AuthToken(myToken.getAuthToken(), myUser.getUsername());
        CreateGameService myCreate = new CreateGameService();

        //good request
        CreateGameRequest goodRequest = new CreateGameRequest();
        goodRequest.setAuthorization(token.getAuthToken());
        goodRequest.setGameName("gamey");

        CreateGameResult gameResult = new CreateGameResult();

        gameResult = myCreate.CreateGame(goodRequest);
        Assertions.assertNull(gameResult.getMessage(),"An error occured");
        Assertions.assertNotEquals(gameResult.getGameID(), "returned no id");
    }

    @Test
    public void testCreateGameBad() {
        AuthToken token = new AuthToken(myToken.getAuthToken(), myUser.getUsername());
        CreateGameService myCreate = new CreateGameService();

        CreateGameResult gameResult = new CreateGameResult();

        //bad requests
        CreateGameRequest badRequest = new CreateGameRequest();
        gameResult = myCreate.CreateGame(badRequest);
        Assertions.assertNotNull(gameResult.getMessage(), "No error returned with no token");
    }

    @Test
    public void testJoinGameGood() {
        AuthToken token = new AuthToken(myToken.getAuthToken(), myUser.getUsername());
        JoinGameService myJoin = new JoinGameService();

        //good request
        JoinGameRequest goodRequest = new JoinGameRequest();
        goodRequest.setAuthorization(token.getAuthToken());
        goodRequest.setPlayerColor("WHITE");
        goodRequest.setGameID(1);

        JoinGameResult gameResult = new JoinGameResult();

        gameResult = myJoin.joinGame(goodRequest);
        Assertions.assertNull(gameResult.getMessage(),"An error occured");
        //check that object has changed in database
    }

    @Test
    public void testJoinGameBad() {
        AuthToken token = new AuthToken(myToken.getAuthToken(), myUser.getUsername());
        JoinGameService myJoin = new JoinGameService();

        JoinGameResult gameResult = new JoinGameResult();

        //bad requests
        JoinGameRequest badRequest = new JoinGameRequest();
        gameResult = myJoin.joinGame(badRequest);
        Assertions.assertNotNull(gameResult.getMessage(), "No error returned with no token");
    }
}
