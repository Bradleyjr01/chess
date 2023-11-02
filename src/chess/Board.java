package chess;

import passoffTests.chessTests.chessPieceTests.KnightMoveTests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Board implements ChessBoard {

    private ChessPiece[][] myBoard = new ChessPiece[8][8];
    private ArrayList<ChessPiece> capturedPieces = new ArrayList<ChessPiece>();
    private ChessMove lastMove;


    public Board(){}

    public Board(ChessBoard oldBoard) {
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                myBoard[i][j] = oldBoard.getPiece(new Position(i + 1, j + 1));
            }
        }

    }

    @Override
    public void addPiece(ChessPosition position, ChessPiece piece) {
        myBoard[position.getRow()][position.getColumn()] = piece;
        //System.out.println("IN TRANSIT:\n" + this.toString());
    }

    public void addPiece(ChessPosition[] positions, ChessPiece piece){
        for(int i = 0; i < positions.length; i++) {
            myBoard[positions[i].getRow()][positions[i].getColumn()] = piece;
        }
    }

    public void capturePiece(ChessPiece captured) {
        capturedPieces.add(captured);
    }

    public ChessPiece undoCaptured() {
        ChessPiece lastCaptured = capturedPieces.get(capturedPieces.size() - 1);
        capturedPieces.remove(capturedPieces.size() - 1);
        return lastCaptured;
    }

    @Override
    public ChessPiece getPiece(ChessPosition position) {
        return myBoard[position.getRow()][position.getColumn()];
    }

    public void removePiece(ChessPosition position) {
        myBoard[position.getRow()][position.getColumn()] = null;
    }

    @Override
    public void resetBoard() {

        //reset middle
        for(int i = 1; i < 9; i++) {
            for(int j = 1; j < 9; j++) {
                addPiece(new Position(i , j), null);
            }
        }

        //white rooks
        addPiece(new Position[]{ new Position(1, 'a'), new Position(1, 'h')},
                new Piece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK, false));
        //white knights
        addPiece(new Position[]{ new Position(1, 'b'), new Position(1, 'g')},
                new Piece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT, false));
        //white bishops
        addPiece(new Position[]{ new Position(1, 'c'), new Position(1, 'f')},
                new Piece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP, false));
        //white queen
        addPiece(new Position(1, 'd'), new Piece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN, false));
        //white king
        addPiece(new Position(1, 'e'), new Piece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING, false));
        //white pawns
        Position[] whitePawns = new Position[8];
        for(int i = 0; i < whitePawns.length; i++) {
            whitePawns[i] = new Position(2, (char)('a' + i));
        }
        addPiece(whitePawns, new Piece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN, false));

        //black rooks
        addPiece(new Position[]{ new Position(8, 'a'), new Position(8, 'h')},
                new Piece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK, false));
        //black knights
        addPiece(new Position[]{ new Position(8, 'b'), new Position(8, 'g')},
                new Piece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT, false));
        //black bishops
        addPiece(new Position[]{ new Position(8, 'c'), new Position(8, 'f')},
                new Piece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP, false));
        //black queen
        addPiece(new Position(8, 'd'), new Piece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN, false));
        //black king
        addPiece(new Position(8, 'e'), new Piece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING, false));
        //black pawns
        Position[] blackPawns = new Position[8];
        for(int i = 0; i < blackPawns.length; i++) {
            blackPawns[i] = new Position(7, (char)('a' + i));
        }
        addPiece(blackPawns, new Piece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN, false));

    }

    public ChessMove getLastMove() { return lastMove; }

    @Override
    public String toString() {
        StringBuilder printBoard = new StringBuilder();
        printBoard.append(" a b c d e f g h \n");
        for(int r = 7; r >= 0; r--) {
            for(int c = 0; c < 8; c++) {
                if(myBoard[r][c] == null) printBoard.append("| ");
                else printBoard.append("|").append(myBoard[r][c].boardPieceToString());
            }
            printBoard.append("| " + (r + 1) + "\n");
        }
        return printBoard.toString();
    }

    public ChessPiece[][] getMyBoard() {
        return myBoard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return Arrays.equals(myBoard, board.myBoard) && Objects.equals(capturedPieces, board.capturedPieces) && Objects.equals(lastMove, board.lastMove);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(capturedPieces, lastMove);
        result = 31 * result + Arrays.hashCode(myBoard);
        return result;
    }


}
