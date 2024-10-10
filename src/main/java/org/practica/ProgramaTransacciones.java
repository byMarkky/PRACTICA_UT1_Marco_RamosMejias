package org.practica;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.practica.process.Procesator;

public class ProgramaTransacciones {

    private List<String> fileSplited;
    private int linePerProcess;
    private File file;
    private int nProcess;
    private File PROCESS_ERROR_FILE = new File("errores_conversion.csv");

    /**
     * Constructor for ProgramaTransacciones
     * @param file File object with data
     * @param n Number of process per line
     */
    public ProgramaTransacciones(File file, int n) {
        this.fileSplited = new ArrayList<>();

        int fileLines = countLines(file);
        this.nProcess = n;

        // Process per line rounded following ((p-1) / K) + 1
        this.linePerProcess = (int) Math.round((double) (fileLines - 1) / this.nProcess) + 1;

        fileSplited = splitFile(file);

        System.out.println("Process per Line: " + this.linePerProcess);
    }

    /**
     * Methot that start the work of the processes
     */
    public void start() {
        List<List<String>> splited = split(this.fileSplited, this.linePerProcess);
        for (int i = 0; i < this.nProcess; i++) {
            ProcessBuilder pb = new ProcessBuilder(
                    "java",
                    Procesator.class.getName(),
                    Arrays.toString(splited.get(i).toArray()),   // Pass the list as a string
                    String.valueOf(i)                            // Assign the process number
            );

            // Config the proccess
            pb.directory(new File("target/classes"));
            pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
            pb.redirectError(ProcessBuilder.Redirect.appendTo(PROCESS_ERROR_FILE));

            try {
                pb.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // TODO Coment the operation of this function
    /**
     * Methot, copied from stackoverflow, that split the list in sublists
     * for the lines per process. The operation of the function is explained
     * in the code comments.
     * @param list List to be splited
     * @param targetSize Size for each sublist
     * @return List of lists of strings
     * @param <T> Idk :)
     */
    public <T> List<List<T>> split(List<T> list,
                                   int targetSize) {
        List<List<T>> lists = new ArrayList<List<T>>();
        for (int i = 0; i < list.size(); i += targetSize) {
            lists.add(list.subList(i, Math.min(i + targetSize, list.size())));
        }
        return lists;
    }

    /**
     * Function that split the file in the number of processes
     * that the user want
     * @param file File to be splited
     * @return Splited file
     */
    private List<String> splitFile(File file) {
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
    private int countLines(File file) {
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
