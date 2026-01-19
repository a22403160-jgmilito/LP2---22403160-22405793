package pt.ulusofona.lp2.greatprogrammingjourney;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

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
    public void test03_AplicarEfeito_DeveDesativarJogador() {
        Abismos ab = new CicloInfinito();

        Player p = new Player(1, "Ana", "Java", "Azul");
        List<Player> players = new ArrayList<>();
        players.add(p);

        Board b = new Board(10, players);

        assertTrue(p.isEnabled(), "Pré-condição: jogador começa enabled");

        ab.aplicarEfeito(p, b, 2);

        assertFalse(p.isEnabled(), "Depois do Ciclo Infinito, o jogador deve ficar preso (enabled=false)");
    }

    @Test
    public void test04_AplicarEfeito_PosicaoNaMensagem_DeveSerAPosicaoDoBoard() {
        Abismos ab = new CicloInfinito();

        Player p = new Player(2, "Bruno", "C", "Vermelho");
        List<Player> players = new ArrayList<>();
        players.add(p);

        Board b = new Board(10, players);

        // mover via Board (para garantir que board.getPlayerPosicao devolve 5)
        b.setPlayerPosicao(2, 5);

        String msg = ab.aplicarEfeito(p, b, 4);

        assertNotNull(msg);
        assertTrue(msg.contains("casa 5"));
        assertEquals(5, p.getPosicao(), "Ciclo Infinito não deve mover o jogador");
    }

    @Test
    public void test05_AplicarEfeito_MensagemDeveSerExata() {
        Abismos ab = new CicloInfinito();

        Player p = new Player(3, "Carla", "Python", "Verde");
        Board b = new Board(10, List.of(p));

        b.setPlayerPosicao(3, 7);

        String msg = ab.aplicarEfeito(p, b, 1);

        assertEquals(
                "O programador Carla entrou num Ciclo Infinito na casa 7 e ficou preso, sem poder avançar até que outro programador caia na mesma casa.",
                msg
        );
    }

    @Test
    public void test06_AplicarEfeito_DuasVezes_ContinuaPreso() {
        Abismos ab = new CicloInfinito();

        Player p = new Player(4, "Diana", "Kotlin", "Rosa");
        Board b = new Board(10, List.of(p));

        b.setPlayerPosicao(4, 3);

        ab.aplicarEfeito(p, b, 1);
        assertFalse(p.isEnabled());

        ab.aplicarEfeito(p, b, 6);
        assertFalse(p.isEnabled());
    }
}
