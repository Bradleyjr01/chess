package Resources;

import MovementRules.*;
import StartCode.*;

import java.util.Collection;

public class Piece implements ChessPiece {

    ChessGame.TeamColor myTeam;
    ChessPiece.PieceType myType;

    boolean hasMoved;

    public Piece(String myColor, String myRole, boolean moved){
        switch(myColor) {
            case "black":
                myTeam = ChessGame.TeamColor.BLACK;
                break;
            case "white":
                myTeam = ChessGame.TeamColor.WHITE;
                break;
            default:
                myTeam = null;
        }

        switch(myRole) {
            case "king":
                myType = PieceType.KING;
                break;
            case "queen":
                myType = PieceType.QUEEN;
                break;
            case "bishop":
                myType = PieceType.BISHOP;
                break;
            case "knight":
                myType = PieceType.KNIGHT;
                break;
            case "rook":
                myType = PieceType.ROOK;
                break;
            case "pawn":
                myType = PieceType.PAWN;
            default:
                myType = null;
        }

        hasMoved = moved;
    }

    public Piece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type, boolean moved) {
        myTeam = pieceColor;
        myType = type;
        hasMoved = moved;
    }

    @Override
    public ChessGame.TeamColor getTeamColor() {
        return myTeam;
    }

    @Override
    public PieceType getPieceType() {
        return myType;
    }

    public boolean getHasMoved() { return hasMoved; }

    //return to this later
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        switch(myType) {
            case KING -> {
                return new KingMovementRule().validMoves(board, myPosition);
            }
            case QUEEN -> {
                return new QueenMovementRule().validMoves(board, myPosition);
            }
            case BISHOP -> {
                return new BishopMovementRule().validMoves(board, myPosition);
            }
            case KNIGHT -> {
                return new KnightMovementRule().validMoves(board, myPosition);
            }
            case ROOK -> {
                return new RookMovementRule().validMoves(board, myPosition);
            }
            case PAWN -> {
                return new PawnMovementRule().validMoves(board, myPosition);
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return getTeamColor().toString() + " " + getPieceType().toString();
    }

    public String boardPieceToString() {
        if(myType == null) return " ";
        switch(myType) {
            case KING -> {
                switch(myTeam) {
                    case WHITE -> { return "K"; }
                    case BLACK -> { return "k"; }
                }
            }
            case QUEEN -> {
                switch(myTeam) {
                    case WHITE -> { return "Q"; }
                    case BLACK -> { return "q"; }
                }
            }
            case BISHOP -> {
                switch(myTeam) {
                    case WHITE -> { return "B"; }
                    case BLACK -> { return "b"; }
                }
            }
            case KNIGHT -> {
                switch(myTeam) {
                    case WHITE -> { return "N"; }
                    case BLACK -> { return "n"; }
                }
            }
            case ROOK -> {
                switch(myTeam) {
                    case WHITE -> { return "R"; }
                    case BLACK -> { return "r"; }
                }
            }
            case PAWN -> {
                switch(myTeam) {
                    case WHITE -> { return "P"; }
                    case BLACK -> { return "p"; }
                }
            }
        }
        return null;
    }
}
