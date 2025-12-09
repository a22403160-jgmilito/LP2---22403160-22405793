package pt.ulusofona.lp2.greatprogrammingjourney;

public class ErroDeLogica extends Abismos {

    public ErroDeLogica() {
        super(1);
    }

    @Override
    public String getNome() {
        return "Erro de Lógica";
    }

    @Override
    public String aplicarEfeito(Player jogador, Board board, int valorDado) {

        int posAtual = jogador.getPosicao();
        int n = valorDado / 2;

        // Se por algum motivo valorDado vier 0 ou negativo, não recua
        if (n <= 0) {
            return "O programador " + jogador.getNome()
                    + " caiu num Erro de Lógica, mas o impacto foi nulo e não recuou casas.";
        }
        int novaPos = posAtual - n;
        if (novaPos < 1) {
            novaPos = 1; // nunca abaixo da casa 1
        }
        jogador.setPosicao(novaPos, board.getSize());
        if (novaPos < posAtual) {
            return "O programador " + jogador.getNome()
                    + " cometeu um Erro de Lógica e recuou " + n
                    + " casas, indo para a posição " + novaPos + ".";
        } else {
            // Caso extremo em que já estava na 1 e não pode recuar
            return "O programador " + jogador.getNome()
                    + " cometeu um Erro de Lógica, mas já estava na primeira casa e não recuou.";
        }
    }
}
