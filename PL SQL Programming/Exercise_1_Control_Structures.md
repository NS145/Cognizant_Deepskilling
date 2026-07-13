# Exercise 1: Control Structures

## Scenario 1
**The bank wants to apply a discount to loan interest rates for customers above 60 years old.**  
**Question:** Write a PL/SQL (now MySQL) block that loops through all customers, checks their age, and if they are above 60, apply a 1% discount to their current loan interest rates.

### Solution
*Note: MySQL does not support anonymous PL/SQL-style blocks. Loops must be placed inside a Stored Procedure.*

```mysql
DELIMITER //

CREATE PROCEDURE ApplyDiscountForSeniors()
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE v_loan_id INT;
    DECLARE v_dob DATE;
    DECLARE v_age INT;
    
    DECLARE cur CURSOR FOR 
        SELECT l.LoanID, c.DOB 
        FROM Customers c 
        JOIN Loans l ON c.CustomerID = l.CustomerID;
        
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
    
    OPEN cur;
    
    read_loop: LOOP
        FETCH cur INTO v_loan_id, v_dob;
        IF done THEN
            LEAVE read_loop;
        END IF;
        
        -- Calculate age in years
        SET v_age = TIMESTAMPDIFF(YEAR, v_dob, CURDATE());
        
        IF v_age > 60 THEN
            UPDATE Loans
            SET InterestRate = InterestRate - 1
            WHERE LoanID = v_loan_id;
        END IF;
    END LOOP;
    
    CLOSE cur;
    SELECT 'Interest rate discounts applied for eligible customers.' AS Result;
END //

DELIMITER ;
```

---

## Scenario 2
**A customer can be promoted to VIP status based on their balance.**  
**Question:** Write a PL/SQL (now MySQL) block that iterates through all customers and sets a flag `IsVIP` to `TRUE` for those with a balance over $10,000.

### Solution
*Note: The schema does not have an `IsVIP` column natively. You might want to run `ALTER TABLE Customers ADD IsVIP VARCHAR(5) DEFAULT 'FALSE';` prior to this block.*

```mysql
DELIMITER //

CREATE PROCEDURE PromoteToVIP()
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE v_customer_id INT;
    DECLARE v_balance DECIMAL(15,2);
    
    DECLARE cur CURSOR FOR SELECT CustomerID, Balance FROM Customers;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
    
    OPEN cur;
    
    read_loop: LOOP
        FETCH cur INTO v_customer_id, v_balance;
        IF done THEN
            LEAVE read_loop;
        END IF;
        
        IF v_balance > 10000 THEN
            UPDATE Customers
            SET IsVIP = 'TRUE'
            WHERE CustomerID = v_customer_id;
        END IF;
    END LOOP;
    
    CLOSE cur;
    SELECT 'VIP statuses updated based on customer balance.' AS Result;
END //

DELIMITER ;
```

---

## Scenario 3
**The bank wants to send reminders to customers whose loans are due within the next 30 days.**  
**Question:** Write a PL/SQL (now MySQL) block that fetches all loans due in the next 30 days and prints a reminder message for each customer.

### Solution
```mysql
DELIMITER //

CREATE PROCEDURE SendReminders()
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE v_name VARCHAR(100);
    DECLARE v_end_date DATE;
    
    DECLARE cur CURSOR FOR 
        SELECT c.Name, l.EndDate 
        FROM Customers c 
        JOIN Loans l ON c.CustomerID = l.CustomerID
        WHERE l.EndDate BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 30 DAY);
        
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
    
    OPEN cur;
    
    read_loop: LOOP
        FETCH cur INTO v_name, v_end_date;
        IF done THEN
            LEAVE read_loop;
        END IF;
        
        -- MySQL uses SELECT to output messages to the console/client
        SELECT CONCAT('Reminder: Dear ', v_name, ', your loan is due on ', v_end_date, '.') AS Message;
    END LOOP;
    
    CLOSE cur;
END //

DELIMITER ;
```
