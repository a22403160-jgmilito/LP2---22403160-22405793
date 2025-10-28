package pt.ulusofona.lp2.greatprogrammingjourney;
import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class GameManager {

    public GameManager() {
    }
    ArrayList<Player> players = new ArrayList<>();

    // guarda o tamanho do tabuleiro
    private int boardSize = 0;

    private int totalTurns = 0;

    private Integer winnerId = null;

    private int currentPlayerIndex = 0;

    // posição atual de cada jogador: id -> posição
    private final HashMap<Integer, Integer> playerPos = new HashMap<>();



    public boolean createInitialBoard(String[][] playerInfo, int worldSize){
        if (playerInfo == null || playerInfo.length < 2 || playerInfo.length > 4) {
            return false;
        }
        if (worldSize < playerInfo.length * 2) {
            return false;
        }

        Set<String> coresValidas = Set.of("Purple", "Green", "Brown", "Blue");
        Set<Integer> ids = new HashSet<>();
        Set<String> cores = new HashSet<>();

        ArrayList<Player> tempPlayers = new ArrayList<>();

        for (String[] row : playerInfo) {
            // agora 4 colunas: id, nome, linguagens, cor
            if (row == null || row.length < 4) {
                return false;
            }

            int id;
            try {
                id = Integer.parseInt(row[0]);
            } catch (NumberFormatException e) {
                return false;
            }
            if (id <= 0 || !ids.add(id)) {
                return false;
            }

            String nome = row[1];
            if (nome == null || nome.trim().isEmpty()) {
                return false;
            }
            String linguagens = row[2];
            if (linguagens == null) {
                linguagens = ""; // pode estar vazio
            }
            String cor = row[3];
            if (cor == null || !coresValidas.contains(cor) || !cores.add(cor)) {
                return false;
            }
            // Se tudo válido, cria o Player temporariamente
            tempPlayers.add(new Player(id, nome, linguagens, cor));
        }

        // se chegou aqui, tudo foi validado → grava no estado interno
        players = tempPlayers;
        boardSize = worldSize;
        // TODOS COMEÇAM NA CASA 1
        playerPos.clear();
        for (Player p : players) {
            playerPos.put(p.getId(), 1);
        }
        totalTurns = 0;
        winnerId = null;
        return true;
    }

    public String getImagePng(int nrSquare){
        // tabuleiro ainda não inicializado
        if (boardSize <= 0) {
            return null;
        }

        // fora do intervalo [1, boardSize] → null
        if (nrSquare < 1 || nrSquare > boardSize) {
            return null;
        }

        // última casa (meta)
        if (nrSquare == boardSize) {
            return "glory.png";
        }
        return null;

    }

    public String[] getProgrammerInfo(int id){
        if (players == null || players.isEmpty()) {
            return new String[] {"", "", "", ""};
        }
        for (Player p : players) {
            if (p.getId() == id) {
                return new String[] {
                        String.valueOf(p.getId()),
                        p.getNome(),
                        p.getLinguagens(),
                        p.getCor()
                };
            }
        }
        return new String[] {"", "", "", ""};
    }

    public String getProgrammerInfoAsStr(int id){
        if (players == null || players.isEmpty()) {
            return null;
        }

        for (Player p : players) {
            if (p.getId() == id) {
                int pos = playerPos.getOrDefault(id, 1);
                String estado = (boardSize > 0 && pos == boardSize) ? "Chegou à Meta" : "Em Jogo";

                return p.getId() + " | "
                        + p.getNome() + " | "
                        + pos + " | "
                        + (p.getLinguagens().isEmpty() ? "Sem linguagens" : p.getLinguagens())
                        + " | " + estado;
            }
        }
        return null;
    }

    public String[] getSlotInfo(int position){
        if (boardSize <= 0 || position < 1 || position > boardSize) {
            return null;
        }
        if (players == null || players.isEmpty()) {
            return null;
        }

        ArrayList<String> ids = new ArrayList<>();
        for (Player p : players) {
            int pos = playerPos.getOrDefault(p.getId(), 1);
            if (pos == position) {
                ids.add(String.valueOf(p.getId()));
            }
        }
        return ids.isEmpty() ? null : ids.toArray(new String[0]);
    }

    public int getCurrentPlayerID(){
        if (players == null || players.isEmpty()) {
            return 0;
        }

        if (currentPlayerIndex < 0 || currentPlayerIndex >= players.size()) {
            currentPlayerIndex = 0;
        }
        // devolve o ID do jogador atual
        return players.get(currentPlayerIndex).getId();
    }

    public boolean moveCurrentPlayer(int nrSpaces){
        if (players == null || players.isEmpty() || boardSize <= 0) {
            return false;
        }

        // jogador atual
        Player atual = players.get(currentPlayerIndex);
        int id = atual.getId();

        // posição atual e nova posição
        int posAtual = playerPos.getOrDefault(id, 1);
        int novaPos = posAtual + nrSpaces;

        // impede ultrapassar o final
        if (novaPos > boardSize) {
            novaPos = boardSize;
        }

        // atualiza posição
        playerPos.put(id, novaPos);

        // se chegou ao fim do tabuleiro, não muda de jogador
        if (novaPos < boardSize) {
            // passa o turno ao próximo jogador (ordem circular)
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        }

        // Atualiza contador de turnos
        totalTurns++;

        // Regista vencedor se alguém chegou à meta
        if (novaPos == boardSize && winnerId == null) {
            winnerId = id;
        }

        return true;
    }

    public boolean gameIsOver() {
        if (boardSize <= 0 || playerPos.isEmpty()) {
            return false;
        }

        for (int pos : playerPos.values()) {
            if (pos == boardSize) {
                return true; // Pelo menos um jogador chegou à meta
            }
        }
        return false; // Nenhum jogador chegou ainda
    }


    public ArrayList<String> getGameResults() {
        ArrayList<String> out = new ArrayList<>();

        // TÍTULO
        out.add("THE GREAT PROGRAMMING JOURNEY");
        out.add(""); // linha vazia

        // NR. DE TURNOS
        out.add("NR. DE TURNOS");
        out.add(String.valueOf(totalTurns));
        out.add(""); // linha vazia

        // VENCEDOR
        out.add("VENCEDOR");
        String vencedorNome = "";
        if (winnerId != null) {
            for (Player p : players) {
                if (p.getId() == winnerId) {
                    vencedorNome = p.getNome();
                    break;
                }
            }
        }
        out.add(vencedorNome);
        out.add(""); // linha vazia

        // RESTANTES
        out.add("RESTANTES");

        ArrayList<Player> restantes = new ArrayList<>();
        for (Player p : players) {
            if (winnerId == null || p.getId() != winnerId) {
                restantes.add(p);
            }
        }

        // ordenar por posição desc; em empate, por nome asc
        restantes.sort((a, b) -> {
            int posA = playerPos.getOrDefault(a.getId(), 1);
            int posB = playerPos.getOrDefault(b.getId(), 1);
            if (posA != posB) {
                return Integer.compare(posB, posA); // desc
            }
            return a.getNome().compareToIgnoreCase(b.getNome());
        });

        for (Player p : restantes) {
            int pos = playerPos.getOrDefault(p.getId(), 1);
            out.add(p.getNome() + " " + pos);
        }

        return out;
    }

    // Nao obrigatorio
    public JPanel getAuthorsPanel(){
        return null;
    }
    public HashMap customizeBoard() {
        // Podes devolver vazio (a GUI usa defaults)
        return new HashMap();
        // Se no futuro quiseres customizar algo, podes colocar pares chave-valor aqui.
    }
}
