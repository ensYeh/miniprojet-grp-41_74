package fr.uvsq.cprog;

import java.io.File;
import java.util.Scanner;

public class App {
    private String currentDirectoryPath;
    private Scanner scanner;
    private Commande commande;

    public App() {
        this.commande = new Commande();
        this.currentDirectoryPath = System.getProperty("user.dir");
    }

    public void setCurrentDirectory(String directoryPath) {
        File directory = new File(directoryPath);

        if (directory.isDirectory()) {
            this.currentDirectoryPath = directory.getAbsolutePath();
            System.out.println("Chemin du répertoire actuel : " + this.currentDirectoryPath);
        } else {
            System.out.println("Le chemin spécifié ne correspond pas à un dossier.");
        }
    }

    public String getCurrentDirectory() {
        return this.currentDirectoryPath;
    }

    public static void main(String[] args) {
        String cheminDossier = "c:/Users/kbyan/Documents/cours";
        App app = new App();
        app.setCurrentDirectory(cheminDossier);
        app.afficherInterfaceRepertoire();
    }

    public void afficherInterfaceRepertoire() {
        scanner = new Scanner(System.in);  // Initialisation du scanner

        while (true) {
            String cheminDossier = getCurrentDirectory();
            File dossier = new File(cheminDossier);

            if (dossier.isDirectory()) {
                File[] fichiers = dossier.listFiles();

                System.out.println("Contenu du répertoire :");

                if (fichiers != null && fichiers.length > 0) {
                    for (int i = 0; i < fichiers.length; i++) {
                        System.out.println(i + 1 + ". " + fichiers[i].getName());
                    }

                    int choixUtilisateur = 0;

                    try {
                        System.out.print("Saisissez le numéro de l'élément du répertoire (0 pour quitter) ou entrez une commande : ");

                        String input = scanner.nextLine();

                        if (input.matches("\\d+")) {
                            choixUtilisateur = Integer.parseInt(input);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (choixUtilisateur == 0) {
                        System.out.println("Sortie de l'interface.");
                        break;
                    } else if (choixUtilisateur > 0 && choixUtilisateur <= fichiers.length) {
                        System.out.println("Vous avez sélectionné : " + fichiers[choixUtilisateur - 1].getName());
                        System.out.print("Que voulez-vous faire? (find pour rechercher, autre pour continuer) : ");
                        String action = scanner.next();

                        processUserAction(action, fichiers[choixUtilisateur - 1].getName());

                        // Supprime cette partie qui affiche la liste du répertoire après avoir exécuté l'action
                        // for (int i = 0; i < fichiers.length; i++) {
                        //     System.out.println(i + 1 + ". " + fichiers[i].getName());
                        // }
                    } else {
                        System.out.println("Numéro invalide. Veuillez réessayer.");
                    }
                } else {
                    System.out.println("Le dossier est vide.");
                    break;
                }
            } else {
                System.out.println("Le chemin spécifié ne correspond pas à un dossier.");
                break;
            }
        }

        scanner.close();  // Fermeture du scanner à la fin de la méthode
    }

    public void processUserAction(String action, String fileName) {
        switch (action.toLowerCase()) {
            case "find":
                commande.findCommand(getCurrentDirectory(), fileName);
                break;
            default:
                System.out.println("Action non reconnue.");
        }
    }
}