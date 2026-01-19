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
    public void test04_AplicarEfeito_MensagemDeveSerExata() {
        Abismos ab = new BlueScreenOfDeath();
        Player p = new Player(2, "Bruno", "C", "Vermelho");

        String msg = ab.aplicarEfeito(p, null, 6);

        assertEquals(
                "O programador Bruno sofreu um Blue Screen of Death e foi derrotado, ficando fora do jogo.",
                msg
        );
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
    @Test
    public void test06_AplicarEfeito_IgnoraBoardEValorDado() {
        Abismos ab = new BlueScreenOfDeath();
        Player p = new Player(10, "Diana", "Kotlin", "Rosa");

        // board null e valor qualquer
        String msg1 = ab.aplicarEfeito(p, null, 1);
        assertFalse(p.isAlive());

        // “ressuscitar” só para repetir o efeito (se existir setAlive(true))
        // Se não existir setAlive(true), cria novo Player:
        Player p2 = new Player(11, "Diana", "Kotlin", "Rosa");
        String msg2 = ab.aplicarEfeito(p2, null, 999);

        assertEquals(msg1, msg2);
    }

}
