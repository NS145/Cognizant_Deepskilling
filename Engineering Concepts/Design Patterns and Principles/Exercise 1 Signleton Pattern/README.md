# Exercise 1 - Singleton Pattern

## Objective

Implement the Singleton Design Pattern to ensure only one instance of the Logger class exists throughout the application.

---

## Theory

The Singleton Pattern is a Creational Design Pattern that restricts object creation to a single instance.

### Advantages

- Only one object is created.
- Global access point.
- Saves memory.
- Suitable for Logging, Configuration and Database Connections.

---

## Logger.java

```java
class Logger {

    private static Logger instance;

    private Logger() {}

    public static Logger getInstance() {

        if(instance == null)
            instance = new Logger();

        return instance;
    }

    public void log(String message) {
        System.out.println("LOG : " + message);
    }
}
```

---

## SingletonTest.java

```java
public class SingletonTest {

    public static void main(String[] args) {

        Logger logger1 = Logger.getInstance();
        Logger logger2 = Logger.getInstance();

        logger1.log("Application Started");

        System.out.println(logger1 == logger2);
    }
}
```

---

## Sample Output

```
LOG : Application Started
true
```

---

## Advantages

- Single instance
- Thread-safe with modifications
- Easy access

---

## Conclusion

Singleton Pattern ensures only one Logger object exists and is shared across the application.