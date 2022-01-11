package com.example.kafka;

public class Producto {
    private String nombre;
    private Double precio;

    public Producto(String nombre, Double precio){
        this.nombre = nombre;
        this.precio = precio;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "nombre='" + nombre + '\'' +
                ", precio=" + precio +
                '}';
    }
}
