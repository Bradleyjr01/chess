package chess.MovementRules;

import chess.*;
import chess.MovementRules.MovementRule;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovementRule implements MovementRule {
    @Override
    public Collection<ChessMove> validMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> myMoves = new ArrayList<ChessMove>();
        ChessPiece currentPiece = board.getPiece(position);
        if(currentPiece == null) return myMoves;
        //System.out.println(board);

        //left up
        for (int p = 1; p < 8; p++) {
            int row = position.getRow() + p;
            int col = position.getColumn() - p;
                //outside board
                if (row < 0 || row > 7) continue;
                if (col < 0 || col > 7) continue;
                //check for empty spot
                ChessPosition movePosition = new Position(row + 1, col + 1);
                if (board.getPiece(movePosition) == null) {
                    myMoves.add(new Move(position, movePosition, null));
                }
                //adjacent is same team
                else if (board.getPiece(movePosition).getTeamColor().equals(currentPiece.getTeamColor())) break;
                //adjacent is other team
                else if (!board.getPiece(movePosition).getTeamColor().equals(currentPiece.getTeamColor())) {
                    myMoves.add(new Move(position, movePosition, null));
                    break;
                }
        }
        /*System.out.println("l-u");
        for(ChessMove m : myMoves) {
            System.out.println(m.toString());
        }*/

        //left down
        for (int p = 1; p < 8; p++) {
            int row = position.getRow() - p;
            int col = position.getColumn() - p;
            //outside board
            if (row < 0 || row > 7) continue;
            if (col < 0 || col > 7) continue;
            //check for empty spot
            ChessPosition movePosition = new Position(row + 1, col + 1);
            if (board.getPiece(movePosition) == null) {
                myMoves.add(new Move(position, movePosition, null));
            }
            //adjacent is same team
            else if (board.getPiece(movePosition).getTeamColor().equals(currentPiece.getTeamColor())) break;
                //adjacent is other team
            else if (!board.getPiece(movePosition).getTeamColor().equals(currentPiece.getTeamColor())) {
                myMoves.add(new Move(position, movePosition, null));
                break;
            }
        }
        /*System.out.println("l-d");
        for(ChessMove m : myMoves) {
            System.out.println(m.toString());
        }*/

        //right up
        for (int p = 1; p < 8; p++) {
            int row = position.getRow() + p;
            int col = position.getColumn() + p;
            //outside board
            if (row < 0 || row > 7) continue;
            if (col < 0 || col > 7) continue;
            //check for empty spot
            ChessPosition movePosition = new Position(row + 1, col + 1);
            if (board.getPiece(movePosition) == null) {
                myMoves.add(new Move(position, movePosition, null));
            }
            //adjacent is same team
            else if (board.getPiece(movePosition).getTeamColor().equals(currentPiece.getTeamColor())) break;
                //adjacent is other team
            else if (!board.getPiece(movePosition).getTeamColor().equals(currentPiece.getTeamColor())) {
                myMoves.add(new Move(position, movePosition, null));
                break;
            }
        }
        /*System.out.println("r-u");
        for(ChessMove m : myMoves) {
            System.out.println(m.toString());
        }*/

        //right down
        for (int p = 1; p < 8; p++) {
            int row = position.getRow() - p;
            int col = position.getColumn() + p;
            //outside board
            if (row < 0 || row > 7) continue;
            if (col < 0 || col > 7) continue;
            //check for empty spot
            ChessPosition movePosition = new Position(row + 1, col + 1);
            if (board.getPiece(movePosition) == null) {
                myMoves.add(new Move(position, movePosition, null));
            }
            //adjacent is same team
            else if (board.getPiece(movePosition).getTeamColor().equals(currentPiece.getTeamColor())) break;
                //adjacent is other team
            else if (!board.getPiece(movePosition).getTeamColor().equals(currentPiece.getTeamColor())) {
                myMoves.add(new Move(position, movePosition, null));
                break;
            }
        }
        /*System.out.println("r-d");
        for(ChessMove m : myMoves) {
            System.out.println(m.toString());
        }*/

        /*System.out.println("all");
        for(ChessMove m : myMoves) {
            System.out.println(m.toString());
        }*/

        return myMoves;
    }
}
