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
            System.out.
            println("Le chemin spécifié ne correspond pas à un dossier.");
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

        // Appel de la fonction pour afficher l'interface du répertoire
        app.afficherInterfaceRepertoire();
    }

    /**
     * Affiche l'interface du répertoire.
     */
    public void afficherInterfaceRepertoire() {
        scanner = new Scanner(System.in);  // Initialisation du scanner

        while (true) {
            String cheminDossier = getCurrentDirectory();
            File dossier = new File(cheminDossier);

            // Vérifie si le chemin spécifié est un dossier
            if (dossier.isDirectory()) {
                File[] fichiers = dossier.listFiles();

                // Visualisation du contenu du répertoire
                System.out.println("Contenu du répertoire :");

                if (fichiers != null && fichiers.length > 0) {
                    for (int i = 0; i < fichiers.length; i++) {
                        System.out.
                        println(i + 1 + ". " + fichiers[i].getName());
                    }

                    int choixUtilisateur = 0;

                    // Demande à l'utilisateur de choisir le fichier qu'il veut
                    try {
                        System.out.print("Saisissez le numéro de l'élément du");
                        System.out.print("répertoire, (0 pour quitter) ou ");
                        System.out.print("entrez une commande : ");

                        String input = scanner.nextLine();

                        // Lire le fichier choisie
                        if (input.matches("\\d+")) {
                            choixUtilisateur = Integer.parseInt(input);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // Vérifie si l'utilisateur a saisi 0 pour quitter
                    if (choixUtilisateur == 0) {
                        System.out.println("Sortie de l'interface.");
                        break;
                    } else if (choixUtilisateur > 0
                    && choixUtilisateur <= fichiers.length) {
                        // Affichez le nom de l'élément sélectionné
                        System.out.println("Vous avez sélectionné : "
                        + fichiers[choixUtilisateur - 1].getName());
                        System.out.print("Que voulez-vous faire? (find pour r");
                        System.out.print("echercher, autre pour continuer) : ");
                        String action = scanner.next();

                        processUserAction(action, fichiers[choixUtilisateur - 1]
                        .getName());

                    } else {
                        System.out
                        .println("Numéro invalide. Veuillez réessayer.");
                    }
                } else {
                    System.out.println("Le dossier est vide.");
                    break;
                }
            } else {
                System.out.
                println("Le chemin spécifié ne correspond pas à un dossier.");
                break;
            }
        }

        scanner.close();  // Fermeture du scanner à la fin de la méthode
    }

    /**
     * Traite l'action de l'utilisateur.
     * @param action Action de l'utilisateur.
     * @param fileName Nom du fichier.
     */
    public void processUserAction(final String action, final String fileName) {
        switch (action.toLowerCase()) {
            case "find":
                commande.findCommand(getCurrentDirectory(), fileName);
                break;
            default:
                System.out.println("Action non reconnue.");
        }
    }
}
