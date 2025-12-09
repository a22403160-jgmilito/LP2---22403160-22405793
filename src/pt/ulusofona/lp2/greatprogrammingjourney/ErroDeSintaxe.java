package pt.ulusofona.lp2.greatprogrammingjourney;

public class ErroDeSintaxe extends Abismos {

    public ErroDeSintaxe() {
        super(0);
    }

    @Override
    public String getNome() {
        return "Erro de Sintaxe";
    }

    @Override
    public String aplicarEfeito(Player jogador, Board board, int valorDado) {

        int posAtual = jogador.getPosicao();

        // recuar 1 casa, mas nunca abaixo de 1
        int novaPos = (posAtual > 1) ? posAtual - 1 : 1;

        jogador.setPosicao(novaPos, board.getSize());

        if (posAtual > 1) {
            return "O programador " + jogador.getNome()
                    + " caiu num Erro de Sintaxe e recuou 1 casa para a posição " + novaPos + ".";
        } else {
            return "O programador " + jogador.getNome()
                    + " caiu num Erro de Sintaxe, mas já estava na primeira casa e não recuou.";
        }
    }
}
