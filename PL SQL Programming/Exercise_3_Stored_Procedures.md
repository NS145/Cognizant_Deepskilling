# Exercise 3: Stored Procedures

## Scenario 1
**The bank needs to process monthly interest for all savings accounts.**  
**Question:** Write a stored procedure `ProcessMonthlyInterest` that calculates and updates the balance of all savings accounts by applying an interest rate of 1% to the current balance.

### Solution
```mysql
DELIMITER //

CREATE PROCEDURE ProcessMonthlyInterest()
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SELECT 'Error processing monthly interest.' AS ErrorMessage;
    END;

    START TRANSACTION;
    
    UPDATE Accounts
    SET Balance = Balance + (Balance * 0.01),
        LastModified = CURDATE()
    WHERE AccountType = 'Savings';
    
    COMMIT;
    SELECT 'Monthly interest processed successfully for savings accounts.' AS Result;
END //

DELIMITER ;
```

---

## Scenario 2
**The bank wants to implement a bonus scheme for employees based on their performance.**  
**Question:** Write a stored procedure `UpdateEmployeeBonus` that updates the salary of employees in a given department by adding a bonus percentage passed as a parameter.

### Solution
```mysql
DELIMITER //

CREATE PROCEDURE UpdateEmployeeBonus(
    IN p_department VARCHAR(50),
    IN p_bonus_percentage DECIMAL(5,2)
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SELECT 'Error applying employee bonus.' AS ErrorMessage;
    END;

    START TRANSACTION;
    
    UPDATE Employees
    SET Salary = Salary + (Salary * (p_bonus_percentage / 100))
    WHERE Department = p_department;
    
    IF ROW_COUNT() = 0 THEN
        SELECT CONCAT('No employees found in department: ', p_department) AS Message;
        ROLLBACK;
    ELSE
        COMMIT;
        SELECT CONCAT('Bonus applied successfully to department: ', p_department) AS Result;
    END IF;
END //

DELIMITER ;
```

---

## Scenario 3
**Customers should be able to transfer funds between their accounts.**  
**Question:** Write a stored procedure `TransferFunds` that transfers a specified amount from one account to another, checking that the source account has sufficient balance before making the transfer.

### Solution
```mysql
DELIMITER //

CREATE PROCEDURE TransferFunds(
    IN p_from_account INT,
    IN p_to_account INT,
    IN p_amount DECIMAL(15,2)
)
BEGIN
    DECLARE v_balance DECIMAL(15,2);
    DECLARE e_insufficient_funds CONDITION FOR SQLSTATE '45000';
    DECLARE e_invalid_account CONDITION FOR SQLSTATE '45001';
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SELECT 'An unexpected error occurred.' AS ErrorMessage;
    END;
    
    DECLARE EXIT HANDLER FOR e_insufficient_funds
    BEGIN
        ROLLBACK;
        SELECT 'Error: Insufficient funds in the source account.' AS ErrorMessage;
    END;

    DECLARE EXIT HANDLER FOR e_invalid_account
    BEGIN
        ROLLBACK;
        SELECT 'Error: Destination or source account does not exist.' AS ErrorMessage;
    END;

    START TRANSACTION;
    
    SELECT Balance INTO v_balance FROM Accounts WHERE AccountID = p_from_account FOR UPDATE;
    
    IF v_balance IS NULL THEN
        SIGNAL e_invalid_account;
    END IF;

    IF v_balance < p_amount THEN
        SIGNAL e_insufficient_funds;
    END IF;
    
    UPDATE Accounts SET Balance = Balance - p_amount WHERE AccountID = p_from_account;
    
    UPDATE Accounts SET Balance = Balance + p_amount WHERE AccountID = p_to_account;
    
    IF ROW_COUNT() = 0 THEN
        SIGNAL e_invalid_account;
    END IF;
    
    COMMIT;
    SELECT 'Funds transferred successfully.' AS Result;
END //

DELIMITER ;
```
