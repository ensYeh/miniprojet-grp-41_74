package fr.uvsq.cprog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Classe représentant une commande de recherche.
 */
public class Commande {

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

    /**
     * Affiche le contenu du fichier associé au numéro NER.
     * Si le fichier est un fichier texte, affiche son contenu.
     * Si le fichier n'est pas un fichier texte, affiche sa taille.
     * @param cheminDossier Le chemin du dossier à explorer.
     * @param ner Le numéro associé à l'élément du répertoire.
     */
    public void visuCommand(final String cheminDossier, final int ner) {
        File dossier = new File(cheminDossier);

        if (dossier.isDirectory()) {
            File[] fichiers = dossier.listFiles();

            if (fichiers != null && ner > 0 && ner <= fichiers.length) {
                File selectedFile = fichiers[ner - 1];

                if (selectedFile.isFile()) {
                    if (isTextFile(selectedFile)) {
                        afficherContenuTexte(selectedFile);
                    } else {
                        afficherTailleFichier(selectedFile);
                    }
                } else {
                    System.out.println("L'élément sélectionné n'est ");
                    System.out.println("pas un fichier.");
                }
            } else {
                System.out.println("Numéro NER invalide.");
            }
        } else {
            System.out.println("Le chemin spécifié ne ");
            System.out.println("correspond pas à un dossier.");
        }
    }

    /**
     * Vérifie si le fichier est un fichier texte.
     * @param file Le fichier à vérifier.
     * @return true si c'est un fichier texte, false sinon.
     */
    private boolean isTextFile(final File file) {
        return file.getName().toLowerCase().endsWith(".txt");
    }

    /**
     * Affiche le contenu du fichier texte.
     * @param file Le fichier texte à afficher.
     */
    private void afficherContenuTexte(final File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            System.out.println("Contenu du fichier texte : "
            + file.getAbsolutePath());
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture du fichier texte.");
        }
    }

    /**
     * Affiche la taille du fichier.
     * @param file Le fichier dont la taille doit être affichée.
     */
    private void afficherTailleFichier(final File file) {
        System.out.println("Taille du fichier : " + file.length() + " octets");
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
