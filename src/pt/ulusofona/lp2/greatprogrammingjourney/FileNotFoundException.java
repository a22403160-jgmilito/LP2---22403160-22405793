package pt.ulusofona.lp2.greatprogrammingjourney;

public class FileNotFoundException extends Abismos {

    public FileNotFoundException() {
        super(3);
    }

    @Override
    public String getNome() {
        return "FileNotFoundException";
    }

    @Override
    public String aplicarEfeito(Player jogador, Board board, int valorDado) {

        int posAtual = jogador.getPosicao();

        // Recuar 3 casas, mínimo 1
        int novaPos = posAtual - 3;
        if (novaPos < 1) {
            novaPos = 1;
        }
        jogador.setPosicao(novaPos, board.getSize());

        if (novaPos < posAtual) {
            return "O programador " + jogador.getNome()
                    + " encontrou um FileNotFoundException e recuou 3 casas, indo para a posição "
                    + novaPos + ".";
        } else {
            return "O programador " + jogador.getNome()
                    + " encontrou um FileNotFoundException, mas já estava muito perto da primeira casa e permaneceu na posição "
                    + novaPos + ".";
        }
    }
}
