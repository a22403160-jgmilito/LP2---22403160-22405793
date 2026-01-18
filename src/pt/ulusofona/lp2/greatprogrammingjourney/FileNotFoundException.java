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
        if (jogador == null || board == null) return "";

        int id = jogador.getId();
        int posAtual = board.getPlayerPosicao(id);

        int recuo = Math.min(3, posAtual - 1);
        if (recuo > 0) {
            board.movePlayer(id, -recuo);
        }

        int novaPos = board.getPlayerPosicao(id);

        if (novaPos < posAtual) {
            return "O programador " + jogador.getNome()
                    + " encontrou um FileNotFoundException e recuou 3 casas, indo para a posição "
                    + novaPos + ".";
        }
        return "O programador " + jogador.getNome()
                + " encontrou um FileNotFoundException, mas já estava muito perto da primeira casa e permaneceu na posição "
                + novaPos + ".";
    }

}
