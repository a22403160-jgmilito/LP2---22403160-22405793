package pt.ulusofona.lp2.greatprogrammingjourney;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    void construtor_forcaJogadoresNaCasa1() {
        Player p1 = new Player(1, "A", "Java", "#f00");
        Player p2 = new Player(2, "B", "Java", "#0f0");
        p1.setPosicao(5, 10);
        Board b = new Board(10, List.of(p1, p2));
        assertEquals(1, b.getPlayerPosicao(1));
        assertEquals(1, b.getPlayerPosicao(2));
    }


    @Test
    void movePlayer_clampNoInicioAoRecuar() {
        Player p1 = new Player(1, "A", "Java", "#f00");
        Board b = new Board(10, List.of(p1));

        b.movePlayer(1, -5);
        assertEquals(1, b.getPlayerPosicao(1));

        b.movePlayer(1, 3); // 4
        b.movePlayer(1, -10);
        assertEquals(1, b.getPlayerPosicao(1));
    }

    @Test
    void getJogadoresNaPosicao_listaIds() {
        Player p1 = new Player(1, "A", "Java", "#f00");
        Player p2 = new Player(2, "B", "Java", "#0f0");
        Board b = new Board(10, List.of(p1, p2));

        assertEquals(List.of(1, 2), b.getJogadoresNaPosicao(1));

        b.movePlayer(2, 2); // p2 vai para 3
        assertEquals(List.of(1), b.getJogadoresNaPosicao(1));
        assertEquals(List.of(2), b.getJogadoresNaPosicao(3));
    }

    @Test
    void getTodasPosicoes_mapaCompleto() {
        Player p1 = new Player(1, "A", "Java", "#f00");
        Player p2 = new Player(2, "B", "Java", "#0f0");
        Board b = new Board(10, List.of(p1, p2));

        b.movePlayer(2, 4); // p2 -> 5
        Map<Integer, Integer> m = b.getTodasPosicoes();
        assertEquals(1, m.get(1));
        assertEquals(5, m.get(2));
    }

    @Test
    void setPlayerPosicao_clampEntre1ESize() {
        Player p1 = new Player(1, "A", "Java", "#f00");
        Board b = new Board(10, List.of(p1));

        b.setPlayerPosicao(1, 0);
        assertEquals(1, b.getPlayerPosicao(1));

        b.setPlayerPosicao(1, 999);
        assertEquals(10, b.getPlayerPosicao(1));

        b.setPlayerPosicao(1, 7);
        assertEquals(7, b.getPlayerPosicao(1));
    }
}
