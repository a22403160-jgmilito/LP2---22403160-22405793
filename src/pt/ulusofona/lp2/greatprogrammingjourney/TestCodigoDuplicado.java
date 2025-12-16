package pt.ulusofona.lp2.greatprogrammingjourney;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestCodigoDuplicado {

    @Test
    public void test01_IdDeveSer5() {
        Abismos ab = new CodigoDuplicado();
        assertEquals(5, ab.getId());
    }

    @Test
    public void test02_NomeDeveSerExato() {
        Abismos ab = new CodigoDuplicado();
        assertEquals("Código Duplicado", ab.getNome());
    }

    @Test
    public void test03_AplicarEfeito_DeveVoltarParaCasaAnterior() {
        Player p = new Player(1, "Ana", "Java", "Azul");
        Board b = new Board(10, List.of(p));

        // Simular histórico: estava na 3 e depois foi para 6
        p.registarPosicao(3);
        b.setPlayerPosicao(1, 6);

        // garantir histórico da posição atual também (como o teu GameManager faz)
        p.registarPosicao(6);

        Abismos ab = new CodigoDuplicado();
        ab.aplicarEfeito(p, b, 4);

        assertEquals(3, b.getPlayerPosicao(1), "Deve voltar para a posição anterior (3)");
    }

    @Test
    public void test04_AplicarEfeito_SePosAnteriorInvalida_DeveIrPara1() {
        Player p = new Player(2, "Bruno", "C", "Vermelho");
        Board b = new Board(10, List.of(p));

        // Simular histórico inválido (0) como posição anterior
        p.registarPosicao(0);
        b.setPlayerPosicao(2, 5);
        p.registarPosicao(5);

        Abismos ab = new CodigoDuplicado();
        ab.aplicarEfeito(p, b, 2);

        assertEquals(1, b.getPlayerPosicao(2), "Se a posição anterior for inválida, deve ir para 1");
    }

    @Test
    public void test05_AplicarEfeito_SeJaEstiverNaCasaAnterior_NaoMove() {
        Player p = new Player(3, "Carla", "Python", "Verde");
        Board b = new Board(10, List.of(p));

        // Histórico diz que a casa anterior é 4
        p.registarPosicao(4);

        // Jogador já está na casa 4
        b.setPlayerPosicao(3, 4);
        p.registarPosicao(4);

        Abismos ab = new CodigoDuplicado();
        String msg = ab.aplicarEfeito(p, b, 1);

        assertEquals(4, b.getPlayerPosicao(3), "Se já estiver na casa anterior, não deve mudar");
        assertNotNull(msg);
        assertTrue(msg.contains("Carla"));
        assertTrue(msg.contains("posição 4"));
    }
}
