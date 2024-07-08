package com.aluracursos.literalura.modelos;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosAux(
        @JsonAlias("count") Integer total,
        @JsonAlias("results") List<DatosLibro> libros) {
}