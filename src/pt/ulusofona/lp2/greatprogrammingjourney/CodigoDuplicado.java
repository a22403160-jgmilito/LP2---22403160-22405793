package pt.ulusofona.lp2.greatprogrammingjourney;

public class CodigoDuplicado extends Abismos {

    public CodigoDuplicado() {
        super(5); // ID do abismo Código Duplicado
    }

    @Override
    public String getNome() {
        return "Código Duplicado";
    }

    @Override
    public String aplicarEfeito(Player jogador, Board board, int valorDado) {

        int posAtual = jogador.getPosicao();

        // posição de 1 jogada atrás (movimento anterior)
        int posAnterior = jogador.getPosicaoJogadas(1);

        // segurança: garantir que é válida
        if (!board.posicaoValida(posAnterior)) {
            posAnterior = 1;
        }
        jogador.setPosicao(posAnterior, board.getSize());

        if (posAnterior != posAtual) {
            return "O programador " + jogador.getNome()
                    + " caiu no abismo Código Duplicado e voltou para a casa anterior (posição "
                    + posAnterior + ").";
        } else {
            return "O programador " + jogador.getNome()
                    + " caiu no abismo Código Duplicado, mas manteve-se na mesma posição.";
        }
    }
}
