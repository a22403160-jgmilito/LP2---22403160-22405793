package pt.ulusofona.lp2.greatprogrammingjourney;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestCrash {

    @Test
    public void test01_IdDeveSer4() {
        Abismos ab = new Crash();
        assertEquals(4, ab.getId());
    }

    @Test
    public void test02_NomeDeveSerExato() {
        Abismos ab = new Crash();
        assertEquals("Crash", ab.getNome());
    }

    @Test
    public void test03_AplicarEfeito_DeveVoltarParaCasa1() {
        Player p = new Player(1, "Ana", "Java", "Azul");
        Board b = new Board(10, List.of(p));

        // colocar jogador numa casa diferente
        p.setPosicao(7, b.getSize());
        assertEquals(7, p.getPosicao());

        Abismos ab = new Crash();
        ab.aplicarEfeito(p, b, 3);

        assertEquals(1, p.getPosicao(), "Depois do Crash, o jogador deve voltar à casa 1");
    }

    @Test
    public void test04_AplicarEfeito_NaoDeveAlterarOutrosJogadores() {
        Player p1 = new Player(1, "Ana", "Java", "Azul");
        Player p2 = new Player(2, "Bruno", "C", "Vermelho");

        List<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);

        Board b = new Board(10, players);

        // posicionar ambos
        p1.setPosicao(8, b.getSize());
        p2.setPosicao(6, b.getSize());

        Abismos ab = new Crash();
        ab.aplicarEfeito(p1, b, 2);

        assertEquals(1, p1.getPosicao(), "p1 deve voltar à casa 1");
        assertEquals(6, p2.getPosicao(), "p2 não deve ser afetado");
    }

    @Test
    public void test05_MensagemDeveConterNomeETexto() {
        Player p = new Player(3, "Carla", "Python", "Verde");
        Board b = new Board(10, List.of(p));

        p.setPosicao(5, b.getSize());

        Abismos ab = new Crash();
        String msg = ab.aplicarEfeito(p, b, 6);

        assertNotNull(msg);
        assertTrue(msg.contains("Carla"));
        assertTrue(msg.contains("Crash"));
        assertTrue(msg.contains("voltou à primeira casa"));
    }
    @Test
    public void test06_AplicarEfeito_JogadorNull_DeveRetornarStringVazia() {
        Abismos ab = new Crash();

        Board b = new Board(10, List.of(new Player(1, "Ana", "Java", "Azul")));

        String msg = ab.aplicarEfeito(null, b, 3);

        assertEquals("", msg);
    }

    @Test
    public void test07_AplicarEfeito_BoardNull_DeveRetornarStringVazia() {
        Abismos ab = new Crash();

        Player p = new Player(1, "Ana", "Java", "Azul");

        String msg = ab.aplicarEfeito(p, null, 3);

        assertEquals("", msg);
    }

    @Test
    public void test08_AplicarEfeito_MensagemDeveSerExata() {
        Abismos ab = new Crash();

        Player p = new Player(4, "Diana", "Kotlin", "Rosa");
        Board b = new Board(10, List.of(p));

        b.setPlayerPosicao(4, 7);

        String msg = ab.aplicarEfeito(p, b, 1);

        assertEquals(1, b.getPlayerPosicao(4));
        assertEquals("O programador Diana sofreu um Crash e voltou à primeira casa.", msg);
    }

}
