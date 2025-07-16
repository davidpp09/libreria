package com.proyectolib.libreria.principal;

import com.proyectolib.libreria.model.*;
import com.proyectolib.libreria.repository.AutorRepository;
import com.proyectolib.libreria.repository.LibroRepository;
import com.proyectolib.libreria.service.ConsumoAPI;
import com.proyectolib.libreria.service.ConvierteDatos;
import org.springframework.dao.DataIntegrityViolationException;
import java.util.List;
import java.util.Scanner;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/?search=";
    private ConvierteDatos conversor = new ConvierteDatos();
    private LibroRepository repository;
    private AutorRepository autorRepository;

    public Principal(LibroRepository repository, AutorRepository autorRepository) {
        this.repository = repository;
        this.autorRepository=autorRepository;
    }


    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0){
            var menu=  """
        1 - Buscar libro por título
        2 - Listar libros registrados
        3 - Listar autores registrados
        4 - Listar autores vivos en un determinado año
        5 - Listar libros por idioma
        0 - Salir de la aplicación
        """;
            System.out.println(menu);
            try {
                String entrada = teclado.nextLine();
                opcion = Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Por favor ingresa un número válido.");
                continue;
            }
            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    listarLibrosRegistrados();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresVivosPorAño();
                    break;
                case 5:
                    listarLibrosPorIdioma();
                    break;
                case 6:
                    System.out.println("Saliendo de la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }

        }
    }

    private void listarLibrosPorIdioma() {
        System.out.print("Ingresa el código de idioma (ej: 'en', 'es'): ");
        String idioma = teclado.nextLine();

        List<Libro> libros = repository.findByLanguagesContaining(idioma);
        if (libros.isEmpty()) {
            System.out.println("No hay libros en ese idioma.");
        } else {
            libros.forEach(l -> System.out.println(l.getTitulo()));
        }
    }

    private void listarAutoresVivosPorAño() {
        System.out.print("Ingresa el año: ");
        int anio = teclado.nextInt();
        teclado.nextLine();

        List<Autor> autores = autorRepository.findByNacimientoLessThanEqualAndFallecimientoGreaterThanEqual(anio, anio);
        if (autores.isEmpty()) {
            System.out.println("No hay autores vivos en ese año.");
        } else {
            autores.forEach(a -> System.out.println(a.getName() + " (Nac.: " + a.getNacimiento() + ", Fallec.: " + a.getFallecimiento() + ")"));
        }
    }

    private void listarAutoresRegistrados() {
        List<Autor> autores = autorRepository.findAll();
        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados.");
        } else {
            autores.forEach(a -> System.out.println(a.getName()));
        }
    }

    private void listarLibrosRegistrados() {
        List<Libro> libros = repository.findAll();
        if (libros.isEmpty()){
            System.out.println("No hay libros registrados");
        }else{
            libros.forEach(l -> System.out.println(l.getTitulo()));
        }
    }

    private DatosLibro getDatosLibro(){
        System.out.println("Escribe el nombre de la serie que deseas buscar");
        var nombreSerie = teclado.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "%20"));
        System.out.println(json);
        ResultadoBusqueda resultado = conversor.obtenerDatos(json, ResultadoBusqueda.class);
        DatosLibro datos = resultado.results().get(0); // el primer libro de la búsqueda
        return datos;
    }
    private void buscarLibroPorTitulo() {

            DatosLibro datos = getDatosLibro();
        try {
            Libro libro = new Libro(datos);
            repository.save(libro);
            System.out.println("Libro guardado con éxito: " + libro.getTitulo());
        } catch (DataIntegrityViolationException e) {
            System.out.println("Ya existe un libro con ese título en la base de datos.");
        }
    }
}
