# Spring REST Hands-on 1: Spring Core Concepts

## Objectives
- Demonstrate creation of a Spring Boot Application
- Demonstrate loading a bean from a spring configuration file
- Demonstrate inclusion of logging in a Spring Boot Application

## Hands-on 1: Create a Spring Web Project using Maven
1. Go to [start.spring.io](https://start.spring.io/).
2. **Group**: `com.cognizant`
3. **Artifact Id**: `spring-learn`
4. **Dependencies**: Spring Boot DevTools, Spring Web.
5. Generate the project and extract it.
6. Import into Eclipse as an Existing Maven Project.
7. Run the `SpringLearnApplication` class to verify it works.

## Hands-on 2: Load SimpleDateFormat from Spring Configuration XML
Create `date-format.xml` in `src/main/resources`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="dateFormat" class="java.text.SimpleDateFormat">
        <constructor-arg value="dd/MM/yyyy" />
    </bean>

</beans>
```

Add a method `displayDate()` in `SpringLearnApplication.java`:

```java
public void displayDate() {
    ApplicationContext context = new ClassPathXmlApplicationContext("date-format.xml");
    SimpleDateFormat format = context.getBean("dateFormat", SimpleDateFormat.class);
    // Use format to parse a date
}
```

## Hands-on 3: Incorporate Logging
Create/Update `application.properties` in `src/main/resources`:

```properties
logging.level.org.springframework=info
logging.level.com.cognizant.springlearn=debug
logging.pattern.console=%d{yyMMdd}|%d{HH:mm:ss.SSS}|%-20.20thread|%5p|%-25.25logger{25}|%25M|%m%n
```

Include logging in `SpringLearnApplication.java`:

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpringLearnApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringLearnApplication.class);

    public void displayDate() {
        LOGGER.info("START");
        // implementation...
        LOGGER.info("END");
    }
}
```

## Hands-on 4: Load Country from Spring Configuration XML
Create `country.xml` in `src/main/resources`:

```xml
<bean id="country" class="com.cognizant.springlearn.Country">
    <property name="code" value="IN" />
    <property name="name" value="India" />
</bean>
```

Create `Country.java`:

```java
public class Country {
    private String code;
    private String name;

    public Country() {
        // debug log "Inside Country Constructor"
    }

    // Getters, setters, and toString() with appropriate debug logs
}
```

Add `displayCountry()` method in `SpringLearnApplication.java`:

```java
ApplicationContext context = new ClassPathXmlApplicationContext("country.xml");
Country country = (Country) context.getBean("country", Country.class);
LOGGER.debug("Country : {}", country.toString());
```

## Hands-on 5: Singleton and Prototype Scopes
To demonstrate prototype scope, update the bean definition in `country.xml`:

```xml
<bean id="country" class="com.cognizant.springlearn.Country" scope="prototype">
```

Requesting the bean twice will instantiate it twice, invoking the constructor twice.

## Hands-on 6: Load list of countries
Define an ArrayList of countries in `country.xml`:

```xml
<bean id="countryList" class="java.util.ArrayList">
    <constructor-arg>
        <list>
            <ref bean="in"></ref>
            <ref bean="us"></ref>
            <ref bean="de"></ref>
            <ref bean="jp"></ref>
        </list>
    </constructor-arg>
</bean>
```

Load and display the list using `context.getBean("countryList", ArrayList.class)`.
