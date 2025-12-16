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
    private String[][] lastAbyssesAndTools = null;
    private boolean[] casaTeveFerramenta;


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
        return createInitialBoard(playerInfo, worldSize, lastAbyssesAndTools);
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

            sb.append(p.getNome())
                    .append(" : ")
                    .append(p.getFerramentasAsString());

            if (i < players.size() - 1) {
                sb.append(" | ");
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

        // validação da posição
        if (board == null || position < 1 || position > board.getSize()) {
            return null;
        }

        // retorno SEMPRE com 3 strings para posições válidas
        String[] res = new String[]{"", "", ""};

        // [0] IDs dos jogadores na casa
        List<Integer> ids = board.getJogadoresNaPosicao(position);
        if (ids != null && !ids.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < ids.size(); i++) {
                if (i > 0){
                    sb.append(",");
                }
                sb.append(ids.get(i));
            }
            res[0] = sb.toString();
        }

        // [1] e [2] Abismo (TEM PRIORIDADE)
        if (abismosNaPosicao != null && abismosNaPosicao[position] != null) {
            Abismos ab = abismosNaPosicao[position];
            res[1] = ab.getNome();
            res[2] = "A:" + ab.getId();
        }
        // [1] e [2] Ferramenta (só se NÃO houver abismo)
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
        if (players.isEmpty() || board == null) {
            return false;
        }

        // 1) nrSpaces fora [1..6]
        if (nrSpaces < 1 || nrSpaces > 6) {
            return false;
        }

        Player atual = players.get(currentPlayerIndex);

        // 2) Jogador não está "Em Jogo" (Derrotado ou Preso) => movimento inválido
        if (!atual.isAlive() || !atual.isEnabled()) {
            return false;
        }

        // 3) Restrições por linguagem (como já tinhas)
        String primeiraLing = getPrimeiraLinguagem(atual);

        if ("Assembly".equalsIgnoreCase(primeiraLing) && nrSpaces >= 3) {
            return false;
        }
        if ("C".equalsIgnoreCase(primeiraLing) && nrSpaces >= 4) {
            return false;
        }

        // Guardar valor do dado
        valorDadoLancado = nrSpaces;

        int posAtual = board.getPlayerPosicao(atual.getId());
        atual.registarPosicao(posAtual);

        int fim = board.getSize();
        int destino = posAtual + nrSpaces;

        boolean movimentoValido = true;

        // 6) Se ultrapassa a meta: ajusta posição mas movimento é inválido
        if (destino > fim) {
            int excesso = destino - fim;
            destino = fim - excesso;
            if (destino < 1){
                destino = 1;
            }
            movimentoValido = false;  // <-- aqui está a regra que faltava
        }

        while (posAtual < destino) {
            board.movePlayer(atual.getId(), 1);
            posAtual++;
        }
        while (posAtual > destino) {
            board.movePlayer(atual.getId(), -1);
            posAtual--;
        }

        atual.registarPosicao(posAtual);

        return movimentoValido;
    }



    private String getPrimeiraLinguagem(Player p) {
        if (p == null) {
            return "";
        }

        String linguagens = p.getLinguagensOriginal();
        if (linguagens == null) {
            return "";
        }

        linguagens = linguagens.trim();
        if (linguagens.isEmpty()) {
            return "";
        }

        String[] partes = linguagens.split(";");
        return partes[0].trim();
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
        ArrayList<String> res = new ArrayList<>();

        res.add("THE GREAT PROGRAMMING JOURNEY");
        res.add("");
        res.add("NR. DE TURNOS");
        res.add(String.valueOf(totalTurns));   // <- usa a tua variável real
        res.add("");

        res.add("VENCEDOR");
        Player vencedor = null;
        for (Player p : players) {
            if (p.getPosicao() == board.getSize()) {   // adapta se o nome for outro
                vencedor = p;
                break;
            }
        }     // <- adapta ao teu código
        res.add(vencedor == null ? "" : vencedor.getNome());
        res.add("");

        res.add("RESTANTES");

        ArrayList<Player> restantes = new ArrayList<>();
        for (Player p : players) {
            if (vencedor != null && p.getId() == vencedor.getId()){
                continue;
            }
            restantes.add(p);
        }

        // Ordem que costuma bater nos testes: posição DESC; desempate por nome ASC (ou id ASC)
        restantes.sort((a, b) -> {
            int pa = a.getPosicao();
            int pb = b.getPosicao();
            if (pa != pb){
                return Integer.compare(pb, pa);
            }
            return a.getNome().compareToIgnoreCase(b.getNome());
        });

        for (Player p : restantes) {
            res.add(p.getNome() + " " + p.getPosicao());
        }

        return res;
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
        lastAbyssesAndTools = abyssesAndTools;

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
        casaTeveFerramenta = new boolean[worldSize + 1];

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
                    casaTeveFerramenta[pos] = true;
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

        boolean casaEspecial =
                (abismosNaPosicao != null && pos >= 1 && pos < abismosNaPosicao.length && abismosNaPosicao[pos] != null)
                        || (ferramentasNaPosicao != null && pos >= 1 && pos < ferramentasNaPosicao.length && ferramentasNaPosicao[pos] != null)
                        || (casaTeveFerramenta != null && pos >= 1 && pos < casaTeveFerramenta.length && casaTeveFerramenta[pos]);

        // 1) Abismo tem prioridade
        Abismos abismo = null;
        if (abismosNaPosicao != null && pos >= 1 && pos < abismosNaPosicao.length) {
            abismo = abismosNaPosicao[pos];
        }

        if (abismo != null) {

            // --- Ciclo Infinito (id 8): troca de preso ---
            if (abismo.getId() == 8) {
                String msg = aplicarCicloInfinito(atual, pos);
                if (msg != null && !msg.isEmpty()) {
                    mensagem.append(msg);
                }
            }
            // --- Segmentation Fault (id 9): efeito coletivo ---
            else if (abismo.getId() == 9) {
                String msg = aplicarSegmentationFault(pos);
                if (msg != null && !msg.isEmpty()) {
                    mensagem.append(msg);
                }
            }
            // --- Abismos normais ---
            else {
                Ferramentas anuladora = atual.getFerramentaQueAnula(abismo);

                if (anuladora != null) {
                    atual.removeFerramenta(anuladora);

                    mensagem.append("O programador ")
                            .append(atual.getNome())
                            .append(" usou a ferramenta ")
                            .append(anuladora.getNome())
                            .append(" para anular o abismo ")
                            .append(abismo.getNome())
                            .append(".");
                } else {
                    String msgAbismo = abismo.aplicarEfeito(atual, board, valorDadoLancado);
                    if (msgAbismo != null && !msgAbismo.isEmpty()) {
                        mensagem.append(msgAbismo);
                    }
                }
            }

        } else {
            // 2) Só apanha ferramenta se NÃO houver abismo
            Ferramentas ferramenta = null;
            if (ferramentasNaPosicao != null && pos >= 1 && pos < ferramentasNaPosicao.length) {
                ferramenta = ferramentasNaPosicao[pos];
            }

            if (ferramenta != null) {
                if (!atual.temFerramentaComId(ferramenta.getId())) {
                    atual.adicionarFerramenta(ferramenta);

                    mensagem.append("O programador ")
                            .append(atual.getNome())
                            .append(" apanhou a ferramenta ")
                            .append(ferramenta.getNome())
                            .append(".");
                }
                // ferramenta permanece no tabuleiro
            }
        }

        // 3) Vitória
        int novaPos = board.getPlayerPosicao(idJogador);
        if (board.posicaoVitoria(novaPos) && winnerId == null) {
            winnerId = idJogador;
        }

        // 4) Avançar turno
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        totalTurns++;

        if (mensagem.length() == 0) {
            return casaEspecial ? "" : null;
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
            // 3) Cada jogador
            for (Player p : players) {
                int pos = board.getPlayerPosicao(p.getId());

                String nome = p.getNome();
                String linguagens = p.getLinguagensNormalizadas();
                String cor = p.getCor();

                bw.write("PLAYER;" + p.getId() + ";" + nome + ";" + linguagens + ";" + cor + ";" + pos);
                bw.newLine();
            }
            // 4) Abismos
            int nAbismos = 0;
            if (abismosNaPosicao != null) {
                for (int i = 1; i < abismosNaPosicao.length; i++) {
                    if (abismosNaPosicao[i] != null) {
                        nAbismos++;
                    }
                }
            }
            bw.write("ABYSSES;" + nAbismos);
            bw.newLine();

            if (abismosNaPosicao != null) {
                for (int i = 1; i < abismosNaPosicao.length; i++) {
                    if (abismosNaPosicao[i] != null) {
                        bw.write("ABYSS;" + abismosNaPosicao[i].getId() + ";" + i);
                        bw.newLine();
                    }
                }
            }
            // 5) Ferramentas
            int nTools = 0;
            if (ferramentasNaPosicao != null) {
                for (int i = 1; i < ferramentasNaPosicao.length; i++) {
                    if (ferramentasNaPosicao[i] != null) {
                        nTools++;
                    }
                }
            }
            bw.write("TOOLS;" + nTools);
            bw.newLine();

            if (ferramentasNaPosicao != null) {
                for (int i = 1; i < ferramentasNaPosicao.length; i++) {
                    if (ferramentasNaPosicao[i] != null) {
                        bw.write("TOOL;" + ferramentasNaPosicao[i].getId() + ";" + i);
                        bw.newLine();
                    }
                }
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

        ArrayList<Player> loadedPlayers = new ArrayList<>();
        ArrayList<Integer> loadedPositions = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            int[] w = readWorldLine(br);
            int worldSize = w[0];
            int loadedTotalTurns = w[1];
            int loadedCurrentPlayerIndex = w[2];
            int winnerRaw = w[3];

            readPlayersBlock(br, worldSize, loadedPlayers, loadedPositions);

            if (loadedCurrentPlayerIndex < 0 || loadedCurrentPlayerIndex >= loadedPlayers.size()) {
                throw new InvalidFileException("Índice de jogador atual inválido: " + loadedCurrentPlayerIndex);
            }

            applyLoadedBase(worldSize, loadedPlayers, loadedPositions);

            readAbyssesBlock(br, worldSize);
            readToolsBlock(br, worldSize);

            totalTurns = loadedTotalTurns;
            currentPlayerIndex = loadedCurrentPlayerIndex;
            winnerId = (winnerRaw < 0) ? null : winnerRaw;

        } catch (IOException e) {
            throw new InvalidFileException("Erro ao ler o ficheiro: " + e.getMessage());
        }
    }


    private int parseIntOrThrow(String s, String erro) throws InvalidFileException {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            throw new InvalidFileException(erro);
        }
    }

    private String[] splitOrThrow(String line, String erro) throws InvalidFileException {
        if (line == null) {
            throw new InvalidFileException(erro);
        }
        return line.split(";");
    }

    private int[] readWorldLine(BufferedReader br) throws IOException, InvalidFileException {
        String line = br.readLine();
        String[] p = splitOrThrow(line, "Ficheiro vazio.");

        if (p.length != 5 || !"WORLD".equals(p[0])) {
            throw new InvalidFileException("Cabeçalho WORLD inválido.");
        }

        int worldSize = parseIntOrThrow(p[1], "WORLD size inválido.");
        int totalTurns = parseIntOrThrow(p[2], "WORLD totalTurns inválido.");
        int currentIndex = parseIntOrThrow(p[3], "WORLD currentPlayerIndex inválido.");
        int winnerRaw = parseIntOrThrow(p[4], "WORLD winner inválido.");

        if (worldSize <= 0) {
            throw new InvalidFileException("Tamanho do tabuleiro inválido: " + worldSize);
        }
        return new int[]{worldSize, totalTurns, currentIndex, winnerRaw};
    }

    private void readPlayersBlock(BufferedReader br, int worldSize,
                                  ArrayList<Player> loadedPlayers,
                                  ArrayList<Integer> loadedPositions) throws IOException, InvalidFileException {

        String line = br.readLine();
        String[] p = splitOrThrow(line, "Linha PLAYERS em falta.");

        if (p.length != 2 || !"PLAYERS".equals(p[0])) {
            throw new InvalidFileException("Linha PLAYERS inválida.");
        }

        int nPlayers = parseIntOrThrow(p[1], "Número de jogadores inválido.");
        if (nPlayers <= 0) {
            throw new InvalidFileException("Número de jogadores tem de ser > 0.");
        }

        for (int i = 0; i < nPlayers; i++) {
            line = br.readLine();
            p = splitOrThrow(line, "Linha PLAYER em falta (esperava " + nPlayers + ").");

            if (p.length != 6 || !"PLAYER".equals(p[0])) {
                throw new InvalidFileException("Linha PLAYER inválida: " + line);
            }

            int id = parseIntOrThrow(p[1], "Id inválido em PLAYER.");
            String nome = p[2];
            String linguagens = p[3];
            String cor = p[4];
            int pos = parseIntOrThrow(p[5], "Posição inválida em PLAYER.");

            if (pos < 1 || pos > worldSize) {
                throw new InvalidFileException("Posição inválida para o jogador " + id + ": " + pos);
            }

            loadedPlayers.add(new Player(id, nome, linguagens, cor));
            loadedPositions.add(pos);
        }
    }

    private void applyLoadedBase(int worldSize,
                                 ArrayList<Player> loadedPlayers,
                                 ArrayList<Integer> loadedPositions) {

        players.clear();
        players.addAll(loadedPlayers);

        board = new Board(worldSize, players);

        abismosNaPosicao = new Abismos[worldSize + 1];
        ferramentasNaPosicao = new Ferramentas[worldSize + 1];
        casaTeveFerramenta = new boolean[worldSize + 1];

        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            int pos = loadedPositions.get(i);
            int delta = pos - 1;
            if (delta > 0) {
                board.movePlayer(p.getId(), delta);
            }
        }
    }

    private void readAbyssesBlock(BufferedReader br, int worldSize) throws IOException, InvalidFileException {
        String line = br.readLine();
        if (line == null) {
            return; // compatível com ficheiros antigos
        }

        String[] p = line.split(";");
        if (p.length != 2 || !"ABYSSES".equals(p[0])) {
            throw new InvalidFileException("Linha ABYSSES inválida.");
        }

        int n = parseIntOrThrow(p[1], "Número de abismos inválido.");
        for (int i = 0; i < n; i++) {
            line = br.readLine();
            p = splitOrThrow(line, "Linha ABYSS em falta (esperava " + n + ").");

            if (p.length != 3 || !"ABYSS".equals(p[0])) {
                throw new InvalidFileException("Linha ABYSS inválida: " + line);
            }

            int abyssId = parseIntOrThrow(p[1], "Id de abismo inválido.");
            int pos = parseIntOrThrow(p[2], "Posição de abismo inválida.");

            if (pos < 1 || pos > worldSize) {
                throw new InvalidFileException("Posição inválida de abismo: " + pos);
            }

            Abismos ab = criarAbismoPorId(abyssId);
            if (ab == null) {
                throw new InvalidFileException("Id de abismo inválido: " + abyssId);
            }

            abismosNaPosicao[pos] = ab;
        }
    }

    private void readToolsBlock(BufferedReader br, int worldSize) throws IOException, InvalidFileException {
        String line = br.readLine();
        if (line == null) {
            return;
        }

        String[] p = line.split(";");
        if (p.length != 2 || !"TOOLS".equals(p[0])) {
            throw new InvalidFileException("Linha TOOLS inválida.");
        }

        int n = parseIntOrThrow(p[1], "Número de ferramentas inválido.");
        for (int i = 0; i < n; i++) {
            line = br.readLine();
            p = splitOrThrow(line, "Linha TOOL em falta (esperava " + n + ").");

            if (p.length != 3 || !"TOOL".equals(p[0])) {
                throw new InvalidFileException("Linha TOOL inválida: " + line);
            }

            int toolId = parseIntOrThrow(p[1], "Id de ferramenta inválido.");
            int pos = parseIntOrThrow(p[2], "Posição de ferramenta inválida.");

            if (pos < 1 || pos > worldSize) {
                throw new InvalidFileException("Posição inválida de ferramenta: " + pos);
            }

            Ferramentas f = criarFerramentaPorId(toolId);
            if (f == null) {
                throw new InvalidFileException("Id de ferramenta inválido: " + toolId);
            }

            ferramentasNaPosicao[pos] = f;
            casaTeveFerramenta[pos] = true;
        }
    }

    private String aplicarCicloInfinito(Player atual, int pos) {
        // Se tiver ferramenta aplicável, anula (consome) e ninguém fica preso
        Abismos ab = abismosNaPosicao[pos];
        Ferramentas anuladora = atual.getFerramentaQueAnula(ab);

        if (anuladora != null) {
            atual.removeFerramenta(anuladora);
            return "O programador " + atual.getNome()
                    + " usou a ferramenta " + anuladora.getNome()
                    + " para anular o abismo " + ab.getNome() + ".";
        }

        // Não tem proteção -> liberta um preso que esteja na mesma casa (se existir)
        List<Integer> ids = board.getJogadoresNaPosicao(pos);
        if (ids != null) {
            for (int id : ids) {
                if (id != atual.getId()) {
                    Player outro = getPlayerById(id);
                    if (outro != null && outro.isAlive() && !outro.isEnabled()) {
                        outro.setEnabled(true);
                        break; // só liberta 1 (regra: troca)
                    }
                }
            }
        }

        // Atual fica preso
        atual.setEnabled(false);

        return "O programador " + atual.getNome()
                + " entrou num Ciclo Infinito na casa " + pos
                + " e ficou preso até que outro programador caia na mesma casa.";
    }

    private void moverJogadorDelta(int playerId, int delta) {
        int posAtual = board.getPlayerPosicao(playerId);
        int destino = posAtual + delta;

        if (destino < 1) {
            destino = 1;
        }
        if (destino > board.getSize()) {
            destino = board.getSize();
        }

        while (posAtual < destino) {
            board.movePlayer(playerId, 1);
            posAtual++;
        }
        while (posAtual > destino) {
            board.movePlayer(playerId, -1);
            posAtual--;
        }
    }

    private String aplicarSegmentationFault(int pos) {
        List<Integer> ids = board.getJogadoresNaPosicao(pos);

        if (ids == null || ids.size() < 2) {
            return "";
        }

        Abismos ab = abismosNaPosicao[pos];

        Player jogadorComProtecao = null;
        Ferramentas ferramentaUsada = null;

        for (int id : ids) {
            Player p = getPlayerById(id);
            if (p != null) {
                Ferramentas f = p.getFerramentaQueAnula(ab);
                if (f != null) {
                    jogadorComProtecao = p;
                    ferramentaUsada = f;
                    break;
                }
            }
        }

        if (jogadorComProtecao != null) {
            jogadorComProtecao.removeFerramenta(ferramentaUsada);
            return "Segmentation Fault! O programador " + jogadorComProtecao.getNome()
                    + " usou a ferramenta " + ferramentaUsada.getNome()
                    + " e o efeito foi anulado para todos os programadores na casa " + pos + ".";
        }

        for (int id : ids) {
            moverJogadorDelta(id, -3);
        }

        return "Segmentation Fault! Como havia vários programadores na casa " + pos
                + ", todos recuaram 3 casas.";
    }

//fez certo
}