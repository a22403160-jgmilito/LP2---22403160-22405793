package pt.ulusofona.lp2.greatprogrammingjourney;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestProgramacaoFuncional {

    @Test
    public void test01_idDeveSer1() {
        Ferramentas f = new ProgramacaoFuncional();
        assertEquals(1, f.getId());
    }

    @Test
    public void test02_nomeDeveSerProgramacaoFuncional() {
        Ferramentas f = new ProgramacaoFuncional();
        assertEquals("Programação Funcional", f.getNome());
    }

    @Test
    public void test03_descricao_naoVazia_e_comTamanhoMinimo() {
        Ferramentas f = new ProgramacaoFuncional();
        assertNotNull(f.getDescricao());
        assertTrue(f.getDescricao().length() > 10);
    }

    @Test
    public void test04_podeAnular_apenasCodigoDuplicado_e_EfeitosSecundarios() {
        Ferramentas f = new ProgramacaoFuncional();

        assertTrue(f.podeAnular(new CodigoDuplicado()), "Deve anular Código Duplicado (id 5)");
        assertTrue(f.podeAnular(new EfeitosSecundarios()), "Deve anular Efeitos Secundários (id 6)");

        assertFalse(f.podeAnular(new ErroDeSintaxe()), "Não deve anular Erro de Sintaxe (id 0)");
        assertFalse(f.podeAnular(new ErroDeLogica()), "Não deve anular Erro de Lógica (id 1)");
        assertFalse(f.podeAnular(new ExceptionAbismo()), "Não deve anular Exception (id 2)");
        assertFalse(f.podeAnular(new FileNotFoundException()), "Não deve anular FileNotFoundException (id 3)");
        assertFalse(f.podeAnular(new Crash()), "Não deve anular Crash (id 4)");

        assertFalse(f.podeAnular(null), "Null deve devolver false");
    }
}
