# Capítulos 10 e 11: Garantia de Qualidade e Nuvem

Chegamos à reta final da nossa plataforma de Análise Semântica Teológica. Código que funciona na máquina do desenvolvedor não tem valor até que esteja rodando na nuvem com garantias de que não vai quebrar.

## 10.1 Clean Code e Testes de Integração (Testcontainers)

Para testarmos se nossa aplicação consegue pegar um PDF, passar pelas regras de domínio e salvar o termo "Soteriologia" no Banco de Dados Vetorial, não podemos mockar o banco. Precisamos de um banco real!

A biblioteca **Testcontainers** revolucionou o mundo Java. Durante o comando `mvn test`, o Java acorda a engine do Docker na sua máquina, faz o pull de uma imagem efêmera do PostgreSQL com `pgvector`, sobe, roda todos os testes reais contra ela, e então destrói a imagem automaticamente. 

Você atinge 100% de confiança de que suas Queries SQL e sua Inteligência Artificial estão funcionando juntas antes mesmo de fazer o deploy.

## 11.1 Preparando para a Nuvem com Dockerfiles Independentes

No Capítulo 2 rodamos nosso banco localmente. Agora precisamos empacotar nossa aplicação para a Nuvem (AWS, Render, Heroku).

Para isso, teremos dois fluxos de empacotamento (`Dockerfiles`):

1. **Backend (Spring Boot)**: A compilação em duas etapas (*Multi-stage build*). Na primeira imagem, abrimos um Maven e compilamos o código fonte Java baixando as dependências. Em seguida, pegamos apensa o `.jar` final e colocamos numa imagem enxuta baseada na Alpine Linux. O contêiner final fica levíssimo (apenas com a JRE 21).
   
2. **Frontend (Angular)**: Fazemos a mesma coisa. O Node.js compila os componentes Standalone e gera arquivos estáticos (HTML, JS, CSS). Em seguida, copiamos esses arquivos para dentro de um servidor **Nginx** levíssimo. Reduzimos centenas de megabytes de pacotes npm em parcos KBs que são servidos na velocidade da luz para o navegador.

O deploy torna-se uma questão trivial: basta subir a imagem gerada para o provedor de Cloud e mapear as portas (8080 para a API Java, e 80 para o Nginx servindo o Angular). 

Com isso, entregamos o tão sonhado sistema teológico impulsionado por IA, seguro, testável e rápido, honrando a Clean Architecture.
