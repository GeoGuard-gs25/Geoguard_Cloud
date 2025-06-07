# Etapa de build
FROM maven:3.8.5-openjdk-17-slim AS build

WORKDIR /app

# Copia os arquivos do projeto para o container
COPY . .

# Compila e empacota a aplicação (sem rodar os testes)
RUN mvn clean package -DskipTests

# Etapa de runtime
FROM eclipse-temurin:17-jre-jammy

# Cria um usuário não-root para segurança
RUN useradd -m appuser

WORKDIR /home/appuser

# Copia o JAR gerado na etapa de build (ajuste o nome do arquivo JAR conforme seu pom.xml)
COPY --from=build /app/target/geoguard-0.0.1-SNAPSHOT.jar app.jar

# Define o usuário não-root
USER appuser

# Exponha a porta usada pela aplicação Spring Boot (por padrão é 8080)
EXPOSE 8080

# Comando para executar a aplicação
CMD ["java", "-jar", "app.jar"]
