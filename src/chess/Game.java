package chess;

import chess.MovementRules.KingMovementRule;

import java.util.ArrayList;
import java.util.Collection;

public class Game implements ChessGame {

    private TeamColor myTurn;
    private ChessBoard myBoard;

    public Game(){
        myTurn = TeamColor.WHITE;
        myBoard = new Board();
    }
    @Override
    public TeamColor getTeamTurn() {
        return myTurn;
    }

    @Override
    public void setTeamTurn(TeamColor team) {
        myTurn = team;
    }

    @Override
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        //System.out.println(myBoard);
        ChessPiece moveMe = myBoard.getPiece(startPosition);
        if(myBoard.getPiece(startPosition) == null) return new ArrayList<ChessMove>();
        ArrayList<ChessMove> pieceMoves = new ArrayList<>(moveMe.pieceMoves(myBoard, startPosition));

        ChessGame testGame = new Game();
        //for (ChessMove m : pieceMoves) {
        for(int m = pieceMoves.size() - 1; m > -1; m--) {
            ChessBoard testBoard = new Board(myBoard);
            testBoard = pieceMoves.get(m).makeAMove(myBoard);
            testGame.setBoard(testBoard);
            if(testGame.isInCheck(getTeamTurn())) {
                pieceMoves.remove(m);
            }
        }

        return moveMe.pieceMoves(myBoard, startPosition);
    }

    @Override
    public void makeMove(ChessMove move) throws InvalidMoveException {
        System.out.println(myBoard);
        if(move.getEndPosition().getColumn() < 0 || move.getEndPosition().getRow() < 0
            || move.getEndPosition().getColumn() > 7 || move.getEndPosition().getRow() > 7
            || move.getStartPosition().getColumn() < 0 || move.getStartPosition().getRow() < 0
            || move.getStartPosition().getColumn() > 7 || move.getStartPosition().getRow() > 7) {
            throw(new InvalidMoveException("Out of Bounds"));
        }
        if(myBoard.getPiece(move.getStartPosition()).getTeamColor() != myTurn) throw(new InvalidMoveException("Not Your Turn"));
        Collection<ChessMove> pieceMoves = validMoves(move.getStartPosition());
        boolean valid = false;

        System.out.println("Moves for " + myBoard.getPiece(move.getStartPosition()).toString());
        for(ChessMove m : pieceMoves) {
            System.out.println(m.toString());
        }

        for (ChessMove m : pieceMoves) {
            if(m.getEndPosition().getRow() == move.getEndPosition().getRow()
                    && m.getEndPosition().getColumn() == move.getEndPosition().getColumn()) {
                ChessBoard lastBoard = new Board(myBoard);
                valid = true;
                System.out.println("valid: " + m.toString());
                if(move.getPromotionPiece() != null) System.out.println("aaaAAAAAAAaaaaaaah");
                myBoard = move.makeAMove(myBoard);
                if(isInCheck(getTeamTurn())) {
                    myBoard = new Board(lastBoard);
                    throw(new InvalidMoveException("King in Check"));
                }
            }
        }
        if(!valid) throw(new InvalidMoveException("Move Not Valid"));

        if(getTeamTurn() == TeamColor.WHITE) setTeamTurn(TeamColor.BLACK);
        else setTeamTurn(TeamColor.WHITE);
    }

    @Override
    public boolean isInCheck(TeamColor teamColor) {
        //System.out.println(myBoard.toString());
        int startPos = 0;
        ChessPosition kingPosition = new Position(1,1);
        boolean kingFound = false;
        if(teamColor == TeamColor.WHITE) startPos = 7;
        if(teamColor == TeamColor.BLACK) startPos = 0;

        while(!kingFound) {
                for (int i = 0; i < 8; i++) {
                    ChessPiece checkPiece = myBoard.getPiece(new Position(startPos + 1, i + 1));
                    if(checkPiece == null) continue;
                    //System.out.println(checkPiece.getPieceType());
                    if(checkPiece.getPieceType() == ChessPiece.PieceType.KING && checkPiece.getTeamColor() == teamColor) {
                        kingFound = true;
                        kingPosition = new Position(startPos + 1, i + 1);
                        System.out.println(myBoard.getPiece(kingPosition).getTeamColor() + " " + myBoard.getPiece(kingPosition).getPieceType() + " found at " + kingPosition);
                        break;
                    }
                }
                if(kingFound) break;
                if(teamColor == TeamColor.WHITE) startPos--;
                if(teamColor == TeamColor.BLACK) startPos++;
                if(startPos < 0 || startPos > 7) {return false;}
        }

        Collection<ChessMove> opponentMoves = new ArrayList<ChessMove>();
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                ChessPiece myPiece = myBoard.getPiece(new Position(i + 1, j + 1));
                if(myPiece != null && myPiece.getPieceType() == ChessPiece.PieceType.PAWN && myPiece.getTeamColor() != teamColor){
                    opponentMoves.addAll(new KingMovementRule().kingThreatens(myBoard, new Position(i + 1, j + 1)));
                    //System.out.println("pawn is a threat");
                }
                else if(myPiece != null && myPiece.getTeamColor() != teamColor) {
                    opponentMoves.addAll(myPiece.pieceMoves(myBoard, new Position(i + 1, j + 1)));
                    System.out.println("other is a threat");
                }
            }
        }

        System.out.println("removing by opp moves");
        for(ChessMove m : opponentMoves) {
            //System.out.println(m.toString());
            System.out.println("piece can move to " + m.getEndPosition() + ", king is at " + kingPosition);
            if(m.getEndPosition().getRow() == kingPosition.getRow() && m.getEndPosition().getColumn() == kingPosition.getColumn()) return true;
        }
        return false;
    }

    @Override
    public boolean isInCheckmate(TeamColor teamColor) {
        System.out.println(myBoard.toString());
        //if(!isInCheck(teamColor)) return false;

        int startPos = 0;
        ChessPosition kingPosition = new Position(1,1);
        boolean kingFound = false;
        if(teamColor == TeamColor.WHITE) startPos = 7;
        if(teamColor == TeamColor.BLACK) startPos = 0;

        while(!kingFound) {
            for (int i = 0; i < 8; i++) {
                ChessPiece checkPiece = myBoard.getPiece(new Position(startPos + 1, i + 1));
                if(checkPiece == null) continue;
                //System.out.println(checkPiece.getPieceType());
                if(checkPiece.getPieceType() == ChessPiece.PieceType.KING && checkPiece.getTeamColor() == teamColor) {
                    kingPosition = new Position(startPos + 1, i + 1);
                    System.out.println(myBoard.getPiece(kingPosition).getTeamColor() + " " + myBoard.getPiece(kingPosition).getPieceType() + " found at " + kingPosition);
                    kingFound = true;
                    break;
                }
            }
            if(teamColor == TeamColor.WHITE) startPos--;
            if(teamColor == TeamColor.BLACK) startPos++;
        }

        ArrayList<ChessMove> newKingMoves= new ArrayList<>(validMoves(kingPosition));
        /*for(ChessMove m: newKingMoves) {
            //System.out.println("king moves" + m.toString());
        }*/
        return newKingMoves.isEmpty();
    }

    @Override
    public boolean isInStalemate(TeamColor teamColor) {
        boolean stalemate = true;
        Collection<ChessMove> allMoves = new ArrayList<ChessMove>();
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                ChessPiece piece = myBoard.getPiece(new Position(i + 1, j + 1));
                if(piece != null && piece.getTeamColor() == teamColor) {
                    allMoves.addAll(piece.pieceMoves(myBoard,new Position(i + 1, j + 1)));
                }
                if(!allMoves.isEmpty()) return false;
            }
        }
        if(allMoves.isEmpty()) return true;

        return false;
    }

    @Override
    public void setBoard(ChessBoard board) {
        myBoard = board;
    }

    @Override
    public ChessBoard getBoard() {
        return myBoard;
    }
}
