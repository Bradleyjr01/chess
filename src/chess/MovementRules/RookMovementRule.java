package chess.MovementRules;

import chess.*;
import chess.MovementRules.MovementRule;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovementRule implements MovementRule {
    @Override
    public Collection<ChessMove> validMoves(ChessBoard board, ChessPosition position) {
        //System.out.println(board.toString());
        Collection<ChessMove> myMoves = new ArrayList<ChessMove>();
        ChessPiece currentPiece = board.getPiece(position);
        if(currentPiece == null) return myMoves;

        //left movement
        for (int p = position.getColumn() - 1; p >= 0; p--) {
            ChessPosition moveHorizontal = new Position(position.getRow() + 1, p + 1);
            if (board.getPiece(moveHorizontal) == null) {
                myMoves.add(new Move(position, moveHorizontal, null));
            }
            //adjacent is same team
            else if (board.getPiece(moveHorizontal).getTeamColor().equals(currentPiece.getTeamColor())) break;
            //adjacent is other team
            else if (!board.getPiece(moveHorizontal).getTeamColor().equals(currentPiece.getTeamColor())) {
                myMoves.add(new Move(position, moveHorizontal, null));
                break;
            }

        }
        /*System.out.println("left");
        for(ChessMove m : myMoves) {
            System.out.println(m.toString());
        }*/

        //downward movement
        for (int p = position.getRow() + 1; p < 8; p++) {
            ChessPosition moveVertical = new Position(p + 1, position.getColumn() + 1);
            if (board.getPiece(moveVertical) == null) {
                myMoves.add(new Move(position, moveVertical, null));
            }
            //adjacent is same team
            else if (board.getPiece(moveVertical) != null && board.getPiece(moveVertical).getTeamColor().equals(currentPiece.getTeamColor())) break;
            //adjacent is other team
            else if (board.getPiece(moveVertical) != null && !board.getPiece(moveVertical).getTeamColor().equals(currentPiece.getTeamColor())) {
                myMoves.add(new Move(position, moveVertical, null));
                break;
            }

        }
        /*System.out.println("down");
        for(ChessMove m : myMoves) {
            System.out.println(m.toString());
        }*/

        //right movement
        for (int p = position.getColumn() + 1; p < 8; p++) {;
            ChessPosition moveHorizontal = new Position(position.getRow() + 1, p + 1);
            if (board.getPiece(moveHorizontal) == null) {
                myMoves.add(new Move(position, moveHorizontal, null));
            }
            //adjacent is same team
            else if (board.getPiece(moveHorizontal).getTeamColor().equals(currentPiece.getTeamColor())) break;
            //adjacent is other team
            else if (!board.getPiece(moveHorizontal).getTeamColor().equals(currentPiece.getTeamColor())) {
                myMoves.add(new Move(position, moveHorizontal, null));
                break;
            }

        }
        /*System.out.println("right");
        for(ChessMove m : myMoves) {
            System.out.println(m.toString());
        }*/

        //upward movement
        for (int p = position.getRow() - 1; p >= 0; p--) {
            ChessPosition moveVertical = new Position(p + 1, position.getColumn() + 1);
            if (board.getPiece(moveVertical) == null) {
                myMoves.add(new Move(position, moveVertical, null));
            }
            //adjacent is same team
            else if (board.getPiece(moveVertical).getTeamColor().equals(currentPiece.getTeamColor())) break;
            //adjacent is other team
            else if (!board.getPiece(moveVertical).getTeamColor().equals(currentPiece.getTeamColor())) {
                myMoves.add(new Move(position, moveVertical, null));
                break;
            }

        }
        /*System.out.println("up");
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
