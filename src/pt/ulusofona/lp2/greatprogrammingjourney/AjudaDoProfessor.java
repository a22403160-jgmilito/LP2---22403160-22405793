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
        if (abismo == null) {
            return false;
        }
        int id = abismo.getId();
        return id == 0 || id == 1 || id == 2 || id == 3
                || id == 5 || id == 6 ||  id == 20;
    }

}
