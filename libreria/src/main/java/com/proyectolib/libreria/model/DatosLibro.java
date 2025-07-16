package com.proyectolib.libreria.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibro(
        Integer id,
        String title,
        List<DatosAutor> authors,
        List<String> languages
) {
}
