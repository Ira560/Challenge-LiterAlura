package com.challenge_literalura.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Entity
@Table(name = "libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    private String nombreAutor;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "autor_id")
    private Autor autor;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> idioma;
    private int descargas;


    public Libro() {

    }

    public Libro(DatosLibros datosLibros, Autor autor) {
        this.titulo = datosLibros.titulo();
        this.nombreAutor = datosLibros.autores().stream().map(DatosAutores::nombre).collect(Collectors.toList()).get(0).toString();
        this.idioma = datosLibros.lenguajes();
        this.descargas = datosLibros.numeroDeDescargas();
        this.autor = autor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getNombreAutor() {
        return nombreAutor;
    }

    public void setNombreAutor(String nombreAutor) {
        this.nombreAutor = nombreAutor;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public List<String> getIdioma() {
        return idioma;
    }

    public void setIdioma(List<String> idioma) {
        this.idioma = idioma;
    }

    public int getDescargas() {
        return descargas;
    }

    public void setDescargas(int descargas) {
        this.descargas = descargas;
    }

    @Override
    public String toString() {
        return "------------LIBRO-----------\n"+
                "Titulo: "+ titulo + '\n' +
                "Autor: " + nombreAutor + '\n' +
                "Idiomas: " + idioma + '\n' +
                "Descargas: " + descargas +'\n' +
                "----------------------------" +'\n';
    }
}
