package com.example.smartslate.utility;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class FileHandler {
    private File file;

    public FileHandler(String filename) {
        this.file = new File(filename);
    }
    //Denne metode kører for at se om der er noget på fillen
    public String read() throws IOException {
        Scanner scanner = new Scanner(file);
        StringBuilder contentBuilder = new StringBuilder();

        while (scanner.hasNextLine()) {
            contentBuilder.append(scanner.nextLine()).append("\n");
        }
        scanner.close();
        return contentBuilder.toString();
    }

    //Denne metode kører for at indlæse til fillen
    public void write(String content) throws IOException {
        java.io.FileWriter fileWriter = new java.io.FileWriter(file);
        fileWriter.write(content);
        fileWriter.close();
    }
}
