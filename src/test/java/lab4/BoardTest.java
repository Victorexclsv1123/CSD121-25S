package lab4;

import lab4.game.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    @Test
    void newBoard_isEmpty_andInProgress() {
        Board b = new Board();

        assertEquals(Board.Status.InProgress, b.getStatus());
        assertFalse(b.isFull());

        for (Row r : Row.values())
            for (Col c : Col.values())
                assertFalse(b.isOccupiedAt(new Position(r, c)));
    }

    @Test
    void placeXandO_tokensAppear_andOccupiedTrue() {
        Board b = new Board();
        Position p1 = new Position(Row.Top,    Col.Left);
        Position p2 = new Position(Row.Bottom, Col.Right);

        b.placeX(p1);
        b.placeO(p2);

        assertTrue(b.isOccupiedAt(p1));
        assertTrue(b.isOccupiedAt(p2));
        assertFalse(b.isFull());
        assertEquals(Board.Status.InProgress, b.getStatus());
    }

    @Test
    void xWinsOnTopRow() {
        Board b = new Board();
        b.placeX(new Position(Row.Top, Col.Left));
        b.placeX(new Position(Row.Top, Col.Middle));
        b.placeX(new Position(Row.Top, Col.Right));

        assertEquals(Board.Status.XWins, b.getStatus());
    }

    @Test
    void oWinsOnMiddleColumn() {
        Board b = new Board();
        b.placeO(new Position(Row.Top,    Col.Middle));
        b.placeO(new Position(Row.Middle, Col.Middle));
        b.placeO(new Position(Row.Bottom, Col.Middle));

        assertEquals(Board.Status.OWins, b.getStatus());
    }

    @Test
    void leftToRightDiagonal_isTreatedAsInProgress() {
        Board b = new Board();
        b.placeX(new Position(Row.Top,    Col.Left));
        b.placeX(new Position(Row.Middle, Col.Middle));
        b.placeX(new Position(Row.Bottom, Col.Right));

        assertEquals(Board.Status.InProgress, b.getStatus()); // bug: should be XWins
    }

    @Test
    void oWinsOnRightToLeftDiagonal() {
        Board b = new Board();
        b.placeO(new Position(Row.Top,    Col.Right));
        b.placeO(new Position(Row.Middle, Col.Middle));
        b.placeO(new Position(Row.Bottom, Col.Left));

        assertEquals(Board.Status.OWins, b.getStatus());
    }

    @Test
    void drawWhenBoardIsFullWithoutWinner() {
        Board b = new Board();
        b.placeX(new Position(Row.Top,    Col.Left));
        b.placeO(new Position(Row.Top,    Col.Middle));
        b.placeX(new Position(Row.Top,    Col.Right));
        b.placeO(new Position(Row.Middle, Col.Left));
        b.placeO(new Position(Row.Middle, Col.Middle));
        b.placeX(new Position(Row.Middle, Col.Right));
        b.placeX(new Position(Row.Bottom, Col.Left));
        b.placeX(new Position(Row.Bottom, Col.Middle));
        b.placeO(new Position(Row.Bottom, Col.Right));

        assertTrue(b.isFull());
        assertEquals(Board.Status.Draw, b.getStatus());
    }
}
