# Stage 1, build da aplicação
FROM maven:3.9-eclipse-temurin-21-alpine AS build

WORKDIR /app

# Copiar só o POM para o cache das dependências
COPY pom.xml .

# Fazer download das dependências
RUN mvn dependency:go-offline -B

# Copiar o código
COPY src ./src

# Compilar e gerar jar
RUN mvn package -DskipTests



# Stage 2, imagem final
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copiar o jar final
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Criar utilizador não root
RUN addgroup --system appgroup && adduser --system appuser --ingroup appgroup

USER appuser

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
