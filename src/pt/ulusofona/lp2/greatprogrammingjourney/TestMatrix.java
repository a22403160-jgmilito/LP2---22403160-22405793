package pt.ulusofona.lp2.greatprogrammingjourney;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestMatrix {

    @AfterEach
    public void resetChoice() {
        Matrix.forceChoiceForTests(null);
        Matrix.forceBlueRewardFirstForTests(null);
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
    public void test03_NullSafety_JogadorNull_DeveRetornarVazio() {
        Matrix.forceChoiceForTests(0);
        Matrix.forceBlueRewardFirstForTests(true);

        Board b = new Board(20, List.of(new Player(1, "Ana", "Java", "Azul")));
        Abismos ab = new Matrix();

        assertEquals("", ab.aplicarEfeito(null, b, 2));
    }

    @Test
    public void test04_NullSafety_BoardNull_DeveRetornarVazio() {
        Matrix.forceChoiceForTests(0);
        Matrix.forceBlueRewardFirstForTests(true);

        Player p = new Player(1, "Ana", "Java", "Azul");
        Abismos ab = new Matrix();

        assertEquals("", ab.aplicarEfeito(p, null, 2));
    }

    @Test
    public void test05_SeEstiverNaCasaFinal_NaoAplicaEfeito() {
        Matrix.forceChoiceForTests(0);
        Matrix.forceBlueRewardFirstForTests(true);

        Player p = new Player(1, "Ana", "Java", "Azul");
        Board b = new Board(10, List.of(p));

        // coloca o jogador na casa final (10)
        b.setPlayerPosicao(1, 10);
        assertEquals(10, b.getPlayerPosicao(1));

        Abismos ab = new Matrix();
        String msg = ab.aplicarEfeito(p, b, 6);

        assertEquals("", msg, "Na casa final, Matrix deve devolver string vazia");
        assertEquals(10, b.getPlayerPosicao(1), "Na casa final, não pode mexer na posição");
    }

    @Test
    public void test06_ValorDadoNegativo_DeslocamentoZero_MantemPosicao_MensagemConsistente() {
        // força ordem e escolha para ser determinístico
        Matrix.forceBlueRewardFirstForTests(true); // azul = prémio (mas deslocamento = 0)
        Matrix.forceChoiceForTests(0);             // escolhe azul

        Player p = new Player(1, "Ana", "Java", "Azul");
        Board b = new Board(30, List.of(p));
        b.setPlayerPosicao(1, 10);

        Abismos ab = new Matrix();
        String msg = ab.aplicarEfeito(p, b, -5); // deslocamento = max(0, -5)*3 = 0

        assertEquals(10, b.getPlayerPosicao(1), "Com valorDado negativo, deslocamento é 0 e não deve mexer");
        assertNotNull(msg);
        assertTrue(msg.contains("Ana"));
        assertTrue(msg.contains("pílula azul"));
        assertTrue(msg.contains("avançou 0") || msg.contains("recuou 0"),
                "Como deslocamento=0, a mensagem deve indicar 0 (avançou ou recuou).");
        assertTrue(msg.contains("posição 10"));
    }

    @Test
    public void test07_AzulPrimeiraVezPremio_DepoisPune_AmbasMensagensEPosicoes() {
        Matrix.forceBlueRewardFirstForTests(true);  // azul = prémio na 1ª ativação
        Matrix.forceChoiceForTests(0);              // escolhe azul sempre

        Player p = new Player(1, "Ana", "Java", "Azul");
        Board b = new Board(30, List.of(p));
        b.setPlayerPosicao(1, 5);

        Matrix ab = new Matrix();

        String msg1 = ab.aplicarEfeito(p, b, 2); // +6
        assertEquals(11, b.getPlayerPosicao(1));
        assertTrue(msg1.contains("avançou 6"));

        String msg2 = ab.aplicarEfeito(p, b, 2); // agora azul vira punição => -6
        assertEquals(5, b.getPlayerPosicao(1));
        assertTrue(msg2.contains("recuou 6"));
    }

    @Test
    public void test08_VermelhaPrimeiraVezPune_DepoisPremia() {
        Matrix.forceBlueRewardFirstForTests(true); // azul = prémio, então vermelha = punição na 1ª
        Matrix.forceChoiceForTests(1);             // escolhe vermelha sempre

        Player p = new Player(1, "Ana", "Java", "Azul");
        Board b = new Board(30, List.of(p));
        b.setPlayerPosicao(1, 10);

        Matrix ab = new Matrix();

        String msg1 = ab.aplicarEfeito(p, b, 2); // -6
        assertEquals(4, b.getPlayerPosicao(1));
        assertTrue(msg1.contains("pílula vermelha"));
        assertTrue(msg1.contains("recuou 6"));

        String msg2 = ab.aplicarEfeito(p, b, 2); // agora vermelha vira prémio => +6
        assertEquals(10, b.getPlayerPosicao(1));
        assertTrue(msg2.contains("avançou 6"));
    }

    @Test
    public void test09_AzulPodeSerPunicao_QuandoOrdemInicialFalse() {
        Matrix.forceBlueRewardFirstForTests(false); // azul = punição
        Matrix.forceChoiceForTests(0);              // escolhe azul

        Player p = new Player(1, "Ana", "Java", "Azul");
        Board b = new Board(30, List.of(p));
        b.setPlayerPosicao(1, 10);

        Matrix ab = new Matrix();
        String msg = ab.aplicarEfeito(p, b, 2); // -6

        assertEquals(4, b.getPlayerPosicao(1));
        assertTrue(msg.contains("recuou 6"));
    }
}
