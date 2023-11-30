package Resources;

import StartCode.ChessPosition;

import java.util.Objects;

public class Position implements ChessPosition {

    int row;
    int col;

    public Position(int r, char c){
        row = r - 1;
        col = (int)(c - 'a');
    };

    public Position(int r, int c){
        row = r - 1;
        col = c - 1;
    };

    @Override
    public int getRow() { return row; }

    @Override
    public int getColumn() { return col; }

    @Override
    public String toString() {
        return (row + 1) + "" + (char)('a' + col);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return row == position.row && col == position.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
