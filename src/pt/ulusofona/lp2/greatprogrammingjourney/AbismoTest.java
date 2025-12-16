package pt.ulusofona.lp2.greatprogrammingjourney;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AbismosTest {

    @Test
    void erroDeSintaxe_recuo1OuFicaNaCasa1() {
        Player p = new Player(1, "A", "Java", "#f00");
        Board b = new Board(10, List.of(p));
        b.movePlayer(1, 3); // 4

        String msg = new ErroDeSintaxe().aplicarEfeito(p, b, 2);
        assertEquals(3, p.getPosicao());
        assertTrue(msg.contains("recuou 1"));

        // já na casa 1 não recua
        b.setPlayerPosicao(1, 1);
        msg = new ErroDeSintaxe().aplicarEfeito(p, b, 2);
        assertEquals(1, p.getPosicao());
        assertTrue(msg.toLowerCase().contains("não recuou") || msg.toLowerCase().contains("nao recuou"));
    }

    @Test
    void erroDeLogica_dependeDoDado() {
        Player p = new Player(1, "A", "Java", "#f00");
        Board b = new Board(10, List.of(p));
        b.movePlayer(1, 6); // 7

        Abismos ab = new ErroDeLogica();

        String msgNulo = ab.aplicarEfeito(p, b, 2);
        assertEquals(7, b.getPlayerPosicao(1));
        assertTrue(msgNulo.toLowerCase().contains("nulo"));

        String msg1 = ab.aplicarEfeito(p, b, 3); // recua 1
        assertEquals(6, b.getPlayerPosicao(1));
        assertTrue(msg1.contains("recuou 1"));

        b.setPlayerPosicao(1, 7);
        String msg2 = ab.aplicarEfeito(p, b, 5); // recua 2
        assertEquals(5, b.getPlayerPosicao(1));
        assertTrue(msg2.contains("recuou 2"));
    }

    @Test
    void crash_voltaParaCasa1() {
        Player p = new Player(1, "A", "Java", "#f00");
        Board b = new Board(10, List.of(p));
        b.movePlayer(1, 5); // 6

        String msg = new Crash().aplicarEfeito(p, b, 6);
        assertEquals(1, p.getPosicao());
        assertTrue(msg.contains("voltou"));
    }

    @Test
    void blueScreen_derrotaJogador() {
        Player p = new Player(1, "A", "Java", "#f00");
        Board b = new Board(10, List.of(p));

        String msg = new BlueScreenOfDeath().aplicarEfeito(p, b, 1);
        assertFalse(p.isAlive());
        assertFalse(p.isEnabled());
        assertTrue(msg.toLowerCase().contains("derrotado"));
    }

    @Test
    void cicloInfinito_presoNoAbismo() {
        Player p = new Player(1, "A", "Java", "#f00");
        Board b = new Board(10, List.of(p));
        b.movePlayer(1, 4); // 5

        String msg = new CicloInfinito().aplicarEfeito(p, b, 4);
        assertFalse(p.isEnabled());
        assertTrue(msg.toLowerCase().contains("preso"));
    }

    @Test
    void codigoDuplicado_voltaParaCasaAnterior() {
        Player p = new Player(1, "A", "Java", "#f00");
        Board b = new Board(10, List.of(p));

        b.movePlayer(1, 2); // 3
        b.movePlayer(1, 2); // 5

        String msg = new CodigoDuplicado().aplicarEfeito(p, b, 2);
        assertEquals(3, b.getPlayerPosicao(1));
        assertTrue(msg.toLowerCase().contains("casa anterior"));
    }

}
