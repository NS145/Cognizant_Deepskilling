# Microservices with Spring Boot 3 & Spring Cloud - Hands-On Solutions

This document consolidates solutions for various Microservices exercises including authentication, routing, service discovery, load balancing, and resilience.

## 1. Centralized Authentication & SSO

**Exercise: Implementing OAuth 2.1 / OIDC and Resource Server with JWT**
```xml
<!-- pom.xml Dependencies -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-client</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt</artifactId>
    <version>0.9.1</version>
</dependency>
```

**`application.yml` (OAuth2 & JWT Configuration)**
```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          google:
            client-id: YOUR_CLIENT_ID
            client-secret: YOUR_CLIENT_SECRET
            scope: openid, profile, email
      resourceserver:
        jwt:
          issuer-uri: https://accounts.google.com
    jwt:
      secret: YOUR_CUSTOM_SECRET_KEY
```

**Security Configuration**
```java
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/public").permitAll()
                .anyRequest().authenticated()
                .and()
            .oauth2Login() // For OAuth2 Client (SSO)
                .and()
            .oauth2ResourceServer() // For Resource Server Validation
                .jwt();
    }
}
```

---

## 2. Service Discovery (Eureka Server & Clients)

**Eureka Server Application**
```java
// pom.xml requires: spring-cloud-starter-netflix-eureka-server
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer // Enables Eureka Discovery Server
public class EurekaServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }
}
```
**`application.properties` (Eureka Server)**
```properties
server.port=8761
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
```

**Eureka Client Application (e.g., Account Service)**
```java
// pom.xml requires: spring-cloud-starter-netflix-eureka-client
@SpringBootApplication
@EnableDiscoveryClient
public class AccountServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AccountServiceApplication.class, args);
    }
}
```
**`application.properties` (Account Service)**
```properties
spring.application.name=account-service
server.port=8080
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
```

---

## 3. API Gateway, Load Balancing & Resilience

**Spring Cloud Gateway Configuration**
```yaml
# application.yml for API Gateway
server:
  port: 9090
spring:
  application:
    name: api-gateway
  cloud:
    gateway:
      discovery:
        locator:
          enabled: true
          lower-case-service-id: true
      routes:
        - id: account-service-route
          uri: lb://account-service # lb:// signifies load balancing using Eureka registry
          predicates:
            - Path=/accounts/**
        - id: loan-service-route
          uri: lb://loan-service
          predicates:
            - Path=/loans/**
```

**Global Logging Filter (API Gateway)**
```java
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class LoggingFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("Incoming Request to API Gateway: " + exchange.getRequest().getURI());
        return chain.filter(exchange);
    }
}
```

**Resilience4j Circuit Breaker Setup**
```xml
<!-- pom.xml requires: resilience4j-spring-boot2, spring-cloud-starter-circuitbreaker-reactor-resilience4j -->
```
```yaml
# application.yml Configuration for Circuit Breaker
resilience4j:
  circuitbreaker:
    instances:
      inventoryServiceBreaker:
        registerHealthIndicator: true
        slidingWindowSize: 10
        failureRateThreshold: 50
        waitDurationInOpenState: 10000ms
```

**Using Circuit Breaker with Fallback (WebClient)**
```java
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    
    @CircuitBreaker(name = "inventoryServiceBreaker", fallbackMethod = "fallbackInventory")
    public String checkInventory(String productId) {
        // Assume WebClient calls Inventory Service
        return webClient.get().uri("http://inventory-service/inventory/" + productId)
                        .retrieve().bodyToMono(String.class).block();
    }
    
    public String fallbackInventory(String productId, Throwable t) {
        return "Inventory Service is currently unavailable. Fallback response for product: " + productId;
    }
}
```

---

## 4. Example REST Services

**Account Controller**
```java
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @GetMapping("/{number}")
    public Map<String, Object> getAccount(@PathVariable String number) {
        return Map.of(
            "number", number,
            "type", "savings",
            "balance", 234343
        );
    }
}
```

**Loan Controller (Runs on Port 8081)**
```java
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/loans")
public class LoanController {

    @GetMapping("/{number}")
    public Map<String, Object> getLoan(@PathVariable String number) {
        return Map.of(
            "number", number,
            "type", "car",
            "loan", 400000,
            "emi", 3258,
            "tenure", 18
        );
    }
}
```
