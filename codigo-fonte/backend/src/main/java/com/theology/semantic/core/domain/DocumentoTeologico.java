package com.theology.semantic.core.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class DocumentoTeologico {

    private final UUID id;
    private final String titulo;
    private final String nomeArquivo;
    private final long tamanhoBytes;
    private DocumentStatus status;
    private String textoBrutoExtraido;
    private final List<TermoDoutrinario> termosDoutrinarios;
    private final LocalDateTime dataUpload;

    public DocumentoTeologico(String titulo, String nomeArquivo, long tamanhoBytes) {
        if (tamanhoBytes <= 0) {
            throw new IllegalArgumentException("Arquivo inválido. Tamanho deve ser maior que ZERO bytes.");
        }
        this.id = UUID.randomUUID();
        this.titulo = titulo;
        this.nomeArquivo = nomeArquivo;
        this.tamanhoBytes = tamanhoBytes;
        this.status = DocumentStatus.PENDING;
        this.termosDoutrinarios = new ArrayList<>();
        this.dataUpload = LocalDateTime.now();
    }

    public void registrarTextoExtraido(String textoBruto) {
        this.textoBrutoExtraido = textoBruto;
        this.status = DocumentStatus.ANALYZING_SEMANTICS;
    }

    public void adicionarTermo(TermoDoutrinario termo) {
        this.termosDoutrinarios.add(termo);
    }

    public void concluirAnalise() {
        this.status = DocumentStatus.COMPLETED;
    }

    public void falharAnalise() {
        this.status = DocumentStatus.FAILED;
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public long getTamanhoBytes() {
        return tamanhoBytes;
    }

    public DocumentStatus getStatus() {
        return status;
    }

    public String getTextoBrutoExtraido() {
        return textoBrutoExtraido;
    }

    public List<TermoDoutrinario> getTermosDoutrinarios() {
        return Collections.unmodifiableList(termosDoutrinarios);
    }

    public LocalDateTime getDataUpload() {
        return dataUpload;
    }
}
