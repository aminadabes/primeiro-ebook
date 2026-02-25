# Capítulo 1: O Ecossistema em 2026

Ao decidir arquitetar uma plataforma para Análise Semântica Teológica, não podemos nos basear na mesma mentalidade de sistemas CRUD (Create, Read, Update, Delete) de 10 anos atrás. Estamos construindo um produto que envolve arquivos binários pesados (PDFs com centenas de páginas) e processamento de Inteligência Artificial para catalogar o contexto.

O fluxo é intenso: o usuário envia um documento de 50MB; o servidor extrai o texto bruto; a IA processa e entende o domínio (ex: separando o que é Soteriologia do que é Escatologia); o banco de dados armazena os vetores; e o frontend exibe o resultado para leitura limpa com os conceitos mastigados. Tudo de forma não-bloqueante.

É para este cenário que escolhemos nosso arsenal: **Java 21 com Spring Boot (3.x ou 4)** no backend, e **Angular 18+** no frontend. 

---

## 1. Por que Java 21+ com Spring Boot?

Java sempre foi o padrão ouro para aplicações Enterprise, mas sofria críticas por seu alto consumo de memória e inicialização lenta. Nos últimos anos, isso mudou radicalmente.

### Project Loom: Virtual Threads (A Mágica da Concorrência)
Antes do Java 21, o modelo tradicional da JVM utilizava **Threads de Sistema Operacional (Platform Threads)**. Cada requisição no Spring Boot atrelava uma Platform Thread a si. O problema? Elas custam caro (ocupam cerca de 1MB a 2MB de memória na stack e cobram o pedágio do context switch da CPU).
Quando lidamos com extração de PDF usando bibliotecas como o `PDFBox` ou requisições lentas de rede para LLMs (OpenAI/Ollama), o servidor antigo esgotava suas threads rápidas e caía, mesmo com a CPU a 10%.

Com o Project Loom (Java 21), introduziram-se as **Virtual Threads**.
- São gerenciadas pela própria JVM e não pelo Sistema Operacional.
- Você pode criar literalmente **milhões** delas simultaneamente no mesmo hardware que suportava apenas algumas centenas de threads de plataforma.
- Se a extração do PDF travar esperando o HD Ler o arquivo, a JVM inteligentemente congela a Virtual Thread, libera a Thread Real (Carrier Thread) para outra tarefa, e retoma quando pronta. Processar 50 uploads de PDF simultâneos tornou-se factível num hardware simples.

No Spring Boot, ativar esse superpoder consiste basicamente em inserir `spring.threads.virtual.enabled=true` no arquivo de propriedades da aplicação!

### JIT vs AOT (Ahead-of-Time Generation)
Além das threads, o Spring Boot moderno suporta a geração AOT e builds nativos via **GraalVM**. Isso permite que você compile a aplicação diretamente para o código de máquina do servidor de produção. O tempo de inicialização (startup) vai de "segundos" para "milissegundos", e o uso estático de RAM é drasticado.

---

## 2. Angular 18: A Morte do Zone.js e Ascensão dos Signals

No client layer, precisamos de um ambiente responsivo e com estado previsível. Os textos precisarão ser exibidos lado a lado com os resumos gerados pala IA, e o consumo de DOM é intenso.

Para essa missão, escalamos o Angular, que abandonou muito da complexidade do passado e focou em alta performance. Duas mudanças definem o Angular 18+:

### O Fim do Zone.js
O Angular clássico usava uma biblioteca de patches chamada `Zone.js`. Na prática, ela envelopava todo o navegador para dizer: *"Ei Angular, se o usuário clicar num lugar, ou um timer apitar, vá checar TODO A ÁRVORE da aplicação (Component Tree) pra ver se alguma variável mudou!"*.
Essa checagem sistêmica (Dirty Checking) é extremamente cara em painéis de leitura de texto (como a área de revisão teológica do nosso sistema).

### Signals: Reatividade Cirúrgica
Em substituição a esse modelo imperativo ou de observables excessivamente verbosos (RxJS), nasceram os **Signals**.
Um Signal diz ao framework exatamente de onde veio a mudança e que pedaço específico do HTML (DOM) ele deve repintar. 
```typescript
// Exemplo com Signal
readonly documentStatus = signal('Processando o PDF...');

// Quando o backend informar sucesso via SSE ou Polling:
this.documentStatus.set('Leitura Semântica Finalizada!');
```
Nessa simples atribuição `.set()`, o Angular não roda verificação na árvore global; ele vai *apenas* na \`div\` onde o status é exibido e atualiza o texto instantaneamente. 

### Standalone Components
Esqueça os velhos módulos (`NgModule`) centralizados onde importar uma dependência quebrava a aplicação inteira. Todo o nosso projeto é focado em Componentes Independentes (Standalone). O \`UploadComponent\` existirá por si só, puxando as dependências estritamente necessárias, trazendo *Lazy Loading* (carregamento sob demanda) para o cliente gratuitamente, o que garante a abertura rápida da aplicação para o Pastor ou Aluno, antes de consumirem grandes bytes pela internet.

---

No próximo capítulo, sairemos da teoria e sujaremos as mãos no Terminal e IDE. Vamos subir os containeres Docker (incluindo o PostgreSQL para a base de conhecimento) e estruturar nosso domínio teológico inicial!
