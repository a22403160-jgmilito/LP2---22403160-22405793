package pt.ulusofona.lp2.greatprogrammingjourney;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

public class TestReactCasaEspecial {

    @Test
    void casaComFerramenta_jaPossuiFerramenta_reactDevolveStringVaziaNaoNull() throws Exception {

        GameManager gm = new GameManager();

        String[][] players = {
                {"1", "Ana", "Java", "Azul"},
                {"2", "Bruno", "Java", "Vermelho"}
        };

        // Colocar uma ferramenta IDE (id 4) na casa 2
        String[][] config = {
                {"1", "4", "2"} // tipoLinha=1 tool, id=4 (IDE), pos=2
        };

        assertTrue(gm.createInitialBoard(players, 6, config));

        // Jogador 1 vai para a casa 2 e apanha IDE
        assertTrue(gm.moveCurrentPlayer(1));
        String msg1 = gm.reactToAbyssOrTool();
        assertNotNull(msg1);
        assertTrue(msg1.contains("apanhou a ferramenta IDE"));

        // Passar turno do jogador 2 só para voltar ao jogador 1
        gm.moveCurrentPlayer(1);
        gm.reactToAbyssOrTool();

        // Agora forçar o jogador 1 a estar na casa 2 outra vez (sem "apanhar" outra IDE)
        Field fBoard = GameManager.class.getDeclaredField("board");
        fBoard.setAccessible(true);
        Board b = (Board) fBoard.get(gm);

        b.setPlayerPosicao(1, 2);

        // Agora: existe ferramenta na casa (casa especial), mas ele já tem IDE => mensagem vazia ("") e NÃO null
        String msg2 = gm.reactToAbyssOrTool();

        assertNotNull(msg2, "Não deve devolver null numa casa especial.");
        assertEquals("", msg2, "Como a casa é especial mas não houve evento, deve devolver string vazia.");
    }
}
