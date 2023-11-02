package Server.DataAccessing;

import chess.ChessGame;
import chess.Game;

import java.util.ArrayList;
import java.util.Objects;

public class GameData {

    ChessGame game;
    int gameID;
    String whiteUserName = null;
    String blackUserName = null;
    String gameName = null;
    ArrayList<String> observers = new ArrayList<>();

    public GameData(){}

    public GameData(GameData g) {
        game = g.getGame();
        gameID = g.getGameID();
        whiteUserName = g.getWhiteUserName();
        blackUserName = g.getBlackUserName();
        gameName = g.getGameName();
        observers = g.getObservers();
    }

    public GameData(String myName) {
        gameName = myName;
        gameID = GameDAO.createGameID();
        whiteUserName = null;
        blackUserName = null;
        game = new Game();
        game.getBoard().resetBoard();
    }

    public ChessGame getGame() {
        return game;
    }

    public void setGame(ChessGame game) {
        this.game = game;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public String getWhiteUserName() {
        return whiteUserName;
    }

    public void setWhiteUserName(String whiteUserName) {
        this.whiteUserName = whiteUserName;
    }

    public String getBlackUserName() {
        return blackUserName;
    }

    public void setBlackUserName(String blackUserName) {
        this.blackUserName = blackUserName;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public ArrayList<String> getObservers() {
        return observers;
    }

    public void setObservers(ArrayList<String> observers) {
        this.observers = observers;
    }

    public void addObserver(String observer) {
        this.observers.add(observer);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameData gameData = (GameData) o;
        return Objects.equals(game, gameData.game) && Objects.equals(gameID, gameData.gameID) && Objects.equals(whiteUserName, gameData.whiteUserName) && Objects.equals(blackUserName, gameData.blackUserName) && Objects.equals(gameName, gameData.gameName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(game, gameID, whiteUserName, blackUserName, gameName);
    }
}
