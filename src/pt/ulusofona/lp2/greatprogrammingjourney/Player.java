package pt.ulusofona.lp2.greatprogrammingjourney;

import java.util.ArrayList;

public class Player {
    private int id;
    private String nome;
    private String linguagens;
    private String cor;

    public Player(int id, String nome, String linguagens, String cor) {
        this.id = id;
        this.nome = nome;
        this.linguagens = linguagens;
        this.cor = cor;
    }

    public int getId() {
        return id;
    }
    public String getNome() {
        return nome;
    }
    public String getLinguagens() {
        if (linguagens == null || linguagens.isEmpty()) {
            return "";
        }
        String[] partes = linguagens.split(";");
        ArrayList<String> lista = new ArrayList<>();
        for (String s : partes) {
            s = s.trim();
            if (!s.isEmpty()) {
                lista.add(s);
            }
        }
        lista.sort(String.CASE_INSENSITIVE_ORDER);
        return String.join("; ", lista);
    }
    public String getCor() {
        return cor;
    }
    public String getLinguagensNormalizadas() {
        if (linguagens == null || linguagens.isEmpty()) {
            return "";
        }
        String[] partes = linguagens.split(";");
        ArrayList<String> lista = new ArrayList<>();
        for (String s : partes) {
            s = s.trim();
            if (!s.isEmpty()) {
                lista.add(s);
            }
        }
        lista.sort(String.CASE_INSENSITIVE_ORDER);
        return String.join("; ", lista);
    }
    public String[] asArray() {
        return new String[]{ String.valueOf(id), nome, getLinguagensNormalizadas(), cor};
    }
    @Override
    public String toString() {
        return id + " | " + nome + " | " + getLinguagensNormalizadas() + " | " + cor;
    }
    // fazer um get sobre as informacoes do jogador,
    // um return de um array de string com as infomarcoes todas
    // e usar isso no GameManager nas funcoes getProgrammerInfo e getProgrammerInfoAsStr
    // Ter um toString aqui no player
}
