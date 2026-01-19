package pt.ulusofona.lp2.greatprogrammingjourney;

public class TratamentoDeExcepcoes extends Ferramentas {

    public TratamentoDeExcepcoes() {
        super(3);
    }

    @Override
    public String getNome() {
        return "Tratamento de Excepções";
    }

    @Override
    public String getDescricao() {
        return "Permite lidar com erros em tempo de execução, evitando que o jogo falhe "
                + "abruptamente e garantindo continuidade mesmo perante imprevistos.";
    }

    @Override
    public boolean podeAnular(Abismos abismo) {
        if (abismo == null) {
            return false;
        }
        int id = abismo.getId();
        return id == 2 || id == 3 || id == 4;
    }
}
