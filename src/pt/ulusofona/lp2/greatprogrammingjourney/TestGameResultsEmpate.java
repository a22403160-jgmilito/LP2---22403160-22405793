package pt.ulusofona.lp2.greatprogrammingjourney;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestGameResultsEmpate {

    @Test
    void empate_total_sem_vencedor_devolve_lista_com_participantes_ordenados() throws Exception {

        GameManager gm = new GameManager();

        String[][] players = {
                {"1", "Ana", "Java", "Azul"},
                {"2", "Bruno", "Java", "Vermelho"},
                {"3", "Carla", "Java", "Verde"},
                {"4", "Diana", "Java", "Rosa"}
        };

        assertTrue(gm.createInitialBoard(players, 12));

        // Prender todos (enabled=false), sem ferramentas => gameIsOver deve dar true e winnerId continuar null
        Field fPlayers = GameManager.class.getDeclaredField("players");
        fPlayers.setAccessible(true);
        @SuppressWarnings("unchecked")
        ArrayList<Player> ps = (ArrayList<Player>) fPlayers.get(gm);

        for (Player p : ps) {
            p.setEnabled(false);
        }

        assertTrue(gm.gameIsOver(), "Se todos estão presos e sem ferramentas, o jogo deve terminar (empate).");

        ArrayList<String> results = gm.getGameResults();
        assertNotNull(results);
        assertTrue(results.contains("O jogo terminou empatado."), "Deve indicar empate.");
        assertTrue(results.contains("Participantes:"), "Deve listar participantes.");

        // Todos começam na posição 1, então desempata pelo nome (ascendente): Ana, Bruno, Carla, Diana
        int idx = results.indexOf("Participantes:");
        assertTrue(idx >= 0);

        List<String> linhas = results.subList(idx + 1, results.size());

        assertTrue(linhas.get(0).startsWith("Ana : 1"), "Ordem esperada: Ana primeiro");
        assertTrue(linhas.get(1).startsWith("Bruno : 1"), "Depois Bruno");
        assertTrue(linhas.get(2).startsWith("Carla : 1"), "Depois Carla");
        assertTrue(linhas.get(3).startsWith("Diana : 1"), "Depois Diana");

        // Motivo esperado (por default no teu código): se está vivo e preso => "Ciclo Infinito"
        assertTrue(linhas.get(0).contains("Ciclo Infinito"));
    }
}
