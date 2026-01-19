package pt.ulusofona.lp2.greatprogrammingjourney;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TestBoard {

    @Test
    public void test01_Construtor_DeveColocarTodosNaCasa1() {
        List<Player> players = new ArrayList<>();
        players.add(new Player(1, "Ana", "Java", "Azul"));
        players.add(new Player(2, "Bruno", "C", "Vermelho"));

        Board b = new Board(10, players);

        assertEquals(1, b.getPlayerPosicao(1));
        assertEquals(1, b.getPlayerPosicao(2));
    }

    @Test
    public void test02_PosicaoValida_E_PosicaoVitoria() {
        Board b = new Board(5, List.of(new Player(1, "Ana", "Java", "Azul")));

        assertFalse(b.posicaoValida(0));
        assertTrue(b.posicaoValida(1));
        assertTrue(b.posicaoValida(5));
        assertFalse(b.posicaoValida(6));

        assertFalse(b.posicaoVitoria(4));
        assertTrue(b.posicaoVitoria(5));
    }

    @Test
    public void test03_getJogadoresNaPosicao_DeveRetornarIdsCorretos() {
        List<Player> players = new ArrayList<>();
        players.add(new Player(1, "Ana", "Java", "Azul"));
        players.add(new Player(2, "Bruno", "C", "Vermelho"));

        Board b = new Board(10, players);

        b.setPlayerPosicao(2, 3);

        List<Integer> casa1 = b.getJogadoresNaPosicao(1);
        List<Integer> casa3 = b.getJogadoresNaPosicao(3);

        assertEquals(List.of(1), casa1);
        assertEquals(List.of(2), casa3);
    }

    @Test
    public void test04_movePlayer_AvancoNormal() {
        Board b = new Board(10, List.of(new Player(1, "Ana", "Java", "Azul")));

        int novaPos = b.movePlayer(1, 3);

        assertEquals(4, novaPos);
        assertEquals(4, b.getPlayerPosicao(1));
    }

    // ✅ Corrigido: bounce back dá 8, não 10
    @Test
    public void test05_movePlayer_BounceBackAoUltrapassarFim_E_ClampAoRecuar() {
        Board b = new Board(10, List.of(new Player(1, "Ana", "Java", "Azul")));

        b.setPlayerPosicao(1, 9);
        int posDepois = b.movePlayer(1, 3);
        assertEquals(8, posDepois);

        int posDepois2 = b.movePlayer(1, -20);
        assertEquals(1, posDepois2);
    }

    // ----------------------
    // NOVOS TESTES (para cobertura)
    // ----------------------

    @Test
    public void test06_Construtor_SizeInvalido_DeveLancarExcecao() {
        List<Player> players = List.of(new Player(1, "Ana", "Java", "Azul"));
        assertThrows(IllegalArgumentException.class, () -> new Board(0, players));
        assertThrows(IllegalArgumentException.class, () -> new Board(-5, players));
    }

    @Test
    public void test07_getPlayerPosicao_IdInexistente_DeveRetornar1() {
        Board b = new Board(10, List.of(new Player(1, "Ana", "Java", "Azul")));
        assertEquals(1, b.getPlayerPosicao(999));
    }

    @Test
    public void test08_getJogadoresNaPosicao_PosicaoInvalida_DeveRetornarListaVazia() {
        Board b = new Board(10, List.of(new Player(1, "Ana", "Java", "Azul")));

        assertNotNull(b.getJogadoresNaPosicao(0));
        assertTrue(b.getJogadoresNaPosicao(0).isEmpty());

        assertTrue(b.getJogadoresNaPosicao(11).isEmpty());
    }

    @Test
    public void test09_getJogadoresNaPosicao_MesmaCasa_DeveRetornarDoisIds() {
        List<Player> players = new ArrayList<>();
        players.add(new Player(1, "Ana", "Java", "Azul"));
        players.add(new Player(2, "Bruno", "C", "Vermelho"));
        Board b = new Board(10, players);

        // os dois começam na casa 1
        List<Integer> casa1 = b.getJogadoresNaPosicao(1);

        // a ordem pode depender da lista original, então testa por conteúdo
        assertEquals(2, casa1.size());
        assertTrue(casa1.contains(1));
        assertTrue(casa1.contains(2));
    }

    @Test
    public void test10_movePlayer_JogadorInexistente_DeveRetornarMenos1() {
        Board b = new Board(10, List.of(new Player(1, "Ana", "Java", "Azul")));
        assertEquals(-1, b.movePlayer(999, 3));
    }

    @Test
    public void test11_movePlayer_ZeroNaoMexe() {
        Board b = new Board(10, List.of(new Player(1, "Ana", "Java", "Azul")));

        b.setPlayerPosicao(1, 5);
        int pos = b.movePlayer(1, 0);

        assertEquals(5, pos);
        assertEquals(5, b.getPlayerPosicao(1));
    }

    @Test
    public void test12_movePlayer_AvancoExatoAteAoFim_SemBounce() {
        Board b = new Board(10, List.of(new Player(1, "Ana", "Java", "Azul")));

        b.setPlayerPosicao(1, 7);
        int pos = b.movePlayer(1, 3);

        assertEquals(10, pos);
        assertTrue(b.temJogadorNaPosicaoFinal());
    }

    @Test
    public void test13_temJogadorNaPosicaoFinal_QuandoNaoHaNinguem() {
        Board b = new Board(10, List.of(new Player(1, "Ana", "Java", "Azul")));
        assertFalse(b.temJogadorNaPosicaoFinal());
    }

    @Test
    public void test14_getTodasPosicoes_DeveConterTodosOsIds() {
        List<Player> players = new ArrayList<>();
        players.add(new Player(1, "Ana", "Java", "Azul"));
        players.add(new Player(2, "Bruno", "C", "Vermelho"));
        Board b = new Board(10, players);

        b.setPlayerPosicao(2, 4);

        Map<Integer, Integer> mapa = b.getTodasPosicoes();

        assertEquals(2, mapa.size());
        assertEquals(1, mapa.get(1));
        assertEquals(4, mapa.get(2));
    }

    @Test
    public void test15_setPlayerPosicao_IdInexistente_NaoFazNada() {
        // aqui testamos indiretamente: nada muda para o jogador existente
        Board b = new Board(10, List.of(new Player(1, "Ana", "Java", "Azul")));

        b.setPlayerPosicao(999, 8);
        assertEquals(1, b.getPlayerPosicao(1));
    }

    @Test
    public void test16_setPlayerPosicao_ClampInferiorESuperior() {
        Board b = new Board(10, List.of(new Player(1, "Ana", "Java", "Azul")));

        b.setPlayerPosicao(1, -100);
        assertEquals(1, b.getPlayerPosicao(1));

        b.setPlayerPosicao(1, 999);
        assertEquals(10, b.getPlayerPosicao(1));
    }
}
