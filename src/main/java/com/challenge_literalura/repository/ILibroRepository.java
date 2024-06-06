package com.challenge_literalura.repository;

import com.challenge_literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ILibroRepository extends JpaRepository<Libro, Long> {

    //Deived query
    Libro findByTituloContainsIgnoreCase(String titulo);

    List<Libro> findByIdioma(String idioma);

    //@Query("SELECT l FROM Libro l WHERE l.idioma = :idioma")
    //List<Libro> buscarLibrosPorIdioma(String idioma);
}
