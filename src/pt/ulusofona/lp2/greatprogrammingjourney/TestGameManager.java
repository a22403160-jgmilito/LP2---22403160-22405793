package pt.ulusofona.lp2.greatprogrammingjourney;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestGameManager {

    // id, nome, linguagens, cor
    private String[][] jogadoresBasicos() {
        return new String[][]{
                {"1", "Ana", "Java; C", "Purple"},
                {"2", "Bruno", "", "Green"}
        };
    }

    // id=3 serve só para ter outro jogador antes do Jazz (id=4)
    private String[][] jogadoresComJazz() {
        return new String[][]{
                {"3", "Alice", "C#", "Brown"},
                {"4", "Jazz Jack-a-Rabbit", "Kotlin", "Blue"}
        };
    }

    @Test
    void testCreateInitialBoard_eImagemFinal_1Based() {
        GameManager gm = new GameManager();

        assertTrue(gm.createInitialBoard(jogadoresBasicos(), 10), "Criação deve ser válida.");
        // Posições válidas: 1..10 (meta=10)
        assertEquals("glory.png", gm.getImagePng(10), "Meta deve devolver glory.png.");
        assertNull(gm.getImagePng(0), "0 é fora (1-based).");
        assertNull(gm.getImagePng(11), "11 é fora (tabuleiro=10).");
        assertNull(gm.getImagePng(5), "Casa intermédia não tem imagem.");
    }

    @Test
    void testGetProgrammerInfoAsStr_1Based_SemLinguagens() {
        GameManager gm = new GameManager();
        assertTrue(gm.createInitialBoard(jogadoresBasicos(), 8));

        // Bruno sem linguagens → "Sem linguagens" e posição inicial 1
        String linhaBruno = gm.getProgrammerInfoAsStr(2);
        assertNotNull(linhaBruno);
        assertTrue(linhaBruno.contains("2 | Bruno | 1"), "Posição inicial (1-based) deve ser 1.");
        assertTrue(linhaBruno.endsWith(" | Em Jogo"), "Estado 'Em Jogo' esperado.");
        assertTrue(linhaBruno.contains("Sem linguagens"), "Deveria constar 'Sem linguagens'.");
    }

    @Test
    void testRicochete_1Based_metaBoardSize() {
        GameManager gm = new GameManager();
        assertTrue(gm.createInitialBoard(jogadoresBasicos(), 6)); // casas 1..6 (meta=6)

        // Ana começa na 1. Mover 6: 1+6=7 > 6 → excedente=1 → nova=6-1=5
        assertEquals(1, gm.getCurrentPlayerID(), "Ana deveria começar.");
        assertTrue(gm.moveCurrentPlayer(6), "Movimento válido.");
        String[] slot5 = gm.getSlotInfo(5);
        assertNotNull(slot5, "Após ricochete, alguém deve estar na casa 5.");
        assertEquals("1", slot5[0], "Ana (id=1) deve estar na casa 5.");

        // Não terminou (não está exatamente em 6). Vez deve passar para Bruno.
        assertEquals(2, gm.getCurrentPlayerID(), "Deveria ser a vez do Bruno.");
        assertFalse(gm.gameIsOver(), "Jogo só termina quando alguém está exatamente na meta.");
    }

    @Test
    void testJazzJackRabbit_PosicaoEsperada5_eFormatoLinha() {
        GameManager gm = new GameManager();
        // Duas pessoas: Alice (id=3) e Jazz (id=4). Tabuleiro 10 (meta=10).
        assertTrue(gm.createInitialBoard(jogadoresComJazz(), 10));

        // Alice começa na 1. Queremos pôr Jazz na 5 para reproduzir o caso do teste do professor.
        // Passa a vez: Alice move 1 (vai 1->2)
        assertEquals(3, gm.getCurrentPlayerID(), "Alice deveria começar.");
        assertTrue(gm.moveCurrentPlayer(1), "Alice move 1.");

        // Agora é a vez do Jazz. Move 4 (vai 1->5)
        assertEquals(4, gm.getCurrentPlayerID(), "Agora é a vez do Jazz.");
        assertTrue(gm.moveCurrentPlayer(4), "Jazz move 4 e deve ir para a casa 5.");

        // Linha do Jazz exatamente como o professor espera (posição 5, linguagens mostradas)
        String linhaJazz = gm.getProgrammerInfoAsStr(4);
        assertEquals("4 | Jazz Jack-a-Rabbit | 5 | Kotlin | Em Jogo", linhaJazz,
                "Formato/posição do Jazz não coincide com o esperado (5).");
    }

    @Test
    void testGameOver_ApenasQuandoIgualBoardSize() {
        GameManager gm = new GameManager();
        assertTrue(gm.createInitialBoard(jogadoresBasicos(), 5)); // meta=5

        // Ana: 1->5 (move 4) termina jogo
        assertTrue(gm.moveCurrentPlayer(4), "1->5 (meta).");
        assertTrue(gm.gameIsOver(), "Jogo deve terminar quando alguém atinge exatamente a meta.");
    }

    @Test
    void testTurnoNaoAvancaQuandoChegaNaMeta() {
        GameManager gm = new GameManager();
        assertTrue(gm.createInitialBoard(jogadoresBasicos(), 5)); // meta = 5

        // Ana começa na 1 e vai direto para a meta (move 4)
        assertEquals(1, gm.getCurrentPlayerID(), "Ana deveria começar.");
        assertTrue(gm.moveCurrentPlayer(4), "Movimento até a meta deve ser válido.");

        // Ao atingir exatamente a meta, NÃO deve passar a vez
        assertEquals(1, gm.getCurrentPlayerID(),
                "Após vencer, não deve passar a vez para o Bruno.");
        assertTrue(gm.gameIsOver(), "Jogo deve terminar ao atingir exatamente a meta.");
    }

    @Test
    void testRicocheteMultiplo_1Based() {
        GameManager gm = new GameManager();
        assertTrue(gm.createInitialBoard(jogadoresBasicos(), 6)); // casas 1..6 (meta=6)

        // Ana na 1, move 8: 1+8=9 > 6 → excedente=3 → 6-3=3
        assertEquals(1, gm.getCurrentPlayerID());
        assertTrue(gm.moveCurrentPlayer(8), "Ricochete deve calcular posição válida.");
        String[] slot3 = gm.getSlotInfo(3);
        assertNotNull(slot3, "Deveria haver alguém na casa 3.");
        assertEquals("1", slot3[0], "Ana (id=1) deve estar na casa 3.");
    }

    @Test
    void testSlotInfoVariosJogadoresMesmaCasa_SemEspacos() {
        GameManager gm = new GameManager();
        assertTrue(gm.createInitialBoard(jogadoresBasicos(), 10));

        // Colocar os dois na casa 3: Ana 1->3 (move 2), Bruno 1->3 (move 2)
        assertTrue(gm.moveCurrentPlayer(2)); // Ana: 1->3
        assertTrue(gm.moveCurrentPlayer(2)); // Bruno: 1->3

        String[] slot3 = gm.getSlotInfo(3);
        assertNotNull(slot3);
        // Deve vir "1,2" sem espaços (ordem pode variar, aceitamos ambas)
        assertTrue(slot3[0].equals("1,2") || slot3[0].equals("2,1"),
                "IDs devem estar separados por vírgula sem espaços.");
    }

    @Test
    void testMoveInvalidoNaoAlteraEstado() {
        GameManager gm = new GameManager();
        assertTrue(gm.createInitialBoard(jogadoresBasicos(), 7));
        assertEquals(1, gm.getCurrentPlayerID());

        // Tentar mover 0 e -1 → false e não muda estado
        assertFalse(gm.moveCurrentPlayer(0), "Mover 0 não é válido.");
        assertEquals(1, gm.getCurrentPlayerID(), "Vez não deve mudar.");
        assertFalse(gm.moveCurrentPlayer(-1), "Mover negativo não é válido.");
        assertEquals(1, gm.getCurrentPlayerID(), "Vez não deve mudar após movimento inválido.");

        // Um movimento válido confirma que tudo continua consistente
        assertTrue(gm.moveCurrentPlayer(1));
        assertEquals(2, gm.getCurrentPlayerID(), "Agora sim, passa a vez.");
    }

    @Test
    void testGameResults_OrdenacaoRestantesPorPosDescDepoisNomeAsc() {
        GameManager gm = new GameManager();
        String[][] jogadores = new String[][]{
                {"1", "Zeca", "", "Purple"},
                {"2", "Ana", "", "Green"},
                {"3", "Bruno", "", "Brown"}
        };
        assertTrue(gm.createInitialBoard(jogadores, 6)); // meta=6

        // Estado desejado antes de terminar:
        // Zeca na 5, Ana na 3, Bruno na 3 → empate de posição entre Ana/Bruno
        // Turnos: Zeca(1), Ana(2), Bruno(3)
        assertTrue(gm.moveCurrentPlayer(4)); // Zeca: 1->5
        assertTrue(gm.moveCurrentPlayer(2)); // Ana: 1->3
        assertTrue(gm.moveCurrentPlayer(2)); // Bruno: 1->3

        // Agora Zeca vence: 5->6
        assertTrue(gm.moveCurrentPlayer(1));
        assertTrue(gm.gameIsOver());

        var res = gm.getGameResults();
        assertEquals("VENCEDOR", res.get(5));
        assertEquals("Zeca", res.get(6));

        // Restantes ordenados por pos DESC (3 e 3) e depois nome ASC: "Ana 3" vem antes de "Bruno 3"
        assertEquals("RESTANTES", res.get(8));
        assertTrue(res.get(9).startsWith("Ana ") && res.get(9).endsWith("3"),
                "Primeiro restante deve ser 'Ana 3'.");
        assertTrue(res.get(10).startsWith("Bruno ") && res.get(10).endsWith("3"),
                "Segundo restante deve ser 'Bruno 3'.");
    }

    @Test
    void testCreateInitialBoardValidacoesIdsECores() {
        GameManager gm = new GameManager();

        // IDs duplicados
        String[][] dupIds = new String[][]{
                {"1", "Ana", "", "Purple"},
                {"1", "Bruno", "", "Green"}
        };
        assertFalse(gm.createInitialBoard(dupIds, 10), "IDs duplicados devem falhar.");

        // Cores repetidas
        String[][] dupCores = new String[][]{
                {"1", "Ana", "", "Purple"},
                {"2", "Bruno", "", "Purple"}
        };
        assertFalse(gm.createInitialBoard(dupCores, 10), "Cores repetidas devem falhar.");

        // Nº de jogadores fora do permitido (1)
        String[][] umJogador = new String[][]{
                {"1", "Solo", "", "Purple"}
        };
        assertFalse(gm.createInitialBoard(umJogador, 10), "Mínimo são 2 jogadores.");

        // worldSize insuficiente (regra players*2)
        String[][] dois = new String[][]{
                {"1", "Ana", "", "Purple"},
                {"2", "Bruno", "", "Green"}
        };
        assertFalse(gm.createInitialBoard(dois, 3), "worldSize < players*2 deve falhar.");
    }
}
