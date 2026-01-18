package pt.ulusofona.lp2.greatprogrammingjourney;

public class Crash extends Abismos {

    public Crash() {
        super(4); // ID do abismo Crash
    }

    @Override
    public String getNome() {
        return "Crash";
    }

    @Override
    public String aplicarEfeito(Player jogador, Board board, int valorDado) {
        if (jogador == null || board == null) return "";

        int id = jogador.getId();
        board.setPlayerPosicao(id, 1);

        return "O programador " + jogador.getNome()
                + " sofreu um Crash e voltou à primeira casa.";
    }
}
