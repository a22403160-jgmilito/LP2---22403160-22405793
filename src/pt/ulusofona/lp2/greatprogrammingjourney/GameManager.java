package pt.ulusofona.lp2.greatprogrammingjourney;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


/**
 * GameManager:
 * Classe “central” do jogo. É responsável por:
 * - Guardar jogadores e tabuleiro
 * - Controlar turnos, jogador atual, vencedor
 * - Criar/gerir abismos e ferramentas (parte 2)
 * - Implementar movimento, reações a abismos/ferramentas
 * - Guardar e carregar jogo (save/load)
 * - Expor informação para a GUI (slot info, imagens, autores, etc.)
 */
public class GameManager {

    // Lista de jogadores (2 a 4, conforme validação)
    private final ArrayList<Player> players = new ArrayList<>();

    // Tabuleiro do jogo (assume-se que Board guarda posições e permite mover jogadores)
    private Board board;

    // ----------------------------
    // Estado do jogo (progresso)
    // ----------------------------

    // Contador de turnos (incrementado em reactToAbyssOrTool)
    private int totalTurns = 0;

    // Id do vencedor (null enquanto ninguém ganhar)
    private Integer winnerId = null;

    // Índice do jogador atual dentro de "players"
    private int currentPlayerIndex = 0;

    // ----------------------------
    // Parte 2: abismos e ferramentas
    // ----------------------------

    // Array indexado por posição: se abismosNaPosicao[pos] != null, há abismo nessa casa
    private Abismos[] abismosNaPosicao;

    // Array indexado por posição: se ferramentasNaPosicao[pos] != null, há ferramenta nessa casa
    private Ferramentas[] ferramentasNaPosicao;

    // Guarda o valor do dado lançado no turno (necessário para certos efeitos)
    private int valorDadoLancado = 0;

    // Guarda a última configuração recebida de abismos/ferramentas
    // (útil para recriar o tabuleiro mantendo esse input)
    private String[][] lastAbyssesAndTools = null;

    // Marca se a casa já teve uma ferramenta (mesmo que já tenha sido apanhada).
    // Isto é importante para decidir se reactToAbyssOrTool deve devolver "" ou null.
    private boolean[] casaTeveFerramenta;

    // para a nova variavel do player, experiencia
    private boolean experienciaAtiva = false;
    /**
     * Construtor vazio. O jogo só fica “pronto” após createInitialBoard().
     */
    public GameManager() {
    }

    /**
     * Procura um Player pelo id dentro da lista de jogadores.
     * @param id id do jogador
     * @return Player correspondente ou null se não existir
     */
    private Player getPlayerById(int id) {
        for (Player p : players) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    /**
     * Overload de criação do tabuleiro:
     * se não for fornecida a matriz de abismos/ferramentas,
     * usa a última conhecida (lastAbyssesAndTools).
     */
    public boolean createInitialBoard(String[][] playerInfo, int worldSize) {
        return createInitialBoard(playerInfo, worldSize, lastAbyssesAndTools);
    }

    /**
     * Devolve o nome do PNG associado a uma casa (para a GUI).
     * Neste momento só devolve a imagem final "glory.png" na casa de vitória.
     *
     * @param nrSquare número da casa no tabuleiro
     * @return nome do ficheiro png ou null
     */
    public String getImagePng(int nrSquare) {
        // Se não há tabuleiro, não há imagens
        if (board == null) {
            return null;
        }

        // Se a posição é inválida, não há imagens
        if (!board.posicaoValida(nrSquare)) {
            return null;
        }

        // Casa final: imagem de vitória
        if (board.posicaoVitoria(nrSquare)) {
            return "glory.png";
        }

        // Sem outras imagens definidas
        return null;
    }

    /**
     * Informação resumida de todos os jogadores (para GUI).
     * Formato: "Nome : Ferramentas | Nome : Ferramentas | ..."
     *
     * @return string com info ou "" se jogo ainda não pronto
     */
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

    /**
     * Versão “string formatada” da informação do programador.
     *
     * NOTA: tens aqui um comentário a sugerir melhoria:
     * "alterar essa funcao para chamar o toString do player e nao repetir codigo"
     *
     * @param id id do jogador
     * @return string formatada ou null
     */
    public String getProgrammerInfoAsStr(int id) {
        String[] info = getProgrammerInfo(id);
        if (info == null) {
            return null;
        }

        // Formato pedido: id | nome | posicao | ferramentas | linguagens | estado
        // Atenção ao mapeamento dos índices:
        // [0] id, [1] nome, [4] posicao, [5] ferramentas, [2] linguagens, [6] estado
        return info[0] + " | " + info[1] + " | " + info[4] + " | " + info[5] + " | " + info[2] + " | " + info[6];
    }

    /**
     * Informação sobre uma casa do tabuleiro (slot).
     * Devolve SEMPRE um array de 3 strings se a posição for válida:
     * [0] ids dos jogadores (se existirem) separados por vírgula
     * [1] nome do abismo/ferramenta (se existir)
     * [2] "A:id" ou "T:id" (identificador de abismo/ferramenta)
     *
     * Prioridade: se houver abismo e ferramenta, o abismo “ganha” (aparece o abismo).
     *
     * @param position posição no tabuleiro (1..board.getSize())
     * @return array de 3 strings ou null se posição inválida
     */
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

    /**
     * Devolve o id do jogador atual (é o jogador na vez).
     * @return id do jogador atual ou -1 se não há jogadores
     */
    public int getCurrentPlayerID() {
        if (players.isEmpty()) {
            return -1;
        }
        return players.get(currentPlayerIndex).getId();
    }

    /**
     * Move o jogador atual nrSpaces casas.
     * Regras implementadas:
     * 1) nrSpaces tem de estar no intervalo [1..6]
     * 2) jogador tem de estar vivo e habilitado (não derrotado nem preso)
     * 3) restrições por linguagem:
     *    - Assembly: não pode lançar >=3
     *    - C: não pode lançar >=4
     * 4) Se ultrapassa a meta, “recua” pelo excesso e o movimento é inválido
     *
     * NOTA: O efeito de abismo/ferramenta não é aplicado aqui; isso acontece em reactToAbyssOrTool().
     *
     * @param nrSpaces valor do “dado”
     * @return true se o movimento foi considerado válido; false se inválido (ou proibido)
     */
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

        // 3) Restrições por linguagem
        String primeiraLing = getPrimeiraLinguagem(atual);

        if ("Assembly".equalsIgnoreCase(primeiraLing) && nrSpaces >= 3) {
            return false;
        }
        if ("C".equalsIgnoreCase(primeiraLing) && nrSpaces >= 4) {
            return false;
        }

        // Guardar valor do dado (pode ser usado em efeitos de abismos)
        valorDadoLancado = nrSpaces;

        // Guardar posição antes de mexer (histórico)
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
            movimentoValido = false;
        }

        // Move “passo a passo” para a direita (1 em 1)
        while (posAtual < destino) {
            board.movePlayer(atual.getId(), 1);
            posAtual++;
        }

        // Move “passo a passo” para a esquerda (1 em 1)
        while (posAtual > destino) {
            board.movePlayer(atual.getId(), -1);
            posAtual--;
        }

        // Regista posição final
        atual.registarPosicao(posAtual);

        return movimentoValido;
    }

    /**
     * Obtém a “primeira linguagem” do jogador, a partir da string original,
     * assumindo separação por ';' (ex: "Java;Python;C").
     * @param p jogador
     * @return primeira linguagem (trim) ou "" se não existir
     */
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

    /**
     * Indica se o jogo acabou.
     * Critérios:
     * - Se winnerId já foi definido, acabou.
     * - Ou se o Board indica que há alguém na posição final.
     */
    public boolean gameIsOver() {
        if (board == null) {
            return false;
        }
        if (winnerId != null) {
            return true;
        }
        return board.temJogadorNaPosicaoFinal();
    }

    /**
     * Devolve os resultados do jogo num ArrayList de strings,
     * provavelmente conforme formato esperado por testes.
     *
     * Estrutura:
     * - Título
     * - nr de turnos
     * - vencedor
     * - restantes ordenados (posição desc, desempate por nome asc)
     */
    public ArrayList<String> getGameResults() {
        ArrayList<String> res = new ArrayList<>();

        res.add("THE GREAT PROGRAMMING JOURNEY");
        res.add("");
        res.add("NR. DE TURNOS");
        res.add(String.valueOf(totalTurns));
        res.add("");

        res.add("VENCEDOR");

        // Procura vencedor: quem estiver na última casa
        Player vencedor = null;
        for (Player p : players) {
            if (p.getPosicao() == board.getSize()) {
                vencedor = p;
                break;
            }
        }
        res.add(vencedor == null ? "" : vencedor.getNome());
        res.add("");

        res.add("RESTANTES");

        // Lista “restantes” sem o vencedor
        ArrayList<Player> restantes = new ArrayList<>();
        for (Player p : players) {
            if (vencedor != null && p.getId() == vencedor.getId()){
                continue;
            }
            restantes.add(p);
        }

        // Ordenação: posição DESC, depois nome ASC (case-insensitive)
        restantes.sort((a, b) -> {
            int pa = a.getPosicao();
            int pb = b.getPosicao();
            if (pa != pb){
                return Integer.compare(pb, pa);
            }
            return a.getNome().compareToIgnoreCase(b.getNome());
        });

        // Formato por linha: "Nome posicao"
        for (Player p : restantes) {
            res.add(p.getNome() + " " + p.getPosicao());
        }

        return res;
    }

    /**
     * Painel com autores (GUI).
     * Atualmente tem placeholders.
     */
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

    /**
     * Permite fornecer configurações extra para a GUI (se o enunciado pedir).
     * Aqui só devolve um título.
     */
    public HashMap<String, String> customizeBoard() {
        HashMap<String, String> config = new HashMap<>();

        // Chaves dependem do enunciado/GUI.
        config.put("title", "Great Programming Journey");

        return config;
    }

    /**
     * Devolve informação detalhada sobre um programador num array de 7 strings:
     * [0] id, [1] nome, [2] linguagens, [3] cor, [4] posição, [5] ferramentas, [6] estado
     *
     * @param id id do jogador
     * @return array de 7 strings ou null se jogo/jogador inválido
     */
    public String[] getProgrammerInfo(int id) {
        if (players.isEmpty() || board == null) {
            return null;
        }

        Player p = getPlayerById(id);
        if (p == null) {
            return null;
        }

        // Posição real obtida do Board
        int pos = board.getPlayerPosicao(id);

        // Linguagens (atenção ao método usado; aqui está a chamar normalizadas)
        String linguagens = p.getLinguagensNormalizadas();
        if (linguagens == null) {
            linguagens = "";
        }

        String cor = p.getCor() == null ? "" : p.getCor();

        // Ferramentas ordenadas (responsabilidade do Player)
        String ferramentas = p.getFerramentasAsString();

        // Estado textual (ex: "Em Jogo", "Preso", "Derrotado" - depende do Player)
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

    // =========================================================
    // PARTE 2: criação do tabuleiro com abismos e ferramentas
    // =========================================================

    /**
     * Cria o tabuleiro inicial a partir dos dados de jogadores e configurações.
     *
     * Validações principais:
     * - playerInfo não pode ser null/vazio
     * - cada linha de playerInfo tem pelo menos 4 campos: id, nome, linguagens, cor
     * - número de jogadores: 2..4
     * - worldSize >= 2 * nº jogadores
     * - abyssesAndTools (se existir) tem linhas [tipoLinha, tipoId, pos]
     *   tipoLinha: 0=abismo, 1=ferramenta
     *   pos tem de ser válida
     *   não pode haver repetição no mesmo array (duas ferramentas na mesma casa, etc.)
     *
     * Também inicializa:
     * - currentPlayerIndex = 0
     * - totalTurns = 1
     * - winnerId = null
     */
    public boolean createInitialBoard(String[][] playerInfo, int worldSize, String[][] abyssesAndTools) {
        // Guarda a configuração para poder reusar depois
        lastAbyssesAndTools = abyssesAndTools;

        if (playerInfo == null || playerInfo.length == 0) {
            return false;
        }

        players.clear();

        // Criar jogadores a partir de playerInfo
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

        // Regra: mínimo 2, máximo 4
        if (players.size() < 2 || players.size() > 4) {
            players.clear();
            board = null;
            abismosNaPosicao = null;
            ferramentasNaPosicao = null;
            return false;
        }

        // Regra: worldSize >= 2 * nº jogadores
        int tamanhoMinimo = 2 * players.size();
        if (worldSize < tamanhoMinimo) {
            players.clear();
            board = null;
            abismosNaPosicao = null;
            ferramentasNaPosicao = null;
            return false;
        }

        // Criar tabuleiro e estruturas auxiliares
        board = new Board(worldSize, players);

        // +1 porque as posições são (muito provavelmente) 1..worldSize
        abismosNaPosicao = new Abismos[worldSize + 1];
        ferramentasNaPosicao = new Ferramentas[worldSize + 1];
        casaTeveFerramenta = new boolean[worldSize + 1];

        // Processar lista de abismos e ferramentas
        if (abyssesAndTools != null) {
            for (String[] linha : abyssesAndTools) {
                if (linha == null || linha.length < 3) {
                    return false;
                }

                int tipoLinha; // 0=abismo, 1=ferramenta
                int tipoId;    // id do abismo/ferramenta
                int pos;       // posição onde colocar

                try {
                    tipoLinha = Integer.parseInt(linha[0]);
                    tipoId = Integer.parseInt(linha[1]);
                    pos = Integer.parseInt(linha[2]);
                } catch (NumberFormatException e) {
                    return false;
                }

                // posição tem de ser válida no tabuleiro
                if (!board.posicaoValida(pos)) {
                    return false;
                }

                if (tipoLinha == 0) {
                    // ABISMO
                    if (abismosNaPosicao[pos] != null) {
                        return false; // já existe abismo nessa casa
                    }

                    Abismos ab = criarAbismoPorId(tipoId);
                    if (ab == null) {
                        return false; // id inválido
                    }

                    abismosNaPosicao[pos] = ab;

                } else if (tipoLinha == 1) {
                    // FERRAMENTA
                    if (ferramentasNaPosicao[pos] != null) {
                        return false; // já existe ferramenta nessa casa
                    }

                    Ferramentas f = criarFerramentaPorId(tipoId);
                    if (f == null) {
                        return false; // id inválido
                    }

                    ferramentasNaPosicao[pos] = f;

                    // marca que esta casa já teve ferramenta (para a regra de retorno em reactToAbyssOrTool)
                    casaTeveFerramenta[pos] = true;

                } else {
                    // tipoLinha só pode ser 0 ou 1
                    return false;
                }
            }
        }

        // Estado inicial do jogo
        currentPlayerIndex = 0;
        totalTurns = 1;
        winnerId = null;

        return true;
    }

    /**
     * Factory: cria instância de Abismo a partir do id.
     * @param id id do abismo
     * @return Abismos ou null se id inválido
     */
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
            case 20: return new LLM();
            default:
                return null;
        }
    }

    /**
     * Factory: cria instância de Ferramenta a partir do id.
     * @param id id da ferramenta
     * @return Ferramentas ou null se id inválido
     */
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

    // =========================================================
    // Reação a abismos/ferramentas após movimento
    // =========================================================

    /**
     * Aplica o que existir na casa onde o jogador atual caiu:
     * - Se houver abismo, tem prioridade sobre ferramenta.
     * - Se houver ferramenta e o jogador não tiver uma igual, apanha (mas a ferramenta permanece no tabuleiro).
     * - Trata casos especiais:
     *   * Ciclo Infinito (id 8): “troca” preso
     *   * Segmentation Fault (id 9): efeito coletivo se houver >=2 jogadores
     *
     * Regras de retorno:
     * - Se não houve nada relevante, devolve:
     *     null -> se a casa não é “especial”
     *     ""   -> se a casa é “especial” (abismo/ferramenta/teve-ferramenta)
     * - Se houve uma mensagem de evento, devolve a mensagem.
     *
     * Também:
     * - verifica vitória e define winnerId
     * - avança turno (currentPlayerIndex) e incrementa totalTurns
     */
    public String reactToAbyssOrTool() {
        if (board == null || players.isEmpty()) {
            return null;
        }

        Player atual = players.get(currentPlayerIndex);
        int idJogador = atual.getId();
        int pos = board.getPlayerPosicao(idJogador);

        StringBuilder mensagem = new StringBuilder();

        // casaEspecial = tem abismo OU tem ferramenta OU já teve ferramenta antes
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

            // --- Caso especial: Ciclo Infinito (id 8) ---
            if (abismo.getId() == 8) {
                String msg = aplicarCicloInfinito(atual, pos);
                if (msg != null && !msg.isEmpty()) {
                    mensagem.append(msg);
                }
            }
            // --- Caso especial: Segmentation Fault (id 9) ---
            else if (abismo.getId() == 9) {
                String msg = aplicarSegmentationFault(pos);
                if (msg != null && !msg.isEmpty()) {
                    mensagem.append(msg);
                }
            }
            // --- Caso especial: LLM + Experiência ---
            else if (abismo instanceof LLM && atual.isExperiente()) {
                String msg = abismo.aplicarEfeito(atual, board, valorDadoLancado);
                if (msg != null && !msg.isEmpty()) {
                    mensagem.append(msg);
                }
            }
            // --- Abismos “normais” (inclui LLM sem experiência) ---
            else {
                // Verifica se o jogador tem uma ferramenta que anula este abismo
                Ferramentas anuladora = atual.getFerramentaQueAnula(abismo);

                if (anuladora != null) {
                    // Consome a ferramenta para anular o abismo
                    atual.removeFerramenta(anuladora);

                    mensagem.append("O programador ")
                            .append(atual.getNome())
                            .append(" usou a ferramenta ")
                            .append(anuladora.getNome())
                            .append(" para anular o abismo ")
                            .append(abismo.getNome())
                            .append(".");
                } else {
                    // Sem proteção: aplicar o efeito do abismo
                    String msgAbismo = abismo.aplicarEfeito(atual, board, valorDadoLancado);
                    if (msgAbismo != null && !msgAbismo.isEmpty()) {
                        mensagem.append(msgAbismo);
                    }
                }
            }

        }
        // 3) Vitória: se chegou à casa final, marca winnerId (se ainda não havia)
        int novaPos = board.getPlayerPosicao(idJogador);
        if (board.posicaoVitoria(novaPos) && winnerId == null) {
            winnerId = idJogador;
        }
        // 4) Marcar fim do turno deste jogador
        atual.incrementarTurno();

        // Ativar experiência quando TODOS tiverem feito 3 turnos
        if (!experienciaAtiva) {
            boolean todos3 = true;
            for (Player p : players) {
                if (p.getTurnosJogador() < 3) {
                    todos3 = false;
                    break;
                }
            }
            if (todos3) {
                experienciaAtiva = true;
                for (Player p : players) {
                    p.setExperiente(true);
                }
            }
        }

        // 4) Avançar turno
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        totalTurns++;

        // Se não houve mensagem:
        // - se casa especial: devolve "" (string vazia)
        // - se não: devolve null
        if (mensagem.length() == 0) {
            return casaEspecial ? "" : null;
        }

        return mensagem.toString();
    }

    // =========================================================
    // SAVE GAME
    // =========================================================

    /**
     * Guarda o estado do jogo num ficheiro (formato “;”).
     * Estrutura:
     * WORLD;worldSize;totalTurns;currentPlayerIndex;winnerIdOu-1
     * PLAYERS;n
     * PLAYER;id;nome;linguagens;cor;pos
     * ABYSSES;n
     * ABYSS;abyssId;pos
     * TOOLS;n
     * TOOL;toolId;pos
     *
     * @param file ficheiro destino
     * @return true se guardou com sucesso; false caso contrário
     */
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

            // 3) Cada jogador com posição atual
            for (Player p : players) {
                int pos = board.getPlayerPosicao(p.getId());

                String nome = p.getNome();
                String linguagens = p.getLinguagensNormalizadas();
                String cor = p.getCor();

                bw.write("PLAYER;" + p.getId() + ";" + nome + ";" + linguagens + ";" + cor + ";" + pos);
                bw.newLine();
            }

            // 4) Abismos: contar quantos existem
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

            // Escrever cada abismo
            if (abismosNaPosicao != null) {
                for (int i = 1; i < abismosNaPosicao.length; i++) {
                    if (abismosNaPosicao[i] != null) {
                        bw.write("ABYSS;" + abismosNaPosicao[i].getId() + ";" + i);
                        bw.newLine();
                    }
                }
            }

            // 5) Ferramentas: contar quantas existem
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

            // Escrever cada ferramenta
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

    // =========================================================
    // LOAD GAME
    // =========================================================

    /**
     * Carrega o jogo a partir de um ficheiro no formato esperado pelo saveGame().
     * Pode lançar:
     * - FileNotFoundException: se o ficheiro não existir
     * - InvalidFileException: se o formato/conteúdo for inválido
     *
     * @param file ficheiro origem
     */
    public void loadGame(File file) throws InvalidFileException, java.io.FileNotFoundException {
        if (file == null || !file.exists()) {
            throw new java.io.FileNotFoundException("Ficheiro não encontrado: " + file);
        }

        ArrayList<Player> loadedPlayers = new ArrayList<>();
        ArrayList<Integer> loadedPositions = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            // Lê linha WORLD e extrai worldSize/turnos/index/winner
            int[] w = readWorldLine(br);
            int worldSize = w[0];
            int loadedTotalTurns = w[1];
            int loadedCurrentPlayerIndex = w[2];
            int winnerRaw = w[3];

            // Lê bloco PLAYERS + PLAYERs
            readPlayersBlock(br, worldSize, loadedPlayers, loadedPositions);

            // Valida o índice do jogador atual
            if (loadedCurrentPlayerIndex < 0 || loadedCurrentPlayerIndex >= loadedPlayers.size()) {
                throw new InvalidFileException("Índice de jogador atual inválido: " + loadedCurrentPlayerIndex);
            }

            // Aplica base carregada (board + arrays + posições)
            applyLoadedBase(worldSize, loadedPlayers, loadedPositions);

            // Lê blocos ABYSSES e TOOLS
            readAbyssesBlock(br, worldSize);
            readToolsBlock(br, worldSize);

            // Atualiza estado do jogo
            totalTurns = loadedTotalTurns;
            currentPlayerIndex = loadedCurrentPlayerIndex;
            winnerId = (winnerRaw < 0) ? null : winnerRaw;

        } catch (IOException e) {
            throw new InvalidFileException("Erro ao ler o ficheiro: " + e.getMessage());
        }
    }

    // =========================================================
    // Funções auxiliares do LOAD (parsing e validação)
    // =========================================================

    /**
     * Faz parse de um int ou lança InvalidFileException com mensagem.
     */
    private int parseIntOrThrow(String s, String erro) throws InvalidFileException {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            throw new InvalidFileException(erro);
        }
    }

    /**
     * Faz split por ';' ou lança InvalidFileException se a linha for null.
     */
    private String[] splitOrThrow(String line, String erro) throws InvalidFileException {
        if (line == null) {
            throw new InvalidFileException(erro);
        }
        return line.split(";");
    }

    /**
     * Lê e valida a linha WORLD:
     * WORLD;worldSize;totalTurns;currentPlayerIndex;winner
     * @return array [worldSize, totalTurns, currentIndex, winnerRaw]
     */
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

    /**
     * Lê bloco de jogadores:
     * PLAYERS;n
     * PLAYER;id;nome;linguagens;cor;pos
     * ...
     * @param worldSize para validar posições
     */
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

    /**
     * Aplica “base” carregada:
     * - substitui players
     * - cria board
     * - cria arrays de abismos/ferramentas/flags
     * - posiciona jogadores no board
     */
    private void applyLoadedBase(int worldSize,
                                 ArrayList<Player> loadedPlayers,
                                 ArrayList<Integer> loadedPositions) {

        players.clear();
        players.addAll(loadedPlayers);

        board = new Board(worldSize, players);

        abismosNaPosicao = new Abismos[worldSize + 1];
        ferramentasNaPosicao = new Ferramentas[worldSize + 1];
        casaTeveFerramenta = new boolean[worldSize + 1];

        // Coloca cada jogador na posição carregada:
        // Assume-se que Board inicia na posição 1, então delta = pos - 1
        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            int pos = loadedPositions.get(i);
            int delta = pos - 1;
            if (delta > 0) {
                board.movePlayer(p.getId(), delta);
            }
        }
    }

    /**
     * Lê bloco ABYSSES; n e depois n linhas ABYSS;id;pos.
     * Se não houver bloco (EOF), retorna sem erro (compatibilidade com ficheiros antigos).
     */
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

    /**
     * Lê bloco TOOLS; n e depois n linhas TOOL;id;pos.
     * Se não houver bloco (EOF), retorna sem erro.
     */
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

            // marca que essa casa já “teve ferramenta”
            casaTeveFerramenta[pos] = true;
        }
    }

    // =========================================================
    // Casos especiais de abismos (Ciclo Infinito / Segmentation Fault)
    // =========================================================

    /**
     * Aplica regra do Ciclo Infinito (abismo id 8):
     * - Se o jogador tiver ferramenta que anula, consome-a e não prende ninguém.
     * - Caso contrário:
     *   * liberta UM jogador preso que esteja na mesma casa (se existir)
     *   * prende o jogador atual (enabled=false)
     */
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
                        // liberta o outro preso
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

    /**
     * Move um jogador por delta casas (positivo ou negativo), respeitando limites 1..size.
     * Move “passo a passo” usando board.movePlayer(±1) para manter consistência.
     */
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

    /**
     * Aplica regra do Segmentation Fault (abismo id 9):
     * - Se houver menos de 2 jogadores na casa, não faz nada.
     * - Se algum jogador na casa tiver ferramenta que anula este abismo:
     *     consome essa ferramenta e anula para todos.
     * - Caso contrário:
     *     todos os jogadores na casa recuam 3 casas.
     */
    private String aplicarSegmentationFault(int pos) {
        List<Integer> ids = board.getJogadoresNaPosicao(pos);

        // Precisa de “vários programadores” na casa para aplicar o efeito
        if (ids == null || ids.size() < 2) {
            return "";
        }

        Abismos ab = abismosNaPosicao[pos];

        Player jogadorComProtecao = null;
        Ferramentas ferramentaUsada = null;

        // Procura se alguém tem ferramenta de anulação (o primeiro que tiver, anula para todos)
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

        // Se encontrou proteção, consome e anula o efeito global
        if (jogadorComProtecao != null) {
            jogadorComProtecao.removeFerramenta(ferramentaUsada);
            return "Segmentation Fault! O programador " + jogadorComProtecao.getNome()
                    + " usou a ferramenta " + ferramentaUsada.getNome()
                    + " e o efeito foi anulado para todos os programadores na casa " + pos + ".";
        }

        // Sem proteção: todos recuam 3 casas
        for (int id : ids) {
            moverJogadorDelta(id, -3);
        }

        return "Segmentation Fault! Como havia vários programadores na casa " + pos
                + ", todos recuaram 3 casas.";
    }
}