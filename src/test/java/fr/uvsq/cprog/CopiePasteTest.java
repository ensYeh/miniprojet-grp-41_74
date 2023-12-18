package fr.uvsq.cprog;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.Objects;

public class CopiePasteTest {

    private ByteArrayOutputStream outContent;

    @BeforeEach
    public void setUpStreams() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(System.out);
    }

    private CopiePaste copiePaste;
    private File testDir;

    @BeforeEach
    public void setUp() throws IOException {
        copiePaste = new CopiePaste();
        testDir = Files.createTempDirectory("testDir").toFile();
        testDir.deleteOnExit();
    }

    @AfterEach
    public void tearDown() {
        deleteRecursive(testDir);
    }

    private boolean deleteRecursive(File file) {
        if (!file.exists()) {
            return true;
        }

        if (file.isDirectory()) {
            File[] contents = file.listFiles();
            if (contents != null) {
                for (File f : contents) {
                    if (!deleteRecursive(f)) {
                        return false;
                    }
                }
            }
        }

        return file.delete();
    }


    @Test
    public void testCopyCommand_FileExists() throws IOException {
        File testFile = new File(testDir, "testFile.txt");
        assertTrue(testFile.createNewFile());
        copiePaste.copyCommand(testDir.getAbsolutePath(), 1);
        assertEquals(testFile, copiePaste.getElementCopie());
    }

    @Test
    public void testCopyCommand_InvalidNER() {
        copiePaste.copyCommand(testDir.getAbsolutePath(), -1);
        assertNull(copiePaste.getElementCopie());
    }

    @Test
    public void testCopyCommand_NotADirectory() {
        copiePaste.copyCommand("invalidPath", 1);
        assertNull(copiePaste.getElementCopie());
    }

    @Test
    public void testPastCommand_WithCopiedFile() throws IOException {
        File testFile = new File(testDir, "testFile.txt");
        assertTrue(testFile.createNewFile());

        copiePaste.copyCommand(testDir.getAbsolutePath(), 1);
        copiePaste.pastCommand(testDir.getAbsolutePath());

        File pastedFile = new File(testDir, testFile.getName() + "-copy");
        assertTrue(pastedFile.exists());
    }

    @Test
    public void testPastCommand_NothingToPaste() {
        copiePaste.pastCommand(testDir.getAbsolutePath());
    }

    @Test
    public void testCutCommand_FileExists() throws IOException {
        File testFile = new File(testDir, "testFile.txt");
        assertTrue(testFile.createNewFile());

        copiePaste.cutCommand(testDir.getAbsolutePath(), 1);

        assertEquals(testFile, copiePaste.getElementCopie());
        assertTrue(copiePaste.isCut());
    }

    @Test
    public void testCutCommand_InvalidNER() {
        copiePaste.cutCommand(testDir.getAbsolutePath(), -1);
        assertNull(copiePaste.getElementCopie());
        assertFalse(copiePaste.isCut());
    }

    @Test
    public void testCutCommand_NotADirectory() {
        copiePaste.cutCommand("invalidPath", 1);
        assertNull(copiePaste.getElementCopie());
        assertFalse(copiePaste.isCut());
    }

    @Test
    public void testCutCommand_DirectoryExists() throws IOException {
        File testDirectory = new File(testDir, "testDirectory");
        assertTrue(testDirectory.mkdir());

        copiePaste.cutCommand(testDir.getAbsolutePath(), 1);

        assertEquals(testDirectory, copiePaste.getElementCopie());
        assertTrue(copiePaste.isCut());
    }

    @Test
    public void testCopyDirectory_WithFiles() throws IOException {

        File sourceDir = new File(testDir, "source");
        sourceDir.mkdirs();

        File file1 = new File(sourceDir, "file1.txt");
        assertTrue(file1.createNewFile());

        File file2 = new File(sourceDir, "file2.txt");
        assertTrue(file2.createNewFile());

        File destinationDir = new File(testDir, "destination");
        copiePaste.copyDirectory(sourceDir, destinationDir);

        File copiedFile1 = new File(destinationDir, "file1.txt");
        assertTrue(copiedFile1.exists());

        File copiedFile2 = new File(destinationDir, "file2.txt");
        assertTrue(copiedFile2.exists());
    }

    @Test
    public void testCopyDirectory_WithSubdirectories() throws IOException {

        File sourceDir = new File(testDir, "source");
        sourceDir.mkdirs();

        File subDir1 = new File(sourceDir, "subdir1");
        subDir1.mkdirs();

        File file1 = new File(subDir1, "file1.txt");
        assertTrue(file1.createNewFile());

        File subDir2 = new File(sourceDir, "subdir2");
        subDir2.mkdirs();

        File file2 = new File(subDir2, "file2.txt");
        assertTrue(file2.createNewFile());

        File destinationDir = new File(testDir, "destination");
        copiePaste.copyDirectory(sourceDir, destinationDir);

        File copiedSubDir1 = new File(destinationDir, "subdir1");
        assertTrue(copiedSubDir1.exists());

        File copiedFile1 = new File(copiedSubDir1, "file1.txt");
        assertTrue(copiedFile1.exists());

        File copiedSubDir2 = new File(destinationDir, "subdir2");
        assertTrue(copiedSubDir2.exists());

        File copiedFile2 = new File(copiedSubDir2, "file2.txt");
        assertTrue(copiedFile2.exists());
    }

    @Test
    public void testCopyDirectory_WithEmptySourceDirectory() throws IOException {

        File sourceDir = new File(testDir, "source");
        sourceDir.mkdirs();

        File destinationDir = new File(testDir, "destination");
        copiePaste.copyDirectory(sourceDir, destinationDir);

        assertTrue(destinationDir.exists());
        assertTrue(Objects.requireNonNull(destinationDir.listFiles()).length == 0);
    }

    @Test
    public void testDeleteRecursive_WithFilesAndDirectories() throws IOException {

        File mainDir = new File(testDir, "mainDir");
        File file1 = new File(mainDir, "file1.txt");
        File subDir = new File(mainDir, "subDir");
        File file2 = new File(subDir, "file2.txt");

        assertTrue(mainDir.mkdirs());
        assertTrue(file1.createNewFile());
        assertTrue(subDir.mkdirs());
        assertTrue(file2.createNewFile());

        copiePaste.deleteRecursive(mainDir);

        assertFalse(mainDir.exists());
        assertFalse(file1.exists());
        assertFalse(subDir.exists());
        assertFalse(file2.exists());
    }

    @Test
    public void testDeleteRecursive_WithEmptyDirectory() throws IOException {

        File emptyDir = new File(testDir, "emptyDir");
        assertTrue(emptyDir.mkdirs());
        copiePaste.deleteRecursive(emptyDir);
        assertFalse(emptyDir.exists());
    }

    @Test
    public void testDeleteRecursive_WithNonExistentDirectory() {

        File nonExistentDir = new File(testDir, "nonExistentDir");
        copiePaste.deleteRecursive(nonExistentDir);
        assertTrue(true);
    }

    @Test
    public void testPastCommand_WithoutCopiedFile() {

        copiePaste.pastCommand(testDir.getAbsolutePath());
        assertTrue(true);
    }

    @Test
    public void testPastCommand_InvalidDirectory() {

        copiePaste.pastCommand("invalidPath");
        assertTrue(true);
    }

    @Test
    public void testNouveauNom_FileWithSameNameExists() throws IOException {

        File testFile = new File(testDir, "testFile.txt");
        assertTrue(testFile.createNewFile());

        String result = copiePaste.nouveauNom(testDir.getAbsolutePath(), "testFile.txt");
        assertTrue(result.startsWith("testFile.txt-copy"));

        File testFileCopy = new File(testDir, result);
        assertTrue(testFileCopy.createNewFile());

        result = copiePaste.nouveauNom(testDir.getAbsolutePath(), "testFile.txt");
        assertTrue(result.startsWith("testFile.txt-copy(1)"));
        File testFileCopy2 = new File(testDir, result);
        assertTrue(testFileCopy2.createNewFile());

        result = copiePaste.nouveauNom(testDir.getAbsolutePath(), "testFile.txt");
        assertTrue(result.startsWith("testFile.txt-copy(2)"));
    }

    @Test
    public void testCopyFile_SourceNotExists() {
        File sourceFile = new File("nonexistentfile.txt");
        File destinationFile = new File("destination.txt");

        copiePaste.copyFile(sourceFile, destinationFile);

        assertTrue(outContent.toString().contains("Erreur lors de la copie du fichier"));
    }
}