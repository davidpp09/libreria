package com.proyectolib.libreria.model;
import jakarta.persistence.*;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name="libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String titulo;
    @ManyToMany(mappedBy = "libros", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Autor> autors;
    @ElementCollection
    private List<String> languages;
public Libro (){}

    public Libro(DatosLibro datosLibro){
        this.titulo = datosLibro.title();
        this.languages = datosLibro.languages();

        if (datosLibro.authors() != null) {
            this.autors = datosLibro.authors().stream().map(a -> {
                Autor autor = new Autor();
                autor.setName(a.name());
                autor.setNacimiento(a.nacimiento());
                autor.setFallecimiento(a.fallecimiento());
                autor.setLibros(List.of(this));
                return autor;
            }).toList();
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<Autor> getAutors() {
        return autors;
    }

    public void setAutors(List<Autor> autors) {
        this.autors = autors;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }
}
