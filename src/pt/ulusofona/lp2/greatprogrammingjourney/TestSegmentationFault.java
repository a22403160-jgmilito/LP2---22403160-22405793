package pt.ulusofona.lp2.greatprogrammingjourney;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestSegmentationFault {

    @Test
    public void test01_idDeveSer9() {
        Abismos ab = new SegmentationFault();
        assertEquals(9, ab.getId());
    }

    @Test
    public void test02_nomeDeveSerSegmentationFault() {
        Abismos ab = new SegmentationFault();
        assertEquals("Segmentation Fault", ab.getNome());
    }

    @Test
    public void test03_aplicarEfeito_naoDeveDarErro_e_deveRetornarStringNaoNula() {
        Player p = new Player(1, "Ana", "Java", "Azul");
        Board b = new Board(10, List.of(p));

        Abismos ab = new SegmentationFault();

        String msg = ab.aplicarEfeito(p, b, 3);

        assertNotNull(msg, "Mesmo que esteja vazio, não deve devolver null");
        // como o método atualmente retorna "", isto passa
        assertEquals("", msg);
    }

    @Test
    public void test04_aplicarEfeito_comNulls_deveSerSeguro() {
        Abismos ab = new SegmentationFault();

        // Se no futuro decidires retornar "", isto continua a passar.
        // Se preferires lançar exceção, muda o teste.
        assertDoesNotThrow(() -> ab.aplicarEfeito(null, null, 0));
    }
}
