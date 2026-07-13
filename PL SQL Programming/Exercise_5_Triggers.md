# Exercise 5: Triggers

## Scenario 1
**Automatically update the last modified date when a customer's record is updated.**  
**Question:** Write a trigger `UpdateCustomerLastModified` that updates the `LastModified` column of the Customers table to the current date whenever a customer's record is updated.

### Solution
```mysql
DELIMITER //

CREATE TRIGGER UpdateCustomerLastModified
BEFORE UPDATE ON Customers
FOR EACH ROW
BEGIN
    SET NEW.LastModified = CURDATE();
END //

DELIMITER ;
```

---

## Scenario 2
**Maintain an audit log for all transactions.**  
**Question:** Write a trigger `LogTransaction` that inserts a record into an AuditLog table whenever a transaction is inserted into the Transactions table.

### Solution
*Note: This assumes the `AuditLog` table has an auto-incrementing ID column for the Primary Key, standard in MySQL.*
```mysql
-- Assuming DDL:
-- CREATE TABLE AuditLog (LogID INT AUTO_INCREMENT PRIMARY KEY, TransactionID INT, ActionDate DATE, Description VARCHAR(255));

DELIMITER //

CREATE TRIGGER LogTransaction
AFTER INSERT ON Transactions
FOR EACH ROW
BEGIN
    INSERT INTO AuditLog (TransactionID, ActionDate, Description)
    VALUES (
        NEW.TransactionID, 
        CURDATE(), 
        CONCAT('New transaction of type ', NEW.TransactionType, ' processed for Account ', NEW.AccountID)
    );
END //

DELIMITER ;
```

---

## Scenario 3
**Enforce business rules on deposits and withdrawals.**  
**Question:** Write a trigger `CheckTransactionRules` that ensures withdrawals do not exceed the balance and deposits are positive before inserting a record into the Transactions table.

### Solution
```mysql
DELIMITER //

CREATE TRIGGER CheckTransactionRules
BEFORE INSERT ON Transactions
FOR EACH ROW
BEGIN
    DECLARE v_balance DECIMAL(15,2);
    
    IF NEW.TransactionType = 'Withdrawal' THEN
        SELECT Balance INTO v_balance FROM Accounts WHERE AccountID = NEW.AccountID;
        
        IF NEW.Amount > v_balance THEN
            -- Using SIGNAL to throw a custom error in MySQL
            SIGNAL SQLSTATE '45000' 
            SET MESSAGE_TEXT = 'Error: Withdrawal amount exceeds current account balance.';
        END IF;
        
    ELSEIF NEW.TransactionType = 'Deposit' THEN
        IF NEW.Amount <= 0 THEN
            SIGNAL SQLSTATE '45000' 
            SET MESSAGE_TEXT = 'Error: Deposit amount must be a positive number.';
        END IF;
    END IF;
END //

DELIMITER ;
```
