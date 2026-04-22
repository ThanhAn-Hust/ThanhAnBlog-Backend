package cloud.lmao.backend.controller;

import cloud.lmao.backend.security.JwtService;
import cloud.lmao.backend.security.TokenBlacklistService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final TokenBlacklistService tokenBlacklistService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        
        UserDetails user = userDetailsService.loadUserByUsername(request.getUsername());
        String jwtToken = jwtService.generateToken(user);
        
        return ResponseEntity.ok(new AuthResponse(jwtToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            long expirationTime = jwtService.extractExpiration(jwt).getTime() - System.currentTimeMillis();
            if (expirationTime > 0) {
                tokenBlacklistService.addToBlacklist(jwt, expirationTime);
            }
        }
        return ResponseEntity.ok("Logged out successfully");
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class AuthRequest {
    private String username;
    private String password;
}

@Data
@AllArgsConstructor
class AuthResponse {
    private String token;
}
