package com.aluracursos.literalura.principal;

import com.aluracursos.literalura.modelos.*;
import com.aluracursos.literalura.repository.AutorRepository;
import com.aluracursos.literalura.service.ConsumoAPI;
import com.aluracursos.literalura.service.ConvierteDatos;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/";
    private ConvierteDatos conversor = new ConvierteDatos();
    private AutorRepository repository;

    public Principal(AutorRepository repository) {
        this.repository = repository;
    }

    public void muestraElMenu(){
        int op = 0;
        Scanner opci = new Scanner(System.in);
        System.out.println("\nHola, Bienvenido a Literalura\n");
        while (op!=7){
            System.out.println("""
                    
                    1 - Buscar Libro por Título
                    2 - Buscar Autor por Nombre
                    3 - Mostrar Libros Registrados
                    4 - Mostrar Libros por Idioma
                    5 - Mostrar Autores Registrados
                    6 - Mostrar Autores Vivos en Determinado Año
                    
                    7 - Salir
                    """);
            op = Integer.parseInt(opci.nextLine());
            switch (op){
                case 1:
                    buscarLibro();
                    break;
                case 2:
                    buscarAutor();
                    break;
                case 3:
                    mostrarLibrosRegistrados();
                    break;
                case 4:
                    mostrarLibrosPorIdioma();
                    break;
                case 5:
                    mostrarAutoresRegistrados();
                    break;
                case 6:
                    mostrarAutoresVivos();
                    break;
                case 7:
                    System.out.println("Gracias, vuelva pronto c:");
                    break;
                default:
                    System.out.println("Opción NO válida :c");
                    break;
            }
        }

    }

    //Opción 1
    private void buscarLibro(){
        System.out.println("Escribe el nombre del libro que deseas buscar");
        var nombreLibro = teclado.nextLine();
        //var nombreLibro = "Romeo and Juliet";

        String url = URL_BASE + "?search=" +nombreLibro.replace(" ", "%20").toLowerCase();
        var json = consumoAPI.obtenerDatos(url);
        var datos = conversor.obtenerDatos(json, DatosAux.class);

        Optional<DatosLibro> libroBuscado = datos.libros().stream().findFirst();

        if (libroBuscado.isPresent()) {
            System.out.println(
                            "\nLIBRO ENCONTRADO\n" +
                            "Título: " + libroBuscado.get().titulo() +"\n"+
                            "Autor: " + libroBuscado.get().autores().stream()
                            .map(a -> a.nombre()).limit(1).collect(Collectors.joining()) +"\n"+
                            "Idioma: " + libroBuscado.get().idiomas().stream()
                            .collect(Collectors.joining()) +"\n"+
                            "Número de descargas: " + libroBuscado.get().numeroDeDescargas() +"\n"
            );

            try {
                List<Libro> libroEncontrado = libroBuscado.stream()
                        .map(a -> new Libro(a))
                        .collect(Collectors.toList());

                Autor autorAPI = libroBuscado.stream()
                        .flatMap(l -> l.autores().stream()
                                .map(a -> new Autor(a)))
                        .collect(Collectors.toList()).stream().findFirst().get();

                Optional<Autor> autorBD = repository.buscarAutorPorNombre(libroBuscado.get().autores().stream()
                        .map(a -> a.nombre())
                        .collect(Collectors.joining()));

                Optional<Libro> libroOpcional = repository.buscarLibroPorNombre(nombreLibro);

                if (libroOpcional.isPresent()) {
                    System.out.println("El libro ya está guardado en la Base de Datos");
                } else {
                    Autor autor;
                    if (autorBD.isPresent()) {
                        autor = autorBD.get();
                        System.out.println("EL autor ya está guardado en la Base de Datos");
                    } else {
                        autor = autorAPI;
                        repository.save(autor);
                    }
                    autor.setLibros(libroEncontrado);
                    repository.save(autor);
                }
            } catch (Exception e) {
                System.out.println("Libro NO válido :c \n ¡ADVERTENCIA! \n" + e.getMessage() + "\n\n");
            }
        } else {
            System.out.println("ERROR Libro no encontrado :c");
        }

    }
    //Opción 2
    private void buscarAutor(){
        System.out.println("Escribe el nombre del autor que desea buscar");
        var autorBuscado = teclado.nextLine();
        try {
            Optional<Autor> autor = repository.buscarAutorPorNombre(autorBuscado);
            if (autor.isPresent()){
                autor.stream()
                        .forEach(System.out::println);
            } else {
                System.out.println("ERROR Autor no encontrado :c");
            }
        } catch (Exception e){
            System.out.println("Nombre NO válido :c \n ¡ADVERTNCIA! " + e.getMessage());
        }

    }

    //Opción 3
    private void mostrarLibrosRegistrados() {
        List<Libro> libros = repository.librosRegistrados();
        libros.forEach(System.out::println);
    }

    //Opción 4
    private void mostrarLibrosPorIdioma() {
        var menuIdiomas = """
                Estos son los idiomas disponibles
                
                1 - Inglés
                2 - Español
                3 - Francés
                4 - Portugués
                5 - Alemán
                
                6 - Salir
                """;
        System.out.println(menuIdiomas);

        try {
            var opcionIdioma = Integer.parseInt(teclado.nextLine());

            switch (opcionIdioma) {
                case 1:
                    buscarLibrosPorIdioma("ENG");
                    break;
                case 2:
                    buscarLibrosPorIdioma("ESP");
                    break;
                case 3:
                    buscarLibrosPorIdioma("FRA");
                    break;
                case 4:
                    buscarLibrosPorIdioma("PTG");
                    break;
                case 5:
                    buscarLibrosPorIdioma("DET");
                    break;
                case 6:
                    System.out.println("Gracias, vuelva pronto c:");
                    break;
                default:
                    System.out.println("Opción NO válida");
            }
        } catch (NumberFormatException e) {
            System.out.println("Idioma NO válido :c \n ¡ADVERTENCIA!: " + e.getMessage());
        }
    }//Continuación de Opción 4
    private void buscarLibrosPorIdioma(String idioma) {
        try {
            Idioma idiomaEnum = Idioma.valueOf(idioma);
            List<Libro> libros = repository.librosPorIdioma(idiomaEnum);

            if (!libros.isEmpty()){
                libros.stream()
                        .forEach(System.out::println);
            } else {
                System.out.println("ERROR No hay libros registrados en ese idioma :c");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Idioma NO válido :c \n ¡ADVERTENCIA!: "+ e.getMessage());
        }
    }

    //Opción 5
    private void mostrarAutoresRegistrados() {
        List<Autor> autor = repository.findAll();
        autor.stream().sorted(Comparator.comparing(Autor::getNombre))
                .forEach(System.out::println);
    }

    //Opción 6
    private void mostrarAutoresVivos() {
        System.out.println("Digita un año para verificar qué autores estaban vivos");
        var fecha = Integer.parseInt(teclado.nextLine());
        try {
            List<Autor> autores = repository.listarAutoresVivos(fecha);

            if (!autores.isEmpty()){
                autores.stream()
                        .sorted(Comparator.comparing(Autor::getNombre))
                        .forEach(System.out::println);
            } else {
                System.out.println("Ningún autor vivo encontrado en este año");
            }

        } catch (NumberFormatException e){
            System.out.println("Año NO válido :c " + e.getMessage());
        }

    }
}
