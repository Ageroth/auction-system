package pl.lodz.p.it.auctionsystem.mok.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.lodz.p.it.auctionsystem.entities.User;
import pl.lodz.p.it.auctionsystem.exceptions.ApplicationException;
import pl.lodz.p.it.auctionsystem.mok.dtos.ApiResponseDto;
import pl.lodz.p.it.auctionsystem.mok.dtos.JwtTokenDto;
import pl.lodz.p.it.auctionsystem.mok.dtos.LoginDto;
import pl.lodz.p.it.auctionsystem.mok.dtos.SignUpDto;
import pl.lodz.p.it.auctionsystem.mok.services.UserServiceImpl;
import pl.lodz.p.it.auctionsystem.mok.utils.MessageService;
import pl.lodz.p.it.auctionsystem.security.jwt.JwtTokenUtils;
import pl.lodz.p.it.auctionsystem.security.services.UserDetailsImpl;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    private AuthenticationManager authenticationManager;
    
    private UserServiceImpl userService;
    
    private JwtTokenUtils jwtTokenUtils;
    
    private MessageService messageService;
    
    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
    
    @Autowired
    public void setUserService(UserServiceImpl userService) {
        this.userService = userService;
    }
    
    @Autowired
    public void setJwtTokenUtils(JwtTokenUtils jwtTokenUtils) {
        this.jwtTokenUtils = jwtTokenUtils;
    }
    
    @Autowired
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }
    
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenUtils.generateToken(authentication);
        
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> accessLevels = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        
        return new ResponseEntity<>(
                new JwtTokenDto(jwt, userDetails.getUsername(), accessLevels),
                HttpStatus.OK);
    }
    
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpDto signUpDto) throws ApplicationException {
        final User user = new User(signUpDto.getUsername(), signUpDto.getPassword(),
                signUpDto.getEmail(), signUpDto.getFirstName(), signUpDto.getLastName(),
                signUpDto.getPhoneNumber());
        
        final User result = userService.registerUser(user);
        
        final URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();
        
        final String message = messageService.getMessage("userRegistration");
        
        return ResponseEntity.created(location).body(new ApiResponseDto(true, message));
    }
}