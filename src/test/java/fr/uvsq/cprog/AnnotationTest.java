package fr.uvsq.cprog;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.junit.jupiter.api.Test;

public class AnnotationTest {

    @Test
    public void testSaveAnnotations() throws IOException {

        Annotation annotation = new Annotation();
        
        annotation.ajouterAnnotation(1, "Annotation pour NER 1");
        annotation.ajouterAnnotation(2, "Annotation pour NER 2");

        annotation.saveAnnotations();
        String savedContent = readSavedContent();

        assertTrue(savedContent.contains("# Annotations"));
        assertTrue(savedContent.contains("## NER 1\nAnnotation pour NER 1\n"));
        assertTrue(savedContent.contains("## NER 2\nAnnotation pour NER 2\n"));
    }

    @Test
    public void testAjouterAnnotation_NouveauNER() throws IOException {

        Annotation annotation = new Annotation();
        annotation.ajouterAnnotation(1, "Nouvelle annotation pour NER 1");
        assertEquals("Nouvelle annotation pour NER 1", annotation.getAnnotationText(1));

        String savedContent = readSavedContent();
        assertTrue(savedContent.contains("## NER 1\nNouvelle annotation pour NER 1\n"));
    }

    @Test
    public void testAjouterAnnotation_AncienNER() throws IOException {

        Annotation annotation = new Annotation();
        annotation.ajouterAnnotation(1, "Première annotation pour NER 1");
        annotation.ajouterAnnotation(1, "Deuxième annotation pour NER 1");
        assertEquals("Première annotation pour NER 1 Deuxième annotation pour NER 1", annotation.getAnnotationText(1));

        String savedContent = readSavedContent();
        assertTrue(savedContent.contains("## NER 1\nPremière annotation pour NER 1 Deuxième annotation pour NER 1\n"));
    }

    @Test
    public void testGetAnnotationText_ExistingNER() throws IOException {

        Annotation annotation = new Annotation();
        annotation.ajouterAnnotation(1, "Annotation pour NER 1");
        assertEquals("Annotation pour NER 1", annotation.getAnnotationText(1));
    }

    @Test
    public void testGetAnnotationText_NonExistingNER() {

        Annotation annotation = new Annotation();
        assertEquals("", annotation.getAnnotationText(1));
    }

    @Test
    public void testRetirerAnnotation_ExistingNER() throws IOException {
        
        Annotation annotation = new Annotation();
        annotation.ajouterAnnotation(1, "Annotation pour NER 1");
        annotation.retirerAnnotation(1);
        assertEquals("", annotation.getAnnotationText(1));
        String savedContent = readSavedContent();
        assertFalse(savedContent.contains("## NER 1\nAnnotation pour NER 1\n"));
    }

    @Test
    public void testRetirerAnnotation_NonExistingNER() {

        Annotation annotation = new Annotation();
        annotation.retirerAnnotation(1);
    }

    private String readSavedContent() throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader("notes.md"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }
    
}
