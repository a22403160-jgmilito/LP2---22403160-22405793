package pt.ulusofona.lp2.greatprogrammingjourney;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestErroDeLogica {

    @Test
    public void test01_IdDeveSer1() {
        Abismos ab = new ErroDeLogica();
        assertEquals(1, ab.getId());
    }

    @Test
    public void test02_NomeDeveSerExato() {
        Abismos ab = new ErroDeLogica();
        assertEquals("Erro de Lógica", ab.getNome());
    }

    @Test
    public void test03_ValorDadoNaoEspecial_ImpactoNulo_NaoMove() {
        Player p = new Player(1, "Ana", "Java", "Azul");
        Board b = new Board(10, List.of(p));

        b.setPlayerPosicao(1, 6);

        Abismos ab = new ErroDeLogica();
        String msg = ab.aplicarEfeito(p, b, 2); // não é 3/5/6 -> recuo = 0

        assertEquals(6, b.getPlayerPosicao(1), "Não deve mover quando impacto é nulo");
        assertNotNull(msg);
        assertTrue(msg.contains("Ana"));
        assertTrue(msg.contains("impacto foi nulo"));
    }

    @Test
    public void test04_ValorDado3_Recuo1() {
        Player p = new Player(2, "Bruno", "Java", "Vermelho");
        Board b = new Board(10, List.of(p));

        b.setPlayerPosicao(2, 6);

        Abismos ab = new ErroDeLogica();
        String msg = ab.aplicarEfeito(p, b, 3); // recuo = 1

        assertEquals(5, b.getPlayerPosicao(2), "Com dado=3 deve recuar 1");
        assertNotNull(msg);
        assertTrue(msg.contains("Bruno"));
        assertTrue(msg.contains("recuou 1"));
        assertTrue(msg.contains("posição 5"));
    }

    @Test
    public void test05_ValorDado5_Recuo2() {
        Player p = new Player(3, "Carla", "Java", "Verde");
        Board b = new Board(10, List.of(p));

        b.setPlayerPosicao(3, 6);

        Abismos ab = new ErroDeLogica();
        String msg = ab.aplicarEfeito(p, b, 5); // recuo = 2

        assertEquals(4, b.getPlayerPosicao(3), "Com dado=5 deve recuar 2");
        assertNotNull(msg);
        assertTrue(msg.contains("Carla"));
        assertTrue(msg.contains("recuou 2"));
        assertTrue(msg.contains("posição 4"));
    }

    @Test
    public void test06_ValorDado6_Recuo3() {
        Player p = new Player(4, "Diana", "Java", "Rosa");
        Board b = new Board(10, List.of(p));

        b.setPlayerPosicao(4, 6);

        Abismos ab = new ErroDeLogica();
        String msg = ab.aplicarEfeito(p, b, 6); // recuo = 3

        assertEquals(3, b.getPlayerPosicao(4), "Com dado=6 deve recuar 3");
        assertNotNull(msg);
        assertTrue(msg.contains("Diana"));
        assertTrue(msg.contains("recuou 3"));
        assertTrue(msg.contains("posição 3"));
    }
}
