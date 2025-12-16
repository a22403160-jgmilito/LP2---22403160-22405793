package pt.ulusofona.lp2.greatprogrammingjourney;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class SaveLoadTest {

    private static String[][] players() {
        return new String[][]{
                {"1", "Ana", "Java", "#FF0000"},
                {"2", "Bruno", "Java", "#00FF00"}
        };
    }


    @Test
    void load_invalido_lancaInvalidFileException(@TempDir Path tempDir) throws Exception {
        File f = tempDir.resolve("broken.txt").toFile();
        try (FileWriter fw = new FileWriter(f)) {
            fw.write("WORLD;10;1;0;0\n"); // winner=0 inválido mas aceitável como int
            fw.write("PLAYERS;2\n");
            fw.write("PLAYER;1;Ana;Java;#FF0000;1\n");
            // falta 2.º PLAYER
        }

        GameManager gm = new GameManager();
        assertThrows(InvalidFileException.class, () -> gm.loadGame(f));
    }

    @Test
    void load_ficheiroInexistente_lancaFileNotFound(@TempDir Path tempDir) {
        File f = tempDir.resolve("nao_existe.txt").toFile();
        GameManager gm = new GameManager();
        assertThrows(java.io.FileNotFoundException.class, () -> gm.loadGame(f));
    }
}
