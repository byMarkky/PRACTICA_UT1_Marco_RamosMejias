package org.practica.process;

public class Transaction {
    private int id;
    private String nombre;
    private String fecha;
    private double monto;
    private String moneda = "";
    private static final double USD_VALUE = 0.85;
    private static final double LIBRA_VALUE = 1.17;

    public Transaction(int id, String nombre, String fecha, double monto, String moneda) {
        this.id = id;
        this.nombre = nombre;
        this.fecha = fecha;
        this.moneda = moneda;
        this.monto = convertToEuro(monto);
    }

    private double convertToEuro(double c) {
        double res;
        switch (this.moneda) {
            case "USD":
                res = (c * USD_VALUE);
                break;
            case "GBP":
                res = (c * LIBRA_VALUE);
                break;
            default:
                res = c;
                break;
        }

        this.moneda = "EUR";
        return res;
    }

    public String toCSV() {
        return (id + "," + nombre + "," + fecha + "," + monto + "," + moneda);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", fecha='" + fecha + '\'' +
                ", monto=" + monto +
                ", moneda='" + moneda + '\'' +
                '}';
    }
}
