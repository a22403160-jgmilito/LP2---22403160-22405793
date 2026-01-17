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

        int posAnterior = jogador.getPosicaoJogadas(1);
        if (!board.posicaoValida(posAnterior)) {
            posAnterior = 1;
        }

        board.setPlayerPosicao(jogador.getId(), posAnterior);

        return "O programador " + jogador.getNome()
                + " caiu no abismo Código Duplicado e voltou para a casa anterior (posição "
                + posAnterior + ").";
    }

}
