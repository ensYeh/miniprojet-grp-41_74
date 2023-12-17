package fr.uvsq.cprog;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.junit.jupiter.api.Assertions.*;

public class MkdirTest {

    @Test
    public void testMkdirCommandSuccess() {
        Mkdir mkdir = new Mkdir();
        String testFolderPath = "C:\\Users\\kbyan\\Documents\\code\\miniprojet-grp-41_74\\exemple";
        String newDirectoryName = "testDirectory";

        File testFolder = new File(testFolderPath);
        assertTrue(testFolder.isDirectory());

        String newDirectoryPath = testFolderPath + "/" + newDirectoryName;
        mkdir.mkdirCommand(testFolderPath, newDirectoryName);

        File createdDirectory = new File(newDirectoryPath);
        assertTrue(createdDirectory.exists());
        assertTrue(createdDirectory.isDirectory());

        // Nettoie le répertoire créé pour les tests
        cleanupTestDirectory(createdDirectory);
    }

    @Test
    public void testMkdirCommandInvalidPath() {
        Mkdir mkdir = new Mkdir();
        String invalidPath = "invalid/path";
        String newDirectoryName = "testDirectory";

        File invalidFolder = new File(invalidPath);
        assertFalse(invalidFolder.isDirectory());

        mkdir.mkdirCommand(invalidPath, newDirectoryName);

        File createdDirectory = new File(invalidPath + "/" + newDirectoryName);
        assertFalse(createdDirectory.exists());
    }

    @Test
    public void testMkdirCommandExistingParentDirectory() {
        Mkdir mkdir = new Mkdir();
        String testFolderPath = "C:\\Users\\kbyan\\Documents\\code\\miniprojet-grp-41_74\\exemple";
        String existingDirectoryName = "aaaa";

        File existingDirectory = new File(testFolderPath, existingDirectoryName);
        assertTrue(existingDirectory.exists());
        assertTrue(existingDirectory.isDirectory());

        String newDirectoryName = "test";
        mkdir.mkdirCommand(testFolderPath + "/" + existingDirectoryName, newDirectoryName);

        File createdDirectory = new File(testFolderPath + "/" + existingDirectoryName + "/" + newDirectoryName);
        assertTrue(createdDirectory.exists());
        assertTrue(createdDirectory.isDirectory());
    }

    private void cleanupTestDirectory(File directory) {
        try {
            Files.walk(Paths.get(directory.getAbsolutePath()))
                    .map(Path::toFile)
                    .sorted((o1, o2) -> -o1.compareTo(o2))
                    .forEach(File::delete);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
