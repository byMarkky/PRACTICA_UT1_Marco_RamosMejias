package org.practica;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.practica.process.Procesator;

public class ProgramaTransacciones {

    private List<String> fileSplited;
    private final int linePerProcess;
    private final int nProcess;
    private final File PROCESS_ERROR_FILE = new File("errores_conversion.csv");
    private final List<Integer> PROCESS_IDS;
    private final File resultFile;

    /**
     * Constructor for ProgramaTransacciones
     * @param file File object with data
     * @param n Number of process per line
     */
    public ProgramaTransacciones(File file, int n) {
        this.fileSplited = new ArrayList<>();
        this.PROCESS_IDS = new ArrayList<>();
        this.resultFile = new File("transacciones_final.csv");

        ProgramaTransacciones.clearFile(PROCESS_ERROR_FILE);

        int fileLines = countLines(file);
        this.nProcess = n;

        // Process per line rounded following ((p-1) / K) + 1
        this.linePerProcess = (int) Math.round((double) (fileLines - 1) / this.nProcess) + 1;

        fileSplited = splitFile(file);
        System.out.println("Process per Line: " + this.linePerProcess);
    }

    /**
     * Method that start the work of the processes
     */
    public void start() {
        List<List<String>> splited = split(this.fileSplited, this.linePerProcess);
        ProcessBuilder pb;
        for (int i = 0; i < this.nProcess; i++) {
            this.PROCESS_IDS.add(i);
            pb = new ProcessBuilder(
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

    /**
     * Method to create a file 'transacciones_final.csv'
     */
    public void generateResult() {

        // Clear the file
        ProgramaTransacciones.clearFile(this.resultFile);

        for (Integer i : this.PROCESS_IDS) {
            String PATH_TEMP_FILES = "temp/";
            File tempFile = Paths.get(PATH_TEMP_FILES + "process_" + i + ".tmp").toFile();
            try (BufferedReader br = new BufferedReader(new FileReader(tempFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    writeLine(line + "\n");
                }
            } catch (IOException e) {
                System.err.println("[ERROR] Fichero " + tempFile + " no se pudo encontrar. Exception: ");
                throw new RuntimeException(e);
            }
        }

    }

    private static void clearFile(File file) {
        try {
            Files.write(file.toPath(), "".getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeLine(String line) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(this.resultFile, true))) {
            bw.write(line);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method, that split the list in sublist for
     * the lines per process. The operation of the
     * function is explained in the code comments.
     * @param list List to be split
     * @param targetSize Size for each sublist
     * @return List of lists of strings
     * @param <T> Idk :)
     */
    public <T> List<List<T>> split(List<T> list,
                                   int targetSize) {
        List<List<T>> lists = new ArrayList<>();
        for (int i = 0; i < list.size(); i += targetSize) {
            lists.add(list.subList(i, Math.min(i + targetSize, list.size())));
        }
        return lists;
    }

    /**
     * Function that split the file in the number of processes
     * that the user want
     * @param file File to be split
     * @return Split file
     */
    private List<String> splitFile(File file) {
        List<String> splited = new ArrayList<>();

        FileReader fileReader;
        try {
            fileReader = new FileReader(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try (BufferedReader bf = new BufferedReader(fileReader)) {

            String line;
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
     * Method that count the number of lines in the csv file
     * @param file File to be counted
     * @return Number of lines
     */
    private int countLines(File file) {
        int lines;
        try (Stream<String> str = Files.lines(file.toPath()))  {
            // Substract 1 because the header don't count.
            lines = (int) str.count() - 1;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return lines;
    }

}
