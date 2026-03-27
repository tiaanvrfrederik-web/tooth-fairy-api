package com.example.tooth_fairy.auth;

import com.example.tooth_fairy.auth.dto.AuthResponse;
import com.example.tooth_fairy.auth.dto.LoginRequest;
import com.example.tooth_fairy.auth.dto.RegisterRequest;
import com.example.tooth_fairy.auth.dto.RegisterResponse;
import com.example.tooth_fairy.account.Account;
import com.example.tooth_fairy.account.AccountRepository;
import com.example.tooth_fairy.security.JwtService;
import com.example.tooth_fairy.security.UserPrincipal;
import com.example.tooth_fairy.user.User;
import com.example.tooth_fairy.user.UserRepository;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            AccountRepository accountRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");
        }

        Account account = resolveAccount(request.accountId());

        User user = new User();
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRoles("ROLE_USER");
        user.setAccount(account);
        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        return new RegisterResponse(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getRoles(),
                savedUser.getAccount() != null ? savedUser.getAccount().getId() : null,
                "User registered successfully"
        );
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        String token = jwtService.generateToken(new UserPrincipal(user));

        return new AuthResponse(token, "Bearer", user.getAccount().getId());
    }

    private Account resolveAccount(Long accountId) {
        if (accountId == null) {
            Account account = new Account();
            account.setCreatedAt(LocalDateTime.now());
            return accountRepository.save(account);
        }
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
    }
}
