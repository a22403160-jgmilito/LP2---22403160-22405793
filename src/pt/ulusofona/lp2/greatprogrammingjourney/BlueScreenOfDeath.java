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

        jogador.setAlive(false);
        
        return "O programador " + jogador.getNome()
                + " sofreu um Blue Screen of Death e foi derrotado, ficando fora do jogo.";
    }
}
