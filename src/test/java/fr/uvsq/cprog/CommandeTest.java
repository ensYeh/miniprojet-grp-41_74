package fr.uvsq.cprog;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

public class CommandeTest {

    @Test
    public void testFindCommand() {
        
        // Initialiser la commande
        Commande commande = new Commande();

        // Spécifier le chemin du dossier et le nom du fichier à rechercher
        String cheminDossier = "c:/Users/kbyan/Documents/cours";
        String fileName = "abc.txt";

        // Résultat attendu
        String expectedOutput = "Fichier trouvé : c:\\Users\\kbyan\\Documents\\cours\\abc.txt\r\n" + //
                "Fichier trouvé : c:\\Users\\kbyan\\Documents\\cours\\s4\\abc.txt\r\n" + //
                "Fichier trouvé : c:\\Users\\kbyan\\Documents\\cours\\s4-copy\\abc.txt";

        // Rediriger System.out pour capturer la sortie
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Exécuter la commande findCommand
        commande.findCommand(cheminDossier, fileName);

        // Vérifier si la sortie correspond à celle attendue
        assertEquals(expectedOutput.trim(), outContent.toString().trim());

        // Réinitialiser System.out
        System.setOut(System.out);
    }
}
