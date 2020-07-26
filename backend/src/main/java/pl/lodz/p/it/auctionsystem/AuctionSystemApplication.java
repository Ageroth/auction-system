package pl.lodz.p.it.auctionsystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.lodz.p.it.auctionsystem.entities.User;
import pl.lodz.p.it.auctionsystem.exceptions.ApplicationException;
import pl.lodz.p.it.auctionsystem.mok.services.UserService;

@SpringBootApplication
public class AuctionSystemApplication implements CommandLineRunner {
    
    @Autowired
    private UserService userService;
    
    public static void main(String[] args) {
        SpringApplication.run(AuctionSystemApplication.class, args);
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Override
    public void run(String... args) throws ApplicationException {
        User user = new User();
        user.setFirstName("El Pablo");
        user.setLastName("Escobar");
        user.setPhoneNumber("123456789");
        
        userService.updateUserDetailsByUserLogin("Ageroth", user);
    }
}
