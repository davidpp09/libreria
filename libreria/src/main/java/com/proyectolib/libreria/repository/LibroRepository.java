package com.proyectolib.libreria.repository;

import com.proyectolib.libreria.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LibroRepository extends JpaRepository<Libro,Long> {
    List<Libro> findByLanguagesContaining(String idioma);
}
