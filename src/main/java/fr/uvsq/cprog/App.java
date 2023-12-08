/**
 * Ce package contient les classes principales de l'application.
 */
package fr.uvsq.cprog;

import java.io.File;
import java.util.Scanner;

/**
 * Classe principale de l'application.
 */
public final class App {

    /**
     * Chemin du répertoire courant.
     */
    private String currentDirectoryPath;

    /**
     * Scanner utilisé pour la saisie utilisateur.
     */
    private Scanner scanner;

    /**
     * Instance de la classe Commande utilisée pour traiter
     * les commandes de l'utilisateur.
     */
    private Commande commande;

    /**
     * Constructeur de la classe App.
     */
    public App() {
        this.commande = new Commande();
        this.currentDirectoryPath = System.getProperty("user.dir");
    }

    /**
     * Définit le répertoire courant.
     * @param directoryPath Chemin du répertoire.
     */
    public void setCurrentDirectory(final String directoryPath) {
        File directory = new File(directoryPath);

        if (directory.isDirectory()) {
            this.currentDirectoryPath = directory.getAbsolutePath();
            System.out.println("Chemin du répertoire actuel : "
                    + this.currentDirectoryPath);

        } else {
            System.out.println("Le chemin spécifié ne ");
            System.out.println("correspond pas à un dossier.");
        }
    }

    /**
     * Obtient le répertoire courant.
     * @return Chemin du répertoire courant.
     */
    public String getCurrentDirectory() {
        return this.currentDirectoryPath;
    }

    /**
     * Méthode principale.
     * @param args Arguments de la ligne de commande.
     */
    public static void main(final String[] args) {
        // chemin du dossier à explorer
        String cheminDossier = "c:/Users/kbyan/Documents/cours";

        App app = new App();
        app.setCurrentDirectory(cheminDossier);

        // Affiche le contenu du répertoire avant de demander à
        // l'utilisateur quelle commande exécuter.
        app.afficherContenuRepertoire();

        // Appel de la fonction pour afficher l'interface du répertoire
        app.afficherInterfaceRepertoire();
    }

    /**
     * Affiche l'interface du répertoire.
     */
    public void afficherInterfaceRepertoire() {
        scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Que voulez-vous faire? ");
            System.out.print("(pour l'instant, les commandes 'find', ");
            System.out.print("'mkdir' et 'ls' sont supportées) : ");
            String input = scanner.nextLine();

            if (input.equals("0")) {
                System.out.println("Sortie de l'interface.");
                break;
            }

            if (input.equals("ls")) {
                afficherContenuRepertoire();
            } else {
                processUserAction(input);
            }
        }

        scanner.close();
    }

    /**
     * Traite l'action de l'utilisateur.
     * @param input Entrée de l'utilisateur.
     */
    public void processUserAction(final String input) {
        String[] parts = input.split(" ", 2);

        switch (parts[0].toLowerCase()) {
            case "find":
                if (parts.length > 1) {
                    String fileName = parts[1];
                    commande.findCommand(getCurrentDirectory(), fileName);
                } else {
                    System.out.println("Veuillez spécifier le nom");
                    System.out.println(" du fichier à rechercher.");
                }
                break;
            case "mkdir":
                if (parts.length > 1) {
                    String dirName = parts[1];
                    commande.mkdirCommand(getCurrentDirectory(), dirName);
                } else {
                    System.out.println("Veuillez spécifier le ");
                    System.out.println("nom du répertoire à créer.");
                }
                break;
            default:
                System.out.println("Action non reconnue : " + input);
        }
    }

    /**
     * Affiche le contenu du répertoire.
     */
    public void afficherContenuRepertoire() {
        String cheminDossier = getCurrentDirectory();
        File dossier = new File(cheminDossier);

        // Vérifie si le chemin spécifié est un dossier
        if (dossier.isDirectory()) {
            File[] fichiers = dossier.listFiles();

            // Visualisation du contenu du répertoire
            System.out.println("Contenu du répertoire :");

            if (fichiers != null && fichiers.length > 0) {
                for (int i = 0; i < fichiers.length; i++) {
                    System.out.println(i + 1 + ". " + fichiers[i].getName());
                }
            } else {
                System.out.println("Le dossier est vide.");
            }
        } else {
            System.out.println("Le chemin spécifié ne ");
            System.out.println("correspond pas à un dossier.");
        }
    }
}
