package com.challenge_literalura.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String nombre;
    private int fechaDeNacimiento;
    private int fechaDeMuerte;
    @OneToMany(mappedBy = "autor", fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<Libro> libros = new ArrayList<>();


    public Autor() {
    }

    public Autor(DatosAutores datosAutores) {
        this.nombre = datosAutores.nombre();
        this.fechaDeNacimiento = datosAutores.fechaNacimiento();
        this.fechaDeMuerte = datosAutores.fechaMuerte();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public void setFechaDeNacimiento(int fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
    }

    public int getFechaDeMuerte() {
        return fechaDeMuerte;
    }

    public void setFechaDeMuerte(int fechaDeMuerte) {
        this.fechaDeMuerte = fechaDeMuerte;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }

    @Override
    public String toString() {
        String titulosLibros = libros.stream()
                .map(l -> l.getTitulo())
                .collect(Collectors.joining(", "));
        return  "----- AUTOR -----" +
                "\nNombre: " + nombre +
                "\nFecha de nacimiento: " + fechaDeNacimiento +
                "\nFecha de fallecimiento: " + fechaDeMuerte +
                "\nLibros: " + titulosLibros +
                "\n-----------------";
    }
}
