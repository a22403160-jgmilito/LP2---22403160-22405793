package pt.ulusofona.lp2.greatprogrammingjourney;

import java.util.List;

public class SegmentationFault extends Abismos {

    public SegmentationFault() {
        super(9);
    }

    @Override
    public String getNome() {
        return "Segmentation Fault";
    }

    @Override
    public String aplicarEfeito(Player jogador, Board board, int valorDado) {

        return "";
    }
}
