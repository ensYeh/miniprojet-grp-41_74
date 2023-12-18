package fr.uvsq.cprog;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AppTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Test
    public void testLsCommand() {
        App app = new App();

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        app.afficherContenuRepertoireAvecNER();

        System.out.println("expected Contenu du répertoire :");
        System.out.println(outContent.toString());

        System.setOut(System.out);
    } 

    @Test
    public void testProcessUserActionWithNERVisu() {

        try (ByteArrayInputStream in = new ByteArrayInputStream("1 visu".getBytes())) {
            System.setIn(in);

            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outContent));
    
            App app = new App();
    
            try {
                try {
                    app.afficherInterfaceRepertoire();
                } catch (Exception e) {
                    e.printStackTrace();
                }
    
                String expectedOutput = "Quelle commande voulait vous "
                + "effectuer? help pour voir la liste de commande : " +
        "L'élément sélectionné n'est pas un fichier.\r\n" +
        "Quelle commande voulait vous "
                + "effectuer? help pour voir la liste de commande : ";;
                assertEquals(expectedOutput.trim(), outContent.toString().trim());
            } finally {
                System.setOut(System.out);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProcessUserActionWithNER() {
        App app = new App();

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        app.processUserActionWithNER("1 .");
        String expectedCdOutput = "Chemin du répertoire actuel : C:\\Users\\kbyan\\Documents\\code\\miniprojet-grp-41_74\\.git\r\n";
        assertEquals(expectedCdOutput, outContent.toString());

        outContent.reset();
        app.processUserActionWithNER("1 visu");
        String expectedVisuOutput = "Taille du fichier : 284 octets\r\n";
        assertEquals(expectedVisuOutput, outContent.toString());

        outContent.reset();
        app.processUserActionWithNER("1 copy");
        String expectedCopyOutput = "Élément copié : COMMIT_EDITMSG\r\n";
        assertEquals(expectedCopyOutput, outContent.toString());

        outContent.reset();
        System.setOut(System.out);
    }

    @Test
    public void testProcessUserAction() {
        App app = new App();

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        app.processUserAction("find abc.txt");
        String expectedFindOutput = "Fichier trouvé : "
            + app.getCurrentDirectory() + "\\exemple\\abc.txt\r\n";
        assertEquals(expectedFindOutput, outContent.toString());

        outContent.reset();

        app.processUserAction("mkdir testDir");
        File createdDirectory = new File(app.getCurrentDirectory() + "\\testDir");
        assertTrue(createdDirectory.exists());
        assertTrue(createdDirectory.isDirectory());

        outContent.reset();

        app.processUserAction("ls");
        String expectedLsOutput = "Contenu du répertoire :\r\n"
            + "1. .git\r\n"
            + "2. .gitignore\r\n"
            + "3. .mvn\r\n"
            + "4. .vscode\r\n"
            + "5. exemple\r\n"
            + "6. google_checks.xml\r\n"
            + "7. ManuelTechnique.adoc\r\n"
            + "8. ManuelUtilisateur.adoc\r\n"
            + "9. mvnw\r\n"
            + "10. mvnw.cmd\r\n"
            + "11. notes.md\r\n"
            + "12. pom.xml\r\n"
            + "13. README.adoc\r\n"
            + "14. src\r\n"
            + "15. target\r\n"
            + "16. testDir\r\n";
        assertEquals(expectedLsOutput, outContent.toString());

        outContent.reset();
        app.processUserAction("..");
        String expectedNewDirectory = app.getCurrentDirectory().replace("\\testDir", "");
        assertEquals(expectedNewDirectory, app.getCurrentDirectory());

        outContent.reset();

        app.processUserAction("past");
        outContent.reset();

        app.processUserAction("unknownAction");
        String expectedDefaultOutput = "Action non reconnue : unknownAction\r\n";
        assertEquals(expectedDefaultOutput, outContent.toString());

        System.setOut(System.out);
    }

    @Test
    public void testAfficherInterfaceRepertoire() {
        App app = new App();

        ByteArrayInputStream in = new ByteArrayInputStream("0".getBytes());
        System.setIn(in);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        app.afficherInterfaceRepertoire();
        assertTrue(outContent.toString().contains("Sortie de l'interface."));

        System.setIn(System.in);
        System.setOut(System.out);
    }

    @Test
    public void testProcessUserActionWithNERCd() {
        App app = new App();

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        app.processUserActionWithNER("1 .");
        String expectedCdOutput = "Chemin du répertoire actuel : C:\\Users\\kbyan\\Documents\\code\\miniprojet-grp-41_74\\.git\r\n";
        assertEquals(expectedCdOutput, outContent.toString());

        System.setOut(System.out);
    }

    @Test
    public void testProcessUserActionWithNERAnnotation() {
        App app = new App();

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        app.processUserActionWithNER("1 annotation");
        System.setOut(System.out);
    }

    @Test
    public void testProcessUserActionWithNERCut() {
        App app = new App();

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        File tempFile = createTempFile("tempFileContent");

        app.processUserActionWithNER("1 cut");
        System.setOut(System.out);
        deleteTempFile(tempFile);
    }

    private File createTempFile(String content) {
        try {
            File tempFile = File.createTempFile("tempFile", ".txt");
            Files.write(tempFile.toPath(), content.getBytes());
            return tempFile;
        } catch (IOException e) {
            throw new RuntimeException("Error creating temporary file.", e);
        }
    }

    private void deleteTempFile(File tempFile) {
        if (tempFile.exists() && !tempFile.delete()) {
            throw new RuntimeException("Error deleting temporary file.");
        }
    }

    @Test
    public void testSetCurrentDirectoryNotADirectory() {
        App app = new App();
        String invalidDirectoryPath = "C:\\path\\to\\invalid\\directory";

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        app.setCurrentDirectory(invalidDirectoryPath);

        String expectedOutput = "Le chemin spécifié ne correspond pas à un dossier." + System.lineSeparator();
        assertEquals(expectedOutput, outContent.toString());

        System.setOut(System.out);
    }

    @Test
    public void testProcessUserActionFindMissingFileName() {
        App app = new App();
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        app.processUserAction("find");
        String expectedOutput = "Veuillez spécifier le nom" +
                " du fichier à rechercher." + System.lineSeparator();
        assertEquals(expectedOutput, outContent.toString());

        System.setOut(System.out);
    }

    @Test
    public void testProcessUserActionMkdirMissingDirName() {
        App app = new App();

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        app.processUserAction("mkdir");
        String expectedOutput = "Veuillez spécifier le " +
                "nom du répertoire à créer." + System.lineSeparator();
        assertEquals(expectedOutput, outContent.toString());

        System.setOut(System.out);
    }

    @Test
    public void testMainMethod() {
        ByteArrayInputStream in = new ByteArrayInputStream("0\n".getBytes());
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        try {
            App.main(new String[]{});
            String output = outContent.toString();
            assertTrue(output.contains("Sortie de l'interface."));

        } finally {
            System.setOut(System.out);
            scanner.close();
        }
    }

    @Test
    public void testProcessUserActionWithNER_DefaultCase() {
        App app = new App();

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        app.processUserActionWithNER("1 unknownCommand");
        String expectedOutput = "Action non reconnue : 1 unknownCommand\r\n";
        assertEquals(expectedOutput, outContent.toString());

        System.setOut(System.out);
    }

    @Test
    public void testProcessUserActionWithNER_AddAnnotation() {
        App app = new App();

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        app.processUserActionWithNER("1 + \"Nouvelle annotation\"");

        String expectedOutput = "Annotation ajouté\r\n";
        assertEquals(expectedOutput, outContent.toString());

        Annotation annotation = app.getAnnotation();
        String annotationText = annotation.getAnnotationText(1);
        assertEquals("Nouvelle annotation", annotationText);

        System.setOut(System.out);
    }

    @Test
    public void testProcessUserActionWithNER_InvalidSyntax() {
        App app = new App();
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        app.processUserActionWithNER("1 + InvalidAnnotationSyntax");

        String expectedOutput = "Syntaxe incorrecte pour les guillemets.\r\n";
        assertEquals(expectedOutput, outContent.toString());

        System.setOut(System.out);
    }

    @Test
    public void testProcessUserActionWithNER_NoSecondPart() {
        App app = new App();

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        app.processUserActionWithNER("1");

        String expectedOutput = "L'élément sélectionné n'est pas un fichier.\r\n";
        assertEquals(expectedOutput, outContent.toString());

        System.setOut(System.out);
    }

    @Test
    public void testProcessUserActionWithNER_InvalidNER() {
        App app = new App();

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        app.processUserActionWithNER("invalidNER visu");

        String expectedOutput = "Numéro NER invalide.\r\n";
        assertEquals(expectedOutput, outContent.toString());

        System.setOut(System.out);
    }

    @Test
    public void testAfficherContenuRepertoireAvecNER_DossierVide() {
        App app = new App();

        String tempDirectoryPath = createTempEmptyDirectory();
        app.setCurrentDirectory(tempDirectoryPath);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        app.afficherContenuRepertoireAvecNER();
        String expectedOutput = "Contenu du répertoire :\r\nLe dossier est vide.\r\n";
        assertEquals(expectedOutput, outContent.toString());

        System.setOut(System.out);
    }

    private String createTempEmptyDirectory() {
        try {
            Path tempDir = Files.createTempDirectory("emptyDirectory");
            return tempDir.toAbsolutePath().toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Test
    public void testSetAnnotation() {
        App app = new App();
        Annotation mockAnnotation = new Annotation();
        app.setAnnotation(mockAnnotation);
        assertNotNull(app.getAnnotation());
    }

    @Test
    public void testHelp() {

        System.setOut(new PrintStream(outContent));

        App app = new App();
        app.help();

        System.setOut(originalOut);

        String helpOutput = outContent.toString();
        assertTrue(helpOutput.contains("Les commandes du gestionnaire de fichiers sont :"));
        assertTrue(helpOutput.contains("[<NER>] copy: Copie l'élément correspondant au NER."));
        assertTrue(helpOutput.contains("[<NER>] cut : Coupe l'élément correspondant au NER."));
        assertTrue(helpOutput.contains("past : Colle l'élement copié/coupé à l'emplacement actuel."));
    }
}
