# Capítulo 2: Configurando o Ambiente com Docker e Banco de Dados

Um dos maiores desafios no desenvolvimento de software não é escrever o código, é garantir que o código que roda na máquina do desenvolvedor rode de forma idêntica no servidor de produção. Como acadêmicos e pastores frequentemente não são desenvolvedores com ambientes complexos, a base da nossa Análise Semântica será **100% conteinerizada**.

Neste capítulo vamos fugir do inferno de dependências construindo nossa infraestrutura usando **Docker**.

---

## 2.1 Por que Dockerizar o Banco de Dados?

No passado, instalar um banco PostgreSQL direto no seu Sistema Operacional significava abrir caminhos para conflitos de bibliotecas. E pior: nosso classificador usa **Spring AI**, o que exige um Banco de Dados Vetorial (Vector Database).

A extensão `pgvector` do PostgreSQL é o padrão para armazenar **Embeddings**. Um "Embedding" nada mais é do que a representação matemática (em um array gigante de números) dos termos doutrinários.

Para ter isso localmente com zero dor de cabeça, não instalamos nada. Rodamos a imagem oficial pré-configurada da Ankane!

## 2.2 O Arquivo Maestro: docker-compose.yml

Na raiz do nosso projeto, criaremos um arquivo chamado `docker-compose.yml`. Ele é um descritivo arquitetural da nossa infra!

Ele conterá:
- Um serviço de Banco de Dados (`postgres` com a citada extensão `pgvector`).
- Uma interface visual (`pgadmin`) para vermos os dados da nossa aplicação fluindo sem precisar de terminal.
- (Opcional, de acordo com o ambiente local): Containers isolados rodando o Java/Maven local e Node/Angular para isolar completamente o ambiente de versões da máquina local.

```yaml
version: '3.8'

services:
  theology-db:
    image: ankane/pgvector:latest
    container_name: semantic-pg
    environment:
      POSTGRES_USER: admin
      POSTGRES_PASSWORD: password
      POSTGRES_DB: semantic_theology
    ports:
      - "5432:5432"
    volumes:
      - pgdata:/var/lib/postgresql/data

  theology-db-admin:
    image: dpage/pgadmin4
    container_name: semantic-pgadmin
    environment:
      PGADMIN_DEFAULT_EMAIL: admin@teologia.com
      PGADMIN_DEFAULT_PASSWORD: admin
    ports:
      - "5050:80"

volumes:
  pgdata:
```

Ao executar um simples comando mágico `docker-compose up -d`, tanto nossa base relacional capaz de lidar com IA quanto a interface gráfica gerencial se levantam sozinhas!

## 2.3 Solucionando o Paradoxo das Versões

Na **Fase 1**, mencionamos que para usar Angular 18 precisaríamos de Node 18+ (ou 20+), e para nosso Backend super rápido com Virtual Threads, precisaremos de um JDK (Java Development Kit) rodando na versão 21 ou mais recente.

Para evitar que você perca uma tarde configurando ferramentas `SDKMAN` ou NVM no seu sistema operacional, adotaremos o uso do próprio Docker para orquestrar o esqueleto da sua aplicação. Essa é uma abordagem profissional que blinda projetos de estilhaços.

### O Scaffolding Angular e Spring via Container

Para gerar nossos esqueletos sem instalar nada na máquina nativa:

- Para o **Angular**, invocamos uma imagem Docker imaculada do *Node* que irá gerar a pasta "Frontend" mapeando a porta 4200.
- Para o **Spring Boot**, utilizaremos imagem do *Maven* para baixar diretamente do Spring Initializr.

---
No próximo passo da nossa jornada interativa, a aplicação tomará forma! Iremos de fato instanciar estas tecnologias em disco, iniciar o nosso banco de dados relacional (já equipado com as extensões para IA) e dar os primeiros passos nas Entidades do Sistema!
