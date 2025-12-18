package pt.ulusofona.lp2.greatprogrammingjourney;

import java.util.ArrayList;
import java.util.List;

/**
 * Player:
 * Representa um jogador/programador no jogo.
 * Guarda:
 * - Identificação (id, nome, cor)
 * - Linguagens (string original e versão normalizada/ordenada)
 * - Posição atual no tabuleiro
 * - Estado do jogador (vivo/derrotado e ativo/preso)
 * - Histórico de posições (para consultas retrospetivas)
 * - Ferramentas apanhadas (inventário)
 */
public class Player {

    // Identificador único do jogador
    private int id;

    // Nome do jogador
    private String nome;

    // Linguagens em formato de texto (ex: "Java;Python;C")
    private String linguagens;

    // Cor associada ao jogador (para GUI)
    private String cor;

    // Posição atual no tabuleiro (1..boardSize)
    private int posicao;

    // Estado do jogador:
    // isAlive = true => ainda está no jogo (pode estar preso ou livre)
    // isAlive = false => derrotado (fora do jogo)
    private boolean isAlive = true;

    // isEnabled = true => pode jogar neste turno
    // isEnabled = false => está preso (não pode mover)
    private boolean isEnabled = true;

    // Histórico das posições do jogador (inclui posição inicial e cada atualização)
    private List<Integer> historicoPosicoes = new ArrayList<>();

    // Lista de ferramentas apanhadas pelo jogador
    private List<Ferramentas> ferramentas = new ArrayList<>();

    /**
     * Construtor do jogador.
     * Inicializa os dados básicos e coloca o jogador na posição inicial (casa 1),
     * registando essa posição no histórico.
     *
     * @param id id do jogador
     * @param nome nome do jogador
     * @param linguagens linguagens (string separada por ';')
     * @param cor cor do jogador
     */
    public Player(int id, String nome, String linguagens, String cor) {
        this.id = id;
        this.nome = nome;
        this.linguagens = linguagens;
        this.cor = cor;

        // posição inicial = 1
        this.posicao = 1;

        // registar posição inicial no histórico
        historicoPosicoes.add(posicao);
    }

    /**
     * @return id do jogador
     */
    public int getId() {
        return id;
    }

    /**
     * @return nome do jogador
     */
    public String getNome() {
        return nome;
    }

    /**
     * @return cor do jogador
     */
    public String getCor() {
        return cor;
    }

    /**
     * Devolve as linguagens “normalizadas”:
     * - separa por ';'
     * - remove espaços extra
     * - remove entradas vazias
     * - ordena alfabeticamente (case-insensitive)
     * - junta com "; " (com espaço após ;)
     *
     * Ex:
     * "  C;Python ; java" -> "C; java; Python" (dependendo da ordenação)
     *
     * @return linguagens normalizadas e ordenadas
     */
    public String getLinguagensNormalizadas() {
        if (linguagens == null || linguagens.isEmpty()) {
            return "";
        }

        // separa por ';'
        String[] partes = linguagens.split(";");

        // recolhe linguagens válidas (trim e não vazias)
        ArrayList<String> lista = new ArrayList<>();
        for (String s : partes) {
            s = s.trim();
            if (!s.isEmpty()) {
                lista.add(s);
            }
        }

        // ordena alfabeticamente ignorando maiúsculas/minúsculas
        lista.sort(String.CASE_INSENSITIVE_ORDER);

        // junta com "; " (nota: aqui há espaço; noutras partes do projeto pode ser pedido sem espaço)
        return String.join("; ", lista);
    }

    /**
     * Converte o jogador num array de strings com dados básicos,
     * tipicamente útil para criar o tabuleiro.
     *
     * @return {id, nome, linguagensNormalizadas, cor}
     */
    public String[] asArray() {
        return new String[]{ String.valueOf(id), nome, getLinguagensNormalizadas(), cor};
    }

    /**
     * Representação textual do jogador.
     * Formato: id | nome | posicao | ferramentas | linguagensNormalizadas
     */
    @Override
    public String toString() {
        return id + " | " + nome + " | " + posicao + " | " + getFerramentasAsString() + " | " + getLinguagensNormalizadas();
    }

    /**
     * @return posição atual no tabuleiro
     */
    public int getPosicao() {
        return posicao;
    }

    /**
     * Define a posição do jogador com validação (clamp 1..boardSize)
     * e regista essa posição no histórico.
     *
     * @param novaPos posição pretendida
     * @param boardSize tamanho do tabuleiro (limite superior)
     */
    public void setPosicao(int novaPos, int boardSize) {
        // clamp inferior
        if (novaPos < 1) {
            novaPos = 1;
        }
        // clamp superior
        else if (novaPos > boardSize) {
            novaPos = boardSize;
        }

        // atualiza posição
        this.posicao = novaPos;

        // regista no histórico
        historicoPosicoes.add(this.posicao);
    }

    /**
     * Permite consultar uma posição anterior do jogador, baseada no histórico.
     * - n=0 -> posição atual (última do histórico)
     * - n=1 -> posição anterior
     * - n=2 -> duas jogadas atrás, etc.
     * Se n exceder o histórico disponível, devolve a primeira posição registada.
     *
     * @param n número de “passos para trás” no histórico
     * @return posição correspondente no histórico
     */
    public int getPosicaoJogadas(int n) {
        if (historicoPosicoes.isEmpty()) {
            return posicao;
        }

        int idx = historicoPosicoes.size() - 1 - n;

        // se pedir demasiado para trás, fica no início do histórico
        if (idx < 0) {
            idx = 0;
        }

        return historicoPosicoes.get(idx);
    }

    // =========================================================
    // Parte 2: Ferramentas (inventário)
    // =========================================================

    /**
     * Adiciona uma ferramenta ao inventário (se não for null).
     * Nota: não valida duplicados aqui (isso é feito noutros sítios).
     *
     * @param f ferramenta a adicionar
     */
    public void adicionarFerramenta(Ferramentas f) {
        if (f != null) {
            ferramentas.add(f);
        }
    }

    /**
     * Devolve uma cópia da lista de ferramentas,
     * para proteger a lista interna contra alterações externas.
     *
     * @return cópia do inventário de ferramentas
     */
    public List<Ferramentas> getFerramentas() {
        return new ArrayList<>(ferramentas);
    }

    /**
     * Procura no inventário uma ferramenta que anule o abismo dado.
     * Usa f.podeAnular(abismo).
     *
     * @param abismo abismo a anular
     * @return a primeira ferramenta aplicável encontrada, ou null
     */
    public Ferramentas getFerramentaQueAnula(Abismos abismo) {
        for (Ferramentas f : ferramentas) {
            if (f.podeAnular(abismo)) {
                return f;
            }
        }
        return null;
    }

    /**
     * Remove uma ferramenta do inventário (se existir).
     * @param f ferramenta a remover
     */
    public void removeFerramenta(Ferramentas f) {
        ferramentas.remove(f);
    }

    /**
     * Devolve as ferramentas como string.
     * Se não houver ferramentas, devolve "No tools".
     * Caso haja, devolve os nomes separados por ';' sem espaços:
     * ex: "IDE;Herança"
     *
     * @return string com ferramentas
     */
    public String getFerramentasAsString() {
        if (ferramentas == null || ferramentas.isEmpty()) {
            return "No tools";
        }

        ArrayList<String> nomes = new ArrayList<>();
        for (Ferramentas f : ferramentas) {
            if (f != null && f.getNome() != null) {
                nomes.add(f.getNome());
            }
        }

        if (nomes.isEmpty()) {
            return "No tools";
        }

        // Sem espaços, como no enunciado: "IDE;Herança"
        return String.join(";", nomes);
    }

    // =========================================================
    // Estado do jogador (vivo/preso)
    // =========================================================

    /**
     * @return true se o jogador ainda está no jogo (mesmo que preso), false se derrotado
     */
    public boolean isAlive() {
        return isAlive;
    }

    /**
     * Define se o jogador está vivo.
     * Se ficar derrotado (alive=false), também fica automaticamente desativado (enabled=false).
     *
     * @param alive true para vivo, false para derrotado
     */
    public void setAlive(boolean alive) {
        this.isAlive = alive;
        if (!alive) {
            // se morrer, deixa de fazer sentido estar "ativo"
            this.isEnabled = false;
        }
    }

    /**
     * @return true se o jogador pode jogar neste turno, false se está preso
     */
    public boolean isEnabled() {
        return isEnabled;
    }

    /**
     * Define se o jogador está ativo (enabled) ou preso (disabled).
     * Só permite mudar para enabled/disabled se o jogador estiver vivo.
     *
     * @param enabled true para ativo, false para preso
     */
    public void setEnabled(boolean enabled) {
        // só pode ficar enabled/disabled se estiver vivo
        if (isAlive) {
            this.isEnabled = enabled;
        }
    }

    /**
     * Devolve o estado textual do jogador:
     * - "Derrotado" se não estiver vivo
     * - "Preso" se estiver vivo mas não enabled
     * - "Em Jogo" se estiver vivo e enabled
     *
     * @return estado em texto
     */
    public String getEstadoComoTexto() {
        if (!isAlive) {
            return "Derrotado";
        }
        if (!isEnabled) {
            return "Preso";
        }
        return "Em Jogo";
    }

    /**
     * Verifica se o jogador já tem uma ferramenta com um dado id.
     * Útil para evitar duplicados.
     *
     * @param idFerramenta id a procurar
     * @return true se encontrar, false caso contrário
     */
    public boolean temFerramentaComId(int idFerramenta) {
        for (Ferramentas f : ferramentas) {
            if (f != null && f.getId() == idFerramenta) {
                return true;
            }
        }
        return false;
    }

    /**
     * Devolve a string de linguagens tal como foi introduzida (trim),
     * sem ordenar nem reformatar.
     *
     * @return linguagens originais (trim) ou "" se null
     */
    public String getLinguagensOriginal() {
        return linguagens == null ? "" : linguagens.trim();
    }

    /**
     * Regista uma posição no histórico, mas evita duplicar a mesma posição seguida.
     * Isto é útil quando o jogo regista a posição antes e depois do movimento.
     *
     * @param pos posição a registar
     */
    public void registarPosicao(int pos) {
        // evita repetir a mesma posição seguida
        if (historicoPosicoes.isEmpty() || historicoPosicoes.get(historicoPosicoes.size() - 1) != pos) {
            historicoPosicoes.add(pos);
        }
    }
}
