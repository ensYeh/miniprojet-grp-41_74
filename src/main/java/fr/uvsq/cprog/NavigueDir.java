package fr.uvsq.cprog;

import java.io.File;

public class NavigueDir {

    /**
     * Constructeur par défaut de la classe NavigueDir.
     * Permettant de se déplacer dans les répertoires.
     */
    public NavigueDir() {
    }

    /**
    * Change le répertoire courant pour celui associé au numéro NER.
    * Si le NER correspond à un répertoire, renvoie le nouveau chemin.
    * @param cheminDossier Le chemin du dossier à explorer.
    * @param ner Le numéro associé à l'élément du répertoire.
    * @return Le nouveau chemin du répertoire courant.
    */
    public String cdCommand(final String cheminDossier, final int ner) {
        File dossier = new File(cheminDossier);

        if (dossier.isDirectory()) {
            File[] fichiers = dossier.listFiles();

            if (fichiers != null && ner > 0 && ner <= fichiers.length) {
                File selectedFile = fichiers[ner - 1];

                if (selectedFile.isDirectory()) {
                    return selectedFile.getAbsolutePath();
                } else {
                    System.out.println("L'élément sélectionné ");
                    System.out.println("n'est pas un répertoire.");
                }
            } else {
                System.out.println("Numéro NER invalide.");
            }
        } else {
            System.out.println("Le chemin spécifié ne ");
            System.out.println("correspond pas à un dossier.");
        }

        // En cas d'échec, renvoie le chemin actuel
        return cheminDossier;
    }

    /**
    * Change le répertoire courant pour remonter d'un
    * cran dans le système de fichiers.
    * @param cheminDossier Le chemin du dossier actuel.
    * @return Le chemin du dossier parent, ou null si impossible de remonter.
    */
    public String remonterDossier(final String cheminDossier) {
        File dossier = new File(cheminDossier);
        File parentDirectory = dossier.getParentFile();

        if (parentDirectory != null) {
            return parentDirectory.getAbsolutePath();
        } else {
            System.out.println("Impossible de remonter d'un cran.");
            return null;
        }
    }

}
