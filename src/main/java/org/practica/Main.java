package org.practica;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner reader = new Scanner(System.in);

        System.out.print("Nombre o ruta del fichero de datos: ");
        String fileName = reader.next();

        File file = new File(fileName);

        System.out.print("Numero de procesos: ");
        int nProcesses = reader.nextInt();

        // Validsmos la entrada
        if (!Files.exists(file.toPath())) {
            System.err.println("ERROR: Fichero no existe");
            return;
        }
        if (nProcesses <= 0) {
            System.err.println("ERROR: Numero de procesos invalido");
            return;
        }

        int fileLines = countLines(file);

        // Process per line rounded following ((p-1) / K) + 1
        int processPerLine = (int) Math.round((double) (fileLines - 1) / nProcesses) + 1;

        List<String> splitedFile = splitFile(file);

        System.out.println("Process per Line: " + processPerLine);

    }

    /**
     * Function that split the file in the number of processes
     * that the user want
     * @param file File to be splited
     * @return Splited file
     */
    private static List<String> splitFile(File file) {
        List<String> splited = new ArrayList<>();

        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try (BufferedReader bf = new BufferedReader(fileReader)) {

            String line = "";
            // Read until EOF of the file
            while ((line = bf.readLine()) != null) {
                splited.add(line);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Delete the header of the csv
        splited.remove(splited.removeFirst());

        return splited;
    }

    /**
     * Methot that count the number of lines in the csv file
     * @param file File to be counted
     * @return Number of lines
     */
    private static int countLines(File file) {
        int lines = 0;
        try {
            // Substract 1 because the header don't count.
            lines = (int) Files.lines(file.toPath()).count() - 1;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return lines;
    }

}