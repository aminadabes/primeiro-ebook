package com.theology.semantic.core.domain;

import java.util.UUID;

public class TermoDoutrinario {

    private final UUID id;
    private final String palavra;
    private final String categoriaTeologica; // ex: Soteriologia
    private final double pesoRelevancia;

    public TermoDoutrinario(String palavra, String categoriaTeologica, double pesoRelevancia) {
        if (palavra == null || palavra.isBlank()) {
            throw new IllegalArgumentException("O termo não pode ser vazio.");
        }
        this.id = UUID.randomUUID();
        this.palavra = palavra;
        this.categoriaTeologica = categoriaTeologica;
        this.pesoRelevancia = pesoRelevancia;
    }

    public UUID getId() {
        return id;
    }

    public String getPalavra() {
        return palavra;
    }

    public String getCategoriaTeologica() {
        return categoriaTeologica;
    }

    public double getPesoRelevancia() {
        return pesoRelevancia;
    }
}
