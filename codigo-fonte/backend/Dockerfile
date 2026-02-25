# Estágio 1: Build da aplicação usando Maven e Java 21
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Compila e empacota ignorando testes para o build
RUN ./mvnw clean package -DskipTests || mvn clean package -DskipTests

# Estágio 2: Cria a imagem final otimizada apenas com o JRE
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# Copia o artefato .jar gerado no builder
COPY --from=builder /app/target/semantic-api-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
