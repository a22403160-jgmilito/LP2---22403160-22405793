package pt.ulusofona.lp2.greatprogrammingjourney;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void linguagensNormalizadas_ordenaEFormata() {
        Player p = new Player(1, "Ana", "  C  ; Java ;Python; ", "#fff");
        assertEquals("C; Java; Python", p.getLinguagensNormalizadas());
    }

    @Test
    void ferramentas_stringVaziaMostraNoTools() {
        Player p = new Player(1, "Ana", "Java", "#fff");
        assertEquals("No tools", p.getFerramentasAsString());
    }

    @Test
    void ferramentas_adicionarRemoverEProcurarPorId() {
        Player p = new Player(1, "Ana", "Java", "#fff");
        Ferramentas ide = new IDE();
        Ferramentas heranca = new Heranca();

        p.adicionarFerramenta(ide);
        p.adicionarFerramenta(heranca);

        assertTrue(p.temFerramentaComId(4));
        assertTrue(p.temFerramentaComId(0));
        assertFalse(p.temFerramentaComId(999));

        // ordem de inserção (o teu código não ordena)
        assertEquals("IDE;Herança", p.getFerramentasAsString());

        p.removeFerramenta(ide);
        assertFalse(p.temFerramentaComId(4));
        assertEquals("Herança", p.getFerramentasAsString());
    }

    @Test
    void estado_textoDerrotadoPresoEmJogo() {
        Player p = new Player(1, "Ana", "Java", "#fff");

        assertEquals("Em Jogo", p.getEstadoComoTexto());

        p.setEnabled(false);
        assertEquals("Preso", p.getEstadoComoTexto());

        p.setAlive(false);
        assertEquals("Derrotado", p.getEstadoComoTexto());
        // se morreu, não pode ficar enabled=true
        p.setEnabled(true);
        assertFalse(p.isEnabled());
    }

    @Test
    void historico_posicaoJogadas_devolvePosicaoAnterior() {
        Player p = new Player(1, "Ana", "Java", "#fff");
        Board b = new Board(10, List.of(p));

        b.movePlayer(1, 3); // vai para 4
        b.movePlayer(1, 2); // vai para 6

        assertEquals(6, p.getPosicao());
        assertEquals(4, p.getPosicaoJogadas(1));
        assertEquals(1, p.getPosicaoJogadas(2)); // posição inicial
        assertEquals(1, p.getPosicaoJogadas(999)); // clamp
    }
}
