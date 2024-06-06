package com.challenge_literalura.repository;

import com.challenge_literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IAutorRepository extends JpaRepository<Autor, Long> {

    //Derived query
    Autor findByNombre(String nombre);

    //Query nativa
    @Query("SELECT a FROM Autor a WHERE a.fechaDeNacimiento <= :año AND a.fechaDeMuerte >= :año")
    List<Autor> buscarAutoresPorDeterminadoAño(int año);
}
