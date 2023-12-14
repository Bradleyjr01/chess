import Resources.Board;
import Resources.Move;
import Resources.Piece;
import Resources.Position;
import Server.DataAccessing.GameData;
import Server.Results.CreateGameResult;
import Server.Results.JoinGameResult;
import Server.Results.ListGamesResult;
import Server.Results.UserAccessResult;
import StartCode.*;
import WebSocket.MessageHandler;
import WebSocket.WebSocketFacade;
import com.google.gson.Gson;
import ui.EscapeSequences;
import webSocketMessages.userCommands.ResignCommand;
import webSocketMessages.userCommands.UserGameCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

public class ChessClient {

    private String currentUser = null;
    private int currentGameID = -1;
    private String currentPOV = null;

    private GameData currentGame = null;
    String myToken = null;
    private final ServerFacade server;
    private final MessageHandler notificationHandler;
    private  WebSocketFacade ws;
    private final String serverUrl;

    private State state = State.LOGGEDOUT;

    public ChessClient(String serverUrl, MessageHandler notificationHandler) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.notificationHandler = notificationHandler;
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
                case "move" -> move(inputReq);
                case "leave" -> leave();
                case "resign" -> resign();
                case "redraw" -> redraw();
                case "pov" -> pov(inputReq[0]);
                case "highlight" -> highlight(inputReq);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public String register(String[] inputReq) {
        if (inputReq.length == 3) {
            try {
                UserAccessResult res = server.facadeRegister(inputReq);
                currentUser = res.getUsername();
                myToken = res.getAuthToken();
                state = State.LOGGEDIN;
                return "Logged in as " + currentUser;
            } catch (Exception e) {
                return e.getMessage();
            }
        }
        return "Invalid input. \n Format: \"register <USERNAME> <PASSWORD> <EMAIL>\"";
    }

    public String logIn(String[] inputReq) {
        if (inputReq.length == 2) {
            try {
                UserAccessResult res = server.facadeLogin(inputReq);
                currentUser = res.getUsername();
                myToken = res.getAuthToken();
                state = State.LOGGEDIN;
                return "Logged in as " + currentUser;
            } catch (Exception e) {
                return e.getMessage();
            }
        }
        return "Invalid input. \n Format: \"login <USERNAME> <PASSWORD>\"";
    }

    public String createGame(String inputReq) {
        try {
            assertLoggedIn();
        } catch (Exception ex) {
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
        } catch (Exception ex) {
            //System.out.println("catch on assertLoggedIn");
            return ex.getMessage();
        }
        try {
            Gson gson = new Gson();
            ListGamesResult[] allGames = server.facadeListGames(myToken);
            System.out.println("allGames length = " + allGames.length);
            for (int l = 0; l < allGames.length - 1; l++) {
                System.out.println(allGames[l].toString());
            }
            StringBuilder showGames = new StringBuilder();
            for (int i = 0; i < allGames.length - 1; i++) {
                showGames.append("(" + allGames[i].getGameID() + ") " + allGames[i].getGameName() + "\n");
                showGames.append("White Player: " + allGames[i].getWhiteUsername() + "\n");
                showGames.append("Black Player: " + allGames[i].getBlackUsername() + "\n\n");
            }
            System.out.println("cp 4");
            return showGames.toString();
        } catch (Exception e) {
            //System.out.println("catch on json");
            return e.getMessage();
        }

    }

    public String logout() {
        try {
            assertLoggedIn();
        } catch (Exception ex) {
            return ex.getMessage();
        }
        try {
            server.facadeLogout(myToken);
            currentUser = null;
            state = State.LOGGEDOUT;
            return "Logged out";
        } catch (Exception e) {
            return e.getMessage();
        }

    }

    public String joinGame(String[] inputReq) {
        try {
            assertLoggedIn();
        } catch (Exception ex) {
            return ex.getMessage();
        }
        if (inputReq.length >= 1) {
            try {
                JoinGameResult res = server.facadeJoinGame(inputReq, myToken);
                int joinId = res.getGameID();
                String joinColor;
                ws = new WebSocketFacade(serverUrl, notificationHandler);
                if (inputReq.length > 1) {
                    joinColor = inputReq[1];
                    state = State.PLAYING;
                    ChessGame.TeamColor color;
                    if(joinColor.equalsIgnoreCase("WHITE")) {
                        currentPOV = "WHITE";
                        color = ChessGame.TeamColor.WHITE;
                    }
                    else {
                        currentPOV = "BLACK";
                        color = ChessGame.TeamColor.BLACK;
                    }
                    ws.joinGame(myToken, currentUser, color, currentGameID);
                }
                else {
                    joinColor = "OBSERVER";
                    state = State.INGAME;
                    currentPOV = "WHITE";
                    ws.observeGame(myToken, currentUser, currentGameID);
                }
                currentGame = res.getJoined();
                currentGameID = joinId;
                //displayBoard(currentGame.getGame(), currentPOV);
                return "Joined game with ID '" + joinId + "' as " + joinColor.toUpperCase();
            } catch (Exception e) {
                return e.getMessage();
            }
        }
        return "Invalid input. \n Format: \"join <ID> [WHITE|BLACK|<empty>]\"";
    }

    public String observe(String inputReq) {
        try {
            assertLoggedIn();
        } catch (Exception ex) {
            return ex.getMessage();
        }
        if (!inputReq.isEmpty()) {
            try {
                ws = new WebSocketFacade(serverUrl, notificationHandler);
                JoinGameResult res = server.facadeObserve(inputReq, myToken);
                int joinId = res.getGameID();
                state = State.INGAME;
                currentGame = res.getJoined();
                currentPOV = "WHITE";
                ws.observeGame(myToken, currentUser, currentGameID);
                //displayBoard(currentGame.getGame(), currentPOV);
                return "Joined game with ID '" + joinId + "' as OBSERVER";
            } catch (Exception e) {
                return e.getMessage();
            }
        }
        return "Invalid input. \n Format: \"observe <ID>\"";
    }

    public String help() {
        if (state == State.LOGGEDOUT) {
            return """
                    register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                    login <USERNAME> <PASSWORD> to play chess
                    quit - playing chess
                    help - with possible commands
                    """;
        }
        else if(state == State.PLAYING) {
            return """
                    move <FROM_ROW> <FROM_COL> <TO_ROW> <TO_COL> [QUEEN|BISHOP|KNIGHT|ROOK|empty] - move the
                                     piece at FROM_ROW FROM_COL to TO_ROW TO_COL. Requires input for a
                                     promotion piece if pawn can promote
                    leave - return to lobby
                    resign - concede current match
                    help - with possible commands
                    redraw - redraw the current game's board
                    highlight <ROW> <COL> show all legal moves for the piece at the position
                                    designated by ROW COL
                    """;
        }
        else if(state == State.INGAME) {
            return """
                    leave - return to lobby
                    help - with possible commands
                    redraw - redraw the current game's board using white.
                    pov [BLACK|WHITE] - redraw the current game's board using the selected
                                    point of view
                    highlight <ROW> <COL> show all legal moves for the piece at the position
                                    designated by ROW COL
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

    public String move(String[] inputReq) {
        try {
            assertPlaying();
        } catch (Exception ex) {
            return ex.getMessage();
        }
        if (inputReq.length > 3) {
            try {
                ws = new WebSocketFacade(serverUrl, notificationHandler);

                int fromRow = Integer.parseInt(inputReq[0]);
                int fromCol = (inputReq[1].charAt(0) - 'a');
                Position from = new Position(fromRow, fromCol);

                int toRow = Integer.parseInt(inputReq[2]);
                int toCol = (inputReq[3].charAt(0) - 'a');
                Position to = new Position(toRow, toCol);

                ChessPiece.PieceType promote = null;
                if(inputReq.length > 4) {
                    if(inputReq[4].equalsIgnoreCase("QUEEN")) {
                        promote = ChessPiece.PieceType.QUEEN;
                    }
                    else if(inputReq[4].equalsIgnoreCase("BISHOP")) {
                        promote = ChessPiece.PieceType.BISHOP;
                    }
                    else if(inputReq[4].equalsIgnoreCase("KNIGHT")) {
                        promote = ChessPiece.PieceType.KNIGHT;
                    }
                    else if(inputReq[4].equalsIgnoreCase("ROOK")) {
                        promote = ChessPiece.PieceType.ROOK;
                    }
                    else return "Invalid promotion piece. \n Format: \"move <FROM_ROW> <FROM_COL> <TO_ROW> <TO_COL> [QUEEN|BISHOP|KNIGHT|ROOK|empty]\"";
                }
                Move myMove = new Move(from, to, promote);

                ws.makeMove(myToken, currentUser, myMove, currentGameID);
                return null;
            } catch (Exception e) {
                return e.getMessage();
            }
        }
        return "Invalid input. \n Format: \"move <FROM_ROW> <FROM_COL> <TO_ROW> <TO_COL> [QUEEN|BISHOP|KNIGHT|ROOK|empty]\"";
    }

    public String leave() {
        try {
            assertInGame();
        } catch (Exception ex) {
            return ex.getMessage();
        }
        try {
            ws = new WebSocketFacade(serverUrl, notificationHandler);
            ws.leaveGame(myToken, currentUser, currentGameID);
        }
        catch (Exception e) {
            return e.getMessage();
        }
        state = State.LOGGEDIN;
        return "Returning to lobby";
    }

    public String resign() {
        try {
            assertPlaying();
        } catch (Exception ex) {
            return ex.getMessage();
        }
        try {
            ws = new WebSocketFacade(serverUrl, notificationHandler);
            ws.resignGame(myToken, currentUser, currentGameID);
            state = State.INGAME;
            return "Resigned";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String redraw(){
        try {
            assertInGame();
        } catch (Exception ex) {
            return ex.getMessage();
        }
        displayBoard(currentGame.getGame(), currentPOV);
        return "";
    }

    public String pov(String inputReq){
        try {
            assertInGame();
        } catch (Exception ex) {
            return ex.getMessage();
        }
        if (!inputReq.isEmpty()) {
            if(inputReq.equalsIgnoreCase("WHITE")) {
                currentPOV = "WHITE";
            }
            else if(inputReq.equalsIgnoreCase("BLACK")) {
                currentPOV = "BLACK";
            }
            else {
                return "Invalid input. \n Format: \"pov [BLACK|WHITE]\"";
            }
            displayBoard(currentGame.getGame(), currentPOV);
            return "";
        }
        return "Invalid input. \n Format: \"pov [BLACK|WHITE]\"";
    }

    public String highlight(String[] inputReq){
        try {
            assertInGame();
        } catch (Exception ex) {
            return ex.getMessage();
        }
        if(inputReq.length == 2) {

            int pieceRow = Integer.parseInt(inputReq[0]);
            int pieceCol = (inputReq[1].charAt(0) - 'a');
            System.out.println("Highlighting moves for " + pieceRow + ":" + pieceCol);
            displayHighlightBoard(currentGame, currentPOV, pieceRow, pieceCol);
            return "";
        }
        else {
            return "Invalid input. \n Format: \"highlight <ROW> <COL>\"";
        }
    }

    private void assertLoggedIn() throws Exception {
        if (state == State.LOGGEDOUT) {
            throw new Exception("Error, you must be logged in to use this command");
        }
    }

    private void assertInGame() throws Exception {
        assertLoggedIn();
        if (state == State.LOGGEDIN) {
            throw new Exception("Error, you must be in a game to use this command");
        }
    }

    private void assertPlaying() throws Exception {
        assertInGame();
        if (state == State.INGAME) {
            throw new Exception("Error, you must be playing in this game to use this command");
        }
    }

    public String getState() {
        return "[" + state.toString() + "]";
    }

    public static void displayBoard(ChessGame game, String pov) {
        System.out.println(EscapeSequences.SET_BG_COLOR_DARK_GREY);
        String[][] myBoard = generateBoard(game.getBoard(), pov);
        for(int row = 0; row < 10; row++) {
            for(int col = 0; col < 10; col++) {
                System.out.print(myBoard[row][col]);
            }
            System.out.println(EscapeSequences.SET_BG_COLOR_DARK_GREY + EscapeSequences.SET_TEXT_COLOR_WHITE);
        }
    }

    public static String[][] generateBoard(ChessBoard myBoard, String pov) {
        String[][] genBoard = new String[10][10];
        if (pov.equalsIgnoreCase("BLACK")) {
            for (int row = 0; row < 10; row++) {
                for (int col = 0; col < 10; col++) {
                    if (row == 0 || row == 9) {
                        if (col > 0 && col < 9) {
                            if(col % 2 == 1) genBoard[row][col] = (EscapeSequences.SET_BG_COLOR_DARK_GREY + "\u200A" + EscapeSequences.PARTIAL_SPACE + (char)('a' + col - 1) + EscapeSequences.PARTIAL_SPACE);
                            else genBoard[row][col] = (EscapeSequences.SET_BG_COLOR_DARK_GREY + EscapeSequences.PARTIAL_SPACE + (char)('a' + col - 1) + EscapeSequences.PARTIAL_SPACE);
                        }
                        else {
                            genBoard[row][col] = (EscapeSequences.SET_BG_COLOR_DARK_GREY + "   ");
                        }
                    }
                    else if (row % 2 != 0) {
                        if(col == 0 || col == 9) {
                            genBoard[row][col] = (EscapeSequences.SET_BG_COLOR_DARK_GREY + " " + row + " ");
                        }
                        else if(col % 2 != 0) {
                            genBoard[row][col] = (EscapeSequences.SET_BG_COLOR_LIGHT_GREY + " " + displayPiece(myBoard.getPiece(new Position(row, col))) + " ");
                        }
                        else {
                            genBoard[row][col] = (EscapeSequences.SET_BG_COLOR_DARK_GREY + " " + displayPiece(myBoard.getPiece(new Position(row, col))) + " ");
                        }
                    }
                    else {
                        if(col == 0 || col == 9) {
                            genBoard[row][col] = (EscapeSequences.SET_BG_COLOR_DARK_GREY + " " + row + " ");
                        }
                        else if(col % 2 != 0) {
                            genBoard[row][col] = (EscapeSequences.SET_BG_COLOR_DARK_GREY + " " + displayPiece(myBoard.getPiece(new Position(row, col))) + " ");
                        }
                        else {
                            genBoard[row][col] = (EscapeSequences.SET_BG_COLOR_LIGHT_GREY + " " + displayPiece(myBoard.getPiece(new Position(row, col))) + " ");
                        }
                    }
                }
            }
        }
        else {
            for (int row = 0; row < 10; row++) {
                for (int col = 0; col < 10; col++) {
                    if (row == 0 || row == 9) {
                        if (col > 0 && col < 9) {
                            if(col % 2 == 1) genBoard[9 - row][9 - col] = (EscapeSequences.SET_BG_COLOR_DARK_GREY + "\u200A" + EscapeSequences.PARTIAL_SPACE + (char)('a' + col - 1) + EscapeSequences.PARTIAL_SPACE);
                            else genBoard[9 - row][9 - col] = (EscapeSequences.SET_BG_COLOR_DARK_GREY + EscapeSequences.PARTIAL_SPACE + (char)('a' + col - 1) + EscapeSequences.PARTIAL_SPACE);
                        }
                        else {
                            genBoard[9 - row][9 - col] = (EscapeSequences.SET_BG_COLOR_DARK_GREY + "   ");
                        }
                    }
                    else if (row % 2 != 0) {
                        if(col == 0 || col == 9) {
                            genBoard[9 - row][9 - col] = (EscapeSequences.SET_BG_COLOR_DARK_GREY + EscapeSequences.SET_TEXT_COLOR_WHITE + " " + (row) + " ");
                        }
                        else if(col % 2 != 0) {
                            genBoard[9 - row][9 - col] = (EscapeSequences.SET_BG_COLOR_LIGHT_GREY + " " + displayPiece(myBoard.getPiece(new Position(row, col))) + " ");
                        }
                        else {
                            genBoard[9 - row][9 - col] = (EscapeSequences.SET_BG_COLOR_DARK_GREY + " " + displayPiece(myBoard.getPiece(new Position(row, col))) + " ");
                        }
                    }
                    else {
                        if(col == 0 || col == 9) {
                            genBoard[9 - row][9 - col] = (EscapeSequences.SET_BG_COLOR_DARK_GREY + EscapeSequences.SET_TEXT_COLOR_WHITE + " " + (row) + " ");
                        }
                        else if(col % 2 != 0) {
                            genBoard[9 - row][9 - col] = (EscapeSequences.SET_BG_COLOR_DARK_GREY + " " + displayPiece(myBoard.getPiece(new Position(row, col))) + " ");
                        }
                        else {
                            genBoard[9 - row][9 - col] = (EscapeSequences.SET_BG_COLOR_LIGHT_GREY + " " + displayPiece(myBoard.getPiece(new Position(row, col))) + " ");
                        }
                    }
                }
            }
        }
        return genBoard;
    }

    public static void displayHighlightBoard(GameData game, String pov, int myRow, int myCol) {
        ChessBoard myBoard = game.getGame().getBoard();
        String[][] genBoard = generateBoard(game.getGame().getBoard(), pov);

        int adjRow, adjCol;
        if(pov.equalsIgnoreCase("BLACK")){
            adjRow = myRow;
            adjCol = myCol + 1;
        }
        else {
            adjRow = myRow;
            adjCol = myCol + 1;
        }
        ChessPosition myPos = new Position(adjRow , adjCol);
        genBoard[adjRow][adjCol] = (EscapeSequences.SET_BG_COLOR_YELLOW + " " + displayPiece(myBoard.getPiece(myPos)) + " ");

        Collection<ChessMove> highlightMoves = myBoard.getPiece(myPos).pieceMoves(myBoard, myPos);
        for(ChessMove m : highlightMoves) {
            int highlightRow = m.getEndPosition().getRow() + 1;
            int highlightCol = m.getEndPosition().getColumn() + 1;
            if(highlightRow % 2 == 0 && highlightCol % 2 == 0) {
                genBoard[highlightRow][highlightCol] = (EscapeSequences.SET_BG_COLOR_GREEN + " " + displayPiece(myBoard.getPiece(m.getEndPosition())) + " ");
            }
            else if(highlightRow % 2 == 0 && highlightCol % 2 == 1) {
                genBoard[highlightRow][highlightCol] = (EscapeSequences.SET_BG_COLOR_DARK_GREEN + " " + displayPiece(myBoard.getPiece(m.getEndPosition())) + " ");
            }
            else if(highlightRow % 2 == 1 && highlightCol % 2 == 0) {
                genBoard[highlightRow][highlightCol] = (EscapeSequences.SET_BG_COLOR_DARK_GREEN + " " + displayPiece(myBoard.getPiece(m.getEndPosition())) + " ");
            }
            else {
                genBoard[highlightRow][highlightCol] = (EscapeSequences.SET_BG_COLOR_GREEN + " " + displayPiece(myBoard.getPiece(m.getEndPosition())) + " ");
            }
        }

        for(int row = 0; row < 10; row++) {
            for(int col = 0; col < 10; col++) {
                System.out.print(genBoard[row][col]);
            }
            System.out.println(EscapeSequences.SET_BG_COLOR_DARK_GREY + EscapeSequences.SET_TEXT_COLOR_WHITE);
        }

    }

    public static String displayPiece(ChessPiece p) {
        if(p == null) return EscapeSequences.EMPTY;
        if(p.getTeamColor() == ChessGame.TeamColor.WHITE) {
            return switch (p.getPieceType()) {
                case KING -> EscapeSequences.SET_TEXT_COLOR_WHITE + EscapeSequences.WHITE_KING;
                case QUEEN -> EscapeSequences.SET_TEXT_COLOR_WHITE + EscapeSequences.WHITE_QUEEN;
                case BISHOP -> EscapeSequences.SET_TEXT_COLOR_WHITE + EscapeSequences.WHITE_BISHOP;
                case KNIGHT -> EscapeSequences.SET_TEXT_COLOR_WHITE + EscapeSequences.WHITE_KNIGHT;
                case ROOK -> EscapeSequences.SET_TEXT_COLOR_WHITE + EscapeSequences.WHITE_ROOK;
                case PAWN -> EscapeSequences.SET_TEXT_COLOR_WHITE + EscapeSequences.WHITE_PAWN;
            };
        }
        else {
            return switch (p.getPieceType()) {
                case KING -> EscapeSequences.SET_TEXT_COLOR_BLACK + EscapeSequences.BLACK_KING + EscapeSequences.SET_TEXT_COLOR_WHITE;
                case QUEEN -> EscapeSequences.SET_TEXT_COLOR_BLACK + EscapeSequences.BLACK_QUEEN + EscapeSequences.SET_TEXT_COLOR_WHITE;
                case BISHOP -> EscapeSequences.SET_TEXT_COLOR_BLACK + EscapeSequences.BLACK_BISHOP + EscapeSequences.SET_TEXT_COLOR_WHITE;
                case KNIGHT -> EscapeSequences.SET_TEXT_COLOR_BLACK + EscapeSequences.BLACK_KNIGHT + EscapeSequences.SET_TEXT_COLOR_WHITE;
                case ROOK -> EscapeSequences.SET_TEXT_COLOR_BLACK + EscapeSequences.BLACK_ROOK + EscapeSequences.SET_TEXT_COLOR_WHITE;
                case PAWN -> EscapeSequences.SET_TEXT_COLOR_BLACK + EscapeSequences.BLACK_PAWN + EscapeSequences.SET_TEXT_COLOR_WHITE;
            };
        }
    }
}


