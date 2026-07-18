# Spring REST Hands-on 4: POST, PUT, DELETE and Validation

## Objectives
- Demonstrate implementation of RESTful Web Service using POST/PUT/DELETE method with input validation.
- Learn HTTP method types and REST service URL naming guidelines.
- Learn JSON to bean mapping using `@RequestBody`.
- Validate input using `javax.validation` and global exception handling.

## RESTful Web Service Resource Naming Guidelines
- Ensure resources have specific, unique URLs.
- Resources should be named in plural (e.g., `/countries`, `/employees`).
- Use HTTP methods to define the action:
  - `GET /countries`: Retrieve all
  - `GET /countries/{id}`: Retrieve specific
  - `POST /countries`: Create
  - `PUT /countries`: Update
  - `DELETE /countries/{id}`: Delete
- Use hyphens for multi-word resources (e.g., `/menu-items`).

## Handling POST Requests
Using `@PostMapping` to create a resource, parsing JSON into a Bean automatically using `@RequestBody`.

```java
@PostMapping("/countries")
public Country addCountry(@RequestBody @Valid Country country) {
    LOGGER.info("Start");
    // Add logic to save the country...
    LOGGER.info("End");
    return country;
}
```
*Note: Include `@Valid` to enforce JSR-380 bean validations.*

## Validating Input
Add validation annotations directly to your Bean class:

```java
public class Country {
    @NotNull
    @Size(min=2, max=2, message="Country code should be 2 characters")
    private String code;
    
    // ...
}
```

Other useful annotations:
- `@NotBlank`, `@Min`, `@Max`
- `@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")` for dates.

## Global Exception Handling
Creating a unified mechanism to handle validation or formatting errors across all controllers.

```java
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        
        LOGGER.info("Start");
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());

        // Get all validation errors
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());

        body.put("errors", errors);
        LOGGER.info("End");
        
        return new ResponseEntity<>(body, headers, status);
    }
    
    // Handle invalid formats (e.g., entering strings into numeric fields)
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());
        body.put("error", "Bad Request");

        if (ex.getCause() instanceof InvalidFormatException) {
            final Throwable cause = ex.getCause() == null ? ex : ex.getCause();
            for (InvalidFormatException.Reference reference : ((InvalidFormatException) cause).getPath()) {
                body.put("message", "Incorrect format for field '" + reference.getFieldName() + "'");
            }
        }
        return new ResponseEntity<>(body, headers, status);
    }
}
```

## Implementing PUT and DELETE
**Update Resource (PUT):**
```java
@PutMapping("/employees")
public void updateEmployee(@RequestBody @Valid Employee employee) throws EmployeeNotFoundException {
    employeeService.updateEmployee(employee);
}
```

**Delete Resource (DELETE):**
```java
@DeleteMapping("/employees/{id}")
public void deleteEmployee(@PathVariable long id) throws EmployeeNotFoundException {
    employeeService.deleteEmployee(id);
}
```
