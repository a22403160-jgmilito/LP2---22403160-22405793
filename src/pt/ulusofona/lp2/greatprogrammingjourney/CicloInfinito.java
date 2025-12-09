package pt.ulusofona.lp2.greatprogrammingjourney;

public class CicloInfinito extends Abismos {

    public CicloInfinito() {
        super(8); // ID do abismo Ciclo Infinito
    }

    @Override
    public String getNome() {
        return "Ciclo Infinito";
    }

    @Override
    public String aplicarEfeito(Player jogador, Board board, int valorDado) {

        int posAtual = jogador.getPosicao();

        // Não altera a posição: o jogador fica preso na casa atual.
        // A mudança de estado para "Preso" e a lógica de ser libertado
        // quando outro jogador cair aqui serão tratadas no GameManager.

        return "O programador " + jogador.getNome()
                + " entrou num Ciclo Infinito na casa " + posAtual
                + " e ficou preso, sem poder avançar até que outro programador caia na mesma casa.";
    }
}
