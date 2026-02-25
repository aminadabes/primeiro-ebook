package com.theology.semantic.application.usecases;

// Inversão de Dependência: A camada de Aplicação diz O QUE o banco deve ter
// e a Camada de Infraestrutura será obrigada a implementar
import com.theology.semantic.core.domain.DocumentoTeologico;

public interface DocumentoRepositoryGateway {
    DocumentoTeologico salvar(DocumentoTeologico documento);

    DocumentoTeologico buscarPorId(java.util.UUID id);
}
