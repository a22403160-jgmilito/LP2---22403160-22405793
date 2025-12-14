package pt.ulusofona.lp2.greatprogrammingjourney;

public class CodigoDuplicado extends Abismos {

    public CodigoDuplicado() {
        super(5);
    }

    @Override
    public String getNome() {
        return "Código Duplicado";
    }

    @Override
    public String aplicarEfeito(Player jogador, Board board, int valorDado) {

        int posAtual = board.getPlayerPosicao(jogador.getId());

        // posição anterior (1 jogada atrás)
        int posAnterior = jogador.getPosicaoJogadas(1);
        if (!board.posicaoValida(posAnterior)) {
            posAnterior = 1;
        }

        int delta = posAnterior - posAtual;

        if (delta != 0) {
            board.movePlayer(jogador.getId(), delta);
        }

        return "O programador " + jogador.getNome()
                + " caiu no abismo Código Duplicado e voltou para a casa anterior (posição "
                + posAnterior + ").";
    }
}
