# Stage 1: Build ứng dụng bằng Maven
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

# Copy toàn bộ source code vào container
COPY . .

# Tạo file application.yml từ biến môi trường (không cần đưa lên Github)
RUN cat > src/main/resources/application.yml <<'ENDOFYML'
spring:
  datasource:
    url: ${DB_URL}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
    properties:
      hibernate:
        format_sql: false
        dialect: org.hibernate.dialect.PostgreSQLDialect
  sql:
    init:
      mode: never
  data:
    redis:
      host: ${REDIS_HOST}
      port: ${REDIS_PORT}
      password: ${REDIS_PASSWORD}
      ssl:
        enabled: true
  mail:
    host: smtp.gmail.com
    port: 587
    username: ${MAIL_USERNAME}
    password: ${MAIL_PASSWORD}
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
jwt:
  secret: ${JWT_SECRET}
  expiration: ${JWT_EXPIRATION:86400000}
server:
  port: ${PORT:8080}
ENDOFYML

# Cấp quyền thực thi cho Maven Wrapper và Build file JAR
RUN chmod +x ./mvnw
RUN ./mvnw clean package -DskipTests

# Stage 2: Môi trường chạy siêu nhẹ
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy file JAR đã được build từ Stage 1 sang
COPY --from=builder /app/target/*.jar app.jar

# Khai báo ENV PORT mặc định
ENV PORT=10000
EXPOSE 10000

# Chạy ứng dụng với giới hạn RAM (Free tier Render chỉ có 512MB) và ép cổng theo Render
ENTRYPOINT ["sh", "-c", "java -Xmx300m -jar app.jar --server.port=${PORT}"]
