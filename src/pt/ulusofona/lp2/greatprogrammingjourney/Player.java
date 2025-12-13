package pt.ulusofona.lp2.greatprogrammingjourney;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private int id;
    private String nome;
    private String linguagens;
    private String cor;
    private int posicao;
    private boolean isAlive = true;     // true = Em jogo / Preso, false = Derrotado
    private boolean isEnabled = true;   // true = ativo, false = Preso


    public Player(int id, String nome, String linguagens, String cor) {
        this.id = id;
        this.nome = nome;
        this.linguagens = linguagens;
        this.cor = cor;


        // posição inicial = 1
        this.posicao = 1;
        // registar posição inicial no histórico
        historicoPosicoes.add(posicao);
    }
    private List<Integer> historicoPosicoes = new ArrayList<>();
    private List<Ferramentas> ferramentas = new ArrayList<>();

    public int getId() {
        return id;
    }
    public String getNome() {
        return nome;
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
        return id + " | " + nome + " | " + posicao + " | " + getFerramentasAsString() + " | " + getLinguagensNormalizadas();
    }

    public int getPosicao() {
        return posicao;
    }
    public void setPosicao(int novaPos, int boardSize) {
        if (novaPos < 1) {
            novaPos = 1;
        } else if (novaPos > boardSize) {
            novaPos = boardSize;
        }
        this.posicao = novaPos;
        historicoPosicoes.add(this.posicao);
    }
    public int getPosicaoJogadas(int n) {
        if (historicoPosicoes.isEmpty()) {
            return posicao;
        }
        int idx = historicoPosicoes.size() - 1 - n;
        if (idx < 0) {
            idx = 0;
        }
        return historicoPosicoes.get(idx);
    }

    //Parte 2
    public void adicionarFerramenta(Ferramentas f) {
        if (f != null) {
            ferramentas.add(f);
        }
    }
    public List<Ferramentas> getFerramentas() {
        return new ArrayList<>(ferramentas);
    }
    /**
     * Devolve uma ferramenta que consiga anular o abismo dado,
     * ou null se não tiver nenhuma aplicável.
     */
    public Ferramentas getFerramentaQueAnula(Abismos abismo) {
        for (Ferramentas f : ferramentas) {
            if (f.podeAnular(abismo)) {
                return f;
            }
        }
        return null;
    }
    public void removeFerramenta(Ferramentas f) {
        ferramentas.remove(f);
    }
    public String getFerramentasAsString() {
        if (ferramentas == null || ferramentas.isEmpty()) {
            return "No tools";
        }

        ArrayList<String> nomes = new ArrayList<>();
        for (Ferramentas f : ferramentas) {
            if (f != null && f.getNome() != null) {
                nomes.add(f.getNome());
            }
        }

        if (nomes.isEmpty()) {
            return "No tools";
        }

        // Sem espaços, como no enunciado: "IDE;Herança"
        return String.join(";", nomes);
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        this.isAlive = alive;
        if (!alive) {
            // se morrer, deixa de fazer sentido estar "ativo"
            this.isEnabled = false;
        }
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        // só pode ficar enabled se estiver vivo
        if (isAlive) {
            this.isEnabled = enabled;
        }
    }

    public String getEstadoComoTexto() {
        if (!isAlive) {
            return "Derrotado";
        }
        if (!isEnabled) {
            return "Preso";
        }
        return "Em Jogo";
    }

    public boolean temFerramentaComId(int idFerramenta) {
        for (Ferramentas f : ferramentas) {
            if (f != null && f.getId() == idFerramenta) {
                return true;
            }
        }
        return false;
    }

    public String getLinguagensOriginal() {
        return linguagens == null ? "" : linguagens.trim();
    }

}
