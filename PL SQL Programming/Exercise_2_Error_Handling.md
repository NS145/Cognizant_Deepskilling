# Exercise 2: Error Handling

## Scenario 1
**Handle exceptions during fund transfers between accounts.**  
**Question:** Write a stored procedure `SafeTransferFunds` that transfers funds between two accounts. Ensure that if any error occurs (e.g., insufficient funds), an appropriate error message is logged and the transaction is rolled back.

### Solution
```mysql
DELIMITER //

CREATE PROCEDURE SafeTransferFunds(
    IN p_from_account INT,
    IN p_to_account INT,
    IN p_amount DECIMAL(15,2)
)
BEGIN
    DECLARE v_balance DECIMAL(15,2);
    
    -- Custom conditions for error handling
    DECLARE e_insufficient_funds CONDITION FOR SQLSTATE '45000';
    DECLARE e_invalid_account CONDITION FOR SQLSTATE '45001';
    
    -- Error Handlers
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SELECT 'An unexpected error occurred during the transfer.' AS ErrorMessage;
    END;
    
    DECLARE EXIT HANDLER FOR e_insufficient_funds
    BEGIN
        ROLLBACK;
        SELECT CONCAT('Error: Insufficient funds in account ', p_from_account) AS ErrorMessage;
    END;

    DECLARE EXIT HANDLER FOR e_invalid_account
    BEGIN
        ROLLBACK;
        SELECT 'Error: One or both accounts do not exist.' AS ErrorMessage;
    END;

    START TRANSACTION;
    
    -- Lock the row for update to prevent race conditions
    SELECT Balance INTO v_balance FROM Accounts WHERE AccountID = p_from_account FOR UPDATE;
    
    IF v_balance IS NULL THEN
        SIGNAL e_invalid_account;
    END IF;

    IF v_balance < p_amount THEN
        SIGNAL e_insufficient_funds;
    END IF;
    
    -- Perform transfer
    UPDATE Accounts SET Balance = Balance - p_amount WHERE AccountID = p_from_account;
    
    UPDATE Accounts SET Balance = Balance + p_amount WHERE AccountID = p_to_account;
    IF ROW_COUNT() = 0 THEN
        SIGNAL e_invalid_account;
    END IF;
    
    COMMIT;
    SELECT 'Transfer successful.' AS Result;
END //

DELIMITER ;
```

---

## Scenario 2
**Manage errors when updating employee salaries.**  
**Question:** Write a stored procedure `UpdateSalary` that increases the salary of an employee by a given percentage. If the employee ID does not exist, handle the exception and log an error message.

### Solution
```mysql
DELIMITER //

CREATE PROCEDURE UpdateSalary(
    IN p_employee_id INT,
    IN p_percentage DECIMAL(5,2)
)
BEGIN
    DECLARE e_not_found CONDITION FOR SQLSTATE '45000';
    
    DECLARE EXIT HANDLER FOR e_not_found
    BEGIN
        ROLLBACK;
        SELECT CONCAT('Error: Employee ID ', p_employee_id, ' does not exist.') AS ErrorMessage;
    END;
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SELECT 'An unexpected error occurred.' AS ErrorMessage;
    END;

    START TRANSACTION;
    
    UPDATE Employees
    SET Salary = Salary + (Salary * (p_percentage / 100))
    WHERE EmployeeID = p_employee_id;
    
    -- Check if any row was actually updated
    IF ROW_COUNT() = 0 THEN
        SIGNAL e_not_found;
    END IF;
    
    COMMIT;
    SELECT 'Salary updated successfully.' AS Result;
END //

DELIMITER ;
```

---

## Scenario 3
**Ensure data integrity when adding a new customer.**  
**Question:** Write a stored procedure `AddNewCustomer` that inserts a new customer into the Customers table. If a customer with the same ID already exists, handle the exception by logging an error and preventing the insertion.

### Solution
```mysql
DELIMITER //

CREATE PROCEDURE AddNewCustomer(
    IN p_customer_id INT,
    IN p_name VARCHAR(100),
    IN p_dob DATE,
    IN p_balance DECIMAL(15,2)
)
BEGIN
    -- MySQL Error Code 1062 represents a duplicate entry for a key
    DECLARE EXIT HANDLER FOR 1062 
    BEGIN
        ROLLBACK;
        SELECT CONCAT('Error: Customer with ID ', p_customer_id, ' already exists.') AS ErrorMessage;
    END;
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SELECT 'An unexpected error occurred.' AS ErrorMessage;
    END;

    START TRANSACTION;
    
    INSERT INTO Customers (CustomerID, Name, DOB, Balance, LastModified)
    VALUES (p_customer_id, p_name, p_dob, p_balance, CURDATE());
    
    COMMIT;
    SELECT 'New customer added successfully.' AS Result;
END //

DELIMITER ;
```
