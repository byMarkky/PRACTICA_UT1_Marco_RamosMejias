package org.practica;

import java.io.*;
import java.nio.file.Files;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner reader = new Scanner(System.in);

        System.out.print("Nombre o ruta del fichero de datos: ");
        String fileName = reader.nextLine();

        File file = new File(fileName);

        System.out.print("Numero de procesos: ");
        int nProcesses = reader.nextInt();

        // Validate the input
        if (!Files.exists(file.toPath())) {
            System.err.println("ERROR: Fichero no existe");
            return;
        }
        if (nProcesses <= 0) {
            System.err.println("ERROR: Numero de procesos invalido");
            return;
        }

        ProgramaTransacciones programa = new ProgramaTransacciones(file, nProcesses);
        programa.start();

        programa.generateResult();

    }



}