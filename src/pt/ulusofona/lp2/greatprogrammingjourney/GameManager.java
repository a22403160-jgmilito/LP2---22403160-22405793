package pt.ulusofona.lp2.greatprogrammingjourney;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class GameManager {

    private final ArrayList<Player> players = new ArrayList<>();
    private Board board;

    // Estado do jogo
    private int totalTurns = 0;
    private Integer winnerId = null;
    private int currentPlayerIndex = 0;

    public GameManager() {
    }

    private Player getPlayerById(int id) {
        for (Player p : players) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }
    public boolean createInitialBoard(String[][] playerInfo, int worldSize) {
        if (playerInfo == null || playerInfo.length == 0) {
            return false;
        }
        if (worldSize <= 0) {
            return false;
        }

        players.clear();

        // Cada linha de playerInfo: [0] id, [1] nome, [2] linguagens, [3] cor
        for (String[] info : playerInfo) {
            if (info == null || info.length < 4) {
                continue;
            }

            int id;
            try {
                id = Integer.parseInt(info[0]);
            } catch (NumberFormatException e) {
                return false;
            }

            String nome = info[1];
            String linguagens = info[2];
            String cor = info[3];

            players.add(new Player(id, nome, linguagens, cor));
        }

        if (players.isEmpty()) {
            return false;
        }

        // cria o tabuleiro com todos os jogadores na posição inicial (1)
        board = new Board(worldSize, players);

        currentPlayerIndex = 0;
        totalTurns = 1;
        winnerId = null;

        return true;
    }
    public String getImagePng(int nrSquare) {
        if (board == null) {
            return null;
        }
        if (!board.posicaoValida(nrSquare)) {
            return null;
        }
        // Casa final
        if (board.posicaoVitoria(nrSquare)) {
            return "glory.png";
        }
        return null;
    }
    public String[] getProgrammerInfo(int id) {
        Player p = getPlayerById(id);
        if (p == null) {
            return null;
        }
        return p.asArray();
    }
    public String getProgrammerInfoAsStr(int id) {
        if (players.isEmpty() || board == null) {
            return null;
        }

        Player p = getPlayerById(id);
        if (p == null) {
            return null;
        }

        int pos = board.getPlayerPosicao(id);
        String estado = board.posicaoVitoria(pos) ? "Vencedor" : "Em Jogo";

        String linguagens = p.getLinguagensNormalizadas();
        if (linguagens == null || linguagens.isEmpty()) {
            linguagens = "Sem linguagens";
        }

        // Formato típico: "id | nome | pos | linguagens | estado"
        return p.getId() + " | "
                + p.getNome() + " | "
                + pos + " | "
                + linguagens + " | "
                + estado;
    }
    public String[] getSlotInfo(int position) {
        if (board == null || !board.posicaoValida(position)) {
            return null;
        }
        if (players.isEmpty()) {
            return null;
        }
        List<Integer> ids = board.getJogadoresNaPosicao(position);
        if (ids.isEmpty()) {
            return null;
        }
        ArrayList<String> idsStr = new ArrayList<>();
        for (Integer id : ids) {
            idsStr.add(String.valueOf(id));
        }
        String todosIds = String.join(",", idsStr);
        return new String[]{todosIds};
    }
    public int getCurrentPlayerID() {
        if (players.isEmpty()) {
            return -1;
        }
        return players.get(currentPlayerIndex).getId();
    }
    public boolean moveCurrentPlayer(int nrSpaces) {
        if (players.isEmpty() || board == null || nrSpaces <= 0) {
            return false;
        }

        Player atual = players.get(currentPlayerIndex);
        int id = atual.getId();

        int novaPos = board.movePlayer(id, nrSpaces);

        // Se ainda não chegou ao fim, passa a vez
        if (!board.posicaoVitoria(novaPos)) {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        }

        totalTurns++;

        // Regista vencedor (só o primeiro)
        if (board.posicaoVitoria(novaPos) && winnerId == null) {
            winnerId = id;
        }

        return true;
    }
    public boolean gameIsOver() {
        if (board == null) {
            return false;
        }
        if (winnerId != null) {
            return true;
        }
        // Ou qualquer jogador na posição final
        return board.temJogadorNaPosicaoFinal();
    }
    public ArrayList<String> getGameResults() {
        ArrayList<String> resultados = new ArrayList<>();

        if (board == null || players.isEmpty()) {
            return resultados;
        }

        ArrayList<Player> ordenado = new ArrayList<>(players);

        // Ordena: primeiro quem chegou ao fim; depois pela posição; depois pelo id
        ordenado.sort(new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                int pos1 = board.getPlayerPosicao(p1.getId());
                int pos2 = board.getPlayerPosicao(p2.getId());

                boolean p1Goal = board.posicaoVitoria(pos1);
                boolean p2Goal = board.posicaoVitoria(pos2);

                if (p1Goal && !p2Goal) return -1;
                if (!p1Goal && p2Goal) return 1;

                // Se nenhum ou ambos chegaram ao fim, ordena por posição descrescente
                if (pos1 != pos2) {
                    return Integer.compare(pos2, pos1);
                }

                // Desempate por id
                return Integer.compare(p1.getId(), p2.getId());
            }
        });

        for (int i = 0; i < ordenado.size(); i++) {
            Player p = ordenado.get(i);
            int pos = board.getPlayerPosicao(p.getId());
            String linha = (i + 1) + "º: " + p.getId() + " | " + p.getNome() + " | " + pos;
            resultados.add(linha);
        }

        return resultados;
    }
    public JPanel getAuthorsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        JLabel titulo = new JLabel("Great Programming Journey");
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel autor1 = new JLabel("Autor: (o teu nome aqui)");
        autor1.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(titulo);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(autor1);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        return panel;
    }
    public HashMap<String, String> customizeBoard() {
        HashMap<String, String> config = new HashMap<>();

        // Aqui podes configurar coisas que a GUI possa usar, conforme o enunciado.
        // Como não temos o enunciado completo, deixo algo simples:
        // (Se o professor tiver indicado chaves específicas, mete-as aqui.)
        config.put("title", "Great Programming Journey");
        // Exemplo: cores, imagens, etc. (se a GUI usar)
        // config.put("board-color", "#FFFFFF");

        return config;
    }

    //part 2
    public String getProgrammersInfo(){
        return "";
    }
    public String reactToAbyssOrTool(){
        return "";
    }

    /*
    public void loadGame(File file){
        throws InvalidFileException, FileNotFoundException;
    }
    */
    public boolean saveGame(File file){
        return false;
    }
}
