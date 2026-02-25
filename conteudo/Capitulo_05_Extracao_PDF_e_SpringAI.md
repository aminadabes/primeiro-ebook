# Capítulo 5: Integrando Extração de PDF e Inteligência Artificial

Até agora temos o coração da aplicação rodando com regras de negócio blindadas. Mas a nossa aplicação tem um objetivo muito prático: ler PDFs de Teologia e extrair contexto usando IA.

## 5.1 O Desafio da Extração (PDFBox)

O formato PDF não é feito para ser "lido" facilmente por máquinas. Ele é uma representação de *impressão*. Ao tentar puxar texto dele, você frequentemente descobre que a formatação e as quebras de linha viram lixo digital.

Para resolver isso, usamos a biblioteca **Apache PDFBox**. 
Mas atenção à Arquitetura Limpa: o nosso `DocumentoTeologico` (no pacote Core) não sabe o que é o PDFBox! Ele só sabe que existe um `ExtratorPdfGateway`.
Quem implementa o PDFBox é um **Adapter** na camada de Infraestrutura.

```java
// Exemplo real do Adapter de PDF (infraestrutura)
public class ApachePdfBoxAdapter implements ExtratorPdfGateway {
    @Override
    public String extrairTexto(InputStream file) {
        try (PDDocument document = PDDocument.load(file)) {
            PDFTextStripper stripper = new PDFTextStripper();
            // ... limpeza de stopwords ...
            return stripper.getText(document);
        }
    }
}
```

## 5.2 O Grande Salto: Spring AI e Semântica

O que diferencia a nossa ferramenta de 2026 de um leitor de 2016 é que não buscamos mais *apenas* a string "Soteriologia" usando `Regex` ou `LIKE` no banco. Nós enviamos blocos do texto para uma IA (ex: Ollama rodando localmente, ou OpenAI) pedindo que ela entenda o contexto doutrinário.

### Spring AI: O Padrão para IAs na JVM
O projeto **Spring AI** foi criado para tornar o uso de Modelos de Linguagem (LLMs) tão fácil quanto usar Bancos de Dados via JPA.

Para usá-lo, adicionamos a dependência (`spring-ai-openai-spring-boot-starter`) e implementamos nosso `AnalisadorSemanticoGateway`. 
Em poucas linhas, injetamos o `AiClient`, criamos um Prompt de Sistema (System Prompt) declarando:
*"Você é um teólogo com PhD. Leia o seguinte texto extraído e liste em formato JSON os principais conceitos doutrinários abordados, como Escatologia, Pneumatologia, etc, e atribua um peso de 0.0 a 1.0."*

A IA retorna os conceitos já formatados, e o nosso código Java simplesmente converte os valores de volta para nossa Entidade Rica (`TermoDoutrinario`), persistindo tudo no nosso Vector Database local!

Assim, quando o usuário final (`Client Layer` / Angular) acessar o Dashboard, ele verá os PDFs classificados magicamente, superando a frustração de procuras comuns (pesquisas léxicas).
