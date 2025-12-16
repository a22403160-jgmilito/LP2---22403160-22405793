package pt.ulusofona.lp2.greatprogrammingjourney;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameManagerTest {

    private static String[][] twoPlayers(String lang1, String lang2) {
        return new String[][]{
                {"1", "Ana", lang1, "#FF0000"},
                {"2", "Bruno", lang2, "#00FF00"}
        };
    }

    @Test
    void createInitialBoard_validacoesBasicas() {
        GameManager gm = new GameManager();

        assertFalse(gm.createInitialBoard(null, 10));
        assertFalse(gm.createInitialBoard(new String[][]{}, 10));

        // só 1 jogador -> false
        assertFalse(gm.createInitialBoard(new String[][]{
                {"1", "Ana", "Java", "#F00"}
        }, 10));

        // worldSize < 2*players -> false
        assertFalse(gm.createInitialBoard(twoPlayers("Java", "Java"), 3));

        // ok
        assertTrue(gm.createInitialBoard(twoPlayers("Java", "Java"), 10));
        assertEquals(1, gm.getCurrentPlayerID());
    }

    @Test
    void getProgrammerInfo_eGetProgrammersInfo_naoVazioQuandoCriado() {
        GameManager gm = new GameManager();
        assertTrue(gm.createInitialBoard(twoPlayers("Java;C", "Python"), 10));

        String[] info = gm.getProgrammerInfo(1);
        assertNotNull(info);
        assertEquals("1", info[0]);
        assertEquals("Ana", info[1]);
        assertEquals("1", info[4]); // posição inicial

        String all = gm.getProgrammersInfo();
        assertTrue(all.contains("Ana"));
        assertTrue(all.contains("Bruno"));
    }

    @Test
    void getSlotInfo_prioridadeAbismoSobreFerramenta() {
        GameManager gm = new GameManager();
        String[][] abyssesAndTools = new String[][]{
                {"0", "0", "3"}, // abismo id0 na casa 3
                {"1", "4", "3"}  // ferramenta id4 na mesma casa 3 (vai falhar porque duplicado)
        };

        // aqui deve falhar porque tenta meter ferramenta onde já existe ferramenta? o código só impede duplicados dentro do mesmo array
        // mas abismo e ferramenta podem coexistir? no teu código sim (arrays separados). Então create deve ser true.
        assertTrue(gm.createInitialBoard(twoPlayers("Java", "Java"), 10, abyssesAndTools));

        String[] slot = gm.getSlotInfo(3);
        assertNotNull(slot);
        assertEquals("Erro de sintaxe", slot[1]);
        assertEquals("A:0", slot[2]);
    }

    @Test
    void moveCurrentPlayer_regrasDadoELinguagens() {
        GameManager gm = new GameManager();
        assertTrue(gm.createInitialBoard(twoPlayers("Assembly", "Java"), 10));

        // assembly não pode >=3
        assertFalse(gm.moveCurrentPlayer(3));
        assertTrue(gm.moveCurrentPlayer(2));

        // troca para jogador 2 só quando reactTo... for chamado; moveCurrentPlayer não avança turno
        // por isso, ainda é o jogador 1
        assertEquals(1, gm.getCurrentPlayerID());
    }

    @Test
    void moveCurrentPlayer_ultrapassaFim_bounce_eMovimentoInvalido() {
        GameManager gm = new GameManager();
        assertTrue(gm.createInitialBoard(twoPlayers("Java", "Java"), 6));

        // jogador 1 na casa 1: move 6 iria para 7 (fora); destino = 6 - 1 = 5 (ver regra)
        boolean ok = gm.moveCurrentPlayer(6);
        assertFalse(ok);

        String[] p1 = gm.getProgrammerInfo(1);
        assertEquals("5", p1[4]);
    }

    @Test
    void reactToAbyssOrTool_apanhaFerramenta() {
        GameManager gm = new GameManager();

        String[][] cfg = new String[][]{
                {"1", "4", "3"} // IDE na casa 3
        };
        assertTrue(gm.createInitialBoard(twoPlayers("Java", "Java"), 10, cfg));

        assertTrue(gm.moveCurrentPlayer(2)); // 1->3
        String msg = gm.reactToAbyssOrTool();
        assertNotNull(msg);
        assertTrue(msg.contains("apanhou") || msg.contains("apanhou"));

        String[] info = gm.getProgrammerInfo(1);
        assertTrue(info[5].contains("IDE"));
    }



    @Test
    void reactToAbyssOrTool_cicloInfinito_trocaPreso() {
        GameManager gm = new GameManager();

        // abismo Ciclo Infinito (id8) na casa 3
        String[][] cfg = new String[][]{
                {"0", "8", "3"}
        };
        assertTrue(gm.createInitialBoard(twoPlayers("Java", "Java"), 10, cfg));

        // jogador 1 cai na casa 3 e fica preso
        assertTrue(gm.moveCurrentPlayer(2)); // 1->3
        String msg1 = gm.reactToAbyssOrTool();
        assertNotNull(msg1);
        assertEquals("Preso", gm.getProgrammerInfo(1)[6]);

        // agora é a vez do jogador 2: cai na mesma casa 3, liberta o 1 e fica preso ele
        assertTrue(gm.moveCurrentPlayer(2)); // 1->3
        String msg2 = gm.reactToAbyssOrTool();
        assertNotNull(msg2);

        assertEquals("Em Jogo", gm.getProgrammerInfo(1)[6]);
        assertEquals("Preso", gm.getProgrammerInfo(2)[6]);
    }

    @Test
    void reactToAbyssOrTool_segmentationFault_variosRecuam3() {
        GameManager gm = new GameManager();

        // Segmentation Fault (id9) na casa 4
        String[][] cfg = new String[][]{
                {"0", "9", "4"}
        };
        assertTrue(gm.createInitialBoard(twoPlayers("Java", "Java"), 10, cfg));

        // colocar os dois na mesma casa 4:
        assertTrue(gm.moveCurrentPlayer(3)); // p1 1->4
        gm.reactToAbyssOrTool(); // avança turno
        assertTrue(gm.moveCurrentPlayer(3)); // p2 1->4
        String msg = gm.reactToAbyssOrTool();

        assertNotNull(msg);
        assertTrue(msg.contains("Segmentation Fault"));

        // ambos recuam 3 => 4 -> 1
        assertEquals("1", gm.getProgrammerInfo(1)[4]);
        assertEquals("1", gm.getProgrammerInfo(2)[4]);
    }

    @Test
    void gameResults_formatoBasico() {
        GameManager gm = new GameManager();
        assertTrue(gm.createInitialBoard(twoPlayers("Java", "Java"), 5));

        // levar jogador 1 à vitória
        assertTrue(gm.moveCurrentPlayer(4)); // 1->5
        gm.reactToAbyssOrTool();

        assertTrue(gm.gameIsOver());

        ArrayList<String> res = gm.getGameResults();
        assertEquals("THE GREAT PROGRAMMING JOURNEY", res.get(0));
        assertTrue(res.contains("VENCEDOR"));
    }
}
