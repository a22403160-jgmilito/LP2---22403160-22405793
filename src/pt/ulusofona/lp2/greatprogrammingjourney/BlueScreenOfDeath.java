package pt.ulusofona.lp2.greatprogrammingjourney;

public class BlueScreenOfDeath extends Abismos {

    public BlueScreenOfDeath() {
        super(7);
    }

    @Override
    public String getNome() {
        return "Blue Screen of Death";
    }

    @Override
    public String aplicarEfeito(Player jogador, Board board, int valorDado) {
        // Aqui não alteramos posição; o efeito real é derrota.
        // A remoção do jogador / mudança de estado para "Derrotado"
        // deve ser tratada pelo GameManager quando processar este abismo.

        return "O programador " + jogador.getNome()
                + " sofreu um Blue Screen of Death e foi derrotado, ficando fora do jogo.";
    }
}
