package fr.uvsq.cprog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CopiePaste {

    /**
    * Indique si l'opération en cours est une opération de coupe (cut).
    */
    private boolean isCut = false;

    /**
    * Représente le fichier copié en mémoire lors de la commande "copy".
    */
    private File elementCopie;

    /**
     * Taille du tampon utilisé pour la copie des fichiers.
     */
    private static final int BUFFER_SIZE = 1024;

    /**
     * Constructeur par défaut de la classe CopiePaste.
     * Permettant de copier, coller et cut
     * les éléments.
     */
    public CopiePaste() {
    }

    /**
    * Copie l'élément associé au numéro NER.
    * @param cheminDossier Le chemin du dossier à explorer.
    * @param ner Le numéro associé à l'élément du répertoire.
    */
    public void copyCommand(final String cheminDossier, final int ner) {
        File dossier = new File(cheminDossier);

        if (dossier.isDirectory()) {
            File[] fichiers = dossier.listFiles();

            if (fichiers != null && ner > 0 && ner <= fichiers.length) {
                File selectedFile = fichiers[ner - 1];

                if (selectedFile.exists()) {
                    // Si l'élément sélectionné est un fichier
                    // ou un dossier, effectue la copie
                    elementCopie = selectedFile;
                    System.out.println("Élément copié : "
                     + elementCopie.getName());
                } else {
                    System.out.println("L'élément sélectionné n'existe pas.");
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
    * Colle l'élément copié à un nouvel
    * emplacement et le supprime du répertoire d'origine.
    * @param cheminDossier Le chemin du dossier à explorer.
    */
    public void pastCommand(final String cheminDossier) {
        File dossier = new File(cheminDossier);

        if (dossier.isDirectory()) {
            if (elementCopie != null) {
                String nomElementCopie = elementCopie.getName();
                String nouveauNomCpi =
                nouveauNom(cheminDossier, nomElementCopie);
                File nouvelElementCpi = new File(cheminDossier, nouveauNomCpi);

                // Copie l'élément copié vers le nouvel emplacement
                if (elementCopie.isDirectory()) {
                    // Si l'élément copié est un dossier, copie récursive
                    copyDirectory(elementCopie, nouvelElementCpi);

                    if (isCut) {
                        // Si c'était une copie-coupe,
                        //supprime l'élément copié du répertoire d'origine
                        deleteRecursive(elementCopie);
                        isCut = false; // Réinitialise l'indicateur cut
                    }
                } else {
                    // Si l'élément copié est un fichier, copie simple
                    copyFile(elementCopie, nouvelElementCpi);

                    if (isCut) {
                        // Si c'était une copie-coupe,
                        //supprime l'élément copié du répertoire d'origine
                        if (elementCopie.delete()) {
                            System.out.println("Élément copié supprimé"
                            + "avec succès.");
                            isCut = false; // Réinitialise l'indicateur cut
                        } else {
                            System.out.println("Erreur lors de la "
                            + "suppression de l'élément copié.");
                        }
                    }
                }

                System.out.println("Élément copié collé avec succès : "
                + nouvelElementCpi.getName());
            } else {
                System.out.println("Aucun élément copié (cut) en mémoire.");
            }
        } else {
            System.out.println("Le chemin spécifié ne"
            + "correspond pas à un dossier.");
        }
    }

    /**
     * Supprime un dossier et son contenu de manière récursive.
     * @param directory Le dossier à supprimer.
     */
    private void deleteRecursive(final File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // Si l'élément est un dossier, supprime récursivement
                    deleteRecursive(file);
                } else {
                    // Si l'élément est un fichier, supprime simplement
                    file.delete();
                }
            }
        }

        // Supprime le dossier une fois que son contenu est vidé
        if (directory.delete()) {
            System.out.println("Dossier supprimé avec succès : "
            + directory.getName());
        } else {
            System.out.println("Erreur lors de la suppression du dossier.");
        }
    }

    /**
     * Obtient un nouveau nom pour l'élément en évitant les collisions.
     * @param cheminDossier Le chemin du dossier actuel.
     * @param nomOriginal Le nom original de l'élément.
     * @return Le nouveau nom sans collision.
     */
    private String nouveauNom(final String cheminDossier,
     final String nomOriginal) {
        String nouveauNom = nomOriginal;
        File dossier = new File(cheminDossier);
        File[] fichiers = dossier.listFiles();

        if (fichiers != null) {
            // Vérifie si un élément du même nom existe déjà
            boolean existeDeja = false;
            for (File fichier : fichiers) {
                if (fichier.getName().equals(nouveauNom)) {
                    existeDeja = true;
                    break;
                }
            }

            // Si un élément du même nom existe déjà
            if (existeDeja) {
                // Ajoute le suffixe "-copy"
                nouveauNom += "-copy";

                // Ajoute un suffixe numérique si nécessaire
                int index = 1;
                while (new File(cheminDossier, nouveauNom).exists()) {
                    nouveauNom = nomOriginal + "-copy(" + index + ")";
                    index++;
                }
            }
        }

        return nouveauNom;
    }

    /**
     * Copie un fichier vers un nouvel emplacement.
     * @param sourceFile Le fichier source.
     * @param destinationFile Le fichier de destination.
     */
    private void copyFile(final File sourceFile, final File destinationFile) {
        try (InputStream in = new FileInputStream(sourceFile);
            OutputStream out = new FileOutputStream(destinationFile)) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de la copie du fichier : "
             + e.getMessage());
        }
    }

    /**
    * Copie un dossier et son contenu vers un nouvel
    * emplacement de manière récursive.
    * @param sourceDirectory Le dossier source.
    * @param destinationDirectory Le dossier de destination.
    */
    private void copyDirectory(final File sourceDirectory,
     final File destinationDirectory) {
        if (!destinationDirectory.exists()) {
            destinationDirectory.mkdir();
        }

        File[] files = sourceDirectory.listFiles();
        if (files != null) {
            for (File file : files) {
                File newFile = new File(destinationDirectory, file.getName());
                if (file.isDirectory()) {
                    copyDirectory(file, newFile);
                } else {
                    copyFile(file, newFile);
                }
            }
        }
    }

    /**
     * Coupe l'élément associé au numéro NER.
     * @param cheminDossier Le chemin du dossier à explorer.
     * @param ner Le numéro associé à l'élément du répertoire.
     */
    public void cutCommand(final String cheminDossier, final int ner) {
        File dossier = new File(cheminDossier);

        if (dossier.isDirectory()) {
            File[] fichiers = dossier.listFiles();

            if (fichiers != null && ner > 0 && ner <= fichiers.length) {
                File selectedFile = fichiers[ner - 1];

                if (selectedFile.exists()) {
                    // Si l'élément sélectionné est un fichier
                    // ou un dossier, effectue la copie
                    elementCopie = selectedFile;
                    isCut = true; // Active l'indicateur cut
                    System.out.println("Élément copié (cut) : "
                    + elementCopie.getName());
                } else {
                    System.out.println("L'élément sélectionné n'existe pas.");
                }
            } else {
                System.out.println("Numéro NER invalide.");
            }
        } else {
            System.out.println("Le chemin spécifié ne ");
            System.out.println("correspond pas à un dossier.");
        }
    }
}
