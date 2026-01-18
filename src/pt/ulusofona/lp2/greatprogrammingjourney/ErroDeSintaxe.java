package pt.ulusofona.lp2.greatprogrammingjourney;

public class ErroDeSintaxe extends Abismos {

    public ErroDeSintaxe() {
        super(0);
    }

    @Override
    public String getNome() {
        return "Erro de sintaxe";
    }

    @Override
    public String aplicarEfeito(Player jogador, Board board, int valorDado) {
        if (jogador == null || board == null){
            return "";
        }

        int id = jogador.getId();
        int posAtual = board.getPlayerPosicao(id);

        if (posAtual > 1) {
            board.movePlayer(id, -1);
            int novaPos = board.getPlayerPosicao(id);
            return "O programador " + jogador.getNome()
                    + " caiu num Erro de Sintaxe e recuou 1 casa para a posição " + novaPos + ".";
        }
        return "O programador " + jogador.getNome()
                + " caiu num Erro de Sintaxe, mas já estava na primeira casa e não recuou.";
    }

}
