package Resources;

import StartCode.ChessGame;
import StartCode.ChessMove;
import StartCode.ChessPiece;
import StartCode.ChessPosition;

import java.util.Objects;

public class Move implements ChessMove {

    private ChessPosition from;
    private ChessPosition to;
    private ChessPiece.PieceType promoteTo;

    /*public Resources.Move(StartCode.ChessPosition moveFrom, StartCode.ChessPosition moveTo){
        from = moveFrom;
        to = moveTo;
        //promoteTo = StartCode.ChessPiece.PieceType.PAWN;
    }*/

    public Move(ChessPosition moveFrom, ChessPosition moveTo, ChessPiece.PieceType promotionPiece){
        from = moveFrom;
        to = moveTo;
        promoteTo = promotionPiece;
        if(promotionPiece != null) System.out.println("promotionPiece: " + promotionPiece);
    }

    public Board makeAMove(Board currentBoard) {
        //TODO: promoteTo never fills based on given Resources.Move()
        System.out.println("given move: " + toString());

        Board moveBoard = new Board(currentBoard);
        ChessPiece toMove = moveBoard.getPiece(getStartPosition());
        if(toMove == null) return currentBoard;
        //System.out.print("moving: " + toMove.toString());
        ChessPiece taken = moveBoard.getPiece(getEndPosition());
        //if(taken != null) System.out.println(" to take " + taken.toString());
        //else System.out.println(" to empty");
        if(promoteTo != null) System.out.println("promote? " + promoteTo.toString());
        else System.out.println("promote? null");
        if(toMove.getPieceType() == ChessPiece.PieceType.PAWN && promoteTo != null) {
            if((toMove.getTeamColor() == ChessGame.TeamColor.WHITE && getEndPosition().getRow() == 7)
                || (toMove.getTeamColor() == ChessGame.TeamColor.BLACK && getEndPosition().getRow() == 0)) {
                //promoteTo = StartCode.ChessPiece.PieceType.QUEEN;
                System.out.print("promoting " + toMove.toString());
                moveBoard.addPiece(getEndPosition(), new Piece(toMove.getTeamColor(), promoteTo, true));
                System.out.println(" to " + promoteTo.toString());
                moveBoard.addPiece(getStartPosition(), null);
            }
            else {
                moveBoard.addPiece(getEndPosition(), new Piece(toMove.getTeamColor(), toMove.getPieceType(), true));
                moveBoard.addPiece(getStartPosition(), null);
            }
        }
        else {
            moveBoard.addPiece(getEndPosition(), new Piece(toMove.getTeamColor(), toMove.getPieceType(), true));
            moveBoard.addPiece(getStartPosition(), null);
        }
        //System.out.println("AFTER\n" + moveBoard.toString());
    System.out.println();

        return moveBoard;
    }

    @Override
    public ChessPosition getStartPosition() {
        return from;
    }

    @Override
    public ChessPosition getEndPosition() {
        return to;
    }

    //needs to be able to get a piece type from the user
    @Override
    public ChessPiece.PieceType getPromotionPiece() {
            return promoteTo;
    }

    @Override
    public String toString() {
        String returnMe = from.toString() + ":" + to.toString();
        if(promoteTo != null) returnMe += "(prom:" + promoteTo.toString() + ")";
        return returnMe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return Objects.equals(from, move.from) && Objects.equals(to, move.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, promoteTo);
    }
}
