package pt.ulusofona.lp2.greatprogrammingjourney;

public class AjudaDoProfessor extends Ferramentas {

    public AjudaDoProfessor() {
        super(5);
    }

    @Override
    public String getNome() {
        return "Ajuda do Professor";
    }

    @Override
    public String getDescricao() {
        return "Oferece suporte e orientações para resolver problemas durante o jogo, "
                + "ajudando a ultrapassar dificuldades e a evitar erros comuns.";
    }

    @Override
    public boolean podeAnular(Abismos abismo) {
        int id = abismo.getId();

        // Anula qualquer abismo comum (0–3, 5,6,9)
        // NÃO anula erros fatais ou mecânicas especiais: Crash, BSOD, Ciclo Infinito
        return id == 0 || id == 1 || id == 2 || id == 3
                || id == 5 || id == 6 || id == 9 || id == 20;
    }
}
