package pt.ulusofona.lp2.greatprogrammingjourney;

public class LLM extends Abismos {

    public LLM() {
        super(20);
    }

    @Override
    public String getNome() {
        return "LLM";
    }

    @Override
    public String aplicarEfeito(Player jogador, Board board, int valorDado) {

        if (jogador == null || board == null) {
            return "";
        }

        // LLM com experiência: avança exatamente o valor do dado
        if (jogador.isExperiente()) {
            int posAtual = board.getPlayerPosicao(jogador.getId());
            int destino = posAtual + valorDado;

            if (destino < 1) {
                destino = 1;
            }
            if (destino > board.getSize()) {
                destino = board.getSize();
            }

            board.setPlayerPosicao(jogador.getId(), destino);

            return "Caiu no LLM mas já tem experiência! Avança tantas casas quantas as do último movimento";
        }

        // LLM sem experiência: recua para a posição anterior
        int posAnterior = jogador.getPosicaoJogadas(1);
        board.setPlayerPosicao(jogador.getId(), posAnterior);

        return "Caiu no LLM! Recua para a posição onde estava antes";
    }
}
