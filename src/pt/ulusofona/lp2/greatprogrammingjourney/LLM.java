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

        int id = jogador.getId();

        // Com experiência: avança exatamente "valorDado"
        if (jogador.isExperiente()) {
            board.movePlayer(id, valorDado);
            return "Caiu no LLM mas já tem experiência! Avança tantas casas quantas as do último movimento";
        }

        // Sem experiência: volta para a posição anterior (1 turno atrás)
        int posAtual = board.getPlayerPosicao(id);
        int posAnterior = jogador.getPosicaoJogadas(1);

        if (!board.posicaoValida(posAnterior)) {
            posAnterior = 1;
        }

        int delta = posAnterior - posAtual;
        board.movePlayer(id, delta);

        return "Caiu no LLM! Recua para a posição onde estava antes";
    }
}
