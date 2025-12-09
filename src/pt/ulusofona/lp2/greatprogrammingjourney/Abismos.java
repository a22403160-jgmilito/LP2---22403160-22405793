package pt.ulusofona.lp2.greatprogrammingjourney;

public abstract class Abismos {

    protected int id;

    public Abismos(int id) {
        this.id = id;
    }

    /** ID do tipo de abismo (0 a 9) */
    public int getId() {
        return id;
    }

    /** Nome legível do abismo (Erro de Sintaxe, Erro de Lógica, Crash, etc.) */
    public abstract String getNome();

    /**
     * Aplica o efeito deste abismo ao jogador.
     *
     * @param jogador   jogador que caiu no abismo
     * @param board     tabuleiro, para ler/alterar a posição
     * @param valorDado valor do dado que levou o jogador até aqui
     * @return mensagem a explicar o que aconteceu
     */
    public abstract String aplicarEfeito(Player jogador, Board board, int valorDado);
}
