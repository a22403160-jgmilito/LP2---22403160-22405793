package pt.ulusofona.lp2.greatprogrammingjourney;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestIDE {

    @Test
    public void test01_IdDeveSer4() {
        Ferramentas f = new IDE();
        assertEquals(4, f.getId());
    }

    @Test
    public void test02_NomeDeveSerIDE() {
        Ferramentas f = new IDE();
        assertEquals("IDE", f.getNome());
    }

    @Test
    public void test03_DescricaoNaoDeveSerVazia() {
        Ferramentas f = new IDE();
        assertNotNull(f.getDescricao());
        assertFalse(f.getDescricao().isEmpty());
    }

    @Test
    public void test04_PodeAnularAbismosEsperados() {
        Ferramentas f = new IDE();

        assertTrue(f.podeAnular(new ErroDeSintaxe()));        // id 0
        assertTrue(f.podeAnular(new ErroDeLogica()));         // id 1
        assertTrue(f.podeAnular(new FileNotFoundException())); // id 3
        assertTrue(f.podeAnular(new CodigoDuplicado()));      // id 5
        assertTrue(f.podeAnular(new SegmentationFault()));    // id 9
    }

    @Test
    public void test05_NaoDeveAnularOutrosAbismos() {
        Ferramentas f = new IDE();

        assertFalse(f.podeAnular(new ExceptionAbismo())); // id 2
        assertFalse(f.podeAnular(new Crash()));           // id 4
        assertFalse(f.podeAnular(new Matrix()));          // id 10
        assertFalse(f.podeAnular(new LLM()));             // id 20
    }

    @Test
    public void test06_NullNaoDeveSerAnulado() {
        Ferramentas f = new IDE();
        assertFalse(f.podeAnular(null));
    }
}
