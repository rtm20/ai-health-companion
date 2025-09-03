package com.habitcompanion.controller;

import com.habitcompanion.dto.JwtResponse;
import com.habitcompanion.dto.LoginRequest;
import com.habitcompanion.dto.SignupRequest;
import com.habitcompanion.service.AuthService;
import com.habitcompanion.repository.UserRepository;
import com.habitcompanion.model.User;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthService authService;
    
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok("Auth controller is working!");
    }
    
    @PostMapping("/debug-password")
    public ResponseEntity<?> debugPassword(@RequestBody LoginRequest loginRequest) {
        try {
            User user = userRepository.findByEmail(loginRequest.getEmail()).orElse(null);
            if (user == null) {
                return ResponseEntity.ok("User not found: " + loginRequest.getEmail());
            }
            
            boolean matches = passwordEncoder.matches(loginRequest.getPassword(), user.getPassword());
            return ResponseEntity.ok("User found: " + user.getEmail() + ", Password matches: " + matches + 
                ", Stored hash: " + user.getPassword() + ", Input password: " + loginRequest.getPassword());
        } catch (Exception e) {
            return ResponseEntity.ok("Error: " + e.getMessage());
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            System.out.println("Login attempt for email: " + loginRequest.getEmail());
            JwtResponse jwtResponse = authService.authenticateUser(loginRequest);
            System.out.println("Login successful for email: " + loginRequest.getEmail());
            return ResponseEntity.ok(jwtResponse);
        } catch (Exception e) {
            System.out.println("Login failed for email: " + loginRequest.getEmail() + " - Error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body("Error: Invalid email or password!");
        }
    }
    
    @PostMapping("/generate-hash")
    public ResponseEntity<?> generateHash(@RequestBody String password) {
        String hash = passwordEncoder.encode(password);
        boolean matches = passwordEncoder.matches(password, hash);
        return ResponseEntity.ok("Password: " + password + " -> Hash: " + hash + " -> Matches: " + matches);
    }
    
    @PostMapping("/test-hash")
    public ResponseEntity<?> testHash(@RequestBody java.util.Map<String, String> request) {
        String password = request.get("password");
        String hash = request.get("hash");
        boolean matches = passwordEncoder.matches(password, hash);
        return ResponseEntity.ok("Password: " + password + " -> Hash: " + hash + " -> Matches: " + matches);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        String result = authService.registerUser(signUpRequest);
        
        if (result.contains("Error")) {
            return ResponseEntity.badRequest().body(result);
        }
        
        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/init-sample-data")
    public ResponseEntity<?> initSampleData(@RequestBody String email) {
        try {
            User user = userRepository.findByEmail(email).orElse(null);
            if (user == null) {
                return ResponseEntity.badRequest().body("User not found: " + email);
            }
            
            // This would normally be done through proper service layers, but for testing:
            String message = "Sample data initialization would go here for user: " + user.getEmail() + " (ID: " + user.getId() + ")";
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/debug-headers")
    public ResponseEntity<?> debugHeaders(HttpServletRequest request) {
        StringBuilder headers = new StringBuilder();
        java.util.Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            headers.append(headerName).append(": ").append(headerValue).append("\n");
        }
        return ResponseEntity.ok("Headers received:\n" + headers.toString());
    }
}
