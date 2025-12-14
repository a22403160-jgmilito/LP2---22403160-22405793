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

        int recuo = 0;

        if (valorDado == 3){
            recuo = 1;
        }

        if (valorDado == 5){
            recuo = 2;
        }

        if (valorDado == 6){
            recuo = 3;
        }

        if (recuo <= 0) {
            return "O programador " + jogador.getNome()
                    + " caiu num Erro de Lógica, mas o impacto foi nulo e não recuou casas.";
        }

        board.movePlayer(jogador.getId(), -recuo);

        int novaPos = board.getPlayerPosicao(jogador.getId());
        return "O programador " + jogador.getNome()
                + " cometeu um Erro de Lógica e recuou " + recuo
                + " casas, indo para a posição " + novaPos + ".";
    }
}
