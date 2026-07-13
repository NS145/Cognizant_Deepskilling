# Exercise 5: Triggers

## Scenario 1
**Automatically update the last modified date when a customer's record is updated.**  
**Question:** Write a trigger `UpdateCustomerLastModified` that updates the `LastModified` column of the Customers table to the current date whenever a customer's record is updated.

### Solution
```sql
CREATE OR REPLACE TRIGGER UpdateCustomerLastModified
BEFORE UPDATE ON Customers
FOR EACH ROW
BEGIN
    :NEW.LastModified := SYSDATE;
END;
/
```

---

## Scenario 2
**Maintain an audit log for all transactions.**  
**Question:** Write a trigger `LogTransaction` that inserts a record into an AuditLog table whenever a transaction is inserted into the Transactions table.

### Solution
*Note: This solution assumes an `AuditLog` table and a corresponding sequence have been created.*
```sql
-- DDL for AuditLog if it doesn't exist:
-- CREATE TABLE AuditLog (LogID NUMBER PRIMARY KEY, TransactionID NUMBER, ActionDate DATE, Description VARCHAR2(255));
-- CREATE SEQUENCE AUDIT_SEQ START WITH 1;

CREATE OR REPLACE TRIGGER LogTransaction
AFTER INSERT ON Transactions
FOR EACH ROW
BEGIN
    INSERT INTO AuditLog (LogID, TransactionID, ActionDate, Description)
    VALUES (
        AUDIT_SEQ.NEXTVAL, 
        :NEW.TransactionID, 
        SYSDATE, 
        'New transaction of type ' || :NEW.TransactionType || ' processed for Account ' || :NEW.AccountID
    );
END;
/
```

---

## Scenario 3
**Enforce business rules on deposits and withdrawals.**  
**Question:** Write a trigger `CheckTransactionRules` that ensures withdrawals do not exceed the balance and deposits are positive before inserting a record into the Transactions table.

### Solution
```sql
CREATE OR REPLACE TRIGGER CheckTransactionRules
BEFORE INSERT ON Transactions
FOR EACH ROW
DECLARE
    v_balance NUMBER;
BEGIN
    IF :NEW.TransactionType = 'Withdrawal' THEN
        -- Fetch the current balance of the account
        SELECT Balance INTO v_balance FROM Accounts WHERE AccountID = :NEW.AccountID;
        
        IF :NEW.Amount > v_balance THEN
            RAISE_APPLICATION_ERROR(-20001, 'Error: Withdrawal amount exceeds current account balance.');
        END IF;
        
    ELSIF :NEW.TransactionType = 'Deposit' THEN
        IF :NEW.Amount <= 0 THEN
            RAISE_APPLICATION_ERROR(-20002, 'Error: Deposit amount must be a positive number.');
        END IF;
    END IF;
END;
/
```
