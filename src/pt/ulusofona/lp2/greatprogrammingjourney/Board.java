package pt.ulusofona.lp2.greatprogrammingjourney;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {

    private final int size;  // tamanho do tabuleiro
    private final List<Player> players;

    public Board(int size, List<Player> players) {
        if (size <= 0) {
            throw new IllegalArgumentException("Board size must be > 0");
        }
        this.size = size;
        this.players = players;

        // garantir que todos começam na casa 1 e isso entra no histórico
        for (Player p : players) {
            p.setPosicao(1, size);
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
        for (Player p : players) {
            if (p.getId() == playerId) {
                return p.getPosicao();
            }
        }
        // por default, se não encontrar, considera na posição 1
        return 1;
    }
    public List<Integer> getJogadoresNaPosicao(int position) {
        List<Integer> ids = new ArrayList<>();
        if (!posicaoValida(position)) {
            return ids;
        }
        for (Player p : players) {
            if (p.getPosicao() == position) {
                ids.add(p.getId());
            }
        }
        return ids;
    }
    public int movePlayer(int playerId, int nrSpaces) {
        if (nrSpaces <= 0) {
            return getPlayerPosicao(playerId);
        }
        Player alvo = null;
        for (Player p : players) {
            if (p.getId() == playerId) {
                alvo = p;
                break;
            }
        }
        if (alvo == null) {
            return -1;
        }
        int pos = alvo.getPosicao();
        while (nrSpaces > 0) {
            pos++;
            if (pos > size) {
                pos = size - (pos - size);
            }
            nrSpaces--;
        }
        // usa SEMPRE o setPosicao do Player
        alvo.setPosicao(pos, size);
        return alvo.getPosicao();
    }
    public boolean temJogadorNaPosicaoFinal() {
        for (Player p : players) {
            if (p.getPosicao() == size) {
                return true;
            }
        }
        return false;
    }
    public Map<Integer, Integer> getTodasPosicoes() {
        Map<Integer, Integer> mapa = new HashMap<>();
        for (Player p : players) {
            mapa.put(p.getId(), p.getPosicao());
        }
        return mapa;
    }
}
