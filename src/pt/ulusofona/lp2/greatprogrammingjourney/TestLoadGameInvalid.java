package pt.ulusofona.lp2.greatprogrammingjourney;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class TestLoadGameInvalid {

    @Test
    void load_worldHeaderInvalido_lancaInvalidFileException(@TempDir Path tempDir) throws Exception {
        File f = tempDir.resolve("bad_world.txt").toFile();
        try (FileWriter fw = new FileWriter(f)) {
            fw.write("W0RLD;10;1;0;-1\n"); // errado: não é "WORLD"
            fw.write("PLAYERS;2\n");
            fw.write("PLAYER;1;Ana;Java;#FF0000;1\n");
            fw.write("PLAYER;2;Bruno;C++;#00FF00;1\n");
        }

        GameManager gm = new GameManager();
        assertThrows(InvalidFileException.class, () -> gm.loadGame(f));
    }

    @Test
    void load_playerPosicaoForaDoTabuleiro_lancaInvalidFileException(@TempDir Path tempDir) throws Exception {
        File f = tempDir.resolve("bad_player_pos.txt").toFile();
        try (FileWriter fw = new FileWriter(f)) {
            fw.write("WORLD;10;1;0;-1\n");
            fw.write("PLAYERS;2\n");
            fw.write("PLAYER;1;Ana;Java;#FF0000;11\n"); // inválida: > worldSize
            fw.write("PLAYER;2;Bruno;C++;#00FF00;1\n");
        }

        GameManager gm = new GameManager();
        assertThrows(InvalidFileException.class, () -> gm.loadGame(f));
    }

    @Test
    void load_toolsHeaderInvalido_lancaInvalidFileException(@TempDir Path tempDir) throws Exception {
        File f = tempDir.resolve("bad_tools_header.txt").toFile();
        try (FileWriter fw = new FileWriter(f)) {
            fw.write("WORLD;10;1;0;-1\n");
            fw.write("PLAYERS;2\n");
            fw.write("PLAYER;1;Ana;Java;#FF0000;1\n");
            fw.write("PLAYER;2;Bruno;C++;#00FF00;1\n");
            fw.write("ABYSSES;0\n");
            fw.write("T00LS;0\n"); // errado: não é "TOOLS"
        }

        GameManager gm = new GameManager();
        assertThrows(InvalidFileException.class, () -> gm.loadGame(f));
    }
}
