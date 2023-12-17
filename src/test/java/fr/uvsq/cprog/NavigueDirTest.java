package fr.uvsq.cprog;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class NavigueDirTest {

    @Test
    public void testCdCommandWithValidDirectory() {
        NavigueDir navigueDir = new NavigueDir();
        String testFolderPath = "C:\\Users\\kbyan\\Documents\\code\\miniprojet-grp-41_74\\exemple";
        int validNer = 1;

        String resultPath = navigueDir.cdCommand(testFolderPath, validNer);
        assertEquals(testFolderPath + "\\aaaa", resultPath);
    }

    @Test
    public void testCdCommandWithInvalidDirectory() {
        NavigueDir navigueDir = new NavigueDir();
        String testFolderPath = "C:\\Users\\kbyan\\Documents\\code\\miniprojet-grp-41_74\\exemple";
        int invalidNer = 30;

        String resultPath = navigueDir.cdCommand(testFolderPath, invalidNer);
        assertEquals(testFolderPath, resultPath);
    }

    @Test
    public void testCdCommandWithNonDirectory() {
        NavigueDir navigueDir = new NavigueDir();
        String testFilePath = "C:\\Users\\kbyan\\Documents\\code\\miniprojet-grp-41_74\\exemple";
        int ner = 2;

        String resultPath = navigueDir.cdCommand(testFilePath, ner);
        assertEquals(testFilePath, resultPath);
    }

    @Test
    public void testRemonterDossier() {
        NavigueDir navigueDir = new NavigueDir();
        String testFolderPath = "C:\\Users\\kbyan\\Documents\\code\\miniprojet-grp-41_74\\exemple\\ezez\\az";

        String resultPath = navigueDir.remonterDossier(testFolderPath);

        // Vérifie que le résultat correspond au chemin du dossier parent
        assertEquals("C:\\Users\\kbyan\\Documents\\code\\miniprojet-grp-41_74\\exemple\\ezez", resultPath);

        // Test quand c'est un fichier
        String resultPathFile = navigueDir.remonterDossier("C:\\Users\\kbyan\\Documents\\code\\miniprojet-grp-41_74\\exemple\\abc.txt");
        assertEquals("C:\\Users\\kbyan\\Documents\\code\\miniprojet-grp-41_74\\exemple", resultPathFile);


        // Test quand on vas dans le dossier parent
        String result = navigueDir.remonterDossier("C:\\Users\\kbyan\\Documents\\code\\miniprojet-grp-41_74\\exemple\\aaaa");
        assertEquals("C:\\Users\\kbyan\\Documents\\code\\miniprojet-grp-41_74\\exemple", result);
    }

    @Test
    public void testCdCommand() {
        NavigueDir navigueDir = new NavigueDir();

        // Test quand le repertoire est introuvable
        String result1 = navigueDir.cdCommand("C:\\Users\\kbyan\\Documentssss\\code\\miniprojet-grp-41_74\\exemple", 30);
        assertEquals("C:\\Users\\kbyan\\Documentssss\\code\\miniprojet-grp-41_74\\exemple", result1);

        // Test quand c'est un fichier
        String result2 = navigueDir.cdCommand("C:\\Users\\kbyan\\Documents\\code\\miniprojet-grp-41_74\\exemple", 2);
        assertEquals("C:\\Users\\kbyan\\Documents\\code\\miniprojet-grp-41_74\\exemple", result2);

        // Changement valide de répertoire
        String result3 = navigueDir.cdCommand("C:\\Users\\kbyan\\Documents\\code\\miniprojet-grp-41_74\\exemple", 1);
        assertEquals("C:\\Users\\kbyan\\Documents\\code\\miniprojet-grp-41_74\\exemple\\aaaa", result3);

        // NER = 0
        String result4 = navigueDir.cdCommand("C:\\Users\\kbyan\\Documents\\code\\miniprojet-grp-41_74\\exemple", 0);
        assertEquals("C:\\Users\\kbyan\\Documents\\code\\miniprojet-grp-41_74\\exemple", result4);

        // NER trop grand
        String result5 = navigueDir.cdCommand("C:\\Users\\kbyan\\Documents\\code\\miniprojet-grp-41_74\\exemple", 100);
        assertEquals("C:\\Users\\kbyan\\Documents\\code\\miniprojet-grp-41_74\\exemple", result5);

        // NER negatif
        String result6 = navigueDir.cdCommand("C:\\Users\\kbyan\\Documents\\code\\miniprojet-grp-41_74\\exemple", -1);
        assertEquals("C:\\Users\\kbyan\\Documents\\code\\miniprojet-grp-41_74\\exemple", result6);
    }
}
