package pt.ulusofona.lp2.greatprogrammingjourney;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestFerramentas {

    @Test
    void nomesEIds_basicos() {
        assertEquals(0, new Heranca().getId());
        assertEquals("Herança", new Heranca().getNome());

        assertEquals(4, new IDE().getId());
        assertEquals("IDE", new IDE().getNome());

        assertEquals(5, new AjudaDoProfessor().getId());
        assertTrue(new AjudaDoProfessor().getDescricao().length() > 5);
    }

    @Test
    void podeAnular_respeitaImplementacaoAtual() {
        Abismos codigoDuplicado = new CodigoDuplicado(); // id 5
        Abismos erroLogica = new ErroDeLogica();          // id 1
        Abismos exceptionAb = new ExceptionAbismo();      // id 2
        Abismos fnf = new FileNotFoundException();        // id 3
        Abismos sintaxe = new ErroDeSintaxe();            // id 0

        assertTrue(new Heranca().podeAnular(codigoDuplicado));
        assertFalse(new Heranca().podeAnular(erroLogica));

        // Nota: no teu código atual, ProgramacaoFuncional anula id 5
        assertTrue(new ProgramacaoFuncional().podeAnular(codigoDuplicado));

        assertTrue(new FerramentaTestesUnitarios().podeAnular(erroLogica));
        assertTrue(new TratamentoDeExcepcoes().podeAnular(exceptionAb));
        assertTrue(new TratamentoDeExcepcoes().podeAnular(fnf));
        assertFalse(new TratamentoDeExcepcoes().podeAnular(sintaxe));

        assertTrue(new IDE().podeAnular(sintaxe));
        assertTrue(new IDE().podeAnular(erroLogica));
        assertTrue(new IDE().podeAnular(codigoDuplicado));

        assertTrue(new AjudaDoProfessor().podeAnular(sintaxe));
        assertTrue(new AjudaDoProfessor().podeAnular(new SegmentationFault()));
        assertFalse(new AjudaDoProfessor().podeAnular(new Crash()));
        assertFalse(new AjudaDoProfessor().podeAnular(new BlueScreenOfDeath()));
        assertFalse(new AjudaDoProfessor().podeAnular(new CicloInfinito()));
    }

    @Test
    void podeAnular_nullNaoExplode() {
        assertFalse(new Heranca().podeAnular(null));
        assertFalse(new FerramentaTestesUnitarios().podeAnular(null));
    }
}
