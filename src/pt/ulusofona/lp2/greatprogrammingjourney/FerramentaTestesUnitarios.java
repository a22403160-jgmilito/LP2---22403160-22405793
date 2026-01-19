package pt.ulusofona.lp2.greatprogrammingjourney;

public class FerramentaTestesUnitarios extends Ferramentas {

    public FerramentaTestesUnitarios() {
        super(2);
    }

    @Override
    public String getNome() {
        return "Testes Unitários";
    }

    @Override
    public String getDescricao() {
        return "Permite validar cada parte do código de forma isolada, "
                + "identificando erros precocemente e garantindo maior qualidade e "
                + "confiabilidade no software.";
    }

    @Override
    public boolean podeAnular(Abismos abismo) {
        if (abismo == null) {
            return false;
        }
        int id = abismo.getId();
        return id == 1 || id == 4 ;
    }
}
