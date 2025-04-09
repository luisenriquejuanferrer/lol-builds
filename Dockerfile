# Etapa 1: Construcción de la aplicación
FROM openjdk:21-slim as build

# Instalar Maven
RUN apt-get update && apt-get install -y maven

# Definir el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el archivo pom.xml y los archivos fuente
COPY pom.xml .
COPY src ./src

# Ejecutar Maven para empaquetar el archivo JAR
RUN mvn clean package -DskipTests

# Etapa 2: Crear la imagen para ejecutar la aplicación
FROM openjdk:21-slim

# Definir el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el archivo JAR generado desde la etapa de construcción
COPY --from=build /app/target/leaguebuilds-0.0.1-SNAPSHOT.jar /app/leaguebuilds.jar

# Exponer el puerto 8080
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app/leaguebuilds.jar"]
