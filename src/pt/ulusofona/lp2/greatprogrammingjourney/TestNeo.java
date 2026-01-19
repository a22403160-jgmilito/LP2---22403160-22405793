package pt.ulusofona.lp2.greatprogrammingjourney;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestNeo {

    @Test
    public void test01_IdDeveSer6() {
        Ferramentas f = new Neo();
        assertEquals(6, f.getId());
    }

    @Test
    public void test02_NomeEDescricao() {
        Ferramentas f = new Neo();

        assertEquals("Neo", f.getNome());
        assertNotNull(f.getDescricao());
        assertTrue(f.getDescricao().contains("Matrix"));
        assertTrue(f.getDescricao().length() > 10);
    }

    @Test
    public void test03_PodeAnular_ApenasMatrix() {
        Ferramentas f = new Neo();

        assertTrue(f.podeAnular(new Matrix()), "Neo deve anular apenas Matrix (id 10)");

        assertFalse(f.podeAnular(new Crash()), "Neo não deve anular Crash (id 4)");
        assertFalse(f.podeAnular(new ErroDeSintaxe()), "Neo não deve anular ErroDeSintaxe (id 0)");
        assertFalse(f.podeAnular(null), "Neo deve devolver false com null");
    }
    @Test
    public void test04_PodeAnular_ComAbismoFakeId10() {
        Ferramentas f = new Neo();

        Abismos fake10 = new Abismos(10) {
            @Override public String getNome() { return "Fake"; }
            @Override public String aplicarEfeito(Player j, Board b, int v) { return ""; }
        };

        assertTrue(f.podeAnular(fake10));
    }

}
