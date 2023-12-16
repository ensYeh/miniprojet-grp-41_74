package fr.uvsq.cprog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.HashMap;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Classe représentant une commande de recherche.
 */
public class Commande {

    /**
    * Indique si l'opération en cours est une opération de coupe (cut).
    */
    private boolean isCut = false;

    /**
    * Map associant les numéros NER aux annotations associées.
    */
    private Map<Integer, StringBuilder> annotations;

    /**
    * Représente le fichier copié en mémoire lors de la commande "copy".
    */
    private File elementCopie;

    /**
     * Taille du tampon utilisé pour la copie des fichiers.
     */
    private static final int BUFFER_SIZE = 1024;

    /**
     * Constructeur par défaut de la classe Commande.
     * Initialise la structure de données pour stocker les annotations.
     */
    public Commande() {
        this.annotations = new HashMap<>();
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

    /**
     * Ajoute ou concatène un texte à l'annotation associée à un numéro NER.
     * @param ner Le numéro associé à l'élément du répertoire.
     * @param texte L'annotation à ajouter ou concaténer.
     */
    public void ajouterAnnotation(final int ner, final String texte) {
        if (annotations.containsKey(ner)) {
            annotations.get(ner).append(" ").append(texte);
        } else {
            annotations.put(ner, new StringBuilder(texte));
        }
        saveAnnotations();
    }

    /**
     * Retire tout le texte associé à l'annotation d'un numéro NER.
     * @param ner Le numéro associé à l'élément du répertoire.
     */
    public void retirerAnnotation(final int ner) {
        annotations.remove(ner);
        saveAnnotations();
    }

    /**
     * Obtient le texte de l'annotation associée à un numéro NER.
     * @param ner Le numéro associé à l'élément du répertoire.
     * @return Le texte de l'annotation ou une
     * chaîne vide si l'annotation n'existe pas.
     */
    public String getAnnotationText(final int ner) {
        return annotations.getOrDefault(ner, new StringBuilder()).toString();
    }

    private void saveAnnotations() {
        try (PrintWriter writer = new PrintWriter("notes.md")) {
            writer.println("# Annotations\n");

            for (Map.Entry<Integer, StringBuilder>
            entry : annotations.entrySet()) {
                writer.println("## NER " + entry.getKey()
                + "\n" + entry.getValue().toString() + "\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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

    /**
     * Supprime l'élément associé au numéro NER.
     * @param cheminDossier Le chemin du dossier à explorer.
     * @param ner Le numéro associé à l'élément du répertoire.
     */
    public void deleteCommand(final String cheminDossier, final int ner) {
        File dossier = new File(cheminDossier);

        if (dossier.isDirectory()) {
            File[] fichiers = dossier.listFiles();

            if (fichiers != null && ner > 0 && ner <= fichiers.length) {
                File selectedFile = fichiers[ner - 1];

                if (selectedFile.exists()) {
                    // Supprime l'élément du répertoire
                    if (selectedFile.isDirectory()) {
                        // Si l'élément est un dossier, supprime récursivement
                        deleteDirectory(selectedFile);
                    } else {
                        // Si l'élément est un fichier, supprime simplement
                        if (selectedFile.delete()) {
                            System.out.println("Élément supprimé avec succès : "
                            + selectedFile.getName());
                        } else {
                            System.out.println("Erreur lors de la ");
                            System.out.println("suppression de l'élément.");
                        }
                    }
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
     * Supprime un dossier et son contenu de manière récursive.
     * @param directory Le dossier à supprimer.
     */
    private void deleteDirectory(final File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // Si l'élément est un dossier, supprime récursivement
                    deleteDirectory(file);
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
}
