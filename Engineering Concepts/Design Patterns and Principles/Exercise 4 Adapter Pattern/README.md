# Exercise 4 - Adapter Pattern

## Objective

Implement the Adapter Pattern to integrate different payment gateways through a common interface.

---

## Theory

The Adapter Pattern is a Structural Design Pattern that converts one interface into another so incompatible classes can work together.

### Advantages

- Improves compatibility
- Promotes code reusability
- Loose coupling

---

## PaymentProcessor.java

```java
interface PaymentProcessor {
    void processPayment(double amount);
}
```

---

## Adaptee Classes

```java
class PayPalGateway {

    public void makePayment(double amount) {
        System.out.println("Paid ₹" + amount + " using PayPal");
    }
}

class StripeGateway {

    public void pay(double amount) {
        System.out.println("Paid ₹" + amount + " using Stripe");
    }
}
```

---

## Adapter Classes

```java
class PayPalAdapter implements PaymentProcessor {

    private PayPalGateway gateway = new PayPalGateway();

    public void processPayment(double amount) {
        gateway.makePayment(amount);
    }
}

class StripeAdapter implements PaymentProcessor {

    private StripeGateway gateway = new StripeGateway();

    public void processPayment(double amount) {
        gateway.pay(amount);
    }
}
```

---

## AdapterTest.java

```java
public class AdapterTest {

    public static void main(String[] args) {

        PaymentProcessor paypal = new PayPalAdapter();
        paypal.processPayment(5000);

        PaymentProcessor stripe = new StripeAdapter();
        stripe.processPayment(3000);

    }
}
```

---

## Sample Output

```
Paid ₹5000.0 using PayPal
Paid ₹3000.0 using Stripe
```

---

## Conclusion

The Adapter Pattern allows different payment gateways with incompatible interfaces to work through a common interface.