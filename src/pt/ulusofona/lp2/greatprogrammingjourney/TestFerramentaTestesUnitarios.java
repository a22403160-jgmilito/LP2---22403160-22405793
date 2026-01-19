package pt.ulusofona.lp2.greatprogrammingjourney;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestFerramentaTestesUnitarios {

    @Test
    public void test01_IdDeveSer2() {
        Ferramentas f = new FerramentaTestesUnitarios();
        assertEquals(2, f.getId());
    }

    @Test
    public void test02_NomeDeveSerTestesUnitarios() {
        Ferramentas f = new FerramentaTestesUnitarios();
        assertEquals("Testes Unitários", f.getNome());
    }

    @Test
    public void test03_DescricaoNaoVazia() {
        Ferramentas f = new FerramentaTestesUnitarios();
        assertNotNull(f.getDescricao());
        assertTrue(f.getDescricao().length() > 10);
    }

    @Test
    public void test04_PodeAnular_ApenasAbismosEsperados() {
        Ferramentas f = new FerramentaTestesUnitarios();

        // Deve anular
        assertTrue(f.podeAnular(new ErroDeLogica()));      // id 1
        assertTrue(f.podeAnular(new Crash()));             // id 4

        // Não deve anular
        assertFalse(f.podeAnular(new ErroDeSintaxe()));    // id 0
        assertFalse(f.podeAnular(new ExceptionAbismo()));  // id 2
        assertFalse(f.podeAnular(new FileNotFoundException())); // id 3
        assertFalse(f.podeAnular(new CodigoDuplicado()));  // id 5
        assertFalse(f.podeAnular(new EfeitosSecundarios())); // id 6
        assertFalse(f.podeAnular(new BlueScreenOfDeath())); // id 7
        assertFalse(f.podeAnular(new SegmentationFault())); // id 9
        assertFalse(f.podeAnular(new Matrix()));            // id 10
    }

    @Test
    public void test05_PodeAnular_NullDeveRetornarFalse() {
        Ferramentas f = new FerramentaTestesUnitarios();
        assertFalse(f.podeAnular(null));
    }
}
