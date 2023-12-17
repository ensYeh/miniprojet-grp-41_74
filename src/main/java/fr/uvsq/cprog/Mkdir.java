package fr.uvsq.cprog;

import java.io.File;

public class Mkdir {

    /**
     * Constructeur par défaut de la classe Mkdir.
     * Permmettant de créer des répertoires.
     */
    public Mkdir() {
    }

    /**
    * Exécute la commande mkdir pour créer un répertoire.
    * @param cheminDossier Le chemin du dossier où créer le répertoire.
    * @param directoryName Le nom du répertoire à créer.
    */
    public void mkdirCommand(final String cheminDossier,
    final String directoryName) {
        File dossier = new File(cheminDossier);

        if (dossier.isDirectory()) {
            createDirectory(dossier, directoryName);
        } else {
            System.out.println("Le chemin spécifié ne ");
            System.out.println("correspond pas à un dossier.");
        }
    }

    private void createDirectory(final File parentDirectory,
    final String directoryName) {
        File newDirectory = new File(parentDirectory, directoryName);

        if (newDirectory.mkdir()) {
            System.out.println("Répertoire créé : "
            + newDirectory.getAbsolutePath());
        } else {
            System.out.println("Erreur lors de la création du répertoire.");
        }
    }
}
