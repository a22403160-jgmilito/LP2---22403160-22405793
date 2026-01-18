package pt.ulusofona.lp2.greatprogrammingjourney;

public class IDE extends Ferramentas {

    public IDE() {
        super(4);
    }

    @Override
    public String getNome() {
        return "IDE";
    }

    @Override
    public String getDescricao() {
        return "Oferece suporte ao desenvolvimento, como autocompletar, deteção de erros "
                + "e ferramentas que ajudam a escrever código mais eficiente e estável.";
    }

    @Override
    public boolean podeAnular(Abismos abismo) {
        int id = abismo.getId();

        // IDEs ajudam com:
        // - Erro de Sintaxe (0)
        // - Erro de Lógica (1)
        // - Código Duplicado (5)
        return id == 0 || id == 1 || id == 5 || id == 8;
    }
}
