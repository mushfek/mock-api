package com.example.mockapi;

import net.datafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@SpringBootApplication
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", exposedHeaders = "*", maxAge = 1800)
// TODO: remove this once proxy is configured
public class MockApiApplication {

    private final Faker faker = new Faker();

    private final Logger log = LoggerFactory.getLogger(MockApiApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(MockApiApplication.class, args);
    }

    @GetMapping("/auth/appStatus")
    public Map<String, String> getTherapSystemStatus() {
        return new HashMap<>() {{
            put("status", "OK");
        }};
    }

    @PostMapping("/therap-api/v1/login")
    public Map<String, String> login(@RequestParam String loginName,
                                     @RequestParam String password,
                                     @RequestParam String providerCode) {
        Map<String, String> authResponse = new HashMap<>();
        authResponse.put("Token", UUID.randomUUID().toString().replace("-", ""));

        return authResponse;
    }

    @GetMapping("/therap-api/v1/connect/search/providers")
    public Map<String, Object> getProvider(@RequestHeader("Authorization") String token,
                                           @RequestParam String providerCode) {
        log.debug("getProvider: token={}, providerCode={}", token, providerCode);

        Random random = new Random();
        int nextInt = random.nextInt(256 * 256 * 256);

        Map<String, Object> user = new HashMap<>();
        user.put("code", providerCode);
        user.put("name", "Provider " + faker.address().city());
        user.put("tz", "US/Eastern");
        user.put("demo", false);

        return user;
    }

    @GetMapping("/therap-api/v1/connect/search/users")
    public Map<String, Object> getUser(@RequestHeader("Authorization") String token,
                                       @RequestParam String loginName,
                                       @RequestParam String providerCode) {
        log.debug("getUser: token={}, loginName={}, providerCode={}", token, loginName, providerCode);

        boolean isAdmin = loginName.endsWith("-adm");

        Map<String, Object> user = new HashMap<>();

        Random random = new Random();
        int nextInt = random.nextInt(256 * 256 * 256);
        String formSuffix = String.format("%06x", nextInt);

        String provCode = providerCode.replace("-", "");

        long phoneNumber = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;

        user.put("formId", "USER-".concat(provCode).concat("-").concat(formSuffix.toUpperCase()));
        user.put("loginName", loginName);
        user.put("firstName", faker.name().firstName());
        user.put("lastName", faker.name().lastName());
        user.put("title", null);
//        user.put("title", "Title-" + nextInt);
        user.put("providerCode", providerCode);
        user.put("tz", "US/Eastern");
        user.put("employeeId", formSuffix);
//        user.put("mobilePhone", String.valueOf(phoneNumber).replaceFirst("(\\d{3})(\\d{3})(\\d+)", "$1-$2-$3"));
        user.put("mobilePhone", "");
//        user.put("email", loginName.concat("@email.com"));
        user.put("email", "");
        user.put("providerAdmin", isAdmin);

        return user;
    }

    @GetMapping("/therap-api/v1/connect/search/individuals")
    public Map<String, Object> getIndividual(@RequestHeader("Authorization") String token,
                                             @RequestParam String firstName,
                                             @RequestParam String lastName,
                                             @RequestParam String dateOfBirth,
                                             @RequestParam String providerCode,
                                             @RequestParam String zipCode) {
        log.debug("getUser: token={}, firstName={}, lastName={}, dateOfBirth={}, providerCode={}, zipCode={}",
                token, firstName, lastName, dateOfBirth, providerCode, zipCode);

        Random random = new Random();
        int nextInt = random.nextInt(256 * 256 * 256);
        String formSuffix = String.format("%06x", nextInt);

        String provCode = providerCode.replace("-", "");

        Map<String, Object> user = new HashMap<>();
        user.put("formId", "IDF-".concat(provCode).concat("-").concat(formSuffix.toUpperCase()));

        return user;
    }
}
