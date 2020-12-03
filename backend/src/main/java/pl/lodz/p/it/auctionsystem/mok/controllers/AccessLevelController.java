package pl.lodz.p.it.auctionsystem.mok.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.auctionsystem.mok.dtos.AccessLevelDto;
import pl.lodz.p.it.auctionsystem.mok.services.AccessLevelService;

import java.util.List;

/**
 * Kontroler obsługujący poziomy dostępu.
 */
@RestController
@RequestMapping("/api/access-levels")
public class AccessLevelController {

    private final AccessLevelService accessLevelService;

    @Autowired
    public AccessLevelController(AccessLevelService accessLevelService) {
        this.accessLevelService = accessLevelService;
    }

    /**
     * Zwraca wszystkie dostępne poziomy dostępu.
     *
     * @return Kod odpowiedzi HTTP 200 z listą obiektów typu {@link AccessLevelDto}
     */
    @GetMapping
    public ResponseEntity<?> getAllAccessLevels() {
        List<AccessLevelDto> accessLevelDtos = accessLevelService.getAllAccessLevels();

        return new ResponseEntity<>(accessLevelDtos, HttpStatus.OK);
    }
}