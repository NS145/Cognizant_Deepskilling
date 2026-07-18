# Spring REST Hands-on 2: GET Methods and MockMVC

## Objectives
- Explain HTTP Request and Response details
- Understand RESTful Web Services concepts
- Demonstrate RESTful Web Service using `GET` method
- Perform end-to-end testing of REST API using `MockMVC`

## HTTP Request and Response Basics
A typical HTTP Request includes:
- **Method Type**: (e.g., `GET`, `POST`)
- **Resource**: (e.g., `/hello.txt`)
- **HTTP Version**: (e.g., `HTTP/1.1`)
- **Headers**: Containing client details (`User-Agent`), Host server details, `Accept-Language`, etc.

A typical HTTP Response includes:
- **HTTP Version and Status Code**: (e.g., `HTTP/1.1 200 OK`)
- **Headers**: Details like `Date`, `Content-Type` (e.g., `application/json`), `Content-Length`.
- **Payload/Body**: The actual response content.

## Hello World RESTful Web Service
Creating a basic GET endpoint to return a string.

```java
@RestController
public class HelloController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloController.class);

    @GetMapping("/hello")
    public String sayHello() {
        LOGGER.info("START");
        String message = "Hello World!!";
        LOGGER.info("END");
        return message;
    }
}
```

## REST - Country Web Service
Returning a JSON response of a loaded bean.

```java
@RestController
public class CountryController {

    @RequestMapping("/country")
    public Country getCountryIndia() {
        ApplicationContext context = new ClassPathXmlApplicationContext("country.xml");
        return context.getBean("in", Country.class);
    }
    
    @GetMapping("/countries")
    public List<Country> getAllCountries() {
        ApplicationContext context = new ClassPathXmlApplicationContext("country.xml");
        return context.getBean("countryList", ArrayList.class);
    }
    
    @GetMapping("/countries/{code}")
    public Country getCountry(@PathVariable String code) {
        // Logic to retrieve the country based on the code (case-insensitive)
        return countryService.getCountry(code);
    }
}
```

## REST - Exception Handling
Handling missing data and responding with appropriate HTTP status codes.

Create a custom exception with `@ResponseStatus`:
```java
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Country not found")
public class CountryNotFoundException extends RuntimeException {
    public CountryNotFoundException(String message) {
        super(message);
    }
}
```

Throw this exception in your service logic when a country is not found based on the provided code.

## Testing with MockMVC
Testing your controllers effectively using Spring's `MockMVC`.

```java
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SpringLearnApplicationTests {

    @Autowired
    private CountryController countryController;

    @Autowired
    private MockMvc mvc;

    @Test
    public void contextLoads() {
        assertNotNull(countryController);
    }

    @Test
    public void testGetCountry() throws Exception {
        ResultActions actions = mvc.perform(get("/country"));
        
        actions.andExpect(status().isOk());
        actions.andExpect(jsonPath("$.code").exists());
        actions.andExpect(jsonPath("$.code").value("IN"));
        actions.andExpect(jsonPath("$.name").exists());
        actions.andExpect(jsonPath("$.name").value("India"));
    }
    
    @Test
    public void testGetCountryException() throws Exception {
        ResultActions actions = mvc.perform(get("/countries/az"));
        
        actions.andExpect(status().isNotFound());
        actions.andExpect(status().reason("Country not found"));
    }
}
```
