package MovementRules;

import StartCode.*;
import Resources.*;

import java.util.Collection;
import java.util.ArrayList;

public class KingMovementRule implements MovementRule {
    @Override
    public Collection<ChessMove> validMoves(ChessBoard board, ChessPosition position) {
        //System.out.println(board.toString());
        Collection<ChessMove> myMoves = new ArrayList<ChessMove>();
        ChessPiece currentPiece = board.getPiece(position);
        if(currentPiece == null) return myMoves;
        for (int r = -1; r < 2; r++) {
            for (int c = -1; c < 2; c++) {
                //outside board
                if (position.getRow() + r < 0 || position.getRow() + r > 7) continue;
                if (position.getColumn() + c < 0 || position.getColumn() + c > 7) continue;
                //check for empty spot
                ChessPosition movePosition = new Position(position.getRow() + r + 1, position.getColumn() + c + 1);
                if (board.getPiece(movePosition) == null) {
                    myMoves.add(new Move(position, movePosition, null));
                }
                //adjacent is other team
                else if (!board.getPiece(movePosition).getTeamColor().equals(currentPiece.getTeamColor())) {
                    myMoves.add(new Move(position, movePosition, null));
                }
            }
        }
        ArrayList<ChessMove> validMoves = new ArrayList<ChessMove>(myMoves);
        ArrayList<ChessMove> opponentMoves = new ArrayList<ChessMove>();
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                ChessPiece myPiece = board.getPiece(new Position(i + 1, j + 1));
                if(myPiece == null) continue;

                if(myPiece.getPieceType() == ChessPiece.PieceType.KING && myPiece.getTeamColor() != board.getPiece(position).getTeamColor()){
                    opponentMoves.addAll(kingThreatens(board, new Position(i + 1,j + 1)));
                }
                else if(myPiece.getPieceType() == ChessPiece.PieceType.PAWN && myPiece.getTeamColor() != board.getPiece(position).getTeamColor()){
                    //System.out.println("pawn move");
                    opponentMoves.addAll(pawnThreatens(board, new Position(i + 1,j + 1)));
                }
                else if(myPiece.getTeamColor() != currentPiece.getTeamColor()) {
                    opponentMoves.addAll(myPiece.pieceMoves(board, new Position(i + 1, j + 1)));
                }
            }
        }
        //original moves
        /*System.out.println("King Moves");
        for(StartCode.ChessMove m: myMoves) {
            System.out.println(m.toString());
        }*/

        /*System.out.println("Opp Moves");
        for(StartCode.ChessMove m: opponentMoves) {
            System.out.println(m.toString());
        }*/

        for(int i = validMoves.size() - 1; i >= 0; i--) {
            for(int j = 0; j < opponentMoves.size(); j++) {
                //find all moves
                if(opponentMoves.get(j).getEndPosition().getRow() == validMoves.get(i).getEndPosition().getRow()
                        && opponentMoves.get(j).getEndPosition().getColumn() == validMoves.get(i).getEndPosition().getColumn()) {
                    //System.out.println("rem "+ validMoves.get(i).toString());
                    validMoves.remove(i);
                    break;
                }
            }
        }

        return validMoves;
    }

    public Collection<ChessMove> pawnThreatens(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> threaten = new ArrayList<ChessMove>();
        ChessPiece pawn = board.getPiece(position);
        int direction = 1;
        for(int i = -1; i < 2; i++) {
            if(i == 0) continue;
            if(pawn.getTeamColor() == ChessGame.TeamColor.BLACK) direction = -1;
            if(position.getColumn() + i < 8 && position.getRow() + direction < 8 && position.getRow() + i > -1 && position.getColumn() + direction > -1){
                ChessPosition moveTo = new Position((position.getRow() + direction + 1), (position.getColumn() + i + 1));
                threaten.add(new Move(position, moveTo, null));
            }

        }
        return threaten;
    }

    public Collection<ChessMove> kingThreatens(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> threaten = new ArrayList<ChessMove>();
        for(int i = -1; i < 2; i++) {
            for(int j = -1; j < 2; j++) {
                if(position.getRow() + i < 8 && position.getRow() + i > -1 &&
                        position.getColumn() + j < 8 && position.getColumn() + j > -1) {
                    ChessPosition moveTo = new Position(position.getRow() + i + 1, position.getColumn() + j + 1);
                    threaten.add(new Move(position, moveTo, null));
                }
            }
        }
        return threaten;
    }

}
