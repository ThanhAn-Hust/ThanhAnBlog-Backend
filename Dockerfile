# Stage 1: Build ứng dụng bằng Maven
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

# Copy toàn bộ source code vào container
COPY . .

# Cấp quyền thực thi cho Maven Wrapper và Build file JAR
RUN chmod +x ./mvnw
RUN ./mvnw clean package -DskipTests

# Stage 2: Môi trường chạy siêu nhẹ
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy file JAR đã được build từ Stage 1 sang
COPY --from=builder /app/target/*.jar app.jar

# Render sẽ dùng cổng biến môi trường PORT, nên chúng ta phơi ra cổng mặc định là 10000
EXPOSE 10000

# Chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]
