# Capítulo 3 e 4: Fortificando o Backend (DDD e Clean Architecture)

Quando desenvolvemos um software para um domínio complexo (como classificar se um texto sobre "Arrebatamento" é *Pré-Tribulacionista* ou *Pós-Tribulacionista*), o Framework Web (Spring Boot) ou a Camada de Banco (JPA) deveriam ser os **últimos** lugares com os quais gastamos nossa energia inicial.

## 3.1 Domain-Driven Design (DDD) na Prática

Ao invés de criar `Tabelas` no banco, primeiro criamos o **Coração da Aplicação**. No DDD, chamamos isso de modelo de Domínio.

O Domínio precisa ser rico, ou seja, suas classes não podem ser apenas "sacos de getters e setters". Elas precisam validar suas próprias regras.

### Nossos Agregados e Entidades de Domínio
Vamos começar mapeando nosso conceito principal: o `DocumentoTeologico`. Ele possui `Identidade` (um ID único) e um ciclo de vida: inicialmente ele é "Enviado", depois "Extraído", em seguida "Analisado pela IA", e finalmente "Concluído".

Também possuímos *Value Objects* (Objetos de Valor), como o `TermoDoutrinario`, que carrega uma palavra e sua relevância, mas não tem identidade própria sem um PDF pai.

## 4.1 Implementando Clean Architecture no Spring

Para blindarmos esse coração contra "invasões" do Spring Boot, usamos a *Clean Architecture* (ou Arquitetura Hexagonal, no contexto de Ports and Adapters). 

A regra de ouro é: **A Dependência sempre aponta para o Centro**.

Isso significa que o núcleo (`domain`) **nunca, jamais** deve importar pacotes como `org.springframework.web` ou `javax.persistence`. 

### A Estrutura de Pacotes

Iremos dividir nossa raiz `com.theology.semantic` em 4 anéis concêntricos:

1. **`core/domain`**: O centro absoluto. Contém nossas entidades de negócio (POJOs puros Java 21). Aqui residem as lógicas como "Validar se um PDF tem mais de zero páginas".
2. **`application/usecases`**: A orquestração. Contém as interfaces dos nossos serviços, que ditarão: "Pegue um PDF enviado, extraia o texto, peça à IA os embeddings, salve no banco". O UseCase sabe *o que* fazer, mas não sabe *como* a IA ou o banco fazem.
3. **`infrastructure/adapters`**: A borda extrema da nossa aplicação, que conecta seu software ao mundo real. Aqui ficam:
   * **Persistence**: Classes que implementam as interfaces do UseCase e comunicam com o Spring Data JPA (PostgreSQL). Aqui sim usamos as anotações `@Entity` e `@Table`.
   * **AI**: O cliente do Spring AI / Ollama.
4. **`web/api`**: Os Controllers REST que recebem chamadas HTTP do Angular 18 e delegam rapidamente para as interfaces da camada de Application. 

Nas próximas implementações Java que faremos, você notará a total ausência de anotações mágicas do Hibernate nas classes do pacote `domain`. Nós mapearemos nossas Entidades de Negócio para Entidades de Banco na fronteira arquitetural (`infrastructure`) através de mappers!

Isso torna seu projeto altamente testável e focado.
