package chess.MovementRules;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.ArrayList;

public interface MovementRule {

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param position the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     *         startPosition
     */
    Collection<ChessMove> validMoves(ChessBoard board, ChessPosition position);

}
