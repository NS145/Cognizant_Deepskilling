# Exercise 4: Functions

## Scenario 1
**Calculate the age of customers for eligibility checks.**  
**Question:** Write a function `CalculateAge` that takes a customer's date of birth as input and returns their age in years.

### Solution
```mysql
DELIMITER //

CREATE FUNCTION CalculateAge(
    p_dob DATE
) 
RETURNS INT
DETERMINISTIC
BEGIN
    -- Calculate difference in years
    RETURN TIMESTAMPDIFF(YEAR, p_dob, CURDATE());
END //

DELIMITER ;
```

---

## Scenario 2
**The bank needs to compute the monthly installment for a loan.**  
**Question:** Write a function `CalculateMonthlyInstallment` that takes the loan amount, interest rate, and loan duration in years as input and returns the monthly installment amount.

### Solution
```mysql
DELIMITER //

CREATE FUNCTION CalculateMonthlyInstallment(
    p_loan_amount DECIMAL(15,2),
    p_interest_rate DECIMAL(5,2),
    p_duration_years INT
) 
RETURNS DECIMAL(15,2)
DETERMINISTIC
BEGIN
    DECLARE v_monthly_rate DECIMAL(10,6);
    DECLARE v_total_months INT;
    DECLARE v_installment DECIMAL(15,2);
    
    -- Calculate monthly rate from the yearly percentage (e.g., 5 for 5%)
    SET v_monthly_rate = (p_interest_rate / 100) / 12;
    SET v_total_months = p_duration_years * 12;
    
    -- Handle 0% interest case to prevent division by zero
    IF v_monthly_rate = 0 THEN
        SET v_installment = p_loan_amount / v_total_months;
    ELSE
        -- MySQL uses POW() instead of POWER()
        SET v_installment = p_loan_amount * (v_monthly_rate * POW(1 + v_monthly_rate, v_total_months)) / (POW(1 + v_monthly_rate, v_total_months) - 1);
    END IF;
    
    RETURN ROUND(v_installment, 2);
END //

DELIMITER ;
```

---

## Scenario 3
**Check if a customer has sufficient balance before making a transaction.**  
**Question:** Write a function `HasSufficientBalance` that takes an account ID and an amount as input and returns a boolean indicating whether the account has at least the specified amount.

### Solution
```mysql
DELIMITER //

CREATE FUNCTION HasSufficientBalance(
    p_account_id INT,
    p_amount DECIMAL(15,2)
) 
RETURNS BOOLEAN
READS SQL DATA
BEGIN
    DECLARE v_balance DECIMAL(15,2);
    
    SELECT Balance INTO v_balance 
    FROM Accounts 
    WHERE AccountID = p_account_id;
    
    IF v_balance IS NULL THEN
        -- If account does not exist
        RETURN FALSE;
    ELSEIF v_balance >= p_amount THEN
        RETURN TRUE;
    ELSE
        RETURN FALSE;
    END IF;
END //

DELIMITER ;
```
