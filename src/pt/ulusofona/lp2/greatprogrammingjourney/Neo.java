package pt.ulusofona.lp2.greatprogrammingjourney;

/**
 * Nova Ferramenta (id 6) - Neo
 *
 * Anula apenas o abismo "Matrix" (id 10).
 */
public class Neo extends Ferramentas {

    public Neo() {
        super(6);
    }

    @Override
    public String getNome() {
        return "Neo";
    }

    @Override
    public String getDescricao() {
        return "Permite sair da Matrix (anula o abismo Matrix).";
    }

    @Override
    public boolean podeAnular(Abismos abismo) {
        return abismo != null && abismo.getId() == 10;
    }
}
