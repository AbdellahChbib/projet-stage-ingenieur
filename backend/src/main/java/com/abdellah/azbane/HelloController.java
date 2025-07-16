package com.abdellah.azbane;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class HelloController {
    @GetMapping("/hello")
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Hello from Spring Boot!");
    }
}
