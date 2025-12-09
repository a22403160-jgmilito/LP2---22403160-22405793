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

        // Voltar diretamente à posição 1
        jogador.setPosicao(1, board.getSize());

        return "O programador " + jogador.getNome()
                + " sofreu um Crash e voltou à primeira casa.";
    }
}
