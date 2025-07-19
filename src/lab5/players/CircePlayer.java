package lab5.players;

import lab5.game.Board;
import lab5.game.Col;
import lab5.game.Position;
import lab5.game.Row;

import java.util.List;

public class CircePlayer extends Player {

    public CircePlayer(String name) {
        super(name);
    }

    @Override
    public Position pickNextMove(Board board) {
        List<Position> empty = board.getEmptyCells();

        int[] preferredOrder = {4, 1, 2, 5, 8, 7, 6, 3, 0};

        for (int index : preferredOrder) {
            Position p = fromIndex(index);
            if (empty.contains(p)) {
                return p;
            }
        }

        // fallback
        return empty.get(0);
    }

    /**
     * Converts a board index (0-8) into a Position(row, col)
     */
    private Position fromIndex(int index) {
        Row row = Row.values()[index / 3];    // 0,1,2 -> Top, Middle, Bottom
        Col col = Col.values()[index % 3];    // 0,1,2 -> Left, Middle, Right
        return new Position(row, col);
    }
}
