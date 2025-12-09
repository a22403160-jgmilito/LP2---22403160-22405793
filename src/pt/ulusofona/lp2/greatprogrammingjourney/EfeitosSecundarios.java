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

        int posAtual = jogador.getPosicao();

        // posição de 2 jogadas atrás (histórico do jogador)
        int posDoisMovimentosAtras = jogador.getPosicaoJogadas(2);

        // segurança: garantir que é válida
        if (!board.posicaoValida(posDoisMovimentosAtras)) {
            posDoisMovimentosAtras = 1;
        }
        jogador.setPosicao(posDoisMovimentosAtras, board.getSize());

        if (posDoisMovimentosAtras != posAtual) {
            return "O programador " + jogador.getNome()
                    + " sofreu Efeitos Secundários e voltou para a posição de 2 movimentos atrás ("
                    + posDoisMovimentosAtras + ").";
        } else {
            return "O programador " + jogador.getNome()
                    + " sofreu Efeitos Secundários, mas manteve-se na mesma posição.";
        }
    }
}
