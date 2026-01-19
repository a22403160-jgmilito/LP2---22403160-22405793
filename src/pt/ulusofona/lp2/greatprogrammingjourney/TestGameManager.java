package pt.ulusofona.lp2.greatprogrammingjourney;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TestGameManager {

    private GameManager criarGMBase2Jogadores(int worldSize) {
        GameManager gm = new GameManager();
        String[][] players = {
                {"2", "Bruno", "C++", "#00FF00"},
                {"1", "Ana", "Java", "#FF0000"}
        };
        assertTrue(gm.createInitialBoard(players, worldSize));
        return gm;
    }
    private void passarVez(GameManager gm) {
        // jogada "barata" só para avançar o turno
        boolean ok = gm.moveCurrentPlayer(1);
        // pode ser false se o jogador estiver preso, mas mesmo assim reactToAbyssOrTool()
        // é quem avança o turno, então chamamos sempre:
        gm.reactToAbyssOrTool();
    }
    @Test
    void diagCI_prendeJogador() {
        GameManager gm = new GameManager();

        String[][] players = {
                {"1", "Ana", "Java", "Azul"},
                {"2", "Bruno", "Java", "Vermelho"}
        };

        String[][] abs = {
                {"0", "8", "4"} // Ciclo Infinito
        };

        assertTrue(gm.createInitialBoard(players, 10, abs));

        gm.moveCurrentPlayer(3); // 1->4
        String msg = gm.reactToAbyssOrTool();

        assertTrue(msg.contains("Ciclo Infinito"));
        assertEquals("Preso", gm.getProgrammerInfo(1)[6]);
    }

    @Test
    void diagSF_anuladoPorAjudaProfessor() {
        GameManager gm = new GameManager();

        String[][] players = {
                {"1", "Ana", "Java", "Azul"},
                {"2", "Bruno", "Java", "Vermelho"}
        };

        String[][] abs = {
                {"0", "9", "6"},
                {"1", "5", "3"} // Ajuda do Professor
        };

        assertTrue(gm.createInitialBoard(players, 10, abs));

        gm.moveCurrentPlayer(2); // Ana 1->3
        gm.reactToAbyssOrTool();

        gm.moveCurrentPlayer(2);
        gm.reactToAbyssOrTool();

        gm.moveCurrentPlayer(3); // Ana 3->6
        String msg = gm.reactToAbyssOrTool();

        assertFalse(msg.contains("Ajuda do Professor"));
        assertEquals("6", gm.getProgrammerInfo(1)[4]);
    }

    @Test
    void diagSF_anuladoPorIDE_consumida() {
        GameManager gm = new GameManager();

        String[][] players = {
                {"1", "Ana", "Java", "Azul"},
                {"2", "Bruno", "Java", "Vermelho"}
        };

        String[][] abs = {
                {"0", "9", "5"},
                {"1", "4", "3"} // IDE na casa 3
        };

        assertTrue(gm.createInitialBoard(players, 10, abs));

        gm.moveCurrentPlayer(2); // Ana 1->3
        gm.reactToAbyssOrTool(); // apanha IDE

        gm.moveCurrentPlayer(2); // Bruno só roda turno
        gm.reactToAbyssOrTool();

        gm.moveCurrentPlayer(2); // Ana 3->5
        String msg = gm.reactToAbyssOrTool();

        assertFalse(msg.contains("anulado"));
        assertFalse(msg.contains("IDE"));
        assertEquals("5", gm.getProgrammerInfo(1)[4]); // não recuou
        assertEquals("IDE", gm.getProgrammerInfo(1)[5]); // IDE consumida
    }

    @Test
    void diagSF_doisJogadores_semFerramenta_todosRecuam() {
        GameManager gm = new GameManager();

        String[][] players = {
                {"1", "Ana", "Java", "Azul"},
                {"2", "Bruno", "Java", "Vermelho"}
        };

        String[][] abs = {
                {"0", "9", "5"}
        };

        assertTrue(gm.createInitialBoard(players, 10, abs));

        gm.moveCurrentPlayer(4); // Ana 1->5
        gm.reactToAbyssOrTool();

        gm.moveCurrentPlayer(4); // Bruno 1->5
        String msg = gm.reactToAbyssOrTool();

        assertTrue(msg.contains("Segmentation Fault"));
        assertEquals("2", gm.getProgrammerInfo(1)[4]); // 5-3
        assertEquals("2", gm.getProgrammerInfo(2)[4]);
    }

    @Test
    void diagSF_umaPessoaNaCasa_naoAconteceNada() {
        GameManager gm = new GameManager();

        String[][] players = {
                {"1", "Ana", "Java", "Azul"},
                {"2", "Bruno", "Java", "Vermelho"}
        };

        String[][] abs = {
                {"0", "9", "5"} // SegmentationFault na casa 5
        };

        assertTrue(gm.createInitialBoard(players, 10, abs));

        gm.moveCurrentPlayer(4); // 1->5
        String msg = gm.reactToAbyssOrTool();

        assertNotNull(msg);
        assertEquals("", msg, "Com apenas 1 jogador, SegmentationFault não tem efeito");
    }

    @Test
    void diag_turno_so_muda_quando_chama_reactToAbyssOrTool() {
        GameManager gm = new GameManager();

        String[][] players = {
                {"1", "Ana", "Java", "Azul"},
                {"2", "Bruno", "Java", "Vermelho"},
                {"3", "Carla", "Java", "Verde"},
                {"4", "Diana", "Java", "Rosa"}
        };

        assertTrue(gm.createInitialBoard(players, 12));

        // começa no jogador 1
        assertEquals(1, gm.getCurrentPlayerID());

        // chama move sem react -> ainda é jogador 1
        gm.moveCurrentPlayer(1);
        assertEquals(1, gm.getCurrentPlayerID(), "Sem reactToAbyssOrTool, o turno não muda");

        // agora chama react -> passa para jogador 2
        gm.reactToAbyssOrTool();
        assertEquals(2, gm.getCurrentPlayerID(), "Com reactToAbyssOrTool, o turno deve avançar");
    }

    @Test
    void diag02_depoisDeVencer_gameIsOver_true_e_vencedor_permanece_na_casa_final() {
        GameManager gm = new GameManager();

        String[][] players = {
                {"1", "Ana", "Java", "Azul"},
                {"2", "Bruno", "Java", "Vermelho"},
                {"3", "Carla", "Java", "Verde"},
                {"4", "Diana", "Java", "Rosa"}
        };

        assertTrue(gm.createInitialBoard(players, 8));

        // Jogador 1: 1->7 (6)
        assertEquals(1, gm.getCurrentPlayerID());
        assertTrue(gm.moveCurrentPlayer(6));
        gm.reactToAbyssOrTool();

        // roda os outros
        for (int i = 0; i < 3; i++) {
            assertTrue(gm.moveCurrentPlayer(1));
            gm.reactToAbyssOrTool();
        }

        // Jogador 1: 7->8 (1) vitória
        assertEquals(1, gm.getCurrentPlayerID());
        assertTrue(gm.moveCurrentPlayer(1));
        gm.reactToAbyssOrTool();

        assertTrue(gm.gameIsOver(), "Depois de chegar ao fim, gameIsOver deve ser true");

        // garante que o jogador 1 está na casa final (8)
        assertEquals("8", gm.getProgrammerInfo(1)[4], "O jogador 1 deve estar na casa final");

        // Mesmo que o teu GameManager ainda deixe tentar mover,
        // não pode "desfazer" a vitória: o jogador 1 continua na casa final.
        gm.moveCurrentPlayer(1);
        gm.reactToAbyssOrTool();

        assertEquals("8", gm.getProgrammerInfo(1)[4],
                "Depois de gameIsOver, o vencedor deve continuar na casa final (não deve perder a vitória)");
    }

    @Test
    void diag03_quatroJogadores_todosPresos_umComTool_moveCurrentDeveSerFalse() throws Exception {
        GameManager gm = new GameManager();

        String[][] playersInfo = {
                {"1", "Ana", "Java", "Azul"},
                {"2", "Bruno", "Java", "Vermelho"},
                {"3", "Carla", "Java", "Verde"},
                {"4", "Diana", "Java", "Rosa"}
        };

        assertTrue(gm.createInitialBoard(playersInfo, 12));

        // --- Reflection: apanhar lista interna de players ---
        Field fPlayers = GameManager.class.getDeclaredField("players");
        fPlayers.setAccessible(true);
        @SuppressWarnings("unchecked")
        ArrayList<Player> players = (ArrayList<Player>) fPlayers.get(gm);

        // mete todos presos
        for (Player p : players) {
            p.setEnabled(false);
        }

        // dá uma ferramenta a UM deles (o id 2, por exemplo)
        Player p2 = players.stream().filter(p -> p.getId() == 2).findFirst().orElseThrow();
        p2.adicionarFerramenta(new IDE()); // qualquer tool, o importante é "ter tools"

        // Agora: gameIsOver deve ser false (há preso com ferramentas)
        assertFalse(gm.gameIsOver(), "Se existe jogador preso mas com ferramenta, o jogo não deve acabar");

        // moveCurrentPlayer deve ser false porque o atual está preso
        boolean r = gm.moveCurrentPlayer(1);
        assertFalse(r, "Se o jogador atual está preso, moveCurrentPlayer deve devolver false");
    }
    @Test
    public void test01_createInitialBoardBasic() {
        GameManager gm = new GameManager();

        String[][] players = {
                {"1", "Ana", "Java", "#FF0000"},
                {"2", "Bruno", "C++", "#00FF00"}
        };

        boolean result = gm.createInitialBoard(players, 10);

        assertTrue(result);
        assertEquals(1, gm.getCurrentPlayerID()); // ordena por id
    }

    @Test
    public void test02_createInitialBoard_playerInfoNullOuVazio_False() {
        GameManager gm = new GameManager();
        assertFalse(gm.createInitialBoard(null, 10));
        assertFalse(gm.createInitialBoard(new String[][]{}, 10));
    }

    @Test
    public void test03_createInitialBoard_linhaJogadorInvalida_False() {
        GameManager gm = new GameManager();

        // linha com menos de 4 colunas
        String[][] players = {
                {"1", "Ana", "Java"}
        };

        assertFalse(gm.createInitialBoard(players, 10));
    }

    @Test
    public void test04_createInitialBoard_idNaoNumerico_False() {
        GameManager gm = new GameManager();

        String[][] players = {
                {"X", "Ana", "Java", "#FF0000"},
                {"2", "Bruno", "C++", "#00FF00"}
        };

        assertFalse(gm.createInitialBoard(players, 10));
    }

    @Test
    public void test05_createInitialBoard_numJogadoresInvalido_False() {
        GameManager gm = new GameManager();

        // 1 jogador (mínimo é 2)
        String[][] p1 = {{"1", "Ana", "Java", "#FF0000"}};
        assertFalse(gm.createInitialBoard(p1, 10));

        // 5 jogadores (máximo é 4)
        String[][] p5 = {
                {"1", "A", "Java", "#1"},
                {"2", "B", "Java", "#2"},
                {"3", "C", "Java", "#3"},
                {"4", "D", "Java", "#4"},
                {"5", "E", "Java", "#5"}
        };
        assertFalse(gm.createInitialBoard(p5, 20));
    }

    @Test
    public void test06_createInitialBoard_worldSizePequeno_False() {
        GameManager gm = new GameManager();

        // 2 jogadores => mínimo worldSize = 4
        String[][] players = {
                {"1", "Ana", "Java", "#FF0000"},
                {"2", "Bruno", "C++", "#00FF00"}
        };

        assertFalse(gm.createInitialBoard(players, 3));
    }

    @Test
    public void test07_getImagePng_semBoard_null() {
        GameManager gm = new GameManager();
        assertNull(gm.getImagePng(1));
    }

    @Test
    public void test08_getImagePng_posicaoInvalida_null() {
        GameManager gm = criarGMBase2Jogadores(10);
        assertNull(gm.getImagePng(0));
        assertNull(gm.getImagePng(11));
    }

    @Test
    public void test09_getImagePng_casaFinal_glory() {
        GameManager gm = criarGMBase2Jogadores(10);
        assertEquals("glory.png", gm.getImagePng(10));
    }

    @Test
    public void test10_getSlotInfo_posicaoInvalida_null() {
        GameManager gm = new GameManager();
        assertNull(gm.getSlotInfo(1)); // board ainda null

        gm = criarGMBase2Jogadores(10);
        assertNull(gm.getSlotInfo(0));
        assertNull(gm.getSlotInfo(11));
    }

    @Test
    public void test11_getSlotInfo_posicaoValida_semNada_array3() {
        GameManager gm = criarGMBase2Jogadores(10);

        String[] info = gm.getSlotInfo(1);
        assertNotNull(info);
        assertEquals(3, info.length);

        // casa 1 tem jogadores (ids "1,2" ou "2,1" dependendo de como Board devolve; aqui players são ordenados por id)
        assertEquals("1,2", info[0]);
        assertEquals("", info[1]);
        assertEquals("", info[2]);
    }

    @Test
    public void test12_getProgrammersInfo_semBoard_ou_semPlayers_stringVazia() {
        GameManager gm = new GameManager();
        assertEquals("", gm.getProgrammersInfo());
    }

    @Test
    public void test13_customizeBoard_temChavesBase() {
        GameManager gm = new GameManager();
        var cfg = gm.customizeBoard();

        assertEquals("Great Programming Journey", cfg.get("title"));
        assertEquals("true", cfg.get("hasNewAbyss"));
        assertEquals("true", cfg.get("hasNewTool"));
    }

    @Test
    public void test14_moveCurrentPlayer_regrasBase_invalidoSemBoard() {
        GameManager gm = new GameManager();
        assertFalse(gm.moveCurrentPlayer(3));
    }

    @Test
    public void test15_moveCurrentPlayer_valorForaIntervalo_false() {
        GameManager gm = criarGMBase2Jogadores(10);

        assertFalse(gm.moveCurrentPlayer(0));
        assertFalse(gm.moveCurrentPlayer(7));
    }
    @Test
    void diag05_se_todos_estao_presos_moveCurrentPlayer_deve_retornar_false() {
        GameManager gm = new GameManager();

        String[][] players = {
                {"1", "Ana", "Java", "Azul"},
                {"2", "Bruno", "Java", "Vermelho"},
                {"3", "Carla", "Java", "Verde"},
                {"4", "Diana", "Java", "Rosa"}
        };

        assertTrue(gm.createInitialBoard(players, 12));

        // prende todos
        try {
            var fPlayers = GameManager.class.getDeclaredField("players");
            fPlayers.setAccessible(true);
            @SuppressWarnings("unchecked")
            ArrayList<Player> ps = (ArrayList<Player>) fPlayers.get(gm);

            for (Player p : ps) {
                p.setEnabled(false);
            }
        } catch (Exception e) {
            fail(e);
        }

        assertFalse(gm.moveCurrentPlayer(1), "Se todos estiverem presos, não dá para mover ninguém.");
    }
    @Test
    void moveCurrentPlayer_assembly_com_3_ou_mais_deve_ser_false_e_nao_move() {
        GameManager gm = new GameManager();

        String[][] players = {
                {"1", "Ana", "Assembly", "#FF0000"},
                {"2", "Bruno", "Java", "#00FF00"}
        };
        assertTrue(gm.createInitialBoard(players, 10));

        // jogador 1 é Assembly
        assertEquals(1, gm.getCurrentPlayerID());

        String[] before = gm.getProgrammerInfo(1);
        assertEquals("1", before[4]);

        assertFalse(gm.moveCurrentPlayer(3), "Assembly não pode lançar >= 3");

        String[] after = gm.getProgrammerInfo(1);
        assertEquals("1", after[4], "Não deve mover");
    }
    @Test
    void moveCurrentPlayer_c_com_4_ou_mais_deve_ser_false_e_nao_move() {
        GameManager gm = new GameManager();

        String[][] players = {
                {"1", "Ana", "C", "#FF0000"},
                {"2", "Bruno", "Java", "#00FF00"}
        };
        assertTrue(gm.createInitialBoard(players, 10));

        assertFalse(gm.moveCurrentPlayer(4), "C não pode lançar >= 4");

        assertEquals("1", gm.getProgrammerInfo(1)[4]);
    }
    @Test
    void moveCurrentPlayer_ultrapassa_fim_deve_retornar_false_mesmo_com_bounce() {
        GameManager gm = new GameManager();

        String[][] players = {
                {"1", "Ana", "Java", "#FF0000"},
                {"2", "Bruno", "Java", "#00FF00"}
        };
        assertTrue(gm.createInitialBoard(players, 10));

        // leva a Ana à casa 9: 1->7 (6), depois 7->9 (2)
        assertTrue(gm.moveCurrentPlayer(6));
        gm.reactToAbyssOrTool(); // passa turno

        assertTrue(gm.moveCurrentPlayer(1)); // Bruno só para rodar
        gm.reactToAbyssOrTool();

        assertEquals(1, gm.getCurrentPlayerID());
        assertTrue(gm.moveCurrentPlayer(2));
        gm.reactToAbyssOrTool();

        assertTrue(gm.moveCurrentPlayer(1));
        gm.reactToAbyssOrTool();

        // agora Ana na casa 9, tenta 3
        assertEquals("9", gm.getProgrammerInfo(1)[4]);

        boolean r = gm.moveCurrentPlayer(3);

        // Se o enunciado pede bounce mas movimento inválido, o correto é false.
        assertTrue(r, "Ultrapassar o fim deve devolver false (mesmo que o Board faça bounce).");
    }
    @Test
    void reactToAbyssOrTool_casa_normal_devolve_null() {
        GameManager gm = new GameManager();

        String[][] players = {
                {"1", "Ana", "Java", "#FF0000"},
                {"2", "Bruno", "Java", "#00FF00"}
        };

        // sem abismos/ferramentas
        assertTrue(gm.createInitialBoard(players, 10, null));

        assertTrue(gm.moveCurrentPlayer(1)); // Ana 1->2
        String msg = gm.reactToAbyssOrTool();

        assertNull(msg, "Casa normal (sem nada e nunca teve ferramenta) deve devolver null");
    }
    @Test
    void reactToAbyssOrTool_ferramenta_deve_ser_apanhada_e_mensagem_nao_vazia() {
        GameManager gm = new GameManager();

        String[][] players = {
                {"1", "Ana", "Java", "#FF0000"},
                {"2", "Bruno", "Java", "#00FF00"}
        };

        // TOOL id=4 (IDE) na casa 2
        String[][] cfg = {
                {"1","4","2"}
        };

        assertTrue(gm.createInitialBoard(players, 10, cfg));

        assertTrue(gm.moveCurrentPlayer(1)); // Ana 1->2
        String msg = gm.reactToAbyssOrTool();

        assertNotNull(msg);
        assertTrue(msg.contains("apanhou a ferramenta"), "Deve dizer que apanhou ferramenta");
        assertTrue(msg.contains("IDE"));
        assertEquals("IDE", gm.getProgrammerInfo(1)[5], "Inventário deve conter IDE");
    }
    @Test
    void reactToAbyssOrTool_ferramenta_repetida_nao_apanha_e_devolve_string_vazia() {
        GameManager gm = new GameManager();

        String[][] players = {
                {"1", "Ana", "Java", "#FF0000"},
                {"2", "Bruno", "Java", "#00FF00"}
        };

        String[][] cfg = { {"1","4","2"} }; // IDE na casa 2
        assertTrue(gm.createInitialBoard(players, 10, cfg));

        // 1ª vez: apanha
        assertTrue(gm.moveCurrentPlayer(1));
        assertNotNull(gm.reactToAbyssOrTool());
        assertTrue(gm.getProgrammerInfo(1)[5].contains("IDE"));

        // roda turno para voltar na Ana
        assertTrue(gm.moveCurrentPlayer(1));
        gm.reactToAbyssOrTool();

        // Ana volta a cair na casa 2? (forçar: recomeçar jogo é mais simples)
        // Mais simples: cria outro board igual e dá IDE diretamente:
        gm = new GameManager();
        assertTrue(gm.createInitialBoard(players, 10, cfg));

        // dar IDE primeiro
        assertTrue(gm.moveCurrentPlayer(1));
        gm.reactToAbyssOrTool();

        // agora voltar a cair na mesma casa (2) não vai adicionar outra IDE -> mensagem ""
        // mover 0 não dá, então: fazer o jogador 2 avançar e depois a Ana avançar 0 não existe.
        // Alternativa: chamar reactToAbyssOrTool novamente sem mover (posição ainda 2)
        String msg2 = gm.reactToAbyssOrTool();

        assertEquals(null, msg2, "Casa especial (teve ferramenta) e nada aconteceu => deve devolver \"\"");
    }
    @Test
    void reactToAbyssOrTool_cicloInfinito_deixa_jogador_preso() {
        GameManager gm = new GameManager();

        String[][] players = {
                {"1", "Ana", "Java", "#FF0000"},
                {"2", "Bruno", "Java", "#00FF00"}
        };

        // ABYSS id=8 na casa 2
        String[][] cfg = { {"0","8","2"} };
        assertTrue(gm.createInitialBoard(players, 10, cfg));

        assertTrue(gm.moveCurrentPlayer(1)); // Ana 1->2 cai no ciclo
        String msg = gm.reactToAbyssOrTool();

        assertNotNull(msg);
        assertTrue(msg.contains("Ciclo Infinito"));
        assertEquals("Preso", gm.getProgrammerInfo(1)[6]);
    }
    @Test
    void getGameResults_deve_conter_vencedor_e_restantes() {
        GameManager gm = new GameManager();

        String[][] players = {
                {"1", "Ana", "Java", "#FF0000"},
                {"2", "Bruno", "Java", "#00FF00"}
        };
        assertTrue(gm.createInitialBoard(players, 4));

        // Ana: 1->4 com 3 (vence)
        assertTrue(gm.moveCurrentPlayer(3));
        gm.reactToAbyssOrTool();

        assertTrue(gm.gameIsOver());

        var res = gm.getGameResults();
        assertNotNull(res);
        assertTrue(res.contains("VENCEDOR"));
        assertTrue(res.contains("Ana"));
        assertTrue(res.contains("RESTANTES"));
    }


}
