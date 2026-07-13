# Exercise 1: Control Structures

## Scenario 1
**The bank wants to apply a discount to loan interest rates for customers above 60 years old.**  
**Question:** Write a PL/SQL block that loops through all customers, checks their age, and if they are above 60, apply a 1% discount to their current loan interest rates.

### Solution
```sql
DECLARE
    v_age NUMBER;
BEGIN
    FOR rec IN (
        SELECT c.CustomerID, c.DOB, l.LoanID, l.InterestRate 
        FROM Customers c 
        JOIN Loans l ON c.CustomerID = l.CustomerID
    ) LOOP
        -- Calculate age in years
        v_age := TRUNC(MONTHS_BETWEEN(SYSDATE, rec.DOB) / 12);
        
        IF v_age > 60 THEN
            UPDATE Loans
            SET InterestRate = InterestRate - 1
            WHERE LoanID = rec.LoanID;
        END IF;
    END LOOP;
    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Interest rate discounts applied for eligible customers.');
END;
/
```

---

## Scenario 2
**A customer can be promoted to VIP status based on their balance.**  
**Question:** Write a PL/SQL block that iterates through all customers and sets a flag `IsVIP` to `TRUE` for those with a balance over $10,000.

### Solution
*Note: The schema does not have an `IsVIP` column natively. You might want to run `ALTER TABLE Customers ADD IsVIP VARCHAR2(5) DEFAULT 'FALSE';` prior to this block.*

```sql
BEGIN
    FOR rec IN (SELECT CustomerID, Balance FROM Customers) LOOP
        IF rec.Balance > 10000 THEN
            -- Assuming the IsVIP column exists
            UPDATE Customers
            SET IsVIP = 'TRUE'
            WHERE CustomerID = rec.CustomerID;
        END IF;
    END LOOP;
    COMMIT;
    DBMS_OUTPUT.PUT_LINE('VIP statuses updated based on customer balance.');
END;
/
```

---

## Scenario 3
**The bank wants to send reminders to customers whose loans are due within the next 30 days.**  
**Question:** Write a PL/SQL block that fetches all loans due in the next 30 days and prints a reminder message for each customer.

### Solution
```sql
BEGIN
    FOR rec IN (
        SELECT c.Name, l.EndDate 
        FROM Customers c 
        JOIN Loans l ON c.CustomerID = l.CustomerID
        WHERE l.EndDate BETWEEN SYSDATE AND (SYSDATE + 30)
    ) LOOP
        DBMS_OUTPUT.PUT_LINE('Reminder: Dear ' || rec.Name || ', your loan is due on ' || TO_CHAR(rec.EndDate, 'YYYY-MM-DD') || '.');
    END LOOP;
END;
/
```
