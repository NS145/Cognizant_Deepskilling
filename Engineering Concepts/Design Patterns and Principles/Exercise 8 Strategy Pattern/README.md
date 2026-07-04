# Exercise 8 - Strategy Pattern

## Objective

Implement the Strategy Pattern to allow selecting different payment methods at runtime.

---

## Theory

The Strategy Pattern is a Behavioral Design Pattern that enables selecting an algorithm's behavior at runtime.

### Advantages

- Easy to switch algorithms
- Promotes Open/Closed Principle
- Reduces conditional statements

---

## PaymentStrategy.java

```java
interface PaymentStrategy {
    void pay(double amount);
}
```

---

## CreditCardPayment.java

```java
class CreditCardPayment implements PaymentStrategy {

    public void pay(double amount) {
        System.out.println("Paid ₹" + amount + " using Credit Card");
    }

}
```

---

## PayPalPayment.java

```java
class PayPalPayment implements PaymentStrategy {

    public void pay(double amount) {
        System.out.println("Paid ₹" + amount + " using PayPal");
    }

}
```

---

## PaymentContext.java

```java
class PaymentContext {

    private PaymentStrategy strategy;

    public void setStrategy(PaymentStrategy strategy) {
        this.strategy = strategy;
    }

    public void makePayment(double amount) {
        strategy.pay(amount);
    }

}
```

---

## StrategyTest.java

```java
public class StrategyTest {

    public static void main(String[] args) {

        PaymentContext context = new PaymentContext();

        context.setStrategy(new CreditCardPayment());
        context.makePayment(5000);

        context.setStrategy(new PayPalPayment());
        context.makePayment(3500);

    }

}
```

---

## Sample Output

```
Paid ₹5000.0 using Credit Card
Paid ₹3500.0 using PayPal
```

---

## Conclusion

The Strategy Pattern enables selecting payment methods dynamically without modifying the client code.