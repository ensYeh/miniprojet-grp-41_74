package fr.uvsq.cprog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Visu {

    /**
     * Constructeur par défaut de la classe Visu.
     * Initialise la structure de données pour visualiser
     * les éléments.
     */
    public Visu() {
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

}
