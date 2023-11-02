package chess.MovementRules;

import chess.*;
import chess.MovementRules.MovementRule;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovementRule implements MovementRule {
    //return to
    @Override
    public Collection<ChessMove> validMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> myMoves = new ArrayList<ChessMove>();
        ChessPiece currentPiece = board.getPiece(position);
        //System.out.println(board.toString());

        for (int c = -2; c < 3; c++) {
            if(currentPiece == null) continue;
            if(c == 0) continue;
            //System.out.println("c = " + c);
            switch (Math.abs(c)) {
                case 2:
                    if(position.getRow() + 1 < 8 && position.getColumn() + c < 8 && position.getColumn() + c >= 0) {
                        ChessPosition movePosition = new Position(position.getRow() + 1 + 1, position.getColumn() + c + 1);
                        if (board.getPiece(movePosition) == null) {
                            myMoves.add(new Move(position, movePosition, null));
                        }
                        //target is other team
                        else if (!board.getPiece(movePosition).getTeamColor().equals(currentPiece.getTeamColor())) {
                            myMoves.add(new Move(position, movePosition, null));
                        }
                        //System.out.println("end pt 1");
                    }
                    if(position.getRow() - 1 >= 0 && position.getColumn() + c < 8 && position.getColumn() + c >= 0) {
                        ChessPosition movePosition = new Position(position.getRow() - 1 + 1, position.getColumn() + c + 1);
                        if (board.getPiece(movePosition) == null) {
                            myMoves.add(new Move(position, movePosition, null));
                        }
                        //target is other team
                        else if (!board.getPiece(movePosition).getTeamColor().equals(currentPiece.getTeamColor())) {
                            myMoves.add(new Move(position, movePosition, null));
                        }
                    }
                    break;
                case 1:
                    if(position.getRow() + 2 < 8 && position.getColumn() + c < 8 && position.getColumn() + c >= 0) {
                        ChessPosition movePosition = new Position(position.getRow() + 2 + 1, position.getColumn() + c + 1);
                        if (board.getPiece(movePosition) == null) {
                            myMoves.add(new Move(position, movePosition, null));
                        }
                        //target is other team
                        else if (!board.getPiece(movePosition).getTeamColor().equals(currentPiece.getTeamColor())) {
                            myMoves.add(new Move(position, movePosition, null));
                        }
                    }
                    if(position.getRow() - 2 >= 0 && position.getColumn() + c < 8 && position.getColumn() + c >= 0) {
                        ChessPosition movePosition = new Position(position.getRow() - 2 + 1, position.getColumn() + c + 1);
                        if (board.getPiece(movePosition) == null) {
                            myMoves.add(new Move(position, movePosition, null));
                        }
                        //target is other team
                        else if (!board.getPiece(movePosition).getTeamColor().equals(currentPiece.getTeamColor())) {
                            myMoves.add(new Move(position, movePosition, null));
                        }
                    }
                    break;
            }
        }
        /*System.out.println("all");
        for(ChessMove m : myMoves) {
            System.out.println(m.toString());
        }*/

        return myMoves;
    }
}
