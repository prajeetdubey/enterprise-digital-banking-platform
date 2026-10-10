# ---------- Stage 1: Build ----------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Pehle dependencies cache hoti hain (rebuild fast hota hai)
COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src ./src
RUN mvn -B clean package -DskipTests

# ---------- Stage 2: Run ----------
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Render PORT env variable deta hai, local pe 8080 use hoga
ENTRYPOINT ["sh", "-c", "java -XX:MaxRAMPercentage=75 -Dserver.port=${PORT:-8080} -jar app.jar"]