package pt.ulusofona.lp2.greatprogrammingjourney;

import javax.swing.*;
import java.awt.*;
import java.io.*;
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

    // parte 2
    private Abismos[] abismosNaPosicao;        // índice = posição no tabuleiro
    private Ferramentas[] ferramentasNaPosicao;
    private int valorDadoLancado = 0;

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
        return createInitialBoard(playerInfo, worldSize, null);
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
    public String getProgrammersInfo() {
        if (board == null || players.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            String info = getProgrammerInfoAsStr(p.getId());
            if (info != null) {
                sb.append(info);
                if (i < players.size() - 1) {
                    sb.append("\n");
                }
            }
        }

        return sb.toString();
    }

    //alterar essa funcao para chamar o to string do player e nao repetir cogigo
    public String getProgrammerInfoAsStr(int id) {
        String[] info = getProgrammerInfo(id);
        if (info == null) {
            return null;
        }

        // id | nome | posicao | ferramentas | linguagens | estado
        return info[0] + " | " + info[1] + " | " + info[4] + " | " + info[5] + " | " + info[2] + " | " + info[6];
    }
    public String[] getSlotInfo(int position) {

        // posição inválida -> null
        if (board == null || !board.posicaoValida(position)) {
            return null;
        }

        // posição válida -> SEMPRE array de 3 strings
        String[] res = new String[]{"", "", ""};

        // [0] IDs dos jogadores
        List<Integer> ids = board.getJogadoresNaPosicao(position);
        if (ids != null && !ids.isEmpty()) {
            ArrayList<String> idsStr = new ArrayList<>();
            for (Integer id : ids) {
                idsStr.add(String.valueOf(id));
            }
            res[0] = String.join(",", idsStr);
        }

        // [1] e [2] Abismo
        if (abismosNaPosicao != null && abismosNaPosicao[position] != null) {
            Abismos ab = abismosNaPosicao[position];
            res[1] = ab.getNome();           // descrição correta
            res[2] = "A:" + ab.getId();      // tipo

        }
        // [1] e [2] Ferramenta
        else if (ferramentasNaPosicao != null && ferramentasNaPosicao[position] != null) {
            Ferramentas f = ferramentasNaPosicao[position];
            res[1] = f.getNome();
            res[2] = "T:" + f.getId();
        }

        return res;
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

        // guardar o valor do dado para usar nos abismos (Erro de Lógica, etc.)
        valorDadoLancado = nrSpaces;
        board.movePlayer(id, nrSpaces);
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

                if (p1Goal && !p2Goal) {
                    return -1;
                }
                if (!p1Goal && p2Goal) {
                    return 1;
                }
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
    public String[] getProgrammerInfo(int id) {
        if (players.isEmpty() || board == null) {
            return null;
        }

        Player p = getPlayerById(id);
        if (p == null) {
            return null;
        }

        int pos = board.getPlayerPosicao(id);

        // Linguagens na ORDEM ORIGINAL (não usar normalizadas aqui)
        String linguagens = p.getLinguagensNormalizadas();
        if (linguagens == null) {
            linguagens = "";
        }

        // Cor em MAIÚSCULAS
        String cor = p.getCor() == null ? "" : p.getCor();


        // Ferramentas ORDENADAS
        String ferramentas = p.getFerramentasAsString();

        String estado = p.getEstadoComoTexto();

        return new String[]{
                String.valueOf(p.getId()),   // [0] ID
                p.getNome(),                 // [1] Nome
                linguagens,                  // [2] Linguagens
                cor,                         // [3] Cor
                String.valueOf(pos),         // [4] Posição
                ferramentas,                 // [5] Ferramentas
                estado                       // [6] Estado
        };
    }





    //part 2
    public boolean createInitialBoard(String[][] playerInfo, int worldSize, String[][] abyssesAndTools) {
        if (playerInfo == null || playerInfo.length == 0) {
            return false;
        }

        players.clear();

        for (String[] info : playerInfo) {
            if (info == null || info.length < 4) {
                return false; // dado de jogador mal formado
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

        if (players.size() < 2 || players.size() > 4) {
            players.clear();
            board = null;
            abismosNaPosicao = null;
            ferramentasNaPosicao = null;
            return false;
        }

        int tamanhoMinimo = 2 * players.size();
        if (worldSize < tamanhoMinimo) {
            players.clear();
            board = null;
            abismosNaPosicao = null;
            ferramentasNaPosicao = null;
            return false;
        }

        board = new Board(worldSize, players);
        abismosNaPosicao = new Abismos[worldSize + 1];
        ferramentasNaPosicao = new Ferramentas[worldSize + 1];

        if (abyssesAndTools != null) {
            for (String[] linha : abyssesAndTools) {
                if (linha == null || linha.length < 3) {
                    return false;
                }
                int tipoLinha;
                int tipoId;
                int pos;
                try {
                    tipoLinha = Integer.parseInt(linha[0]);
                    tipoId = Integer.parseInt(linha[1]);
                    pos = Integer.parseInt(linha[2]);
                } catch (NumberFormatException e) {
                    return false;
                }
                // posição tem de ser válida
                if (!board.posicaoValida(pos)) {
                    return false;
                }
                if (tipoLinha == 0) {
                    // ABISMO
                    if (abismosNaPosicao[pos] != null) {
                        return false;
                    }
                    Abismos ab = criarAbismoPorId(tipoId);
                    if (ab == null) {
                        return false;
                    }
                    abismosNaPosicao[pos] = ab;
                } else if (tipoLinha == 1) {
                    // FERRAMENTA
                    if (ferramentasNaPosicao[pos] != null) {
                        return false;
                    }
                    Ferramentas f = criarFerramentaPorId(tipoId);
                    if (f == null) {
                        return false;
                    }
                    ferramentasNaPosicao[pos] = f;
                } else {
                    return false;
                }
            }
        }
        currentPlayerIndex = 0;
        totalTurns = 1;
        winnerId = null;
        return true;
    }

    private Abismos criarAbismoPorId(int id) {
        switch (id) {
            case 0: return new ErroDeSintaxe();
            case 1: return new ErroDeLogica();
            case 2: return new ExceptionAbismo();
            case 3: return new FileNotFoundException();
            case 4: return new Crash();
            case 5: return new CodigoDuplicado();
            case 6: return new EfeitosSecundarios();
            case 7: return new BlueScreenOfDeath();
            case 8: return new CicloInfinito();
            case 9: return new SegmentationFault();
            default:
                return null;
        }
    }
    private Ferramentas criarFerramentaPorId(int id) {
        switch (id) {
            case 0: return new Heranca();
            case 1: return new ProgramacaoFuncional();
            case 2: return new FerramentaTestesUnitarios();
            case 3: return new TratamentoDeExcepcoes();
            case 4: return new IDE();
            case 5: return new AjudaDoProfessor();
            default:
                return null;
        }
    }
    public String reactToAbyssOrTool() {
        if (board == null || players.isEmpty()) {
            return null;
        }

        Player atual = players.get(currentPlayerIndex);
        int idJogador = atual.getId();
        int pos = board.getPlayerPosicao(idJogador);

        StringBuilder mensagem = new StringBuilder();

        // 1) Verificar se há FERRAMENTA na casa
        Ferramentas ferramenta = null;
        if (ferramentasNaPosicao != null && pos >= 1 && pos < ferramentasNaPosicao.length) {
            ferramenta = ferramentasNaPosicao[pos];
        }

        if (ferramenta != null) {
            // jogador apanha a ferramenta
            atual.adicionarFerramenta(ferramenta);
            // remover a ferramenta do tabuleiro
            ferramentasNaPosicao[pos] = null;

            mensagem.append("O programador ")
                    .append(atual.getNome())
                    .append(" apanhou a ferramenta ")
                    .append(ferramenta.getNome())
                    .append(".");
        }

        // 2) Verificar se há ABISMO na casa
        Abismos abismo = null;
        if (abismosNaPosicao != null
                && pos >= 1 && pos < abismosNaPosicao.length) {
            abismo = abismosNaPosicao[pos];
        }

        if (abismo != null) {
            // ver se o jogador tem ferramenta que anula este abismo
            Ferramentas anuladora = atual.getFerramentaQueAnula(abismo);

            if (anuladora != null) {
                // Consome a ferramenta e anula o efeito
                atual.removeFerramenta(anuladora);

                if (mensagem.length() > 0) {
                    mensagem.append(" ");
                }

                mensagem.append("O programador ")
                        .append(atual.getNome())
                        .append(" usou a ferramenta ")
                        .append(anuladora.getNome())
                        .append(" para anular o abismo ")
                        .append(abismo.getNome())
                        .append(".");
                // Não chamamos aplicarEfeito
            } else {
                // Não tem ferramenta aplicável → sofre o efeito do abismo
                String msgAbismo = abismo.aplicarEfeito(atual, board, valorDadoLancado);

                if (msgAbismo != null && !msgAbismo.isEmpty()) {
                    if (mensagem.length() > 0) {
                        mensagem.append(" ");
                    }
                    mensagem.append(msgAbismo);
                }
            }
        }

        // 3) Atualizar vitória (depois do efeito do abismo)
        int novaPos = board.getPlayerPosicao(idJogador);
        if (board.posicaoVitoria(novaPos) && winnerId == null) {
            winnerId = idJogador;
        }

        // 4) Avançar o turno para o próximo jogador e contar turno
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        totalTurns++;

        // 5) Se não aconteceu nada na casa (sem ferramenta e sem abismo), devolve null
        if (mensagem.length() == 0) {
            return null;
        }

        return mensagem.toString();
    }
    public boolean saveGame(File file) {
        if (board == null || players.isEmpty() || file == null) {
            return false;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {

            // 1) Linha WORLD
            int worldSize = board.getSize();
            int winner = (winnerId == null) ? -1 : winnerId;

            bw.write("WORLD;" + worldSize + ";" + totalTurns + ";" + currentPlayerIndex + ";" + winner);
            bw.newLine();

            // 2) Número de jogadores
            bw.write("PLAYERS;" + players.size());
            bw.newLine();

            // 3) Cada jogador numa linha PLAYER
            for (Player p : players) {
                int pos = board.getPlayerPosicao(p.getId());

                String nome = p.getNome();
                String linguagens = p.getLinguagensNormalizadas();
                String cor = p.getCor();

                bw.write("PLAYER;" + p.getId() + ";" + nome + ";" + linguagens + ";" + cor + ";" + pos);
                bw.newLine();
            }
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public void loadGame(File file) throws InvalidFileException, java.io.FileNotFoundException {
        if (file == null || !file.exists()) {
            throw new java.io.FileNotFoundException("Ficheiro não encontrado: " + file);
        }
        // Variáveis temporárias
        int worldSize;
        int loadedTotalTurns;
        int loadedCurrentPlayerIndex;
        Integer loadedWinnerId;

        ArrayList<Player> loadedPlayers = new ArrayList<>();
        ArrayList<Integer> loadedPositions = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String line = br.readLine();
            if (line == null) {
                throw new InvalidFileException("Ficheiro vazio.");
            }

            // 1) WORLD;...
            String[] partes = line.split(";");
            if (partes.length != 5 || !partes[0].equals("WORLD")) {
                throw new InvalidFileException("Cabeçalho WORLD inválido.");
            }

            try {
                worldSize = Integer.parseInt(partes[1]);
                loadedTotalTurns = Integer.parseInt(partes[2]);
                loadedCurrentPlayerIndex = Integer.parseInt(partes[3]);
                int wId = Integer.parseInt(partes[4]);
                loadedWinnerId = (wId < 0) ? null : wId;
            } catch (NumberFormatException e) {
                throw new InvalidFileException("Valores numéricos inválidos no cabeçalho WORLD.");
            }

            if (worldSize <= 0) {
                throw new InvalidFileException("Tamanho do tabuleiro inválido: " + worldSize);
            }

            // 2) PLAYERS;N
            line = br.readLine();
            if (line == null) {
                throw new InvalidFileException("Linha PLAYERS em falta.");
            }

            partes = line.split(";");
            if (partes.length != 2 || !partes[0].equals("PLAYERS")) {
                throw new InvalidFileException("Linha PLAYERS inválida.");
            }

            int nPlayers;
            try {
                nPlayers = Integer.parseInt(partes[1]);
            } catch (NumberFormatException e) {
                throw new InvalidFileException("Número de jogadores inválido.");
            }

            if (nPlayers <= 0) {
                throw new InvalidFileException("Número de jogadores tem de ser > 0.");
            }

            // 3) N linhas PLAYER;...
            for (int i = 0; i < nPlayers; i++) {
                line = br.readLine();
                if (line == null) {
                    throw new InvalidFileException("Linha PLAYER em falta (esperava " + nPlayers + ").");
                }

                partes = line.split(";");
                if (partes.length != 6 || !partes[0].equals("PLAYER")) {
                    throw new InvalidFileException("Linha PLAYER inválida: " + line);
                }

                try {
                    int id = Integer.parseInt(partes[1]);
                    String nome = partes[2];
                    String linguagens = partes[3];
                    String cor = partes[4];
                    int pos = Integer.parseInt(partes[5]);

                    if (pos < 1 || pos > worldSize) {
                        throw new InvalidFileException("Posição inválida para o jogador " + id + ": " + pos);
                    }

                    Player p = new Player(id, nome, linguagens, cor);
                    loadedPlayers.add(p);
                    loadedPositions.add(pos);

                } catch (NumberFormatException e) {
                    throw new InvalidFileException("Valores numéricos inválidos na linha PLAYER: " + line);
                }
            }

            // Validação extra: currentPlayerIndex
            if (loadedCurrentPlayerIndex < 0 || loadedCurrentPlayerIndex >= loadedPlayers.size()) {
                throw new InvalidFileException("Índice de jogador atual inválido: " + loadedCurrentPlayerIndex);
            }

            // --- Se chegou aqui, os dados são válidos → aplicar ao jogo ---

            players.clear();
            players.addAll(loadedPlayers);

            // Cria novo tabuleiro (isto põe todos na posição 1)
            board = new Board(worldSize, players);

            // Limpa abismos e ferramentas (por agora não estamos a restaurar isso)
            abismosNaPosicao = new Abismos[worldSize + 1];
            ferramentasNaPosicao = new Ferramentas[worldSize + 1];

            // Coloca cada jogador na posição correta
            for (int i = 0; i < players.size(); i++) {
                Player p = players.get(i);
                int pos = loadedPositions.get(i);

                // todos estão em 1; queremos ir até 'pos' usando a lógica normal do jogo
                int delta = pos - 1;  // a partir da casa 1

                if (delta > 0) {
                    board.movePlayer(p.getId(), delta);
                }
            }

            // Restaura estado do jogo
            totalTurns = loadedTotalTurns;
            currentPlayerIndex = loadedCurrentPlayerIndex;
            winnerId = loadedWinnerId;

        } catch (IOException e) {
            throw new InvalidFileException("Erro ao ler o ficheiro: " + e.getMessage());
        }
    }
}

