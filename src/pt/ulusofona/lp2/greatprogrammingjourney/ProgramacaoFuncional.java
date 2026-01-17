package pt.ulusofona.lp2.greatprogrammingjourney;

public class ProgramacaoFuncional extends Ferramentas {

    public ProgramacaoFuncional() {
        super(1);
    }

    @Override
    public String getNome() {
        return "Programação Funcional";
    }

    @Override
    public String getDescricao() {
        return "Permite aplicar conceitos de programação funcional, como funções puras "
                + "e imutabilidade, ajudando a evitar alterações não intencionais de dados "
                + "e a melhorar a lógica do código.";
    }

    @Override
    public boolean podeAnular(Abismos abismo) {
        // Programação Funcional ajuda a evitar erros de lógica,
        // por isso anula o abismo Erro de Lógica (ID 1).
        return abismo.getId() == 6;
    }
}
