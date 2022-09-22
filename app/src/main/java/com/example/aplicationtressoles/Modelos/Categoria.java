package com.example.aplicationtressoles.Modelos;

import java.util.ArrayList;
import java.util.List;

public class Categoria {
    private Long id;
    private String nombre;
    private boolean borrado;

    public Categoria() {

    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean isBorrado() {
        return borrado;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setBorrado(boolean borrado) {
        this.borrado = borrado;
    }
}
