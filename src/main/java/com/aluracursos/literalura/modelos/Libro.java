package com.aluracursos.literalura.modelos;

import jakarta.persistence.*;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String titulo;

    @Enumerated(EnumType.STRING)
    private Idioma idioma;

    private Double numeroDeDescargas;

    @ManyToOne()
    private Autor autor;


    public Libro() {}

    public Libro(DatosLibro datosLibro) {
        this.titulo = datosLibro.titulo();
        this.idioma = Idioma.fromString(datosLibro.idiomas().stream().limit(1).collect(Collectors.joining()));
        this.numeroDeDescargas = datosLibro.numeroDeDescargas();
    }

    //Setters
    public void setId(Long id) {
        this.id = id;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public void setIdioma(Idioma idioma) {
        this.idioma = idioma;
    }
    public void setNumeroDeDescargas(Double numeroDeDescargas) {
        this.numeroDeDescargas = numeroDeDescargas;
    }
    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    //Getters
    public Long getId() {
        return id;
    }
    public String getTitulo() {
        return titulo;
    }
    public Idioma getIdioma() {
        return idioma;
    }
    public Double getNumeroDeDescargas() {
        return numeroDeDescargas;
    }
    public Autor getAutor() {
        return autor;
    }

    @Override
    public String toString() {
        return "Libro{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", idioma=" + idioma +
                ", numeroDeDescargas=" + numeroDeDescargas +
                ", autor=" + autor +
                '}';
    }
}
