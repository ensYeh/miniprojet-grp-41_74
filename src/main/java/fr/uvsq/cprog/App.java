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
     * Instance de la classe Annotation utilisée pour traiter
     * les annotations.
     */
    private Annotation annotation;

    /**
     * Instance de la classe CopiePaste utilisée pour traiter
     * les copier coller et cut.
     */
    private CopiePaste copiepaste;

    /**
     * Instance de la classe Visu utilisée pour traiter
     * visu.
     */
    private Visu visu;

    /**
     * Instance de la classe Find utilisée pour traiter
     * find.
     */
    private Find find;

    /**
     * Instance de la classe Mkdir utilisée pour traiter
     * mkdir.
     */
    private Mkdir mkdir;

    /**
     * Instance de la classe NavigueDir utilisée pour traiter
     * "." et ".." pour de déplacer dans les répertoires.
     */
    private NavigueDir navigueDir;

    /**
    * Nombre maximal de parties lors de la division d'une entrée utilisateur.
    */
    private static final int MAX_PARTS = 3;

    /**
     * Récupère l'annotation associée à l'application.
     *
     * @return L'annotation associée à l'application.
     */
    public Annotation getAnnotation() {
        return this.annotation;
    }

    /**
     * Définit l'annotation pour l'application.
     *
     * @param newAnnotation L'annotation à définir.
     */
    public void setAnnotation(final Annotation newAnnotation) {
        this.annotation = newAnnotation;
    }

    /**
     * Constructeur de la classe App.
     */
    public App() {
        this.navigueDir = new NavigueDir();
        this.annotation = new Annotation();
        this.copiepaste = new CopiePaste();
        this.find = new Find();
        this.visu = new Visu();
        this.mkdir = new Mkdir();
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
            System.out.println("Le chemin spécifié ne "
            + "correspond pas à un dossier.");
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
            System.out.print("Quelle commande voulait vous "
                + "effectuer? help pour voir la liste de commande : ");
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
                    String newDirectory = navigueDir
                    .cdCommand(getCurrentDirectory(), ner);
                    setCurrentDirectory(newDirectory);
                } else if (parts[1].equalsIgnoreCase("visu")) {
                    // Si la deuxième partie est "visu",
                    //considère cela comme une commande "visu"
                    visu.visuCommand(getCurrentDirectory(), ner);
                } else if (parts[1].equalsIgnoreCase("copy")) {
                    // Si la deuxième partie est "copy",
                    //considère cela comme une commande "copy"
                    copiepaste.copyCommand(getCurrentDirectory(), ner);
                } else if (parts[1].equalsIgnoreCase("-")) {
                    // Si la deuxième partie est "-",
                    // considère cela comme une commande de retrait d'annotation
                    annotation.retirerAnnotation(ner);
                    System.out.println("Annotation supprimé");
                } else if (parts[1].equalsIgnoreCase("annotation")) {
                    // Si la deuxième partie est "annotation",
                    // considère cela comme une commande "annotation"
                    String annotationText = annotation.getAnnotationText(ner);
                    System.out.println("Annotation pour NER "
                    + ner + ": " + annotationText);
                } else if (parts[1].equalsIgnoreCase("cut")) {
                    // Si la deuxième partie est "cut",
                    // considère cela comme une commande "cut"
                    copiepaste.cutCommand(getCurrentDirectory(), ner);
                } else {
                    // Si ce n'est pas le cas, traite la commande normalement
                    processUserAction(input);
                }
            } else if (parts.length == MAX_PARTS) {
                if (parts[1].equals("+")) {
                    if (parts[2].matches("\".*\"")) {
                        String texteAnnotation = parts[2]
                        .replaceAll("\"([^\"]*)\".*", "$1");
                        annotation.ajouterAnnotation(ner, texteAnnotation);
                        System.out.println("Annotation ajouté");
                    } else {
                        // Gérer le cas où la syntaxe
                        // des guillemets n'est pas correcte
                        System.out.println("Syntaxe incorrecte "
                        + "pour les guillemets.");
                    }
                }
            } else {
            // Si la deuxième partie est absente,
            // considère cela comme une commande "visu"
            visu.visuCommand(getCurrentDirectory(), ner);
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
                    find.findCommand(getCurrentDirectory(), fileName);
                } else {
                    System.out.println("Veuillez spécifier le nom"
                    + " du fichier à rechercher.");
                }
                break;
            case "mkdir":
                if (parts.length > 1) {
                    String dirName = parts[1];
                    mkdir.mkdirCommand(getCurrentDirectory(), dirName);
                } else {
                    System.out.println("Veuillez spécifier le "
                    + "nom du répertoire à créer.");
                }
                break;
            case "ls":
                afficherContenuRepertoireAvecNER();
                break;
            case "..":
                String newDirectory = navigueDir
                .remonterDossier(getCurrentDirectory());
                if (newDirectory != null) {
                    setCurrentDirectory(newDirectory);
                }
                break;
            case "past":
                copiepaste.pastCommand(getCurrentDirectory());
                break;
            case "help":
                help();
                break;
            default:
                System.out.println("Action non reconnue : " + input);
        }
    }

    /**
     * Affiche les commandes du gestionnaire de fichiers.
     * Les commandes comprennent :
     * - copy: Copie l'élément correspondant au NER.
     * - cut: Coupe l'élément correspondant au NER.
     * - past: Colle l'élément copié/coupé à l'emplacement actuel.
     * - .. : Remonte d'un cran dans le système de fichiers.
     * - ... : Entre dans un répertoire si le NER désigne un répertoire.
     * - mkdir <nom> : Crée un répertoire avec le nom spécifié.
     * - visu: Affiche le contenu d'un fichier texte.
     * - find <nom fichier>: Recherche et affiche le(s) fichier(s)
     *  dans tous les sous-répertoires du répertoire courant.
     * - ... : Ajoute ou concatène le texte spécifié à l'annotation de l'ER.
     * - ... : Retire tout le texte associé à l'annotation de l'ER.
     * - ls : Affiche le contenu du répertoire.
     * - NER annotation : Affiche l'annotation associée à l'ER spécifié.
     */
    public void help() {
        System.out.println("Les commandes du gestionnaire de fichiers sont :");
        System.out.println("[<NER>] copy: Copie l'élément "
        + "correspondant au NER.");
        System.out.println("[<NER>] cut : Coupe l'élément correspondant"
        + " au NER.");
        System.out.println("past : Colle l'élement copié/coupé à "
        + "l'emplacement actuel.");
        System.out.println(".. : Remonte d'un cran dans le système "
        + "de fichiers.");
        System.out.println("[<NER>] . : Entre dans un répertoire si le NER "
        + "désigne un répertoire.");
        System.out.println("mkdir <nom> : Crée un répertoire avec le nom "
        + "spécifié.");
        System.out.println("[<NER>] visu : Affiche le contenu d'un fichier "
        + "texte. Si le fichier n'est pas de type texte, affiche sa taille.");
        System.out.println("find <nom fichier> : Recherche et affiche le(s)"
        + " fichier(s) dans tout les sous-répertoires du répertoire courant.");
        System.out.println("[<NER>] + \"ceci est un texte\" : Ajoute ou "
        + "concatène le texte spécifié à l'annotation de l'ER.");
        System.out.println("[<NER>] - : Retire tout le texte associé à "
        + "l'annotation de l'ER.");
        System.out.println("ls : Affiche le contenu du répertoire.");
        System.out.println("NER annotation : Affiche l'annotation associée à "
        + "l'ER spécifié.");
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
            System.out.println("Le chemin spécifié ne "
            + "correspond pas à un dossier.");
        }
    }
}
