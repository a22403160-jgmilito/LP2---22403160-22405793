package pt.ulusofona.lp2.greatprogrammingjourney;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestEfeitosSecundarios {

    @Test
    public void test01_IdDeveSer6() {
        Abismos ab = new EfeitosSecundarios();
        assertEquals(6, ab.getId());
    }

    @Test
    public void test02_NomeDeveSerExato() {
        Abismos ab = new EfeitosSecundarios();
        assertEquals("Efeitos Secundários", ab.getNome());
    }

    @Test
    public void test03_AplicarEfeito_JogadorNull_DeveRetornarVazio() {
        Abismos ab = new EfeitosSecundarios();
        Board b = new Board(10, List.of(new Player(1, "Ana", "Java", "Azul")));

        String msg = ab.aplicarEfeito(null, b, 1);
        assertEquals("", msg);
    }

    @Test
    public void test04_AplicarEfeito_BoardNull_DeveRetornarVazio() {
        Abismos ab = new EfeitosSecundarios();
        Player p = new Player(1, "Ana", "Java", "Azul");

        String msg = ab.aplicarEfeito(p, null, 1);
        assertEquals("", msg);
    }

    @Test
    public void test05_AplicarEfeito_DeveVoltarParaPosicaoDeDoisMovimentosAtras() {
        Abismos ab = new EfeitosSecundarios();

        Player p = new Player(1, "Ana", "Java", "Azul");
        Board b = new Board(10, List.of(p));

        // histórico: ... 2 movimentos atrás = 3
        // posAtual no board = 8
        p.registarPosicao(3);
        p.registarPosicao(6); // só para ter mais histórico
        b.setPlayerPosicao(1, 8);

        String msg = ab.aplicarEfeito(p, b, 4);

        assertEquals(3, b.getPlayerPosicao(1));
        assertEquals(
                "O programador Ana sofreu Efeitos Secundários e voltou para a posição de 2 movimentos atrás (3).",
                msg
        );
    }

    @Test
    public void test06_AplicarEfeito_PosDoisAtrasInvalidaPorSerMenorQue1_DeveIrPara1() {
        Abismos ab = new EfeitosSecundarios();

        Player p = new Player(2, "Bruno", "C", "Vermelho");
        Board b = new Board(10, List.of(p));

        // 2 atrás inválido (0) -> clamp para 1
        p.registarPosicao(0);
        p.registarPosicao(5);
        b.setPlayerPosicao(2, 7);

        String msg = ab.aplicarEfeito(p, b, 2);

        assertEquals(1, b.getPlayerPosicao(2));
        assertTrue(msg.contains("posição de 2 movimentos atrás (1)"));
    }

    @Test
    public void test07_AplicarEfeito_PosDoisAtrasInvalidaPorSerMaiorQueSize_DeveIrPara1() {
        Abismos ab = new EfeitosSecundarios();

        Player p = new Player(3, "Carla", "Python", "Verde");
        Board b = new Board(10, List.of(p));

        // 2 atrás inválido (> size) -> clamp para 1
        p.registarPosicao(999);
        p.registarPosicao(4);
        b.setPlayerPosicao(3, 6);

        String msg = ab.aplicarEfeito(p, b, 1);

        assertEquals(1, b.getPlayerPosicao(3));
        assertTrue(msg.contains("posição de 2 movimentos atrás (1)"));
    }

    @Test
    public void test08_AplicarEfeito_DeltaZero_NaoMove_E_MensagemDeMantem() {
        Abismos ab = new EfeitosSecundarios();

        Player p = new Player(4, "Diana", "Kotlin", "Rosa");
        Board b = new Board(10, List.of(p));

        // Queremos delta = 0:
        // posAtual = 5 e posDoisAtras = 5
        p.registarPosicao(5);
        p.registarPosicao(8); // intermédia
        b.setPlayerPosicao(4, 5);

        String msg = ab.aplicarEfeito(p, b, 3);

        assertEquals(5, b.getPlayerPosicao(4));
        assertEquals(
                "O programador Diana sofreu Efeitos Secundários, mas manteve-se na mesma posição.",
                msg
        );
    }
}
