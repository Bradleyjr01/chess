package passoffTests.serverTests;

import Server.DataAccessing.*;
import Server.Server;
import Resources.Game;
import org.junit.jupiter.api.*;

public class DatabaseTests {


    private static UserDAO myUserDAO = new UserDAO(Server.MEMORY_DATA_ACCESS);
    private static GameDAO myGameDAO = new GameDAO(Server.MEMORY_DATA_ACCESS);
    private static AuthDAO myAuthDAO = new AuthDAO(Server.MEMORY_DATA_ACCESS);
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
        //Server.MEMORY_DATA_ACCESS.writeAuthToken(myToken);

        myGame.setGame(new Game());
        myGame.setGameName("game");
        myGame.setGameID(1);
        //Server.MEMORY_DATA_ACCESS.newGame(myGame);
    }

    @Test
    public void testClear(){
        myUserDAO.addUser(myUser);
        myUserDAO.clear();
        Assertions.assertTrue(myUserDAO.findAllUsers().isEmpty(), "found something");

        myGameDAO.addGame(myGame);
        myGameDAO.clear();
        Assertions.assertTrue(myGameDAO.findAllGames().isEmpty(), "found something");

        myAuthDAO.addAuth(myToken);
        myAuthDAO.clear();
        Assertions.assertTrue(myAuthDAO.findAllAuths().isEmpty(), "found something");
    }

    @Test
    public void testAddAuthGood(){
        myAuthDAO.addAuth(myToken);

        try {
            Assertions.assertEquals(myToken, myAuthDAO.findAuth(myToken),"didn't find it");
        }
        catch(DataAccessException d) {

        }
    }

    /*@Test
    public void testAddAuthBad(){
        AuthToken newToken = new AuthToken();
        newToken.setAuthToken("12345");
        myAuthDAO.addAuth(newToken);

        try {
            Assertions.assertThrowsExactly(DataAccessException,myAuthDAO.findAuth("12345"),"found something");
        }
        catch(DataAccessException d) {

        }
    }*/

    @Test
    public void testFindAuthGood(){
        myAuthDAO.addAuth(myToken);

        try {
            Assertions.assertEquals(myToken, myAuthDAO.findAuth(myToken),"didn't find it");
        }
        catch(DataAccessException d) {

        }
    }

    @Test
    public void testFindAuthBad(){
        myAuthDAO.clear();
        try {
            Assertions.assertNull(myAuthDAO.findAuth(myToken),"found something");
        }
        catch(DataAccessException d) {

        }
    }

    @Test
    public void testDeleteAuthGood(){
        AuthToken newToken = new AuthToken("12345","bob");
        myAuthDAO.addAuth(newToken);

        try {
            myAuthDAO.deleteAuth(newToken);
            Assertions.assertNull(myAuthDAO.findAuth("12345"),"found something");
        }
        catch(DataAccessException d) {

        }
    }

    @Test
    public void testDeleteAuthBad(){
        AuthToken newToken = new AuthToken("12345","bob");
        myAuthDAO.addAuth(newToken);

        try {
            myAuthDAO.deleteAuth(newToken);
            Assertions.assertNull(myAuthDAO.findAuth("12345"),"didn't find it");
        }
        catch(DataAccessException d) {

        }
    }

    @Test
    public void testAddGameGood(){
        myGameDAO.addGame(myGame);

        try {
            Assertions.assertEquals(myGame, myGameDAO.findGame(myGame),"didn't find it");
        }
        catch(DataAccessException d) {

        }
    }


    @Test
    public void testAddGameBad(){
        myGameDAO.addGame(myGame);
        myGameDAO.clear();
        try {
            Assertions.assertNull(myGameDAO.findGame(myGame),"found something");
        }
        catch(DataAccessException d) {

        }
    }

    @Test
    public void testFindGameGood(){
        myGameDAO.addGame(myGame);

        try {
            Assertions.assertEquals(myGame, myGameDAO.findGame(myGame),"didn't find it");
        }
        catch(DataAccessException d) {

        }
    }

    @Test
    public void testFindGameBad(){
        myGameDAO.clear();
        try {
            Assertions.assertNull(myGameDAO.findGame(myGame),"found something");
        }
        catch(DataAccessException d) {

        }
    }

    @Test
    public void testAddPlayerGood(){
        myGameDAO.addGame(myGame);
        try {
            myGameDAO.addPlayer(myUser.getUsername(), "white", myGame.getGameID());
            //System.out.println("game id & name:" + myGame.getGameID() + "/" + myGame.getGameName());
            //System.out.println("game w & b:" + myGameDAO.findGame(myGame.getGameID()).getWhiteUserName() + "/" + myGameDAO.findGame(myGame.getGameID()).getBlackUserName());
            Assertions.assertNotNull(myGameDAO.findGame(myGame.getGameID()).getWhiteUserName(), "found " + myGame.getWhiteUserName());
            Assertions.assertEquals(myUser.getUsername(), myGameDAO.findGame(myGame.getGameID()).getWhiteUserName(), "wrong or no user");
        }
        catch(DataAccessException e) {

        }
    }

    @Test
    public void testAddPlayerBad(){
        myGameDAO.addGame(myGame);
        try {
            myGameDAO.addPlayer("you", "WHITE", 1);
            myGameDAO.addPlayer(myUser.getUsername(), "WHITE", myGame.getGameID());
            Assertions.assertNotEquals(myUser.getUsername(), myGame.getWhiteUserName(), "wrong user");
        }
        catch(DataAccessException e) {

        }
    }

    @Test
    public void testUpdateGameGood() {
        GameData newGameData = new GameData();
        newGameData.setGameName("game");
        newGameData.setGameID(1);
        Game startNew = new Game();
        startNew.getBoard().resetBoard();
        newGameData.setGame(startNew);
        try {
            myGameDAO.updateGame(myGame, newGameData);
            Assertions.assertEquals(startNew, myGame.getGame(), "game not updated");
        }
        catch(DataAccessException e) {

        }
    }

    @Test
    public void testAddUserGood(){
        myUserDAO.addUser(myUser);

        try {
            Assertions.assertEquals(myUser, myUserDAO.findUser(myUser),"found other");
        }
        catch(DataAccessException d) {

        }
    }

    @Test
    public void testFindUserGood(){
        myUserDAO.addUser(myUser);

        try {
            Assertions.assertEquals(myUser, myUserDAO.findUser(myUser),"didn't find it");
        }
        catch(DataAccessException d) {

        }
    }

    @Test
    public void testFindUserBad(){
        myUserDAO.clear();

        try {
            Assertions.assertNull(myUserDAO.findUser(myUser), "found something");
        }
        catch(DataAccessException d) {

        }
    }

}
