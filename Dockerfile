# Utiliser une image de base officielle Java
FROM eclipse-temurin:26-jre-alpine

# Définir le répertoire de travail
WORKDIR /app

# Copier le fichier JAR de l'application dans le conteneur
COPY target/app.jar app.jar

# Exposer le port sur lequel l'application va tourner
EXPOSE 8080

# Commande pour exécuter l'application
ENTRYPOINT ["java", "-jar", "app.jar"]