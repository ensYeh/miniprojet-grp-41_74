package fr.uvsq.cprog;

import java.io.File;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        // chemin du dossier à explorer
        String cheminDossier = "c:/Users/kbyan/Documents/cours";

        // Appel de la fonction pour afficher l'interface du répertoire
        afficherInterfaceRepertoire(cheminDossier);
    }

    public static void afficherInterfaceRepertoire(String cheminDossier) {
        File dossier = new File(cheminDossier);

        // Vérifie si le chemin spécifié est un dossier
        if (dossier.isDirectory()) {
            // liste des fichiers du dossier
            File[] fichiers = dossier.listFiles();

            // Visualisation du contenu du répertoire
            System.out.println("Contenu du répertoire :");

            if (fichiers != null && fichiers.length > 0) {
                for (int i = 0; i < fichiers.length; i++) {
                    System.out.println(i + 1 + ". " + fichiers[i].getName());
                }

                // Demande à l'utilisateur de choisir le fichier qu'il veut
                try (Scanner scanner = new Scanner(System.in)) {
                    System.out.print("Saisissez le numéro de l'élément du répertoire (0 pour quitter) : ");

                    // Lire le fichier choisie
                    int choixUtilisateur = scanner.nextInt();

                    // Vérifie si l'utilisateur a saisi 0 pour quitter
                    if (choixUtilisateur == 0) {
                        System.out.println("Sortie de l'interface.");
                    } else if (choixUtilisateur > 0 && choixUtilisateur <= fichiers.length) {
                        // Affichez le nom de l'élément sélectionné
                        System.out.println("Vous avez sélectionné : " + fichiers[choixUtilisateur - 1].getName());
                    } else {
                        System.out.println("Numéro invalide. Veuillez réessayer.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Le dossier est vide.");
            }
        } else {
            System.out.println("Le chemin spécifié ne correspond pas à un dossier.");
        }
    }
}