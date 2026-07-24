# JUnit, Mockito, and SLF4J Hands-on Solutions

## 1. Basic JUnit Testing
**Exercise 2: Writing Basic JUnit Tests & Exercise 4: AAA Pattern, Setup and Teardown**
```java
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CalculatorTest {
    private Calculator calculator;

    @Before
    public void setUp() {
        calculator = new Calculator(); // Arrange
    }

    @After
    public void tearDown() {
        calculator = null;
    }

    @Test
    public void testAddition() {
        int result = calculator.add(2, 3); // Act
        assertEquals(5, result); // Assert
    }
}
```

## 2. Advanced JUnit Testing
**Parameterized Tests, Test Order, Exceptions, and Timeouts**
```java
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AdvancedJUnitTest {

    @Order(1)
    @ParameterizedTest
    @ValueSource(ints = {2, 4, 6, 8})
    public void testIsEven(int number) {
        assertTrue(number % 2 == 0);
    }

    @Order(2)
    @Test
    public void testException() {
        assertThrows(IllegalArgumentException.class, () -> {
            throw new IllegalArgumentException("Invalid");
        });
    }

    @Order(3)
    @Test
    @Timeout(1) // Fails if execution takes > 1 second
    public void testPerformance() throws InterruptedException {
        Thread.sleep(500); 
    }
}
```

## 3. Mockito Basic & Advanced Exercises
**Argument Matchers, Void Methods, Verification Order**
```java
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

public class MockitoExercisesTest {

    @Test
    public void testArgumentMatching() {
        ExternalApi mockApi = mock(ExternalApi.class);
        when(mockApi.getData(anyString())).thenReturn("Mocked");
        
        assertEquals("Mocked", mockApi.getData("test"));
    }

    @Test
    public void testVoidMethod() {
        ExternalApi mockApi = mock(ExternalApi.class);
        doNothing().when(mockApi).performAction();
        
        mockApi.performAction();
        verify(mockApi, times(1)).performAction();
    }

    @Test
    public void testVerificationOrder() {
        ExternalApi mockApi = mock(ExternalApi.class);
        
        mockApi.connect();
        mockApi.getData();
        
        InOrder inOrder = inOrder(mockApi);
        inOrder.verify(mockApi).connect();
        inOrder.verify(mockApi).getData();
    }
    
    @Test
    public void testVoidMethodException() {
        ExternalApi mockApi = mock(ExternalApi.class);
        doThrow(new RuntimeException()).when(mockApi).performAction();
        
        assertThrows(RuntimeException.class, () -> mockApi.performAction());
    }
}
```

## 4. Spring Test & Mock Dependencies Exercises
**Service, Repository, and Controller (MockMvc) Tests**
```java
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.util.Optional;

// Exercise 1, 2, & 7: Service with Mocked Repository
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void testGetUserById() {
        User mockUser = new User(1L, "John");
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        
        User user = userService.getUserById(1L);
        assertEquals("John", user.getName());
    }

    @Test
    public void testServiceExceptionHandling() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        
        assertThrows(NoSuchElementException.class, () -> userService.getUserById(99L));
    }
}

// Exercise 3, 5, & 8: Controller Test with MockMvc
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void testGetUserEndpoint() throws Exception {
        User mockUser = new User(1L, "John");
        when(userService.getUserById(1L)).thenReturn(mockUser);
        
        mockMvc.perform(get("/users/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value("John"));
    }

    @Test
    public void testCreateUserEndpoint() throws Exception {
        User mockUser = new User(2L, "Jane");
        when(userService.saveUser(any(User.class))).thenReturn(mockUser);
        
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Jane\"}"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value("Jane"));
    }
    
    @Test
    public void testControllerExceptionHandling() throws Exception {
        when(userService.getUserById(99L)).thenThrow(new NoSuchElementException());
        
        mockMvc.perform(get("/users/99"))
               .andExpect(status().isNotFound())
               .andExpect(content().string("User not found"));
    }
}
```

## 5. SLF4J Logging Exercises
**Parameterized Logging & Appenders**
```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingExercises {
    private static final Logger logger = LoggerFactory.getLogger(LoggingExercises.class);

    public static void main(String[] args) {
        // Exercise 1: Error and Warn
        logger.error("This is an error message");
        logger.warn("This is a warning message");
        
        // Exercise 2: Parameterized Logging
        String user = "Admin";
        int failedAttempts = 3;
        logger.warn("User {} failed to login. Attempts: {}", user, failedAttempts);
        
        // Exercise 3: Using Different Appenders
        // See logback.xml configuration to understand how output is routed to both Console and File appenders.
        logger.info("This will be logged to both console and file as per logback.xml");
    }
}
```
