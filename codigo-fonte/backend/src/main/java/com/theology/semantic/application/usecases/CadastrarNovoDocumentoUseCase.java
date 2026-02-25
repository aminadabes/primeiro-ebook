package com.theology.semantic.application.usecases;

import com.theology.semantic.core.domain.DocumentoTeologico;
import java.io.InputStream;
import java.util.UUID;

// Na Clean Architecture, Use Cases definem "O QUE" a aplicação faz
public interface CadastrarNovoDocumentoUseCase {
    DocumentoTeologico executar(String titulo, String nomeArquivo, long tamanhoBytes, InputStream fileStream);
}
