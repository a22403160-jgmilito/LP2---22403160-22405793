package pt.ulusofona.lp2.greatprogrammingjourney;

public class ExceptionAbismo extends Abismos {

    public ExceptionAbismo() {
        super(2);
    }

    @Override
    public String getNome() {
        return "Exception";
    }

    @Override
    public String aplicarEfeito(Player jogador, Board board, int valorDado) {
        if (jogador == null || board == null) return "";

        int id = jogador.getId();
        int posAtual = board.getPlayerPosicao(id);

        int recuo = Math.min(2, posAtual - 1); // não passar da casa 1
        if (recuo > 0) {
            board.movePlayer(id, -recuo);
        }

        int novaPos = board.getPlayerPosicao(id);

        if (novaPos < posAtual) {
            return "O programador " + jogador.getNome()
                    + " encontrou uma Exception e recuou 2 casas, indo para a posição " + novaPos + ".";
        }
        return "O programador " + jogador.getNome()
                + " encontrou uma Exception, mas já estava muito perto do início e ficou na posição " + novaPos + ".";
    }

}
