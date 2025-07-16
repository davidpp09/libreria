package com.proyectolib.libreria.repository;

import com.proyectolib.libreria.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AutorRepository extends JpaRepository<Autor,Integer> {
    List<Autor> findByNacimientoLessThanEqualAndFallecimientoGreaterThanEqual(Integer anio, Integer anio2);

}
