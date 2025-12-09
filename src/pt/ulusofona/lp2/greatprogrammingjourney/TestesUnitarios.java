package pt.ulusofona.lp2.greatprogrammingjourney;

public class TestesUnitarios extends Ferramentas {

    public TestesUnitarios() {
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
        // Escolha lógica:
        // Testes Unitários ajudam a encontrar problemas simples,
        // como erros de sintaxe, antes da execução.
        //
        // Therefore → anula Erro de Sintaxe (ID 0).
        return abismo.getId() == 0;
    }
}
