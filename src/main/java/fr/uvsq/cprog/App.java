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
    * Nombre maximal de parties lors de la division d'une entrée utilisateur.
    */
    private static final int MAX_PARTS = 3;

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
            System.out.println("Le chemin spécifié ne ");
            System.out.println("correspond pas à un dossier.");
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
        String cheminDossier = System.getProperty("user.dir");

        App app = new App();
        app.setCurrentDirectory(cheminDossier);

        // Affiche le contenu du répertoire avant de demander à
        // l'utilisateur quelle commande exécuter.
        app.afficherContenuRepertoireAvecNER();

        // Appel de la fonction pour afficher l'interface du répertoire
        app.afficherInterfaceRepertoire();
    }

    /**
     * Affiche l'interface du répertoire.
     */
    public void afficherInterfaceRepertoire() {
        scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Que voulez-vous faire? ");
            System.out.print("(pour l'instant, les commandes 'find', ");
            System.out.print("'mkdir', 'ls' et 'visu' sont supportées) : ");
            String input = scanner.nextLine();

            if (input.equals("0")) {
                System.out.println("Sortie de l'interface.");
                break;
            }

            if (input.matches("\\d+\\s.*")) {
                // Si la commande commence par un NER,
                //traite-la comme une commande avec NER
                processUserActionWithNER(input);
            } else {
                // Sinon, traite la commande normalement
                processUserAction(input);
            }
        }

        scanner.close();
    }

    /**
     * Traite l'action de l'utilisateur.
     * @param input Entrée de l'utilisateur.
     */
    public void processUserActionWithNER(final String input) {
        String[] parts = input.split(" ", MAX_PARTS);

        try {
            int ner = Integer.parseInt(parts[0].trim());

            if (parts.length == 2) {
                if (parts[1].equals(".")) {
                    // Si la deuxième partie est ".",
                    //considère cela comme une commande "cd"
                    String newDirectory = commande
                    .cdCommand(getCurrentDirectory(), ner);
                    setCurrentDirectory(newDirectory);
                } else if (parts[1].equalsIgnoreCase("visu")) {
                    // Si la deuxième partie est "visu",
                    //considère cela comme une commande "visu"
                    commande.visuCommand(getCurrentDirectory(), ner);
                } else if (parts[1].equalsIgnoreCase("copy")) {
                    // Si la deuxième partie est "copy",
                    //considère cela comme une commande "copy"
                    commande.copyCommand(getCurrentDirectory(), ner);
                } else if (parts[1].equalsIgnoreCase("-")) {
                    // Si la deuxième partie est "-",
                    // considère cela comme une commande de retrait d'annotation
                    commande.retirerAnnotation(ner);
                    System.out.println("Annotation supprimé");
                } else if (parts[1].equalsIgnoreCase("annotation")) {
                    // Si la deuxième partie est "annotation",
                    // considère cela comme une commande "annotation"
                    String annotationText = commande.getAnnotationText(ner);
                    System.out.println("Annotation pour NER "
                    + ner + ": " + annotationText);
                } else if (parts[1].equalsIgnoreCase("cut")) {
                    // Si la deuxième partie est "cut",
                    // considère cela comme une commande "cut"
                    commande.cutCommand(getCurrentDirectory(), ner);
                } else if (parts[1].equalsIgnoreCase("delete")) {
                    // Si la deuxième partie est "cut",
                    // considère cela comme une commande "cut"
                    commande.deleteCommand(getCurrentDirectory(), ner);
                } else {
                    // Si ce n'est pas le cas, traite la commande normalement
                    processUserAction(input);
                }
            } else if (parts.length == MAX_PARTS) {
                if (parts[1].equals("+")) {
                    if (parts[2].matches("\".*\"")) {
                        String texteAnnotation = parts[2]
                        .replaceAll("\"([^\"]*)\".*", "$1");
                        commande.ajouterAnnotation(ner, texteAnnotation);
                        System.out.println("Annotation ajouté");
                    } else {
                        // Gérer le cas où la syntaxe
                        // des guillemets n'est pas correcte
                        System.out.println("Syntaxe incorrecte ");
                        System.out.println("pour les guillemets.");
                    }
                }
            } else {
            // Si la deuxième partie est absente,
            // considère cela comme une commande "visu"
            commande.visuCommand(getCurrentDirectory(), ner);
        }
    } catch (NumberFormatException e) {
        // Si la conversion en entier échoue, la commande n'est pas valide
        System.out.println("Numéro NER invalide.");
    }
    }

    /**
     * Traite l'action de l'utilisateur.
     * @param input Entrée de l'utilisateur.
     */
    public void processUserAction(final String input) {
        String[] parts = input.split(" ", 2);

        switch (parts[0].toLowerCase()) {
            case "find":
                if (parts.length > 1) {
                    String fileName = parts[1];
                    commande.findCommand(getCurrentDirectory(), fileName);
                } else {
                    System.out.println("Veuillez spécifier le nom");
                    System.out.println(" du fichier à rechercher.");
                }
                break;
            case "mkdir":
                if (parts.length > 1) {
                    String dirName = parts[1];
                    commande.mkdirCommand(getCurrentDirectory(), dirName);
                } else {
                    System.out.println("Veuillez spécifier le ");
                    System.out.println("nom du répertoire à créer.");
                }
                break;
            case "ls":
                afficherContenuRepertoireAvecNER();
                break;
            case "..":
                String newDirectory = commande
                .remonterDossier(getCurrentDirectory());
                if (newDirectory != null) {
                    setCurrentDirectory(newDirectory);
                }
                break;
            case "past":
                commande.pastCommand(getCurrentDirectory());
                break;
            default:
                System.out.println("Action non reconnue : " + input);
        }
    }

    /**
     * Affiche le contenu du répertoire avec les numéros associés.
     */
    public void afficherContenuRepertoireAvecNER() {
        String cheminDossier = getCurrentDirectory();
        File dossier = new File(cheminDossier);

        if (dossier.isDirectory()) {
            File[] fichiers = dossier.listFiles();

            System.out.println("Contenu du répertoire :");
            if (fichiers != null && fichiers.length > 0) {
                for (int i = 0; i < fichiers.length; i++) {
                   System.out.println(i + 1 + ". " + fichiers[i].getName());
                }
            } else {
              System.out.println("Le dossier est vide.");
            }
        } else {
           System.out.println("Le chemin spécifié ne ");
           System.out.println("correspond pas à un dossier.");
        }
    }
}
