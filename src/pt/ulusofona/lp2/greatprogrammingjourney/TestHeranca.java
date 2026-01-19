package pt.ulusofona.lp2.greatprogrammingjourney;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestHeranca {

    @Test
    public void test01_IdDeveSer0() {
        Ferramentas f = new Heranca();
        assertEquals(0, f.getId());
    }

    @Test
    public void test02_NomeDeveSerHeranca() {
        Ferramentas f = new Heranca();
        assertEquals("Herança", f.getNome());
    }

    @Test
    public void test03_DescricaoNaoDeveSerVazia() {
        Ferramentas f = new Heranca();
        assertNotNull(f.getDescricao());
        assertFalse(f.getDescricao().isEmpty());
    }

    @Test
    public void test04_PodeAnularApenasCodigoDuplicado() {
        Ferramentas f = new Heranca();

        assertTrue(f.podeAnular(new CodigoDuplicado()),
                "Herança deve anular apenas Código Duplicado (id 5)");

        assertFalse(f.podeAnular(new ErroDeSintaxe()));
        assertFalse(f.podeAnular(new ErroDeLogica()));
        assertFalse(f.podeAnular(new ExceptionAbismo()));
        assertFalse(f.podeAnular(new FileNotFoundException()));
        assertFalse(f.podeAnular(new Crash()));
        assertFalse(f.podeAnular(null));
    }
}
