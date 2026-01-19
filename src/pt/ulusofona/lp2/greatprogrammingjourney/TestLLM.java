package pt.ulusofona.lp2.greatprogrammingjourney;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestLLM {

    @Test
    public void test01_IdDeveSer20() {
        Abismos ab = new LLM();
        assertEquals(20, ab.getId());
    }

    @Test
    public void test02_NomeDeveSerLLM() {
        Abismos ab = new LLM();
        assertEquals("LLM", ab.getNome());
    }

    @Test
    public void test03_JogadorNullOuBoardNull_DevolveStringVazia() {
        Abismos ab = new LLM();

        Player p = new Player(1, "Ana", "Java", "Azul");
        Board b = new Board(10, List.of(p));

        assertEquals("", ab.aplicarEfeito(null, b, 3));
        assertEquals("", ab.aplicarEfeito(p, null, 3));
    }

    @Test
    public void test04_SeJogadorJaEstaNaMeta_NaoAplicaEfeito() {
        Player p = new Player(1, "Ana", "Java", "Azul");
        Board b = new Board(10, List.of(p));

        p.setPosicao(10, b.getSize()); // meta

        Abismos ab = new LLM();
        String msg = ab.aplicarEfeito(p, b, 3);

        assertEquals(10, b.getPlayerPosicao(1));
        assertEquals("", msg);
    }

    @Test
    public void test05_SemExperiencia_RecuarParaPosicaoAnterior() {
        Player p = new Player(1, "Ana", "Java", "Azul");
        Board b = new Board(20, List.of(p));

        // simular histórico de jogadas
        p.setPosicao(5, b.getSize());
        p.setPosicao(9, b.getSize()); // posição atual

        Abismos ab = new LLM();
        String msg = ab.aplicarEfeito(p, b, 4);

        assertEquals(5, b.getPlayerPosicao(1));
        assertNotNull(msg);
        assertTrue(msg.contains("LLM"));
        assertTrue(msg.contains("Recua"));
    }

    @Test
    public void test06_SemExperiencia_PosicaoAnteriorInvalida_VaiParaCasa1() {
        Player p = new Player(1, "Ana", "Java", "Azul");
        Board b = new Board(20, List.of(p));

        // histórico insuficiente → getPosicaoJogadas(1) inválido
        p.setPosicao(3, b.getSize());

        Abismos ab = new LLM();
        ab.aplicarEfeito(p, b, 2);

        assertEquals(1, b.getPlayerPosicao(1));
    }

    @Test
    public void test07_ComExperiencia_AvancaValorDoDado() {
        Player p = new Player(1, "Ana", "Java", "Azul");
        p.setExperiente(true);

        Board b = new Board(20, List.of(p));
        p.setPosicao(5, b.getSize());

        Abismos ab = new LLM();
        String msg = ab.aplicarEfeito(p, b, 3);

        assertEquals(8, b.getPlayerPosicao(1));
        assertNotNull(msg);
        assertTrue(msg.contains("experiência"));
        assertTrue(msg.contains("Avança"));
    }
}
