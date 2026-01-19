package pt.ulusofona.lp2.greatprogrammingjourney;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestAjudaProfessor {

    @Test
    void test01_IdDeveSer5() {
        Ferramentas f = new AjudaDoProfessor();
        assertEquals(5, f.getId());
    }

    @Test
    void test02_NomeCorreto() {
        Ferramentas f = new AjudaDoProfessor();
        assertEquals("Ajuda do Professor", f.getNome());
    }

    @Test
    void test03_DescricaoNaoVazia() {
        Ferramentas f = new AjudaDoProfessor();
        assertNotNull(f.getDescricao());
        assertFalse(f.getDescricao().isEmpty());
    }

    @Test
    void test04_NaoAnulaNull() {
        Ferramentas f = new AjudaDoProfessor();
        assertFalse(f.podeAnular(null));
    }

    @Test
    void test05_AnulaAbismosPermitidos() {
        Ferramentas f = new AjudaDoProfessor();

        assertTrue(f.podeAnular(new ErroDeSintaxe()));        // id 0
        assertTrue(f.podeAnular(new ErroDeLogica()));         // id 1
        assertTrue(f.podeAnular(new ExceptionAbismo()));      // id 2
        assertTrue(f.podeAnular(new FileNotFoundException())); // id 3
        assertTrue(f.podeAnular(new CodigoDuplicado()));      // id 5
        assertTrue(f.podeAnular(new EfeitosSecundarios()));   // id 6
        assertTrue(f.podeAnular(new BlueScreenOfDeath()));    // id 7
        assertTrue(f.podeAnular(new SegmentationFault()));    // id 9
        assertTrue(f.podeAnular(new LLM()));                  // id 20
    }

    @Test
    void test06_NaoAnulaAbismosNaoPermitidos() {
        Ferramentas f = new AjudaDoProfessor();

        assertFalse(f.podeAnular(new Crash())); // id 4
        assertFalse(f.podeAnular(new Matrix())); // id 10 (regra especial do jogo)
    }
}
