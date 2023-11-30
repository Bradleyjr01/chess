package MovementRules;

import StartCode.*;
import Resources.*;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovementRule implements MovementRule {
    @Override
    public Collection<ChessMove> validMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> myMoves = new ArrayList<ChessMove>();
        ChessPiece currentPiece = board.getPiece(position);
        if(position.getRow() < 0 || position.getRow() > 7) return myMoves;
        if(position.getColumn() < 0 || position.getColumn() > 7) return myMoves;
        if(currentPiece == null) return myMoves;
        //System.out.println(board.toString());

        //black pawn
        if(currentPiece.getTeamColor().equals(ChessGame.TeamColor.BLACK)) {
            ChessPosition movePosition;
            //initial advanced two
            if(position.getRow() == 6) {
                movePosition = new Position(position.getRow() - 2 + 1, position.getColumn() + 1);
                if (board.getPiece(movePosition) == null && board.getPiece(new Position(position.getRow() - 1 + 1, position.getColumn() + 1)) == null) {
                    ChessMove myMove = new Move(position, movePosition, null);
                    //myMove.enPassantAvailable(true);
                    myMoves.add(myMove);
                }
            }
            //move forward
            movePosition = new Position(position.getRow() - 1 + 1, position.getColumn() + 1);
            if ((position.getRow() - 1) > -1 && board.getPiece(movePosition) == null) {
                ChessMove myMove = new Move(position, movePosition, null);
                if(movePosition.getRow() == 0) {
                    //myMove.promotionAvailable();
                    myMove.getPromotionPiece();
                }
                myMoves.add(myMove);
            }
            //can take piece front left
            movePosition = new Position(position.getRow() - 1  + 1, position.getColumn() - 1 + 1);
            if(board.getPiece(movePosition) != null && !board.getPiece(movePosition).getTeamColor().equals(currentPiece.getTeamColor())) {
                myMoves.add(new Move(position, movePosition, null));
            }
            // can take piece front right
            movePosition = new Position(position.getRow() - 1 + 1, position.getColumn() + 1 + 1);
            if(board.getPiece(movePosition) != null && !board.getPiece(movePosition).getTeamColor().equals(currentPiece.getTeamColor())) {
                myMoves.add(new Move(position, movePosition, null));
            }
        }

        //white pawn
        else if(currentPiece.getTeamColor().equals(ChessGame.TeamColor.WHITE)) {
            ChessPosition movePosition;
            //initial advanced two
            if(position.getRow() == 1) {
                movePosition = new Position(position.getRow() + 2 + 1, position.getColumn() + 1);
                if (board.getPiece(movePosition) == null && board.getPiece(new Position(position.getRow() + 1 + 1, position.getColumn() + 1)) == null) {
                    ChessMove myMove = new Move(position, movePosition, null);
                    //myMove.enPassantAvailable(true);
                    myMoves.add(myMove);
                }
            }
            //move forward
            movePosition = new Position(position.getRow() + 1 + 1, position.getColumn() + 1);
            if ((position.getRow() + 1) < 8 && board.getPiece(movePosition) == null) {
                ChessMove myMove = new Move(position, movePosition, null);
                if(movePosition.getRow() == 7) {
                    myMove.getPromotionPiece();
                }
                myMoves.add(myMove);
            }
            //can take piece front left
            if(position.getColumn() - 1 >= 0 && position.getColumn() - 1 < 8) {
                movePosition = new Position(position.getRow() + 1 + 1, position.getColumn() - 1 + 1);
                if (board.getPiece(movePosition) != null
                        && !board.getPiece(movePosition).getTeamColor().equals(currentPiece.getTeamColor())) {
                    myMoves.add(new Move(position, movePosition, null));
                }
            }

            // can take piece front right
            if(position.getColumn() + 1 >= 0 && position.getColumn() + 1 < 8) {
                movePosition = new Position(position.getRow() + 1 + 1, position.getColumn() + 1 + 1);
                if (board.getPiece(movePosition) != null
                        && !board.getPiece(movePosition).getTeamColor().equals(currentPiece.getTeamColor())) {
                    myMoves.add(new Move(position, movePosition, null));
                }
            }

        }

        else return null;

        /*System.out.println("all");
        for(StartCode.ChessMove m : myMoves) {
            System.out.println(m.toString());
        }*/

        return myMoves;
    }
}
