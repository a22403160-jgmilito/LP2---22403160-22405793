package pt.ulusofona.lp2.greatprogrammingjourney;

import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;

public class TestGameManager {

    @Test
    public void testCreateInitialBoardBasic() {
        GameManager gm = new GameManager();

        String[][] players = {
                {"1", "Ana", "Java", "#FF0000"},
                {"2", "Bruno", "C++", "#00FF00"}
        };

        boolean result = gm.createInitialBoard(players, 10);

        assertTrue("O tabuleiro devia ser criado com sucesso", result);
        assertEquals("O primeiro jogador devia ser o id 1", 1, gm.getCurrentPlayerID());
    }
}
