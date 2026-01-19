package pt.ulusofona.lp2.greatprogrammingjourney;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestTratamentoDeExcepcoes {

    @Test
    public void test01_idDeveSer3() {
        Ferramentas f = new TratamentoDeExcepcoes();
        assertEquals(3, f.getId());
    }

    @Test
    public void test02_nomeDeveSerExato() {
        Ferramentas f = new TratamentoDeExcepcoes();
        assertEquals("Tratamento de Excepções", f.getNome());
    }

    @Test
    public void test03_descricaoNaoDeveSerVazia() {
        Ferramentas f = new TratamentoDeExcepcoes();
        assertNotNull(f.getDescricao());
        assertTrue(f.getDescricao().length() > 10);
    }

    @Test
    public void test04_deveAnularExceptionEFileNotFound() {
        Ferramentas f = new TratamentoDeExcepcoes();

        assertTrue(f.podeAnular(new ExceptionAbismo()),
                "Deve anular Exception (id 2)");

        assertTrue(f.podeAnular(new FileNotFoundException()),
                "Deve anular FileNotFoundException (id 3)");
    }

    @Test
    public void test05_deveAnularCrash_conformeImplementacao() {
        Ferramentas f = new TratamentoDeExcepcoes();

        assertTrue(f.podeAnular(new Crash()),
                "De acordo com a implementação atual, também anula Crash (id 4)");
    }

    @Test
    public void test06_naoDeveAnularOutrosAbismos() {
        Ferramentas f = new TratamentoDeExcepcoes();

        assertFalse(f.podeAnular(new ErroDeSintaxe()));
        assertFalse(f.podeAnular(new ErroDeLogica()));
        assertFalse(f.podeAnular(new CodigoDuplicado()));
        assertFalse(f.podeAnular(new CicloInfinito()));
        assertFalse(f.podeAnular(new SegmentationFault()));
        assertFalse(f.podeAnular(new Matrix()));
    }

    @Test
    public void test07_podeAnular_nullDeveSerSeguro() {
        Ferramentas f = new TratamentoDeExcepcoes();

        assertFalse(f.podeAnular(null),
                "podeAnular(null) deve devolver false e não lançar exceção");
    }
}
