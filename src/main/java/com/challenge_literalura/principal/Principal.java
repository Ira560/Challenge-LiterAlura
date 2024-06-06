package com.challenge_literalura.principal;

import com.challenge_literalura.model.*;
import com.challenge_literalura.repository.IAutorRepository;
import com.challenge_literalura.repository.ILibroRepository;
import com.challenge_literalura.service.ConsumoAPI;
import com.challenge_literalura.service.ConvierteDatos;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

    private final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private List<Libro> libro;
    private List<Autor> autor;
    private Scanner teclado = new Scanner(System.in);
    private ILibroRepository libroRepository;
    private IAutorRepository autorRepository;

    public Principal(ILibroRepository libroRepository, IAutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void menu(){
        var opcion = -1;
        while (opcion != 0) {

            infoMenu();
            scannerSoloNumeros();

            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibro();
                    break;
                case 2:
                    mostrarLibrosRegistrados();
                    break;
                case 3:
                    mostrarAutoresRegistrados();
                    break;
                case 4:
                    buscarAutorVivoEnAño();
                    break;
                case 5:
                    buscarLibrosPorIdioma();
                    break;
                case 0:
                    System.out.println("""
                            -------------------------
                            Cerrando la aplicación...
                            Gracias por usarla!!
                            -------------------------
                            """);
                    // Forzar salida de la aplicación
                    //System.exit(0);
                    break;
                default:
                    System.out.println("""
                            ---------------
                            Opción inválida
                            ---------------
                            """);
            }
        }
    }


    public void buscarLibro() {
        System.out.println("Ingrese nombre del libro");
        var buscaLibro = teclado.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + buscaLibro.replace(" ", "+"));
        var buscador = conversor.obtenerDatos(json, Datos.class);

        Optional<DatosLibros> libroBuscado = buscador.resultados().stream()
                .filter(l -> l.titulo().toUpperCase().contains(buscaLibro.toUpperCase()))
                .findFirst();

        //Analisa respuesta de consulta. si ecuentra coincidencia lo procesa, sino informa que no encontro el libro
        if (libroBuscado.isPresent()) {
            DatosLibros datosLibros = libroBuscado.get();
            DatosAutores datosAutor = datosLibros.autores().get(0);
            Autor autor = autorRepository.findByNombre(datosAutor.nombre());

            //Analiza que autor este regsitrado en base de datos
            //Si ya esta registrado no lo almacena, pero si almacena informacion de libro
            if (autor == null) {

                autor = new Autor(datosAutor);
                autorRepository.save(autor);
            }

            //Analiza que libro este regsitrado en base de datos
            //Si ya esta registrado no lo almacena
            Libro libro = libroRepository.findByTituloContainsIgnoreCase(datosLibros.titulo());

            if (libro == null) {
                System.out.println("¡Libro encontrado!");
                libro = new Libro(datosLibros, autor);
                libroRepository.save(libro);
                System.out.println(libro);
            } else {
                System.out.println("Libro ya esta Registrado");
            }
        } else {
            System.out.println("Libro no encontrado");
        }
    }

    private void mostrarLibrosRegistrados(){
        libro = libroRepository.findAll();
        libro.forEach(System.out::println);
    }

    private void mostrarAutoresRegistrados(){
        autor = autorRepository.findAll();
        autor.forEach(System.out::println);
    }

    private void buscarLibrosPorIdioma(){
        System.out.println("""
        ---------------------------------
        Ingrese numero de idioma deseado:
        
        1- Ingles
        2- Español
        3- Portuges
        4- Italiano
        
        --------------------------------
        """);
        scannerSoloNumeros();
        var  numero = teclado.nextInt();
        switch (numero) {
            case 1:
                buscarIdioma("en");
                break;
            case 2:
                buscarIdioma("es");
                break;
            case 3:
                buscarIdioma("pt");
                break;
            case 4:
                buscarIdioma("it");
                break;
            default:
                System.out.println("Opción inválida");
        }
    }
    private void buscarIdioma(String idioma) {
        try {
            libro = libroRepository.findByIdioma(idioma);

            if (libro == null) {
                System.out.println("""
                        -------------------------
                        No hay Libros registrados
                        -------------------------
                        """);
            } else {
                libro.forEach(System.out::println);
            }
        }catch (Exception e){
            System.out.println("""
                    --------------------
                    Error en la busqueda
                    --------------------
                    """);
        }

    }

    private void buscarAutorVivoEnAño () {
        System.out.println("Ingrese año");
        scannerSoloNumeros();
        var año = teclado.nextInt();
        autor = autorRepository.buscarAutoresPorDeterminadoAño(año);
        if (autor == null) {
            System.out.println("No hay registro de autores en ese año");
        } else {
            autor.forEach(System.out::println);
        }
    }

    private void infoMenu(){
        var menu = """
                    ----------------------------------------
                        MENU:
                    
                    1 - Buscar libros por titulo
                    2 - Mostrar libros registrados
                    3 - Mostrar autores registrados
                    4 - Mostrar autores vivos en determinado año
                    5 - Mostrar libros por idiomas
               
                        
                    0 - Salir
                    -----------------------------------------
                    """;
        System.out.println(menu);
    }
    //filtro de opcion para solo permitir numeros
    private void scannerSoloNumeros() {
        while (!teclado.hasNextInt()) {
            System.out.println("Ingrese solo numeros");
            teclado.next();
        }

    }

/*
    public void muestraElMenu(){
        int opcion = -1;
        while (opcion != 0) {
            String menu = """
                    Elija la opción através de su número:
                    1 - Buscar libro por titulo
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma

                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion){
                case 1:
                    buscarYGuardarLibrosPorTitulo();
                    break;
                case 2:
                    mostrarLibrosRegistrados();
                    break;
                case 3:
                    mostrarAutoresRegistrados();
                    break;
                case 4:
                    mostrarAutoresEnDeterminadaFecha();
                    break;
                case 5:
                    mostrarLibrosPorIdioma();
                    break;
                case 0:
                    System.out.println("""
                            -------------------------
                            Cerrando la aplicación...
                            Gracias por usarla!!
                            -------------------------
                            """);
                    break;
                default:
                    System.out.println("""
                            ---------------
                            Opción invalida
                            ---------------
                            """);
                    muestraElMenu();
            }
        }
    }

    public Optional<DatosLibros> getDatosLibro(){
        System.out.println("Escribe el nombre del libro que deseas buscar:");
        String nombreLibro = teclado.nextLine();
        String json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + nombreLibro.replace(" ", "+"));
        System.out.println(json);
        //Aqui obtenemos results de la api en forma de list
        Datos datosBusqueda = conversor.obtenerDatos(json, Datos.class);
        return datosBusqueda.resultados().stream()
                .map(l -> new DatosLibros(l.titulo(),
                        l.autores().stream()
                            .map(a -> new DatosAutores(a.nombre(), a.fechaNacimiento(), a.fechaMuerte()))
                                .collect(Collectors.toList()), l.lenguajes(), l.numeroDeDescargas()))
                .findFirst();
    }

    private void buscarYGuardarLibrosPorTitulo(){
        Optional<DatosLibros> datos = getDatosLibro();
            Libro libro = new Libro(datos);
           Libro tituloLibro = libroRepository.findByTitulo(libro.getTitulo());
            if (tituloLibro != null && tituloLibro.getTitulo().equals(libro.getTitulo())) {
                System.out.println("--- Libro ya registrado ---");
            } else {
                List<Autor> autorList = datos.get().autores().stream()
                        .map(a -> {
                            // Buscar autor existente o crear uno nuevo
                            Optional<Autor> autorExistente = autorRepository
                                    .findByNombreAndFechaDeNacimientoAndFechaDeMuerte(
                                            a.nombre(), a.fechaNacimiento(), a.fechaMuerte());
                            if (autorExistente.isPresent()) {
                                return autorExistente.get();
                            } else {
                                Autor nuevoAutor = new Autor(a.nombre(), a.fechaNacimiento(),
                                        a.fechaMuerte());
                                return autorRepository.save(nuevoAutor);
                            }
                        }).collect(Collectors.toList());
                // Asociar los autores al libro
                libro.setAutor(autorList);
                System.out.println(libro.toString());
                // Guardar el libro
                libroRepository.save(libro);
            }
        } else {
            System.out.println("--- Libro no encontrado ---");
        }
//        if (datos.isPresent()) {
//            String mensaje = """
//                    ----- LIBRO -----
//                    Título: %s
//                    Autor: %s
//                    Idioma: %s
//                    Numero de descargas: %d
//                    -----------------
//                    """.formatted(datos.get().titulo(), datos.get().autores().get(0).nombre(), datos.get().lenguajes().get(0), datos.get().numeroDeDescargas());
//            System.out.println(mensaje);
//            libroRepository.save(libro);
//        } else {
//            System.out.println("----- Libro no encontrado -----");
//        }

//        List<DatosLibros> datos = getDatosLibro();
//        Libro libro = new Libro(datos);
//        libroRepository.save(libro);
//        System.out.println(libro);
    }

    private void mostrarLibrosRegistrados(){
        List<Libro> libros = libroRepository.findAll();
        if (!libros.isEmpty()) { //el signo de exclamacion (!) indica que no esta vacia la lista de libros
            System.out.println(libros);
        } else {
            System.out.println("""
                    ------------------------------------
                    No se encontrarón libros registardos
                    ------------------------------------
                    """);
        }
    }

    private void mostrarAutoresRegistrados(){
        List<Autor> autor = autorRepository.findAll();
        if (!autor.isEmpty()) {  //el signo de exclamacion (!) indica que no esta vacia la lista de libros
            System.out.println(autor);
        } else {
            System.out.println("""
                    -------------------------------------
                    No se encontrarón autores registardos
                    -------------------------------------
                    """);
        }
    }

    private void mostrarAutoresEnDeterminadaFecha(){
        System.out.println("Ingresa la fecha en la que te gustaria buscar un autor");
        int fecha = teclado.nextInt();
        List<Autor> autor = autorRepository.buscarAutoresPorDeterminadoAño(fecha);
        if (!autor.isEmpty()) {
            System.out.println(autor.toString());
        } else {
            System.out.println("""
                    ------------------------------------
                    No se encontrarón autores en ese año
                    ------------------------------------
                    """);
        }
    }

    private void mostrarLibrosPorIdioma(){
        System.out.println("""
                ---------------------------------------------------
                Ingresa el idioma del libro que te gustaria buscar:
                es - Español
                en - Ingles
                jp - Japones
                fr - Frances
                pt - Portugues
                ---------------------------------------------------
                """);
        String idioma = teclado.nextLine();
        List<Libro> libro = libroRepository.buscarLibrosPorIdioma(idioma);
        if (!libro.isEmpty()) {
            System.out.println(libro.toString());
        } else {
            System.out.println("""
                    ------------------------------
                    Idioma del libro no encontrado
                    ------------------------------
                    """);
        }
    }
*/
}
