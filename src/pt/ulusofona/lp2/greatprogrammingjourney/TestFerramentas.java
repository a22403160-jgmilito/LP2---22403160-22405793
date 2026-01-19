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

        assertTrue(new ProgramacaoFuncional().podeAnular(codigoDuplicado));

        assertTrue(new FerramentaTestesUnitarios().podeAnular(erroLogica));
        assertTrue(new TratamentoDeExcepcoes().podeAnular(exceptionAb));
        assertTrue(new TratamentoDeExcepcoes().podeAnular(fnf));

        assertTrue(new IDE().podeAnular(sintaxe));
        assertTrue(new IDE().podeAnular(erroLogica));
        assertTrue(new IDE().podeAnular(codigoDuplicado));

        assertTrue(new AjudaDoProfessor().podeAnular(sintaxe));
        assertTrue(new AjudaDoProfessor().podeAnular(new SegmentationFault()));
    }


    @Test
    void podeAnular_nullNaoExplode() {
        assertFalse(new Heranca().podeAnular(null));
        assertFalse(new FerramentaTestesUnitarios().podeAnular(null));
    }
    @Test
    void test01_Ferramentas_ConstrutorEGetId_SaoCobertos() {

        Ferramentas f = new Ferramentas(99) {
            @Override
            public String getNome() {
                return "Fake";
            }

            @Override
            public String getDescricao() {
                return "Ferramenta fake para testes";
            }

            @Override
            public boolean podeAnular(Abismos abismo) {
                return false;
            }
        };

        assertEquals(99, f.getId());
        assertEquals("Fake", f.getNome());
        assertEquals("Ferramenta fake para testes", f.getDescricao());
        assertFalse(f.podeAnular(new ErroDeSintaxe()));
    }

}
