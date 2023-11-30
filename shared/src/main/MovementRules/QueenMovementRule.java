package MovementRules;

import StartCode.*;
import Resources.*;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovementRule implements MovementRule {

    //return to
    @Override
    public Collection<ChessMove> validMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> myMoves = new ArrayList<ChessMove>();
        ChessPiece currentPiece = board.getPiece(position);
        if(currentPiece == null) return myMoves;
        //System.out.println(board.toString());

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
        for(StartCode.ChessMove m : myMoves) {
            System.out.println(m.toString());
        }*/

        //downward movement
        for (int p = position.getRow() + 1; p < 8; p++) {
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
        /*System.out.println("down");
        for(StartCode.ChessMove m : myMoves) {
            System.out.println(m.toString());
        }*/

        //right movement
        for (int p = position.getColumn() + 1; p < 8; p++) {;
            ChessPosition moveHorizontal = new Position(position.getRow() + 1, p + 1);
            if (board.getPiece(moveHorizontal) == null) {
                myMoves.add(new Move(position, moveHorizontal,null));
            }
            //adjacent is same team
            else if (board.getPiece(moveHorizontal).getTeamColor().equals(currentPiece.getTeamColor())) break;
                //adjacent is other team
            else if (!board.getPiece(moveHorizontal).getTeamColor().equals(currentPiece.getTeamColor())) {
                myMoves.add(new Move(position, moveHorizontal,null));
                break;
            }

        }
        /*System.out.println("right");
        for(StartCode.ChessMove m : myMoves) {
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
        for(StartCode.ChessMove m : myMoves) {
            System.out.println(m.toString());
        }*/

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
        for(StartCode.ChessMove m : myMoves) {
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
        for(StartCode.ChessMove m : myMoves) {
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
        for(StartCode.ChessMove m : myMoves) {
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
        for(StartCode.ChessMove m : myMoves) {
            System.out.println(m.toString());
        }*/

        /*System.out.println("all");
        for(StartCode.ChessMove m : myMoves) {
            System.out.println(m.toString());
        }*/

        return myMoves;
    }
}
