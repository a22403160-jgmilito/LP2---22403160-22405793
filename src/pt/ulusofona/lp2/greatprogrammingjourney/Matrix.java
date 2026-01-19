package pt.ulusofona.lp2.greatprogrammingjourney;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Novo Abismo (id 10) - Matrix
 *
 * Regras:
 * - Só pode ser anulado pela ferramenta Neo (id 6).
 * - Se o jogador não tiver Neo, escolhe entre pílula azul e pílula vermelha.
 * - Em cada activação do abismo, a ordem "prémio"/"punição" troca (para não ser previsível).
 *
 * Nota: para não bloquear testes unitários/execução headless, o diálogo Swing só é mostrado
 * quando o ambiente NÃO é headless. Em headless, a escolha é aleatória (ou forçada via testes).
 */
public class Matrix extends Abismos {

    // true  => azul = prémio (avançar 3x dado) / vermelho = punição (recuar 3x dado)
    // false => azul = punição / vermelho = prémio
    private boolean azulEhPremio = true;

    // Hook para testes (0 = azul, 1 = vermelho). Se null, usa UI/aleatório.
    private static Integer forcedChoiceForTests = null;

    public static void forceChoiceForTests(Integer choice) {
        forcedChoiceForTests = choice;
    }

    public Matrix() {
        super(10);
    }

    @Override
    public String getNome() {
        return "Matrix";
    }

    @Override
    public String aplicarEfeito(Player jogador, Board board, int valorDado) {
        if (jogador == null || board == null) {
            return "";
        }

        int id = jogador.getId();

        int posAtual = board.getPlayerPosicao(id);
        if (board.posicaoVitoria(posAtual)) {
            return "";
        }

        int escolha = obterEscolhaPilula(jogador);
        boolean escolheuAzul = (escolha == 0);

        int deslocamento = Math.max(0, valorDado) * 3;
        boolean ePremio = escolheuAzul ? azulEhPremio : !azulEhPremio;
        int delta = ePremio ? deslocamento : -deslocamento;

        board.movePlayer(id, delta);
        int novaPos = board.getPlayerPosicao(id);

        // Troca a ordem para a próxima activação do mesmo abismo
        azulEhPremio = !azulEhPremio;

        String pilula = escolheuAzul ? "azul" : "vermelha";
        String efeito = ePremio ? ("avançou " + deslocamento) : ("recuou " + deslocamento);

        return "O programador " + jogador.getNome() + " caiu na Matrix, escolheu a pílula "
                + pilula + " e " + efeito + " casas, indo para a posição " + novaPos + ".";
    }

    private int obterEscolhaPilula(Player jogador) {
        if (forcedChoiceForTests != null) {
            return forcedChoiceForTests == 0 ? 0 : 1;
        }

        // Evitar bloquear em ambiente de testes/CI
        if (GraphicsEnvironment.isHeadless()) {
            return ThreadLocalRandom.current().nextInt(0, 2);
        }

        Object[] opcoes = {"Pílula Azul", "Pílula Vermelha"};
        int escolha = JOptionPane.showOptionDialog(
                null,
                "O programador " + jogador.getNome() + " caiu na Matrix!\n\nEscolher uma pílula:",
                "Matrix",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcoes,
                opcoes[0]
        );

        // Se fechar a janela, assume azul.
        if (escolha != 1) {
            return 0;
        }
        return 1;
    }
}
