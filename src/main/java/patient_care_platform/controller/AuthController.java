package patient_care_platform.controller;

import patient_care_platform.model.User;
import patient_care_platform.model.LoginRequest;
import patient_care_platform.model.RegisterRequest;
import patient_care_platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String ADMIN_SIGNUP_CODE = "CLINIC2026";

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already in use");
        }

        if ("ADMIN".equals(request.getRole())) {
            if (!ADMIN_SIGNUP_CODE.equals(request.getAdminCode())) {
                return ResponseEntity.badRequest().body("Invalid admin code");
            }
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(User.Role.valueOf(request.getRole()));

        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(401).body("Invalid email or password");
        }

        User user = userOptional.get();

        boolean passwordMatches = passwordEncoder.matches(loginRequest.getPassword(), user.getPassword());

        if (!passwordMatches) {
            return ResponseEntity.status(401).body("Invalid email or password");
        }

        return ResponseEntity.ok(user);
    }
}