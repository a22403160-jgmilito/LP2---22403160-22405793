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
    public void test03_AplicarEfeito_DeveVoltarParaPosicaoDeDoisMovimentosAtras() {
        Player p = new Player(1, "Ana", "Java", "Azul");
        Board b = new Board(10, List.of(p));

        // Simular histórico: 2 atrás = 3, anterior = 6, atual = 8
        p.registarPosicao(3);
        p.registarPosicao(6);
        p.setPosicao(8, b.getSize());

        Abismos ab = new EfeitosSecundarios();
        String msg = ab.aplicarEfeito(p, b, 4);

        assertEquals(3, p.getPosicao(), "Deve voltar para a posição de 2 movimentos atrás (3)");
        assertNotNull(msg);
        assertTrue(msg.contains("Ana"));
        assertTrue(msg.contains("posição de 2 movimentos atrás (3)"));
    }

    @Test
    public void test04_AplicarEfeito_SeHistoricoInvalido_DeveIrPara1() {
        Player p = new Player(2, "Bruno", "C", "Vermelho");
        Board b = new Board(10, List.of(p));

        // Simular 2 atrás inválido (0)
        p.registarPosicao(0);
        p.registarPosicao(5);
        p.setPosicao(7, b.getSize());

        Abismos ab = new EfeitosSecundarios();
        String msg = ab.aplicarEfeito(p, b, 2);

        assertEquals(1, p.getPosicao(), "Se a posição de 2 movimentos atrás for inválida, deve ir para 1");
        assertNotNull(msg);
        assertTrue(msg.contains("posição de 2 movimentos atrás (1)"));
    }

    @Test
    public void test05_AplicarEfeito_SePosDoisMovimentosAtrasIgualAtual_MantemPosicao() {
        Player p = new Player(3, "Carla", "Python", "Verde");
        Board b = new Board(10, List.of(p));

        // Forçar caso em que 2 atrás = atual.
        // Exemplo: histórico [7, 8] e atual=7,
        // dependendo da implementação do getPosicaoJogadas(2), isto dá 7.
        p.registarPosicao(7);
        p.registarPosicao(8);
        p.setPosicao(7, b.getSize());

        Abismos ab = new EfeitosSecundarios();
        String msg = ab.aplicarEfeito(p, b, 1);

        assertEquals(7, p.getPosicao(), "Deve manter-se na mesma posição");
        assertNotNull(msg);
        assertTrue(msg.contains("Carla"));
        assertTrue(msg.contains("manteve-se na mesma posição"));
    }
}
