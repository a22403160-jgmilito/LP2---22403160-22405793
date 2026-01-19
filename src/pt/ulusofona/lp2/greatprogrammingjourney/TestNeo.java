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
    public void test02_AnulaApenasMatrix() {
        Ferramentas f = new Neo();

        assertTrue(f.podeAnular(new Matrix()));
        assertFalse(f.podeAnular(new Crash()));
        assertFalse(f.podeAnular(null));
    }
}
