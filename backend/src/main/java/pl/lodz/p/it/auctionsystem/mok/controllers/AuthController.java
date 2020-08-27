package pl.lodz.p.it.auctionsystem.mok.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.lodz.p.it.auctionsystem.entities.User;
import pl.lodz.p.it.auctionsystem.exceptions.ApplicationException;
import pl.lodz.p.it.auctionsystem.mok.dtos.ApiResponseDto;
import pl.lodz.p.it.auctionsystem.mok.dtos.JwtTokenDto;
import pl.lodz.p.it.auctionsystem.mok.dtos.LoginDto;
import pl.lodz.p.it.auctionsystem.mok.dtos.SignupDto;
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
    
    private final AuthenticationManager authenticationManager;
    
    private final UserServiceImpl userService;
    
    private final JwtTokenUtils jwtTokenUtils;
    
    private final ModelMapper modelMapper;
    
    private final MessageService messageService;
    
    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserServiceImpl userService,
                          JwtTokenUtils jwtTokenUtils, ModelMapper modelMapper, MessageService messageService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtTokenUtils = jwtTokenUtils;
        this.modelMapper = modelMapper;
        this.messageService = messageService;
    }
    
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
    
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignupDto signupDto) throws ApplicationException {
        User user = new User(signupDto.getUsername(), signupDto.getPassword(),
                signupDto.getEmail(), signupDto.getFirstName(), signupDto.getLastName(),
                signupDto.getPhoneNumber());
        
        User result = userService.registerUser(user);
        
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{userId}")
                .buildAndExpand(result.getId()).toUri();
        
        String message = messageService.getMessage("userRegistered");
        
        return ResponseEntity.created(location).body(new ApiResponseDto(true, message));
    }
    
    @PostMapping("/activation")
    public ResponseEntity<?> activateUser(@RequestParam("code") String code) throws ApplicationException {
        userService.activateUser(code);
    
        String message = messageService.getMessage("userDetailsUpdated");
        
        return ResponseEntity.ok().body(new ApiResponseDto(true, message));
    }
}