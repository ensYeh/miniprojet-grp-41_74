package fr.uvsq.cprog;

import java.io.File;

public class Find {

    /**
     * Constructeur par défaut de la classe find.
     * Permettant de rechercher des fichiers
     * par leur noms.
     */
    public Find() {
    }

    /**
     * Exécute la commande de recherche.
     * @param cheminDossier Le chemin du dossier à explorer.
     * @param fileName Le nom du fichier à rechercher.
     */
    public void findCommand(final String cheminDossier, final String fileName) {
        File dossier = new File(cheminDossier);

        if (dossier.isDirectory()) {
            findMatchingFiles(dossier, fileName);
        } else {
            System.out.println("Le chemin spécifié ne correspond");
            System.out.println(" pas à un dossier.");
        }
    }

    private void findMatchingFiles(final File directory,
     final String fileName) {
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    findMatchingFiles(file, fileName);
                } else if (file.getName().equals(fileName)) {
                    System.out.println("Fichier trouvé : " + file
                    .getAbsolutePath());
                }
            }
        }
    }

}
