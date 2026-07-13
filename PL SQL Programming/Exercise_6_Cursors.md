# Exercise 6: Cursors

## Scenario 1
**Generate monthly statements for all customers.**  
**Question:** Write a PL/SQL (now MySQL) block using an explicit cursor `GenerateMonthlyStatements` that retrieves all transactions for the current month and prints a statement for each customer.

### Solution
```mysql
DELIMITER //

CREATE PROCEDURE GenerateMonthlyStatements()
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE v_name VARCHAR(100);
    DECLARE v_account_id INT;
    DECLARE v_date DATE;
    DECLARE v_amount DECIMAL(15,2);
    DECLARE v_type VARCHAR(20);
    
    DECLARE cur CURSOR FOR 
        SELECT c.Name, a.AccountID, t.TransactionDate, t.Amount, t.TransactionType
        FROM Customers c
        JOIN Accounts a ON c.CustomerID = a.CustomerID
        JOIN Transactions t ON a.AccountID = t.AccountID
        WHERE MONTH(t.TransactionDate) = MONTH(CURDATE())
          AND YEAR(t.TransactionDate) = YEAR(CURDATE())
        ORDER BY c.Name, a.AccountID, t.TransactionDate;
        
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
    
    OPEN cur;
    
    read_loop: LOOP
        FETCH cur INTO v_name, v_account_id, v_date, v_amount, v_type;
        IF done THEN
            LEAVE read_loop;
        END IF;
        
        -- Print the statement using SELECT
        SELECT CONCAT('Customer: ', v_name, ' | Account: ', v_account_id, ' | Date: ', v_date, ' | Type: ', v_type, ' | Amount: $', v_amount) AS Statement;
    END LOOP;
    
    CLOSE cur;
END //

DELIMITER ;
```

---

## Scenario 2
**Apply annual fee to all accounts.**  
**Question:** Write a PL/SQL (now MySQL) block using an explicit cursor `ApplyAnnualFee` that deducts an annual maintenance fee from the balance of all accounts.

### Solution
*Note: MySQL does not support `WHERE CURRENT OF` for updating via cursors. We must fetch the Primary Key explicitly.*

```mysql
DELIMITER //

CREATE PROCEDURE ApplyAnnualFee()
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE v_annual_fee DECIMAL(10,2) DEFAULT 50.00; 
    DECLARE v_account_id INT;
    
    DECLARE cur CURSOR FOR SELECT AccountID FROM Accounts;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
    
    START TRANSACTION;
    OPEN cur;
    
    read_loop: LOOP
        FETCH cur INTO v_account_id;
        IF done THEN
            LEAVE read_loop;
        END IF;
        
        -- Update the specific row by ID
        UPDATE Accounts
        SET Balance = Balance - v_annual_fee,
            LastModified = CURDATE()
        WHERE AccountID = v_account_id;
    END LOOP;
    
    CLOSE cur;
    COMMIT;
    
    SELECT 'Annual fee applied successfully to all accounts.' AS Result;
END //

DELIMITER ;
```

---

## Scenario 3
**Update the interest rate for all loans based on a new policy.**  
**Question:** Write a PL/SQL (now MySQL) block using an explicit cursor `UpdateLoanInterestRates` that fetches all loans and updates their interest rates based on the new policy.

### Solution
```mysql
DELIMITER //

CREATE PROCEDURE UpdateLoanInterestRates()
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE v_loan_id INT;
    DECLARE v_loan_amount DECIMAL(15,2);
    DECLARE v_new_rate DECIMAL(5,2);
    
    DECLARE cur CURSOR FOR SELECT LoanID, LoanAmount FROM Loans FOR UPDATE;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
    
    START TRANSACTION;
    OPEN cur;
    
    read_loop: LOOP
        FETCH cur INTO v_loan_id, v_loan_amount;
        IF done THEN
            LEAVE read_loop;
        END IF;
        
        -- Policy Rules
        IF v_loan_amount > 10000 THEN
            SET v_new_rate = 4.5;
        ELSE
            SET v_new_rate = 5.5;
        END IF;
        
        UPDATE Loans
        SET InterestRate = v_new_rate
        WHERE LoanID = v_loan_id;
    END LOOP;
    
    CLOSE cur;
    COMMIT;
    
    SELECT 'Loan interest rates updated based on the new policy.' AS Result;
END //

DELIMITER ;
```
