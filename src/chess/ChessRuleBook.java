package chess;

import java.util.Collection;

public interface ChessRuleBook {
    Collection<ChessMove> validMove(ChessBoard board, ChessPosition position);

    Boolean isBoardValid(ChessBoard board);

    boolean isInCheck(ChessBoard board, ChessGame.TeamColor teamColor);

    boolean isInCheckmate(ChessBoard board, ChessGame.TeamColor teamColor);

    boolean isInStalemate(ChessBoard board, ChessGame.TeamColor teamColor);
}
