package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.modelos.Autor;
import com.aluracursos.literalura.modelos.Idioma;
import com.aluracursos.literalura.modelos.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {

    @Query("SELECT l FROM Libro l JOIN l.autor a WHERE l.titulo LIKE %:titulo%")
    Optional<Libro> buscarLibroPorNombre(@Param("titulo") String titulo);

    @Query("SELECT a FROM Libro l JOIN l.autor a WHERE a.nombre LIKE %:nombre%")
    Optional<Autor> buscarAutorPorNombre(@Param("nombre") String nombre);

    @Query("SELECT l FROM Autor a JOIN a.libros l ORDER BY l.titulo")
    List<Libro> librosRegistrados();

    @Query("SELECT l FROM Autor a JOIN a.libros l WHERE l.idioma = :idioma")
    List<Libro> librosPorIdioma(@Param("idioma") Idioma idioma);

    @Query("SELECT a FROM Autor a WHERE a.fechaDeNacimiento <= :fecha AND a.fechaDeFallecimiento > :fecha")
    List<Autor> listarAutoresVivos(@Param("fecha") Integer fecha);


}
