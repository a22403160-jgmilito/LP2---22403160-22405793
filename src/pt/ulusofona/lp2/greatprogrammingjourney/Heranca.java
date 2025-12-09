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
        // POR ENQUANTO: não anula nenhum abismo diretamente.
        // Quando tiveres o mapeamento ferramenta x abismo:
        // if (abismo.getId() == 0 /*Erro de Sintaxe*/ ) return true;
        return false;
    }
}
