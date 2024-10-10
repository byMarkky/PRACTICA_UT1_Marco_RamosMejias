package org.practica.process;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Procesator {
    public static void main(String[] args) {
        int processNumber = Integer.parseInt(args[1]);
        List<Transaction> transacciones = parseInput(args[0]);

        checkFraud(transacciones);

        System.out.println("------ PROCESO " + processNumber + " ------");
        System.out.println("Total lineas leidas: " + transacciones.size());
        System.out.println("Primer ID: " + transacciones.getFirst().getId());
        System.out.println("Ultimo ID: " + transacciones.getLast().getId());

        // TODO Write the results in a temp file

    }

    private static void checkFraud(List<Transaction> transactions) {
        for (Transaction tran : transactions) {
            if (tran.getMonto() > 50000) {
                System.err.println("[ERROR] Fraude detectado en la transacción ID: " + tran.getId() + ". Monto: " + tran.getMonto() + "€");
            }
        }
    }

    private static List<Transaction> parseInput(String input) {
        // Create a sub string to delete de [ and ]
        // And split by " " because each line is spaced by " "
        String[] splited = input.substring(1, input.length() - 1).split(" ");

        List<Transaction> res = new ArrayList<>();
        for (String str : splited) {
            String[] strSplit = str.split(",");

            res.add(new Transaction(
                    Integer.parseInt(strSplit[0]),
                    strSplit[1],
                    strSplit[2],
                    Double.parseDouble(strSplit[3]),
                    strSplit[4]
            ));
        }

        return res;
    }
}
