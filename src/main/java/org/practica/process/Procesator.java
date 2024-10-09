package org.practica.process;

public class Procesator {
    public static void main(String[] args) {
        int processNumber = Integer.parseInt(args[1]);
        System.out.println("---- PROCESO " + processNumber + " ----");
        System.out.println(args[0]);
    }
}
