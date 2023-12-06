package fr.uvsq.cprog;

import java.io.File;

public class Commande {
    public void findCommand(String cheminDossier, String fileName) {
        File dossier = new File(cheminDossier);

        if (dossier.isDirectory()) {
            findMatchingFiles(dossier, fileName);
        } else {
            System.out.println("Le chemin spécifié ne correspond pas à un dossier.");
        }
    }

    private void findMatchingFiles(File directory, String fileName) {
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    findMatchingFiles(file, fileName);
                } else if (file.getName().equals(fileName)) {
                    System.out.println("Fichier trouvé : " + file.getAbsolutePath());
                }
            }
        }
    }
}