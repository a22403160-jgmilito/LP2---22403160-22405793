package pt.ulusofona.lp2.greatprogrammingjourney;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestMatrix {

    @AfterEach
    public void resetChoice() {
        Matrix.forceChoiceForTests(null);
    }

    @Test
    public void test01_IdDeveSer10() {
        Abismos ab = new Matrix();
        assertEquals(10, ab.getId());
    }

    @Test
    public void test02_NomeDeveSerMatrix() {
        Abismos ab = new Matrix();
        assertEquals("Matrix", ab.getNome());
    }

    @Test
    public void test03_AzulPrimeiraVezAvancaTriploDepoisRecuoTriplo() {
        Matrix.forceChoiceForTests(0); // Azul

        Player p = new Player(1, "Ana", "Java", "Azul");
        Board b = new Board(30, List.of(p));
        p.setPosicao(5, b.getSize());

        Abismos ab = new Matrix();

        ab.aplicarEfeito(p, b, 2); // 3*2 = 6
        assertEquals(11, p.getPosicao());

        ab.aplicarEfeito(p, b, 2);
        assertEquals(5, p.getPosicao());
    }

    @Test
    public void test04_VermelhaPrimeiraVezRecuoTriploDepoisAvancaTriplo() {
        Matrix.forceChoiceForTests(1); // Vermelha

        Player p = new Player(1, "Ana", "Java", "Azul");
        Board b = new Board(30, List.of(p));
        p.setPosicao(10, b.getSize());

        Abismos ab = new Matrix();
        ab.aplicarEfeito(p, b, 2); // Vermelha = punição na 1a activação
        assertEquals(4, p.getPosicao());

        ab.aplicarEfeito(p, b, 2); // Vermelha = prémio na 2a activação
        assertEquals(10, p.getPosicao());
    }
}
