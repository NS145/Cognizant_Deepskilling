# Exercise 7 - Financial Forecasting

## Objective

To implement a recursive algorithm that predicts future values based on a constant annual growth rate.

---

## Problem Statement

Financial institutions often estimate future investment values using historical growth rates. This exercise demonstrates how recursion can be used to calculate future values.

---

## Theory

### Recursion

Recursion is a programming technique in which a function calls itself until a base condition is reached.

Every recursive function consists of:

- Base Case
- Recursive Case

Example:

```
factorial(n) = n × factorial(n-1)
```

---

## FinancialForecast.java

```java
public class FinancialForecast {

    static double forecast(double currentValue, double growthRate, int years) {

        if (years == 0)
            return currentValue;

        return forecast(currentValue * (1 + growthRate), growthRate, years - 1);
    }

    public static void main(String[] args) {

        double investment = 100000;
        double annualGrowth = 0.10;
        int years = 5;

        double futureValue = forecast(investment, annualGrowth, years);

        System.out.printf("Future Value after %d years = ₹%.2f",
                years,
                futureValue);
    }
}
```

---

## Sample Output

```
Future Value after 5 years = ₹161051.00
```

---

## Dry Run

Initial Investment

```
100000
```

Year 1

```
100000 × 1.10 = 110000
```

Year 2

```
110000 × 1.10 = 121000
```

Year 3

```
121000 × 1.10 = 133100
```

Year 4

```
133100 × 1.10 = 146410
```

Year 5

```
146410 × 1.10 = 161051
```

---

## Time Complexity

Each recursive call computes one year.

```
Time Complexity  : O(n)

Space Complexity : O(n)
```

where **n** is the number of years.

---

## Optimization

The recursive solution creates a new function call for every year.

Possible optimizations include:

- Dynamic Programming (Memoization)
- Iterative approach using loops
- Mathematical formula for compound growth

The iterative solution reduces recursion overhead while maintaining **O(n)** time complexity.

---

## Advantages of Recursion

- Simple implementation
- Easy to understand
- Suitable for divide-and-conquer problems

---

## Disadvantages

- Additional memory due to recursion stack
- Slower than iteration for very large inputs
- Risk of Stack Overflow for deep recursion

---

## Conclusion

Recursion provides a simple way to model repeated financial growth. Although it is elegant and easy to understand, iterative methods or dynamic programming techniques are generally preferred for large inputs due to lower memory usage and better performance.