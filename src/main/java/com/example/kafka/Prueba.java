package com.example.kafka;

import com.google.gson.Gson;

public class Prueba {
    public static void main(String[] args) {
        //Creación de objeto a enviar
        Producto producto = new Producto("lapiz",23D);
        //Objeto deserializador y serializador
        Gson gson = new Gson();

        //Conversión a JSON
        String producto_ajson = gson.toJson(producto);
        System.out.println(producto_ajson);

        //Conversión a Producto
        Producto producto_dejson = gson.fromJson(producto_ajson,Producto.class);
        System.out.println(producto_dejson.toString());
    }
}
