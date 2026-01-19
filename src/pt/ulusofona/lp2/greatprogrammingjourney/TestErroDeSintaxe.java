package pt.ulusofona.lp2.greatprogrammingjourney;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestErroDeSintaxe {

    @Test
    public void test01_IdDeveSer0() {
        Abismos ab = new ErroDeSintaxe();
        assertEquals(0, ab.getId());
    }

    @Test
    public void test02_NomeDeveSerExato() {
        Abismos ab = new ErroDeSintaxe();
        assertEquals("Erro de sintaxe", ab.getNome());
    }

    @Test
    public void test03_AplicarEfeito_PosicaoMaiorQue1_Recuar1() {
        Player p = new Player(1, "Ana", "Java", "Azul");
        Board b = new Board(10, List.of(p));

        b.setPlayerPosicao(1, 6);

        Abismos ab = new ErroDeSintaxe();
        String msg = ab.aplicarEfeito(p, b, 4);

        assertEquals(5, b.getPlayerPosicao(1), "Deve recuar 1 casa");
        assertNotNull(msg);
        assertTrue(msg.contains("Ana"));
        assertTrue(msg.contains("recuou 1"));
        assertTrue(msg.contains("posição 5"));
    }

    @Test
    public void test04_AplicarEfeito_SeJaNaCasa1_NaoRecuar() {
        Player p = new Player(2, "Bruno", "Java", "Vermelho");
        Board b = new Board(10, List.of(p));

        b.setPlayerPosicao(2, 1);

        Abismos ab = new ErroDeSintaxe();
        String msg = ab.aplicarEfeito(p, b, 6);

        assertEquals(1, b.getPlayerPosicao(2), "Se já está na casa 1, não deve recuar");
        assertNotNull(msg);
        assertTrue(msg.contains("Bruno"));
        assertTrue(msg.contains("primeira casa"));
        assertTrue(msg.contains("não recuou"));
    }

    @Test
    public void test05_AplicarEfeito_ComNull_DeveRetornarStringVazia() {
        Abismos ab = new ErroDeSintaxe();
        assertEquals("", ab.aplicarEfeito(null, null, 3));
    }
}
