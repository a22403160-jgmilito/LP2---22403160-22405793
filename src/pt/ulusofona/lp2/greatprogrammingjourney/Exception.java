package pt.ulusofona.lp2.greatprogrammingjourney;

public class Exception extends Abismos {

    public Exception() {
        super(2);
    }

    @Override
    public String getNome() {
        return "Exception";
    }

    @Override
    public String aplicarEfeito(Player jogador, Board board, int valorDado) {

        int posAtual = jogador.getPosicao();

        // recua 2 casas, nunca abaixo de 1
        int novaPos = posAtual - 2;
        if (novaPos < 1) {
            novaPos = 1;
        }

        // Aplicar nova posição usando o Player
        jogador.setPosicao(novaPos, board.getSize());

        if (novaPos < posAtual) {
            return "O programador " + jogador.getNome()
                    + " encontrou uma Exception e recuou 2 casas, indo para a posição " + novaPos + ".";
        } else {
            // caso extremo: já estava na casa 1 ou 2, não consegue recuar tudo
            return "O programador " + jogador.getNome()
                    + " encontrou uma Exception, mas já estava muito perto do início e ficou na posição " + novaPos + ".";
        }
    }
}
