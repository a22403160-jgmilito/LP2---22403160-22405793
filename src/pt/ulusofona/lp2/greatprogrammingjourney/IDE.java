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
        if (abismo == null) {
            return false;
        }
        int id = abismo.getId();
        return id == 0 || id == 1 || id == 3 || id == 5 || id == 8 || id == 9;
    }
}
