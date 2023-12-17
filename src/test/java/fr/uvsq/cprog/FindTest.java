package fr.uvsq.cprog;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FindTest {

    @Test
    public void testFindCommand() {
        Find find = new Find();
        String testFolderPath = "C:\\Users\\kbyan\\Documents\\code\\miniprojet-grp-41_74\\exemple";
        String fileNameToFind = "abc.txt";

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        find.findCommand(testFolderPath, fileNameToFind);

        String expectedOutput = "Fichier trouvé : " + testFolderPath + "\\" + fileNameToFind + System.lineSeparator();
        assertEquals(expectedOutput, outContent.toString());

        System.setOut(System.out);
    }

    @Test
    public void testFindCommandNoMatchingFiles() {
        Find find = new Find();
        String testFolderPath = "C:\\Users\\kbyan\\Documents\\code\\miniprojet-grp-41_74\\exemple";
        String fileNameToFind = "nonexistent.txt";

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        find.findCommand(testFolderPath, fileNameToFind);

        String expectedOutput = "";
        assertEquals(expectedOutput, outContent.toString());

        System.setOut(System.out);
    }

    @Test
    public void testFindCommandNotDirectory() {
        Find find = new Find();
        String testFilePath = "C:\\Users\\kbyan\\Documents\\code\\miniprojet-grp-41_74\\exemple\\abc.txt";
        String fileNameToFind = "abc.txt";

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        find.findCommand(testFilePath, fileNameToFind);

        String expectedOutput = "Le chemin spécifié ne correspond" +
                " pas à un dossier." + System.lineSeparator();
        assertEquals(expectedOutput, outContent.toString());

        System.setOut(System.out);
    }
}
