package pt.ulusofona.lp2.greatprogrammingjourney;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {

    private final int size;  // tamanho do tabuleiro
    private final Map<Integer, Integer> playerPos = new HashMap<>();

    public Board(int size, List<Player> players) {
        if (size <= 0) {
            throw new IllegalArgumentException("Board size must be > 0");
        }
        this.size = size;

        // todos os jogadores começam na casa 1 (1-based)
        for (Player p : players) {
            playerPos.put(p.getId(), 1);
        }
    }

    public int getSize() {
        return size;
    }
    public boolean posicaoValida(int position) {
        return position >= 1 && position <= size;
    }
    public boolean posicaoVitoria(int position) {
        return position == size;
    }
    public int getPlayerPosicao(int playerId) {
        return playerPos.getOrDefault(playerId, 1);
    }
    public List<Integer> getJogadoresNaPosicao(int position) {
        List<Integer> ids = new ArrayList<>();
        if (!posicaoValida(position)) {
            return ids;
        }
        for (Map.Entry<Integer, Integer> entry : playerPos.entrySet()) {
            if (entry.getValue() == position) {
                ids.add(entry.getKey());
            }
        }
        return ids;
    }
    public int movePlayer(int playerId, int nrSpaces) {
        int pos = getPlayerPosicao(playerId);

        while (nrSpaces > 0) {
            pos++;
            if (pos > size) {
                pos = size - (pos - size);
            }
            nrSpaces--;
        }

        playerPos.put(playerId, pos);
        return pos;
    }
    public boolean temJogadorNaPosicaoFinal() {
        for (int pos : playerPos.values()) {
            if (pos == size) {
                return true;
            }
        }
        return false;
    }
    public Map<Integer, Integer> getTodasPosicoes() {
        return new HashMap<>(playerPos);
    }
}
