package com.reliefconnect.backend.controller;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.*;

import com.reliefconnect.backend.entity.User;
import com.reliefconnect.backend.repository.UserRepository;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String mailFrom;

    private final Map<String, OtpRecord> otpStore = new HashMap<>();
    private final Random random = new Random();
    private static final long OTP_TTL_MILLIS = 5 * 60 * 1000;

    private static class OtpRecord {
        private final String code;
        private final long expiresAt;
        private boolean verified;

        private OtpRecord(String code) {
            this.code = code;
            this.expiresAt = Instant.now().toEpochMilli() + OTP_TTL_MILLIS;
            this.verified = false;
        }
    }

    @PostMapping("/send-otp")
    public Map<String, String> sendOtp(@RequestBody Map<String, String> request) {
        String email = normalizeEmail(request.get("email"));

        if (email.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is required");
        }

        if (userRepository.findByEmail(email).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This email is already registered");
        }

        String otp = String.valueOf(100000 + random.nextInt(900000));
        otpStore.put(email, new OtpRecord(otp));

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailFrom);
        message.setTo(email);
        message.setSubject("Your Relief Connect OTP");
        message.setText("Your Relief Connect registration OTP is " + otp + ". It expires in 5 minutes.");

        try {
            mailSender.send(message);
        } catch (MailException ex) {
            otpStore.remove(email);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Could not send OTP email: " + ex.getMostSpecificCause().getMessage(),
                    ex);
        }

        return Map.of("message", "OTP sent to your email");
    }

    @PostMapping("/verify-otp")
    public Map<String, String> verifyOtp(@RequestBody Map<String, String> request) {
        String email = normalizeEmail(request.get("email"));
        String otp = String.valueOf(request.getOrDefault("otp", "")).trim();
        OtpRecord record = otpStore.get(email);

        validateOtp(email, otp, record);
        record.verified = true;

        return Map.of("message", "OTP verified");
    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {

        String email = normalizeEmail(user.getEmail());
        OtpRecord record = otpStore.get(email);

        validateOtp(email, user.getOtp(), record);

        if (!record.verified) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please verify OTP first");
        }

        user.setEmail(email);
        user.setPassword(user.getPassword().trim());
        otpStore.remove(email);

        return userRepository.save(user);
    }

    @PostMapping("/login")
    public User login(@RequestBody User user) {

        User existingUser = userRepository
                .findByEmail(user.getEmail().trim().toLowerCase())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!existingUser.getPassword().trim().equals(user.getPassword().trim())) {
            throw new RuntimeException("Invalid Password");
        }

        return existingUser;
    }

    private String normalizeEmail(String email) {
        return String.valueOf(email == null ? "" : email).trim().toLowerCase();
    }

    private void validateOtp(String email, String otp, OtpRecord record) {
        if (record == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please request a new OTP");
        }

        if (Instant.now().toEpochMilli() > record.expiresAt) {
            otpStore.remove(email);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP expired. Please request a new OTP");
        }

        if (!record.code.equals(String.valueOf(otp == null ? "" : otp).trim())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid OTP");
        }
    }
}
