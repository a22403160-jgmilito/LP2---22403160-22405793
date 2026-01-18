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

        // Se já está na meta, não aplicar efeito nenhum (não pode "sair" da vitória)
        int posAtual = board.getPlayerPosicao(id);
        if (board.posicaoVitoria(posAtual)) {
            return "";
        }

        // Com experiência: avança "valorDado" mas NUNCA passa do fim (não pode dar bounce)
        if (jogador.isExperiente()) {
            int destino = posAtual + valorDado;
            if (destino > board.getSize()) {
                destino = board.getSize();
            }

            int delta = destino - posAtual;
            board.movePlayer(id, delta);

            return "Caiu no LLM mas já tem experiência! Avança tantas casas quantas as do último movimento";
        }

        // Sem experiência: volta para a posição anterior (1 turno atrás)
        int posAnterior = jogador.getPosicaoJogadas(1);

        if (!board.posicaoValida(posAnterior)) {
            posAnterior = 1;
        }

        int delta = posAnterior - posAtual;
        board.movePlayer(id, delta);

        return "Caiu no LLM! Recua para a posição onde estava antes";
    }
}
