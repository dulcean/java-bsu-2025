#!/bin/bash

set -e

BASE_DIR=backend/src/main
JAVA_DIR=$BASE_DIR/java/com/example/cookieclicker
RES_DIR=$BASE_DIR/resources

mkdir -p \
  $JAVA_DIR/controller \
  $JAVA_DIR/service \
  $JAVA_DIR/repository \
  $JAVA_DIR/model \
  $RES_DIR

cat > backend/pom.xml <<'EOF'
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>cookieclicker</artifactId>
    <version>0.0.1-SNAPSHOT</version>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.5.0</version>
    </parent>

    <properties>
        <java.version>17</java.version>
    </properties>

    <dependencies>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>

        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <scope>runtime</scope>
        </dependency>

    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>
EOF

cat > $JAVA_DIR/CookieClickerApplication.java <<'EOF'
package com.example.cookieclicker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CookieClickerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CookieClickerApplication.class, args);
    }
}
EOF

cat > $JAVA_DIR/model/CookieClick.java <<'EOF'
package com.example.cookieclicker.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cookie_clicks")
public class CookieClick {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime clickedAt;

    public CookieClick() {
        this.clickedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getClickedAt() {
        return clickedAt;
    }
}
EOF

cat > $JAVA_DIR/repository/CookieClickRepository.java <<'EOF'
package com.example.cookieclicker.repository;

import com.example.cookieclicker.model.CookieClick;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CookieClickRepository extends JpaRepository<CookieClick, Long> {
}
EOF

cat > $JAVA_DIR/service/CookieService.java <<'EOF'
package com.example.cookieclicker.service;

import com.example.cookieclicker.model.CookieClick;
import com.example.cookieclicker.repository.CookieClickRepository;
import org.springframework.stereotype.Service;

@Service
public class CookieService {

    private final CookieClickRepository repository;

    public CookieService(CookieClickRepository repository) {
        this.repository = repository;
    }

    public long registerClick() {
        repository.save(new CookieClick());
        return repository.count();
    }

    public long getCount() {
        return repository.count();
    }
}
EOF

cat > $JAVA_DIR/controller/CookieController.java <<'EOF'
package com.example.cookieclicker.controller;

import com.example.cookieclicker.service.CookieService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cookie")
@CrossOrigin(origins = "http://localhost")
public class CookieController {

    private final CookieService service;

    public CookieController(CookieService service) {
        this.service = service;
    }

    @PostMapping("/click")
    public long click() {
        return service.registerClick();
    }

    @GetMapping("/count")
    public long count() {
        return service.getCount();
    }
}
EOF

cat > $RES_DIR/application.yml <<'EOF'
spring:
  datasource:
    url: jdbc:postgresql://db:5432/cookie
    username: cookie
    password: cookie
    driver-class-name: org.postgresql.Driver

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
EOF

cat > backend/Dockerfile <<'EOF'
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
EOF

echo "Backend Cookie Clicker structure created successfully"
