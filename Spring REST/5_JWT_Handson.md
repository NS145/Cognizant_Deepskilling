# Spring REST Hands-on 5: JWT Authentication

## Objectives
- Secure RESTful Web Service using Spring Security.
- Perform Basic Authentication and in-memory authorization.
- Understand JWT (JSON Web Token) process flow and structure.
- Generate JWTs upon authentication and authorize subsequent requests via Spring Filters.

## Setting Up Spring Security (Basic Auth)
Add the Spring Security starter dependency:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

Create a configuration class to set up in-memory users with roles, and define endpoint authorization.

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfig.class);

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
            .withUser("admin").password(passwordEncoder().encode("pwd")).roles("ADMIN")
            .and()
            .withUser("user").password(passwordEncoder().encode("pwd")).roles("USER");
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        LOGGER.info("Start");
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable().httpBasic().and()
            .authorizeRequests()
            .antMatchers("/authenticate").hasAnyRole("USER", "ADMIN")
            .anyRequest().authenticated()
            .and()
            .addFilter(new JwtAuthorizationFilter(authenticationManager()));
    }
}
```
*Note: Basic auth sends credentials encoded in Base64 (e.g., `Authorization: Basic YWRtaW46cHdk`), which is not secure without HTTPS and does not scale well in stateless REST architectures. JWT solves this.*

## Generating JWT Token
Create an `AuthenticationController` to issue a token when a valid user logs in. Include the `jjwt` library in `pom.xml`.

```xml
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt</artifactId>
    <version>0.9.0</version>
</dependency>
```

```java
@RestController
public class AuthenticationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);

    @GetMapping("/authenticate")
    public Map<String, String> authenticate(@RequestHeader("Authorization") String authHeader) {
        LOGGER.info("Start");
        LOGGER.debug(authHeader);
        
        Map<String, String> map = new HashMap<>();
        String user = getUser(authHeader);
        String token = generateJwt(user);
        
        map.put("token", token);
        LOGGER.info("End");
        return map;
    }

    private String getUser(String authHeader) {
        String encodedCredentials = authHeader.substring(6);
        byte[] decodedBytes = Base64.getDecoder().decode(encodedCredentials);
        String credentials = new String(decodedBytes);
        return credentials.split(":")[0];
    }

    private String generateJwt(String user) {
        JwtBuilder builder = Jwts.builder();
        builder.setSubject(user);
        builder.setIssuedAt(new Date());
        builder.setExpiration(new Date((new Date()).getTime() + 1200000)); // 20 minutes
        builder.signWith(SignatureAlgorithm.HS256, "secretkey");
        
        return builder.compact();
    }
}
```

## Implementing JWT Authorization Filter
Create a custom filter extending `BasicAuthenticationFilter` to intercept requests, parse the JWT token, and set the security context.

```java
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthorizationFilter.class);

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        LOGGER.info("Start");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res,
            FilterChain chain) throws IOException, ServletException {
        
        LOGGER.info("Start");
        String header = req.getHeader("Authorization");
        LOGGER.debug(header);
        
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(req, res);
            return;
        }
        
        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        chain.doFilter(req, res);
        LOGGER.info("End");
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null) {
            try {
                Jws<Claims> jws = Jwts.parser()
                        .setSigningKey("secretkey")
                        .parseClaimsJws(token.replace("Bearer ", ""));
                
                String user = jws.getBody().getSubject();
                LOGGER.debug(user);
                
                if (user != null) {
                    return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
                }
            } catch (JwtException ex) {
                return null;
            }
        }
        return null;
    }
}
```

## Testing JWT Implementation
1. Get the token via Basic Authentication:
   `curl -s -u user:pwd http://localhost:8090/authenticate`
2. Extract the returned `token` from the JSON response.
3. Call secure services using the retrieved token:
   `curl -s -H "Authorization: Bearer <TOKEN_HERE>" http://localhost:8090/countries`
