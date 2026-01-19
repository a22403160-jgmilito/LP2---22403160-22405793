package pt.ulusofona.lp2.greatprogrammingjourney;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestPlayer {

    @Test
    public void test01_construtor_posicaoInicial_e_estadoInicial() {
        Player p = new Player(1, "Ana", "Java", "Azul");

        assertEquals(1, p.getId());
        assertEquals("Ana", p.getNome());
        assertEquals("Azul", p.getCor());

        assertEquals(1, p.getPosicao(), "Posição inicial deve ser 1");
        assertTrue(p.isAlive(), "Começa vivo");
        assertTrue(p.isEnabled(), "Começa enabled");
        assertEquals("Em Jogo", p.getEstadoComoTexto());

        // histórico: n=0 devolve posição atual (1)
        assertEquals(1, p.getPosicaoJogadas(0));
        // pedir mais atrás do que existe -> fica no início (1)
        assertEquals(1, p.getPosicaoJogadas(10));
    }

    @Test
    public void test02_getLinguagensNormalizadas_removeVazios_trim_e_ordena() {
        Player p = new Player(1, "Ana", "  C;Python ; ; java  ;", "Azul");

        String norm = p.getLinguagensNormalizadas();
        assertEquals("C; java; Python", norm);
    }

    @Test
    public void test03_getLinguagensNormalizadas_nullOuVazia_devolveVazio() {
        Player p1 = new Player(1, "Ana", null, "Azul");
        assertEquals("", p1.getLinguagensNormalizadas());

        Player p2 = new Player(2, "Bruno", "", "Vermelho");
        assertEquals("", p2.getLinguagensNormalizadas());
    }

    @Test
    public void test04_getLinguagensOriginal_trim_e_nullViraVazio() {
        Player p1 = new Player(1, "Ana", "  Java ; Python  ", "Azul");
        assertEquals("Java ; Python", p1.getLinguagensOriginal());

        Player p2 = new Player(2, "Bruno", null, "Vermelho");
        assertEquals("", p2.getLinguagensOriginal());
    }

    @Test
    public void test05_setPosicao_clamp_e_historico() {
        Player p = new Player(1, "Ana", "Java", "Azul");

        p.setPosicao(5, 10);
        assertEquals(5, p.getPosicao());
        assertEquals(5, p.getPosicaoJogadas(0));
        assertEquals(1, p.getPosicaoJogadas(1), "Uma jogada atrás deve ser a posição inicial");

        // clamp inferior
        p.setPosicao(-100, 10);
        assertEquals(1, p.getPosicao());

        // clamp superior
        p.setPosicao(999, 10);
        assertEquals(10, p.getPosicao());

        // confirmar histórico “anda para trás”
        assertEquals(10, p.getPosicaoJogadas(0));
        assertEquals(1, p.getPosicaoJogadas(1));
        assertEquals(5, p.getPosicaoJogadas(2));
        assertEquals(1, p.getPosicaoJogadas(3));
    }

    @Test
    public void test06_toString_contem_camposBase() {
        Player p = new Player(1, "Ana", "Java;Python", "Azul");
        p.setPosicao(3, 10);

        String s = p.toString();
        assertTrue(s.contains("1"));
        assertTrue(s.contains("Ana"));
        assertTrue(s.contains("3"));
        assertTrue(s.contains("No tools"));
        assertTrue(s.contains("Java") || s.contains("Python"));
    }

    @Test
    public void test07_inventario_add_getFerramentas_copia_remove() {
        Player p = new Player(1, "Ana", "Java", "Azul");

        assertEquals("No tools", p.getFerramentasAsString());

        Ferramentas ide = new IDE();
        Ferramentas heranca = new Heranca();

        p.adicionarFerramenta(ide);
        p.adicionarFerramenta(heranca);

        // getFerramentas devolve cópia
        List<Ferramentas> copia = p.getFerramentas();
        assertEquals(2, copia.size());
        copia.clear();
        assertEquals(2, p.getFerramentas().size(), "A lista interna não pode ser alterada por fora");

        // remover
        p.removeFerramenta(ide);
        assertFalse(p.temFerramentaComId(4));
        assertTrue(p.temFerramentaComId(0));
    }

    @Test
    public void test08_getFerramentasAsString_ordena_e_semEspacos() {
        Player p = new Player(1, "Ana", "Java", "Azul");

        p.adicionarFerramenta(new Heranca()); // "Herança"
        p.adicionarFerramenta(new IDE());     // "IDE"

        // ordenação case-insensitive: "Herança" vs "IDE"
        // normalmente "Herança;IDE" (H vem antes de I)
        assertEquals("Herança;IDE", p.getFerramentasAsString());
    }

    @Test
    public void test09_getFerramentaQueAnula_devolvePrimeiraQueAnula() {
        Player p = new Player(1, "Ana", "Java", "Azul");

        Abismos abCodigoDuplicado = new CodigoDuplicado(); // id 5
        Abismos abErroLogica = new ErroDeLogica();         // id 1

        Ferramentas heranca = new Heranca(); // anula 5
        Ferramentas testes = new FerramentaTestesUnitarios(); // anula 1,4,8

        p.adicionarFerramenta(heranca);
        p.adicionarFerramenta(testes);

        assertSame(heranca, p.getFerramentaQueAnula(abCodigoDuplicado));
        assertSame(testes, p.getFerramentaQueAnula(abErroLogica));
    }

    @Test
    public void test10_getFerramentaQueAnula_semFerramentas_ou_semAnulacao_null() {
        Player p = new Player(1, "Ana", "Java", "Azul");

        assertNull(p.getFerramentaQueAnula(new Crash())); // normalmente id 4 (depende da tua classe), mas sem tools -> null
    }

    @Test
    public void test11_estado_alive_enabled_e_morte_desativa() {
        Player p = new Player(1, "Ana", "Java", "Azul");

        p.setEnabled(false);
        assertEquals("Preso", p.getEstadoComoTexto());

        // morrer -> isEnabled passa a false
        p.setAlive(false);
        assertFalse(p.isAlive());
        assertFalse(p.isEnabled());
        assertEquals("Derrotado", p.getEstadoComoTexto());

        // tentar reativar não deve fazer efeito porque não está vivo
        p.setEnabled(true);
        assertFalse(p.isEnabled(), "Jogador derrotado não pode voltar a enabled");
    }

    @Test
    public void test12_registarPosicao_naoDuplicaPosicaoIgualSeguida() {
        Player p = new Player(1, "Ana", "Java", "Azul");

        // começa com histórico [1]
        p.registarPosicao(1);
        p.registarPosicao(1);
        p.registarPosicao(2);
        p.registarPosicao(2);

        // se duplicados não entram, o histórico termina em ... 2 e a posição anterior é 1
        assertEquals(2, p.getPosicaoJogadas(0));
        assertEquals(1, p.getPosicaoJogadas(1));
    }

    @Test
    public void test13_turnos_e_experiencia() {
        Player p = new Player(1, "Ana", "Java", "Azul");

        assertEquals(0, p.getTurnosJogador());
        p.incrementarTurno();
        p.incrementarTurno();
        assertEquals(2, p.getTurnosJogador());

        assertFalse(p.isExperiente());
        p.setExperiente(true);
        assertTrue(p.isExperiente());
    }
}
