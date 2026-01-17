package pt.ulusofona.lp2.greatprogrammingjourney;

public class LLM extends Abismos {

    public LLM() { super(20); }

    @Override
    public String getNome() {
        return "LLM";
    }

    @Override
    public String aplicarEfeito(Player jogador, Board board, int valorDado) {

        if (jogador == null || board == null) {
            return "";
        }
        // Se já for experiente: avança o valor do dado
        if (jogador.isExperiente()) {
            int id = jogador.getId();

            // mover "valorDado" para a frente (respeitando limites e bounce do Board)
            for (int i = 0; i < valorDado; i++) {
                board.movePlayer(id, 1);
            }
            return "Caiu no LLM mas já tem experiência! Avança tantas casas quantas as do último movimento";
        }

        // Se não for experiente: recua para a posição anterior
        int posAnterior = jogador.getPosicaoJogadas(1);
        board.setPlayerPosicao(jogador.getId(), posAnterior);

        return "Caiu no LLM! Recua para a posição onde estava antes";
    }
}
