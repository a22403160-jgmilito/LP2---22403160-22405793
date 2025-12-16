package pt.ulusofona.lp2.greatprogrammingjourney;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestCicloInfinito {

    @Test
    public void test01_IdDeveSer8() {
        Abismos ab = new CicloInfinito();
        assertEquals(8, ab.getId());
    }

    @Test
    public void test02_NomeDeveSerExato() {
        Abismos ab = new CicloInfinito();
        assertEquals("Ciclo Infinito", ab.getNome());
    }

    @Test
    public void test03_AplicarEfeito_DeveDeixarJogadorPreso() {
        Abismos ab = new CicloInfinito();

        Player p = new Player(1, "Ana", "Java", "Azul");
        assertTrue(p.isEnabled(), "Pré-condição: jogador começa livre");

        ab.aplicarEfeito(p, null, 2);

        assertFalse(p.isEnabled(), "Depois do Ciclo Infinito, o jogador deve ficar preso");
    }

    @Test
    public void test04_AplicarEfeito_NaoDeveAlterarPosicao() {
        Abismos ab = new CicloInfinito();

        Player p = new Player(2, "Bruno", "C", "Vermelho");
        p.setPosicao(5, 10);

        ab.aplicarEfeito(p, null, 4);

        assertEquals(5, p.getPosicao(), "Ciclo Infinito não deve mover o jogador");
    }

    @Test
    public void test05_MensagemDeveConterNomeEPosicao() {
        Abismos ab = new CicloInfinito();

        Player p = new Player(3, "Carla", "Python", "Verde");
        p.setPosicao(7, 10);

        String msg = ab.aplicarEfeito(p, null, 1);

        assertNotNull(msg);
        assertTrue(msg.contains("Carla"));
        assertTrue(msg.contains("Ciclo Infinito"));
        assertTrue(msg.contains("casa 7"));
        assertTrue(msg.contains("ficou preso"));
    }
}
