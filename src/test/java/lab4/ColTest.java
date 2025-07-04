package lab4;

import lab4.game.Col;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColTest {



    @Test
    void numericShortcutsAreAccepted() {
        assertEquals(Col.Left,   Col.from("1"));
        assertEquals(Col.Middle, Col.from("2"));
        assertEquals(Col.Right,  Col.from("3"));
    }

    @Test
    void letterShortcutsAreCaseInsensitive() {
        assertEquals(Col.Left,   Col.from("l"));
        assertEquals(Col.Left,   Col.from("L"));

        assertEquals(Col.Middle, Col.from("m"));
        assertEquals(Col.Middle, Col.from("M"));
        assertEquals(Col.Middle, Col.from("c"));
        assertEquals(Col.Middle, Col.from("C"));

        assertEquals(Col.Right,  Col.from("r"));
        assertEquals(Col.Right,  Col.from("R"));
    }



    @Test
    void invalidStringsThrowIllegalArgumentException() {
        for (String bad : new String[]{ "0", "4", "left", "middle", "right", "", "x" })
            assertThrows(IllegalArgumentException.class, () -> Col.from(bad));
    }

    @Test
    void nullInputThrowsNullPointerException() {
        // Col.from calls str.toLowerCase() directly, so null ⇒ NPE
        assertThrows(NullPointerException.class, () -> Col.from(null));
    }
}
