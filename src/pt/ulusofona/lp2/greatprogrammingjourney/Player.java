package pt.ulusofona.lp2.greatprogrammingjourney;

public class Player {
    private int id;
    private String nome;
    private String infoPlayer;
    private String cor;

    public Player(int id, String nome, String linguagens, String cor) {
        this.id = id;
        this.nome = nome;
        this.infoPlayer = linguagens;
        this.cor = cor;
    }

    public int getId() {
        return id;
    }
    public String getNome() {
        return nome;
    }
    public String getLinguagens() {
        return infoPlayer;
    }
    public String getCor() {
        return cor;
    }
}
