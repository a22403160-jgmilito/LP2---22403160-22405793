package pt.ulusofona.lp2.greatprogrammingjourney;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    public void test03_AzulPrimeiraVezAvancaTriploDepoisRecuoTriplo() {
        Matrix.forceBlueRewardFirstForTests(true);  // começa com Azul = prémio
        Matrix.forceChoiceForTests(0);              // escolhe Azul sempre

        Player p = new Player(1, "Ana", "Java", "Azul");
        Board b = new Board(30, List.of(p));
        p.setPosicao(5, b.getSize());

        Abismos ab = new Matrix();

        ab.aplicarEfeito(p, b, 2); // 3*2 = 6
        assertEquals(11, p.getPosicao());

        ab.aplicarEfeito(p, b, 2); // agora inverte => Azul vira punição
        assertEquals(5, p.getPosicao());
    }

    @Test
    public void test04_VermelhaPrimeiraVezRecuoTriploDepoisAvancaTriplo() {
        Matrix.forceBlueRewardFirstForTests(true);  // começa com Azul = prémio
        Matrix.forceChoiceForTests(1);              // escolhe Vermelha sempre

        Player p = new Player(1, "Ana", "Java", "Azul");
        Board b = new Board(30, List.of(p));
        p.setPosicao(10, b.getSize());

        Abismos ab = new Matrix();

        ab.aplicarEfeito(p, b, 2); // Vermelha = punição na 1ª activação
        assertEquals(4, p.getPosicao());

        ab.aplicarEfeito(p, b, 2); // Vermelha = prémio na 2ª activação
        assertEquals(10, p.getPosicao());
    }

    @Test
    public void test05_AzulNaoEhSemprePremio_DependeDaOrdemInicial() {

        Player p = new Player(1, "Ana", "Java", "Azul");
        Board b = new Board(30, List.of(p));
        p.setPosicao(10, b.getSize());

        // Caso A: ordem inicial => Azul é prémio
        Matrix.forceBlueRewardFirstForTests(true);
        Matrix.forceChoiceForTests(0); // escolhe Azul

        Abismos abA = new Matrix();
        abA.aplicarEfeito(p, b, 2); // delta = +6
        assertEquals(16, p.getPosicao());

        // Reset posição
        p.setPosicao(10, b.getSize());

        // Caso B: ordem inicial => Azul é punição
        Matrix.forceBlueRewardFirstForTests(false);
        Matrix.forceChoiceForTests(0); // escolhe Azul

        Abismos abB = new Matrix();
        abB.aplicarEfeito(p, b, 2); // delta = -6
        assertEquals(4, p.getPosicao());
    }
    @Test
    public void test99_Estatistico_DeveExistirMaisDoQueUmComportamentoAoLongoDeMuitasExecucoes() {

        // Não forçar escolha nem ordem inicial -> comportamento aleatório
        Matrix.forceChoiceForTests(null);
        Matrix.forceBlueRewardFirstForTests(null);

        int vezesAvanca = 0;
        int vezesRecuo = 0;

        // 50 execuções só para "ver" o aleatório a acontecer
        for (int i = 0; i < 50; i++) {

            Player p = new Player(1, "Ana", "Java", "Azul");
            Board b = new Board(30, List.of(p));
            p.setPosicao(10, b.getSize());

            Matrix ab = new Matrix();

            int posAntes = p.getPosicao();
            ab.aplicarEfeito(p, b, 2); // deslocamento = 6
            int posDepois = p.getPosicao();

            if (posDepois > posAntes) {
                vezesAvanca++;
            } else if (posDepois < posAntes) {
                vezesRecuo++;
            }
        }

        // “Prova” estatística: em algum momento observamos avanço e recuo
        org.junit.jupiter.api.Assertions.assertTrue(vezesAvanca > 0,
                "Nunca observei avanço em 50 execuções (pode acontecer por azar, mas é improvável).");
        org.junit.jupiter.api.Assertions.assertTrue(vezesRecuo > 0,
                "Nunca observei recuo em 50 execuções (pode acontecer por azar, mas é improvável).");

        System.out.println("Avanços: " + vezesAvanca + " | Recuos: " + vezesRecuo);
    }


}
