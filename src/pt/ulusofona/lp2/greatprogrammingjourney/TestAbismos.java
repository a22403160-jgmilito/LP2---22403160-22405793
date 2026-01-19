package pt.ulusofona.lp2.greatprogrammingjourney;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestAbismos {

    @Test
    void test01_ConstrutorEGetId() {
        Abismos ab = new Abismos(42) {
            @Override
            public String getNome() {
                return "Abismo Fake";
            }

            @Override
            public String aplicarEfeito(Player jogador, Board board, int valorDado) {
                return "efeito fake";
            }
        };

        assertEquals(42, ab.getId());
        assertEquals("Abismo Fake", ab.getNome());
    }

    @Test
    void test02_AplicarEfeito_NaoLancaExcecao() {
        Abismos ab = new Abismos(1) {
            @Override
            public String getNome() {
                return "Teste";
            }

            @Override
            public String aplicarEfeito(Player jogador, Board board, int valorDado) {
                return "ok";
            }
        };

        Player p = new Player(1, "Ana", "Java", "Azul");
        Board b = new Board(10, java.util.List.of(p));

        String res = ab.aplicarEfeito(p, b, 3);

        assertNotNull(res);
        assertEquals("ok", res);
    }

    @Test
    void test03_AplicarEfeito_PodeReceberNulls() {
        Abismos ab = new Abismos(99) {
            @Override
            public String getNome() {
                return "NullSafe";
            }

            @Override
            public String aplicarEfeito(Player jogador, Board board, int valorDado) {
                return "";
            }
        };

        assertDoesNotThrow(() -> ab.aplicarEfeito(null, null, 0));
    }
}
