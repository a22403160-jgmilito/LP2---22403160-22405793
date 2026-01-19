package pt.ulusofona.lp2.greatprogrammingjourney;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestExceptionAbismo {

    @Test
    public void test01_IdDeveSer2() {
        Abismos ab = new ExceptionAbismo();
        assertEquals(2, ab.getId());
    }

    @Test
    public void test02_NomeDeveSerException() {
        Abismos ab = new ExceptionAbismo();
        assertEquals("Exception", ab.getNome());
    }

    @Test
    public void test03_AplicarEfeito_PosicaoMaiorOuIgualA3_Recuar2() {
        Player p = new Player(1, "Ana", "Java", "Azul");
        Board b = new Board(10, List.of(p));

        b.setPlayerPosicao(1, 7);

        Abismos ab = new ExceptionAbismo();
        String msg = ab.aplicarEfeito(p, b, 4);

        assertEquals(5, b.getPlayerPosicao(1), "Deve recuar 2 casas");
        assertNotNull(msg);
        assertTrue(msg.contains("Ana"));
        assertTrue(msg.contains("recuou 2 casas"));
        assertTrue(msg.contains("posição 5"));
    }

    @Test
    public void test04_AplicarEfeito_Posicao2_RecuoLimitadoPara1() {
        Player p = new Player(2, "Bruno", "Java", "Vermelho");
        Board b = new Board(10, List.of(p));

        b.setPlayerPosicao(2, 2);

        Abismos ab = new ExceptionAbismo();
        String msg = ab.aplicarEfeito(p, b, 6);

        assertEquals(1, b.getPlayerPosicao(2), "Na posição 2, deve recuar apenas 1 (até à casa 1)");
        assertNotNull(msg);
        assertTrue(msg.contains("Bruno"));
        assertTrue(msg.contains("Exception"));
        assertTrue(msg.contains("posição 1"));
    }


    @Test
    public void test05_AplicarEfeito_Posicao1_NaoMove_MensagemDePertoDoInicio() {
        Player p = new Player(3, "Carla", "Java", "Verde");
        Board b = new Board(10, List.of(p));

        b.setPlayerPosicao(3, 1);

        Abismos ab = new ExceptionAbismo();
        String msg = ab.aplicarEfeito(p, b, 1);

        assertEquals(1, b.getPlayerPosicao(3), "Na posição 1, não deve recuar");
        assertNotNull(msg);
        assertTrue(msg.contains("Carla"));
        assertTrue(msg.contains("muito perto do início"));
        assertTrue(msg.contains("posição 1"));
    }

    @Test
    public void test06_AplicarEfeito_ComNull_DeveRetornarStringVazia() {
        Abismos ab = new ExceptionAbismo();
        assertEquals("", ab.aplicarEfeito(null, null, 3));
    }
}
