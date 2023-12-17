package fr.uvsq.cprog;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class Annotation {

    /**
    * Map associant les numéros NER aux annotations associées.
    */
    private Map<Integer, StringBuilder> annotations;

    /**
     * Constructeur par défaut de la classe Annotation.
     * Initialise la structure de données pour stocker les annotations.
     */
    public Annotation() {
        this.annotations = new HashMap<>();
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

    /**
 * Sauvegarde les annotations dans un fichier "notes.md".
 * Les annotations sont écrites dans un format Markdown.
 * Chaque annotation est associée à son numéro NER.
 */
public void saveAnnotations() {
    try (PrintWriter writer = new PrintWriter("notes.md")) {
        writer.println("# Annotations\n");

        for (Map.Entry<Integer, StringBuilder> entry : annotations.entrySet()) {
            writer.println("## NER " + entry.getKey()
                + "\n" + entry.getValue().toString() + "\n");
        }
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    }
}
}
