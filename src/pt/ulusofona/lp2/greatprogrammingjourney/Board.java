package pt.ulusofona.lp2.greatprogrammingjourney;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Board:
 * Representa o tabuleiro do jogo.
 * Responsabilidades principais:
 * - Guardar o tamanho do mundo (número de casas)
 * - Manter a lista de jogadores e as suas posições
 * - Validar posições e identificar a casa final
 * - Mover jogadores no tabuleiro (inclui regra de "bounce back" ao ultrapassar o fim)
 * - Fornecer informação útil (quem está em cada casa, mapa de posições, etc.)
 */
public class Board {

    // Tamanho do tabuleiro (casas de 1 até size)
    private final int size;

    // Lista de jogadores (as posições são guardadas dentro de cada Player)
    private final List<Player> players;

    /**
     * Constrói o tabuleiro.
     * - Valida que o tamanho é > 0
     * - Garante que todos os jogadores começam na casa 1
     *   (e que essa posição inicial entra no histórico do Player via setPosicao).
     *
     * @param size tamanho do tabuleiro (número de casas)
     * @param players lista de jogadores
     */
    public Board(int size, List<Player> players) {
        if (size <= 0) {
            throw new IllegalArgumentException("Board size must be > 0");
        }
        this.size = size;
        this.players = players;

        // Garantir que todos começam na casa 1 e isso entra no histórico.
        // Nota: assume-se que Player.setPosicao(pos, size) valida e/ou regista histórico.
        for (Player p : players) {
            p.setPosicao(1, size);
        }
    }

    /**
     * @return tamanho do tabuleiro
     */
    public int getSize() {
        return size;
    }

    /**
     * Verifica se uma posição é válida no tabuleiro.
     * @param position posição (1..size)
     * @return true se está dentro do intervalo, false caso contrário
     */
    public boolean posicaoValida(int position) {
        return position >= 1 && position <= size;
    }

    /**
     * Verifica se a posição é a casa final (vitória).
     * @param position posição
     * @return true se position == size
     */
    public boolean posicaoVitoria(int position) {
        return position == size;
    }

    /**
     * Obtém a posição atual de um jogador pelo id.
     * Se não encontrar o jogador, devolve 1 por defeito.
     *
     * @param playerId id do jogador
     * @return posição do jogador (ou 1 se não existir)
     */
    public int getPlayerPosicao(int playerId) {
        for (Player p : players) {
            if (p.getId() == playerId) {
                return p.getPosicao();
            }
        }
        // Por defeito, se não encontrar, considera na posição 1
        // (isto evita NPEs, mas pode esconder erros de lógica se o id for inválido).
        return 1;
    }

    /**
     * Devolve a lista de ids de jogadores que estão numa determinada posição.
     * Se a posição for inválida, devolve lista vazia.
     *
     * @param position posição a consultar
     * @return lista de ids (pode ser vazia)
     */
    public List<Integer> getJogadoresNaPosicao(int position) {
        List<Integer> ids = new ArrayList<>();

        // Se posição inválida, devolve lista vazia (não devolve null)
        if (!posicaoValida(position)) {
            return ids;
        }

        // Percorre jogadores e recolhe ids dos que estão nessa casa
        for (Player p : players) {
            if (p.getPosicao() == position) {
                ids.add(p.getId());
            }
        }
        return ids;
    }

    /**
     * Move um jogador nrSpaces casas.
     * - Se nrSpaces > 0: anda para a frente. Se ultrapassar o fim, aplica "bounce back"
     *   (recua o excesso: como se batesse na parede e voltasse para trás).
     * - Se nrSpaces < 0: anda para trás. Nunca desce abaixo da casa 1 (clamp).
     * - Se nrSpaces == 0: não mexe.
     *
     * No fim, chama alvo.setPosicao(pos, size) para atualizar a posição
     * (e registar histórico, se o Player o fizer).
     *
     * @param playerId id do jogador a mover
     * @param nrSpaces número de casas a mover (positivo ou negativo)
     * @return posição final do jogador, ou -1 se o jogador não existir
     */
    public int movePlayer(int playerId, int nrSpaces) {

        // Procura o jogador alvo pelo id
        Player alvo = null;
        for (Player p : players) {
            if (p.getId() == playerId) {
                alvo = p;
                break;
            }
        }

        // Se não existe jogador com esse id
        if (alvo == null) {
            return -1;
        }

        // Posição atual do jogador
        int pos = alvo.getPosicao();

        // Andar para a frente (com "recua o excesso" no fim)
        if (nrSpaces > 0) {
            while (nrSpaces > 0) {
                pos++;

                // Se passou o fim, aplica bounce back:
                // ex: size=10, pos=11 -> pos = 10 - (11-10) = 9
                if (pos > size) {
                    pos = size - (pos - size);
                }
                nrSpaces--;
            }
        }
        // Andar para trás (por exemplo, efeitos de abismos)
        else if (nrSpaces < 0) {
            while (nrSpaces < 0) {
                pos--;

                // Não permite descer abaixo de 1
                if (pos < 1) {
                    pos = 1;
                }
                nrSpaces++;
            }
        }
        // nrSpaces == 0 → não mexe

        // Atualiza a posição no Player (com validação/histórico no Player)
        alvo.setPosicao(pos, size);

        // Devolve a posição final
        return alvo.getPosicao();
    }

    /**
     * Verifica se existe pelo menos um jogador na casa final.
     * @return true se algum jogador estiver na posição size
     */
    public boolean temJogadorNaPosicaoFinal() {
        for (Player p : players) {
            if (p.getPosicao() == size) {
                return true;
            }
        }
        return false;
    }

    /**
     * Cria um mapa com a posição atual de todos os jogadores:
     * chave = id do jogador, valor = posição.
     *
     * @return Map<id, posição>
     */
    public Map<Integer, Integer> getTodasPosicoes() {
        Map<Integer, Integer> mapa = new HashMap<>();
        for (Player p : players) {
            mapa.put(p.getId(), p.getPosicao());
        }
        return mapa;
    }

    /**
     * Define diretamente a posição de um jogador.
     * - Se o id não existir, não faz nada.
     * - Faz clamp da posição para 1..size.
     * - Atualiza via Player.setPosicao(pos, size) (regista histórico, se aplicável).
     *
     * @param playerId id do jogador
     * @param pos posição pretendida
     */
    public void setPlayerPosicao(int playerId, int pos) {
        // Procura o jogador alvo pelo id
        Player alvo = null;
        for (Player p : players) {
            if (p.getId() == playerId) {
                alvo = p;
                break;
            }
        }

        // Se não encontrou, sai
        if (alvo == null) {
            return;
        }

        // Clamp inferior
        if (pos < 1){
            pos = 1;
        }

        // Clamp superior
        if (pos > size){
            pos = size;
        }

        // Atualiza no Player
        alvo.setPosicao(pos, size);
    }
}
