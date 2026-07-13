# Exercise 4: Functions

## Scenario 1
**Calculate the age of customers for eligibility checks.**  
**Question:** Write a function `CalculateAge` that takes a customer's date of birth as input and returns their age in years.

### Solution
```sql
CREATE OR REPLACE FUNCTION CalculateAge (
    p_dob DATE
) RETURN NUMBER IS
    v_age NUMBER;
BEGIN
    -- Calculate age by finding the difference in months and dividing by 12
    v_age := TRUNC(MONTHS_BETWEEN(SYSDATE, p_dob) / 12);
    RETURN v_age;
END;
/
```

---

## Scenario 2
**The bank needs to compute the monthly installment for a loan.**  
**Question:** Write a function `CalculateMonthlyInstallment` that takes the loan amount, interest rate, and loan duration in years as input and returns the monthly installment amount.

### Solution
```sql
CREATE OR REPLACE FUNCTION CalculateMonthlyInstallment (
    p_loan_amount NUMBER,
    p_interest_rate NUMBER,
    p_duration_years NUMBER
) RETURN NUMBER IS
    v_monthly_rate NUMBER;
    v_total_months NUMBER;
    v_installment NUMBER;
BEGIN
    -- Calculate monthly rate (interest rate is provided as a percentage, e.g., 5 for 5%)
    v_monthly_rate := (p_interest_rate / 100) / 12;
    v_total_months := p_duration_years * 12;
    
    -- Handle 0% interest case to prevent division by zero
    IF v_monthly_rate = 0 THEN
        v_installment := p_loan_amount / v_total_months;
    ELSE
        v_installment := p_loan_amount * (v_monthly_rate * POWER(1 + v_monthly_rate, v_total_months)) / (POWER(1 + v_monthly_rate, v_total_months) - 1);
    END IF;
    
    -- Return the result rounded to 2 decimal places
    RETURN ROUND(v_installment, 2);
END;
/
```

---

## Scenario 3
**Check if a customer has sufficient balance before making a transaction.**  
**Question:** Write a function `HasSufficientBalance` that takes an account ID and an amount as input and returns a boolean indicating whether the account has at least the specified amount.

### Solution
```sql
CREATE OR REPLACE FUNCTION HasSufficientBalance (
    p_account_id NUMBER,
    p_amount NUMBER
) RETURN BOOLEAN IS
    v_balance NUMBER;
BEGIN
    SELECT Balance INTO v_balance 
    FROM Accounts 
    WHERE AccountID = p_account_id;
    
    IF v_balance >= p_amount THEN
        RETURN TRUE;
    ELSE
        RETURN FALSE;
    END IF;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        -- If account does not exist, return false
        RETURN FALSE;
    WHEN OTHERS THEN
        RETURN FALSE;
END;
/
```
