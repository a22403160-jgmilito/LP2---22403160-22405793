package pt.ulusofona.lp2.greatprogrammingjourney;

public abstract class Ferramentas {

    protected int id;

    public Ferramentas(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
    public abstract String getNome();

    // Descrição da ferramenta (texto explicativo, se precisares)
    public abstract String getDescricao();

    /**
     * Indica se esta ferramenta é capaz de anular o efeito
     * de um determinado abismo.
     */
    public abstract boolean podeAnular(Abismos abismo);
}
