package pt.ulusofona.lp2.greatprogrammingjourney;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestBlueScreenOfDeath {

    @Test
    public void test01_IdDeveSer7() {
        Abismos ab = new BlueScreenOfDeath();
        assertEquals(7, ab.getId());
    }

    @Test
    public void test02_NomeDeveSerExato() {
        Abismos ab = new BlueScreenOfDeath();
        assertEquals("Blue Screen of Death", ab.getNome());
    }

    @Test
    public void test03_AplicarEfeito_DeveDerrotarJogador() {
        Abismos ab = new BlueScreenOfDeath();

        Player p = new Player(1, "Ana", "Java", "Azul");
        assertTrue(p.isAlive(), "Pré-condição: jogador começa vivo");

        ab.aplicarEfeito(p, null, 3);

        assertFalse(p.isAlive(), "Depois do abismo, o jogador deve ficar derrotado");
    }

    @Test
    public void test04_AplicarEfeito_MensagemDeveConterNomeETexto() {
        Abismos ab = new BlueScreenOfDeath();

        Player p = new Player(2, "Bruno", "C", "Vermelho");

        String msg = ab.aplicarEfeito(p, null, 6);

        assertNotNull(msg);
        assertTrue(msg.contains("Bruno"));
        assertTrue(msg.contains("Blue Screen of Death"));
        assertTrue(msg.contains("foi derrotado"));
        assertTrue(msg.contains("ficando fora do jogo"));
    }

    @Test
    public void test05_AplicarEfeito_DuasVezesContinuaDerrotado() {
        Abismos ab = new BlueScreenOfDeath();

        Player p = new Player(3, "Carla", "Python", "Verde");

        ab.aplicarEfeito(p, null, 1);
        assertFalse(p.isAlive());

        ab.aplicarEfeito(p, null, 1);
        assertFalse(p.isAlive(), "Mesmo aplicando outra vez, deve continuar fora do jogo");
    }
}
