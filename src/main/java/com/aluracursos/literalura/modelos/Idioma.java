package com.aluracursos.literalura.modelos;

public enum Idioma {
    ESP("es"),
    ENG("en"),
    FRA("fr"),
    PTG("pt"),
    DET("de");

    String idioma;

    private Idioma(String idioma) {
        this.idioma = idioma;
    }

    public String getIdioma() {
        return idioma;
    }

    public static Idioma fromString(String text) {
        for (Idioma idioma : Idioma.values()) {
            if (idioma.idioma.equalsIgnoreCase(text)) {
                return idioma;
            }
        }
        throw new IllegalArgumentException("No se ha encontrado nada: " + text);
    }
}
