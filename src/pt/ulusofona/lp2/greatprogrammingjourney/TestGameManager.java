package pt.ulusofona.lp2.greatprogrammingjourney;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestGameManager {

    private String[][] jogadoresBasicos() {
        // id, nome, linguagens, cor
        return new String[][]{
                {"1", "Ana", "Java; C", "Purple"},
                {"2", "Bruno", "", "Green"}
        };
    }

    @Test
    void testCreateInitialBoard_eGetImagePng() {
        GameManager gm = new GameManager();

        // Criação válida
        boolean ok = gm.createInitialBoard(jogadoresBasicos(), 10);
        assertTrue(ok, "Tabuleiro inicial deveria ser criado com sucesso.");

        // A última casa deve ter imagem glory.png
        assertEquals("glory.png", gm.getImagePng(10), "A casa final deve devolver glory.png.");
        // Fora do tabuleiro → null
        assertNull(gm.getImagePng(0), "Casa fora do intervalo deve devolver null.");
        assertNull(gm.getImagePng(11), "Casa fora do intervalo deve devolver null.");
        // Casa intermédia → null
        assertNull(gm.getImagePng(5), "Casas não-finais não devem ter imagem.");
    }

    @Test
    void testGetProgrammerInfo_SortLinguagens_eAsStrSemLinguagens() {
        GameManager gm = new GameManager();
        assertTrue(gm.createInitialBoard(jogadoresBasicos(), 8));

        // Linguagens devem vir ordenadas alfabeticamente e separadas por "; "
        String[] infoAna = gm.getProgrammerInfo(1);
        assertNotNull(infoAna, "Info da Ana não deveria ser null.");
        assertEquals("C; Java", infoAna[2], "Linguagens devem estar ordenadas alfabeticamente.");

        // Bruno sem linguagens → "Sem linguagens" em getProgrammerInfoAsStr
        String linhaBruno = gm.getProgrammerInfoAsStr(2);
        assertNotNull(linhaBruno);
        // Posição inicial é 0 (o GameManager coloca 0 no mapa de posições)
        assertTrue(linhaBruno.contains("2 | Bruno | 0"), "Formato com posição inicial 0 não encontrado.");
        assertTrue(linhaBruno.endsWith(" | Em Jogo"), "Estado 'Em Jogo' esperado.");
        assertTrue(linhaBruno.contains("Sem linguagens"), "Sem linguagens deveria aparecer quando vazio.");
    }

    @Test
    void testMoveCurrentPlayer_Ricochete_eGetSlotInfo() {
        GameManager gm = new GameManager();
        assertTrue(gm.createInitialBoard(jogadoresBasicos(), 5));

        // Jogadora Ana (id=1) começa
        assertEquals(1, gm.getCurrentPlayerID(), "Ana deveria começar.");

        // Mover 6 numa grelha de 5 → ricochete: 0+6 > 5 ⇒ 5 - (6-5) = 4
        assertTrue(gm.moveCurrentPlayer(6), "Movimento deveria ser válido.");
        String[] slot4 = gm.getSlotInfo(4);
        assertNotNull(slot4, "Deveria existir alguém na casa 4 após o ricochete.");
        assertEquals("1", slot4[0], "A Ana (id=1) deveria estar na casa 4.");

        // Jogador atual deve rodar para o Bruno (porque não terminou o jogo)
        assertEquals(2, gm.getCurrentPlayerID(), "Após movimento da Ana, deveria ser a vez do Bruno.");
    }

    @Test
    void testGameOver_eGameResults_ComVencedor() {
        GameManager gm = new GameManager();
        assertTrue(gm.createInitialBoard(jogadoresBasicos(), 5));

        // Estado inicial: turno começa em 1 (conforme implementação)
        // 1) Ana move 6 → vai para 4 (ricochete) | totalTurns=2
        assertTrue(gm.moveCurrentPlayer(6));
        // 2) Bruno move 1 → vai para 1 | totalTurns=3
        assertTrue(gm.moveCurrentPlayer(1));
        // 3) Ana move 1 → 4 -> 5 (meta) | totalTurns=4 | vencedor=Ana
        assertTrue(gm.moveCurrentPlayer(1));

        assertTrue(gm.gameIsOver(), "O jogo deveria estar terminado quando alguém atinge a meta.");

        // Validar estrutura e conteúdos de getGameResults
        var res = gm.getGameResults();
        assertNotNull(res);
        assertFalse(res.isEmpty());

        // Estrutura esperada (linhas chave):
        // 0: "THE GREAT PROGRAMMING JOURNEY"
        // 2: "NR. DE TURNOS"
        // 3: totalTurns (esperado "4")
        // 5: "VENCEDOR"
        // 6: nome vencedor ("Ana")
        // 8: "RESTANTES"
        // 9+: "Nome pos"

        assertEquals("THE GREAT PROGRAMMING JOURNEY", res.get(0));
        assertEquals("NR. DE TURNOS", res.get(2));
        assertEquals("4", res.get(3), "Total de turnos deveria ser 4.");

        assertEquals("VENCEDOR", res.get(5));
        assertEquals("Ana", res.get(6), "A Ana deveria constar como vencedora.");

        assertEquals("RESTANTES", res.get(8));
        // Apenas o Bruno resta e está na posição 1
        assertTrue(res.get(9).startsWith("Bruno "), "Linha dos restantes deveria começar por 'Bruno '.");
        assertTrue(res.get(9).endsWith("1"), "Bruno deveria estar na posição 1.");
    }
}
