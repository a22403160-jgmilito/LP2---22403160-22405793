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

        // mover só o jogador 2 para a casa 3
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

        assertEquals(4, novaPos);              // começou em 1, avançou 3 -> 4
        assertEquals(4, b.getPlayerPosicao(1));
    }

    @Test
    public void test05_movePlayer_BounceBackAoUltrapassarFim_E_ClampAoRecuar() {
        Board b = new Board(10, List.of(new Player(1, "Ana", "Java", "Azul")));

        // colocar na casa 9 e avançar 3:
        // 9 -> 10 -> 9 (bounce) -> 8
        b.setPlayerPosicao(1, 9);
        int posDepois = b.movePlayer(1, 3);
        assertEquals(10, posDepois);

        // agora recuar mais do que possível: nunca abaixo de 1
        int posDepois2 = b.movePlayer(1, -20);
        assertEquals(1, posDepois2);
    }
}
