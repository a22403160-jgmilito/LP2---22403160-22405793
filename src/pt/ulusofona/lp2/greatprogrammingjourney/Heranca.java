package pt.ulusofona.lp2.greatprogrammingjourney;

public class Heranca extends Ferramentas {

    public Heranca() {
        super(0);
    }

    @Override
    public String getNome() {
        return "Herança";
    }

    @Override
    public String getDescricao() {
        return "Permite reutilizar características de outras ferramentas, " +
                "representando o conceito de herança em programação orientada a objetos.";
    }

    @Override
    public boolean podeAnular(Abismos abismo) {
        if (abismo == null) {
            return false;
        }
        return abismo.getId() == 5; // Código Duplicado
    }
}
