# Exercise 5 - Decorator Pattern

## Objective

Implement the Decorator Pattern to dynamically add notification channels.

---

## Theory

Decorator Pattern adds new functionality to objects dynamically without modifying existing code.

---

## Notifier.java

```java
interface Notifier {
    void send(String message);
}
```

---

## EmailNotifier.java

```java
class EmailNotifier implements Notifier {

    public void send(String message) {
        System.out.println("Email : " + message);
    }

}
```

---

## NotifierDecorator.java

```java
abstract class NotifierDecorator implements Notifier {

    protected Notifier notifier;

    public NotifierDecorator(Notifier notifier) {
        this.notifier = notifier;
    }

}
```

---

## SMSNotifierDecorator.java

```java
class SMSNotifierDecorator extends NotifierDecorator {

    public SMSNotifierDecorator(Notifier notifier) {
        super(notifier);
    }

    public void send(String message) {

        notifier.send(message);
        System.out.println("SMS : " + message);

    }

}
```

---

## SlackNotifierDecorator.java

```java
class SlackNotifierDecorator extends NotifierDecorator {

    public SlackNotifierDecorator(Notifier notifier) {
        super(notifier);
    }

    public void send(String message) {

        notifier.send(message);
        System.out.println("Slack : " + message);

    }

}
```

---

## DecoratorTest.java

```java
public class DecoratorTest {

    public static void main(String[] args) {

        Notifier notifier =
                new SlackNotifierDecorator(
                        new SMSNotifierDecorator(
                                new EmailNotifier()));

        notifier.send("Server Down");

    }
}
```

---

## Output

```
Email : Server Down
SMS : Server Down
Slack : Server Down
```

---

## Conclusion

Decorator Pattern dynamically extends object behavior without changing existing classes.