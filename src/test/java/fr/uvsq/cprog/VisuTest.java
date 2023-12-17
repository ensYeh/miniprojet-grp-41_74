package fr.uvsq.cprog;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VisuTest {

    private final String testFolderPath = "c:/Users/kbyan/Documents/cours";

    @Test
    public void testIsTextFile() {
        Visu visu = new Visu();

        // Crée un fichier texte
        File textFile = new File(testFolderPath + "/abc.txt");
        assertTrue(visu.isTextFile(textFile));

        // Crée un fichier non texte
        File nonTextFile = new File(testFolderPath + "/34834ce70cb26bcfdec99383fdb9da90.jpg");
        assertFalse(visu.isTextFile(nonTextFile));
    }

    @Test
    public void testAfficherContenuTexte() {
        Visu visu = new Visu();

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        File textFile = new File(testFolderPath + "/abc.txt");
        try {
            visu.afficherContenuTexte(textFile);
            assertTrue(outContent.toString().contains("Contenu du fichier texte"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.setOut(System.out);
    }

    @Test
    public void testAfficherTailleFichier() {
        Visu visu = new Visu();

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        File testFile = new File(testFolderPath + "/34834ce70cb26bcfdec99383fdb9da90.jpg");
        try {
            visu.afficherTailleFichier(testFile);
            assertTrue(outContent.toString().contains("Taille du fichier : " + testFile.length() + " octets"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.setOut(System.out);
    }

    @Test
    public void testVisuCommand_TextFile() throws IOException {
        Visu visu = new Visu();

        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        assertTrue(tempDir.isDirectory());

        File tempTextFile = new File(tempDir, "test.txt");

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        visu.visuCommand(tempDir.getAbsolutePath(), 1);

        System.setOut(System.out);

        System.out.println("Contenu du fichier texte : " + outContent.toString());
        System.out.println("Contenu réel du fichier : ");
        Files.lines(tempTextFile.toPath()).forEach(System.out::println);

        System.out.println("Fin de la lecture du fichier.");

        assertTrue(outContent.toString().contains("Contenu du fichier texte"));
    }

    @Test
    public void testVisuCommand_InvalidNER() {
        Visu visu = new Visu();

        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        assertTrue(tempDir.isDirectory());

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        visu.visuCommand(tempDir.getAbsolutePath(), 0);

        System.setOut(System.out);

        assertTrue(outContent.toString().contains("Numéro NER invalide"));
    }
}
