# Exercise 7: Packages

## MySQL Alternative to Packages
*Note: MySQL does not natively support "Packages" (which are an Oracle PL/SQL feature). To achieve the same modularity and grouping in MySQL, it is standard practice to create independent stored procedures and functions using a **common naming prefix** (e.g., `CustomerManagement_`).*

---

## Scenario 1
**Group all customer-related procedures and functions into a package.**  
**Question:** Create a package `CustomerManagement` with procedures for adding a new customer, updating customer details, and a function to get customer balance.

### Solution
```mysql
DELIMITER //

-- Procedure: AddCustomer
CREATE PROCEDURE CustomerManagement_AddCustomer(
    IN p_id INT, 
    IN p_name VARCHAR(100), 
    IN p_dob DATE, 
    IN p_balance DECIMAL(15,2)
)
BEGIN
    INSERT INTO Customers (CustomerID, Name, DOB, Balance, LastModified)
    VALUES (p_id, p_name, p_dob, p_balance, CURDATE());
END //

-- Procedure: UpdateCustomer
CREATE PROCEDURE CustomerManagement_UpdateCustomer(
    IN p_id INT, 
    IN p_name VARCHAR(100)
)
BEGIN
    UPDATE Customers
    SET Name = p_name, LastModified = CURDATE()
    WHERE CustomerID = p_id;
END //

-- Function: GetBalance
CREATE FUNCTION CustomerManagement_GetBalance(p_id INT) 
RETURNS DECIMAL(15,2)
READS SQL DATA
BEGIN
    DECLARE v_balance DECIMAL(15,2);
    
    SELECT Balance INTO v_balance FROM Customers WHERE CustomerID = p_id;
    RETURN v_balance;
END //

DELIMITER ;
```

---

## Scenario 2
**Create a package to manage employee data.**  
**Question:** Write a package `EmployeeManagement` with procedures to hire new employees, update employee details, and a function to calculate annual salary.

### Solution
```mysql
DELIMITER //

-- Procedure: HireEmployee
CREATE PROCEDURE EmployeeManagement_HireEmployee(
    IN p_id INT, 
    IN p_name VARCHAR(100), 
    IN p_position VARCHAR(50), 
    IN p_salary DECIMAL(15,2), 
    IN p_dept VARCHAR(50), 
    IN p_hire_date DATE
)
BEGIN
    INSERT INTO Employees (EmployeeID, Name, Position, Salary, Department, HireDate)
    VALUES (p_id, p_name, p_position, p_salary, p_dept, p_hire_date);
END //

-- Procedure: UpdateEmployee
CREATE PROCEDURE EmployeeManagement_UpdateEmployee(
    IN p_id INT, 
    IN p_position VARCHAR(50), 
    IN p_salary DECIMAL(15,2)
)
BEGIN
    UPDATE Employees
    SET Position = p_position, Salary = p_salary
    WHERE EmployeeID = p_id;
END //

-- Function: CalculateAnnualSalary
CREATE FUNCTION EmployeeManagement_CalculateAnnualSalary(p_id INT) 
RETURNS DECIMAL(15,2)
READS SQL DATA
BEGIN
    DECLARE v_monthly_salary DECIMAL(15,2);
    
    SELECT Salary INTO v_monthly_salary FROM Employees WHERE EmployeeID = p_id;
    -- Returning NULL if employee not found, otherwise annual salary
    RETURN v_monthly_salary * 12;
END //

DELIMITER ;
```

---

## Scenario 3
**Group all account-related operations into a package.**  
**Question:** Create a package `AccountOperations` with procedures for opening a new account, closing an account, and a function to get the total balance of a customer across all accounts.

### Solution
```mysql
DELIMITER //

-- Procedure: OpenAccount
CREATE PROCEDURE AccountOperations_OpenAccount(
    IN p_acc_id INT, 
    IN p_cust_id INT, 
    IN p_type VARCHAR(20), 
    IN p_balance DECIMAL(15,2)
)
BEGIN
    INSERT INTO Accounts (AccountID, CustomerID, AccountType, Balance, LastModified)
    VALUES (p_acc_id, p_cust_id, p_type, p_balance, CURDATE());
END //

-- Procedure: CloseAccount
CREATE PROCEDURE AccountOperations_CloseAccount(IN p_acc_id INT)
BEGIN
    DELETE FROM Accounts WHERE AccountID = p_acc_id;
END //

-- Function: GetTotalCustomerBalance
CREATE FUNCTION AccountOperations_GetTotalCustomerBalance(p_cust_id INT) 
RETURNS DECIMAL(15,2)
READS SQL DATA
BEGIN
    DECLARE v_total DECIMAL(15,2);
    
    SELECT SUM(Balance) INTO v_total 
    FROM Accounts 
    WHERE CustomerID = p_cust_id;
    
    RETURN IFNULL(v_total, 0);
END //

DELIMITER ;
```
