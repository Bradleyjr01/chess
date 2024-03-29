package StartCode;
import Resources.*;

/**
 * Represents moving a chess piece on a chessboard
 * 
 * Note: You can add to this interface, but you should not alter the existing
 * methods.
 */
public interface ChessMove {
    /**
     * @return StartCode.ChessPosition of starting location
     */
    ChessPosition getStartPosition();

    /**
     * @return StartCode.ChessPosition of ending location
     */
    ChessPosition getEndPosition();

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     * 
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    ChessPiece.PieceType getPromotionPiece();

    public Board makeAMove(Board currentBoard);
}
