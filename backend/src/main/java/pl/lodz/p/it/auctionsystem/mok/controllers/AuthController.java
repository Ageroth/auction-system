package pl.lodz.p.it.auctionsystem.mok.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.auctionsystem.mok.dtos.JwtTokenDto;
import pl.lodz.p.it.auctionsystem.mok.dtos.LoginDto;
import pl.lodz.p.it.auctionsystem.security.jwt.JwtTokenUtils;
import pl.lodz.p.it.auctionsystem.security.services.UserDetailsImpl;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Kontroler służący do uwierzytelnienia użytkownika.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    private final AuthenticationManager authenticationManager;
    
    private final JwtTokenUtils jwtTokenUtils;
    
    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtTokenUtils jwtTokenUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtils = jwtTokenUtils;
    }
    
    /**
     * Uwierzytelnia użytkownika na podstawie przesłanych danych i zwraca odpowiedź.
     *
     * @param loginDto obiekt typu {@link LoginDto}
     * @return HTTP Status 200 z obiektem typu {@link JwtTokenDto}
     */
    @PostMapping("/login")
    public ResponseEntity<?> logIn(@Valid @RequestBody LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        String jwt = jwtTokenUtils.generateToken(authentication);
        
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        List<String> accessLevels = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok().body(new JwtTokenDto(jwt, userDetails.getUsername(), accessLevels));
    }
}