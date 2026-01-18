package pt.ulusofona.lp2.greatprogrammingjourney;

public class EfeitosSecundarios extends Abismos {

    public EfeitosSecundarios() {
        super(6); // ID do abismo Efeitos Secundários
    }

    @Override
    public String getNome() {
        return "Efeitos Secundários";
    }

    @Override
    public String aplicarEfeito(Player jogador, Board board, int valorDado) {
        if (jogador == null || board == null) return "";

        int id = jogador.getId();
        int posAtual = board.getPlayerPosicao(id);

        int posDoisAtras = jogador.getPosicaoJogadas(2);
        if (!board.posicaoValida(posDoisAtras)) {
            posDoisAtras = 1;
        }

        int delta = posDoisAtras - posAtual;
        if (delta != 0) {
            board.movePlayer(id, delta);
        }

        int novaPos = board.getPlayerPosicao(id);

        if (novaPos != posAtual) {
            return "O programador " + jogador.getNome()
                    + " sofreu Efeitos Secundários e voltou para a posição de 2 movimentos atrás ("
                    + novaPos + ").";
        }
        return "O programador " + jogador.getNome()
                + " sofreu Efeitos Secundários, mas manteve-se na mesma posição.";
    }

}
