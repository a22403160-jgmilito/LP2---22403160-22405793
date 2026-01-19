package pt.ulusofona.lp2.greatprogrammingjourney;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestFileNotFoundException {

    @Test
    public void test01_IdDeveSer3() {
        Abismos ab = new FileNotFoundException();
        assertEquals(3, ab.getId());
    }

    @Test
    public void test02_NomeDeveSerExato() {
        Abismos ab = new FileNotFoundException();
        assertEquals("FileNotFoundException", ab.getNome());
    }

    @Test
    public void test03_AplicarEfeito_Recuo3_QuandoHaEspaco() {
        Player p = new Player(1, "Ana", "Java", "Azul");
        Board b = new Board(10, List.of(p));

        b.setPlayerPosicao(1, 8); // 8 -> 5 (recuo 3)

        Abismos ab = new FileNotFoundException();
        String msg = ab.aplicarEfeito(p, b, 6);

        assertEquals(5, b.getPlayerPosicao(1), "Deve recuar 3 casas (8 -> 5)");
        assertNotNull(msg);
        assertTrue(msg.contains("Ana"));
        assertTrue(msg.contains("FileNotFoundException"));
        assertTrue(msg.contains("posição 5"));
    }

    @Test
    public void test04_AplicarEfeito_Posicao2_RecuoLimitadoPara1() {
        Player p = new Player(2, "Bruno", "Java", "Vermelho");
        Board b = new Board(10, List.of(p));

        b.setPlayerPosicao(2, 2); // recuo max 3 mas só pode ir até 1

        Abismos ab = new FileNotFoundException();
        String msg = ab.aplicarEfeito(p, b, 4);

        assertEquals(1, b.getPlayerPosicao(2), "Na posição 2, deve recuar apenas até à casa 1");
        assertNotNull(msg);
        assertTrue(msg.contains("Bruno"));
        assertTrue(msg.contains("FileNotFoundException"));
        assertTrue(msg.contains("posição 1"));
        // aqui não validamos "recuou 3" porque na prática recuou 1
    }

    @Test
    public void test05_AplicarEfeito_Posicao1_NaoRecuar_MensagemPertoDaPrimeiraCasa() {
        Player p = new Player(3, "Carla", "Java", "Verde");
        Board b = new Board(10, List.of(p));

        b.setPlayerPosicao(3, 1);

        Abismos ab = new FileNotFoundException();
        String msg = ab.aplicarEfeito(p, b, 2);

        assertEquals(1, b.getPlayerPosicao(3), "Na posição 1, deve permanecer na casa 1");
        assertNotNull(msg);
        assertTrue(msg.contains("Carla"));
        assertTrue(msg.contains("muito perto da primeira casa"), "Deve entrar no ramo 'muito perto da primeira casa'");
        assertTrue(msg.contains("posição 1"));
    }

    @Test
    public void test06_AplicarEfeito_NullNaoExplode() {
        Abismos ab = new FileNotFoundException();
        assertEquals("", ab.aplicarEfeito(null, null, 3));
    }
}
