# Spring Core and Maven Hands-on Solutions

## Exercise 1: Configuring a Basic Spring Application

**`pom.xml` (Maven setup):**
```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" 
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.library</groupId>
    <artifactId>LibraryManagement</artifactId>
    <version>1.0-SNAPSHOT</version>

    <dependencies>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>5.3.20</version>
        </dependency>
    </dependencies>
</project>
```

**`src/main/resources/applicationContext.xml`:**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="bookRepository" class="com.library.repository.BookRepository"/>
    <bean id="bookService" class="com.library.service.BookService"/>
</beans>
```

**`BookRepository.java`:**
```java
package com.library.repository;

public class BookRepository {
    public void saveBook() {
        System.out.println("Book saved to repository.");
    }
}
```

**`BookService.java`:**
```java
package com.library.service;

import com.library.repository.BookRepository;

public class BookService {
    public void performService() {
        System.out.println("Service logic executed.");
    }
}
```

**`LibraryManagementApplication.java`:**
```java
package com.library;

import com.library.service.BookService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class LibraryManagementApplication {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        BookService service = (BookService) context.getBean("bookService");
        service.performService();
    }
}
```

---

## Exercise 2: Implementing Dependency Injection

**Updated `applicationContext.xml`:**
```xml
    <bean id="bookRepository" class="com.library.repository.BookRepository"/>
    <bean id="bookService" class="com.library.service.BookService">
        <property name="bookRepository" ref="bookRepository"/>
    </bean>
```

**Updated `BookService.java`:**
```java
package com.library.service;

import com.library.repository.BookRepository;

public class BookService {
    private BookRepository bookRepository;

    public void setBookRepository(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void performService() {
        System.out.println("Service logic executed.");
        bookRepository.saveBook();
    }
}
```

---

## Exercise 3: Implementing Logging with Spring AOP

**Updated `pom.xml`:**
```xml
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-aop</artifactId>
            <version>5.3.20</version>
        </dependency>
        <dependency>
            <groupId>org.aspectj</groupId>
            <artifactId>aspectjweaver</artifactId>
            <version>1.9.7</version>
        </dependency>
```

**`LoggingAspect.java`:**
```java
package com.library.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class LoggingAspect {
    @Around("execution(* com.library.service.*.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;
        System.out.println(joinPoint.getSignature() + " executed in " + executionTime + "ms");
        return proceed;
    }
}
```

**Updated `applicationContext.xml`:**
```xml
    <aop:aspectj-autoproxy />
    <bean id="loggingAspect" class="com.library.aspect.LoggingAspect"/>
```

---

## Exercise 4: Creating and Configuring a Maven Project

Adding the Maven Compiler Plugin to `pom.xml`:

```xml
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.8.1</version>
                <configuration>
                    <source>1.8</source>
                    <target>1.8</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
```
*Note: Include spring-webmvc if building a web application.*

---

## Exercise 5: Configuring the Spring IoC Container

*This repeats the core XML-based configuration from Exercises 1 and 2, solidifying the concepts of XML configuration, getter/setter dependencies, and testing it via `ClassPathXmlApplicationContext`.*

---

## Exercise 6: Configuring Beans with Annotations

**Updated `applicationContext.xml`:**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="
       http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd">

    <context:component-scan base-package="com.library" />
</beans>
```

**Updated Classes:**
```java
import org.springframework.stereotype.Repository;
@Repository
public class BookRepository { ... }

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;
    // ...
}
```

---

## Exercise 7: Implementing Constructor and Setter Injection

**XML Configuration Example for both:**
```xml
    <!-- Constructor Injection -->
    <bean id="bookServiceConstructor" class="com.library.service.BookService">
        <constructor-arg ref="bookRepository"/>
    </bean>

    <!-- Setter Injection -->
    <bean id="bookServiceSetter" class="com.library.service.BookService">
        <property name="bookRepository" ref="bookRepository"/>
    </bean>
```

---

## Exercise 8: Implementing Basic AOP with Spring

**`LoggingAspect.java` (Before and After Advice):**
```java
package com.library.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.After;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    @Before("execution(* com.library.service.*.*(..))")
    public void logBefore() {
        System.out.println("Before method execution...");
    }

    @After("execution(* com.library.service.*.*(..))")
    public void logAfter() {
        System.out.println("After method execution...");
    }
}
```
*(Ensure `@EnableAspectJAutoProxy` is used if converting entirely to JavaConfig).*

---

## Exercise 9: Creating a Spring Boot Application

**`application.properties`:**
```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.h2.console.enabled=true
```

**`Book.java`:**
```java
package com.library.model;
import javax.persistence.*;

@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    
    // Getters and Setters
}
```

**`BookRepository.java`:**
```java
package com.library.repository;
import com.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
```

**`BookController.java`:**
```java
package com.library.controller;

import com.library.model.Book;
import com.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookRepository repository;

    @GetMapping
    public List<Book> getAllBooks() {
        return repository.findAll();
    }

    @PostMapping
    public Book createBook(@RequestBody Book book) {
        return repository.save(book);
    }
}
```
