package pt.ulusofona.lp2.greatprogrammingjourney;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
//
public class TestGameManager {
    @Test
    void testgetSlotInfo(){
        GameManager manager = new GameManager();
        assertTrue(manager.createInitialBoard(new String[][]{
                {"1234", "Pedro",  "C+", "Purple"},
                {"12345", "Joao",  "C+", "Brown"}
        }, 50));
        assertEquals(Arrays.toString(manager.getSlotInfo(1)),"[1234,12345]");
    }
}