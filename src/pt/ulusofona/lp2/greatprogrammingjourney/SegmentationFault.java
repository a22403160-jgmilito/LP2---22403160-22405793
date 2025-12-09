package pt.ulusofona.lp2.greatprogrammingjourney;

import java.util.List;

public class SegmentationFault extends Abismos {

    public SegmentationFault() {
        super(9);
    }

    @Override
    public String getNome() {
        return "Segmentation Fault";
    }

    @Override
    public String aplicarEfeito(Player jogador, Board board, int valorDado) {

        int posAtual = jogador.getPosicao();

        // Verificar quantos jogadores estão nesta casa
        List<Integer> jogadoresNaCasa = board.getJogadoresNaPosicao(posAtual);

        if (jogadoresNaCasa == null || jogadoresNaCasa.size() < 2) {
            // Só um jogador na casa → nada acontece
            return "O programador " + jogador.getNome()
                    + " encontrou um Segmentation Fault, mas estava sozinho na casa e nada aconteceu.";
        }

        // Há 2 ou mais jogadores nesta casa.
        // De acordo com a descrição, TODOS deveriam recuar 3 casas.
        // Nesta classe só temos referência ao 'jogador' atual, por isso
        // recuamos apenas este aqui. A aplicação do efeito aos restantes
        // jogadores que partilham a casa pode ser tratada no GameManager,
        // ao detetar este tipo de abismo.

        int novaPos = posAtual - 3;
        if (novaPos < 1) {
            novaPos = 1;
        }

        jogador.setPosicao(novaPos, board.getSize());

        return "Segmentation Fault! Como havia vários programadores na mesma casa, "
                + "o programador " + jogador.getNome()
                + " recuou 3 casas, indo para a posição " + novaPos + ".";
    }
}
