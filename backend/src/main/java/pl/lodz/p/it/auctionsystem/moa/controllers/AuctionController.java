package pl.lodz.p.it.auctionsystem.moa.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.lodz.p.it.auctionsystem.exceptions.ApplicationException;
import pl.lodz.p.it.auctionsystem.moa.dtos.*;
import pl.lodz.p.it.auctionsystem.moa.services.AuctionService;
import pl.lodz.p.it.auctionsystem.security.services.UserDetailsImpl;
import pl.lodz.p.it.auctionsystem.utils.MessageService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Kontroler obsługujący operacje związane z aukcjami.
 */
@RestController
@RequestMapping("/api/auctions")
@RequiredArgsConstructor
public class AuctionController {

    private final AuctionService auctionService;

    private final MessageService messageService;

    /**
     * Dodaje nową aukcję.
     *
     * @param auctionAddDto  obiekt typu {@link AuctionAddDto}
     * @param file           wgrywany plik
     * @param authentication obiekt typu {@link Authentication}
     * @return Kod odpowiedzi HTTP 201 z obiektem typu {@link ApiResponseDto}
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addAuction(@RequestPart("auction") @Valid AuctionAddDto auctionAddDto,
                                        @RequestPart("file") @Valid @NotBlank MultipartFile file,
                                        Authentication authentication) throws ApplicationException {
        try {
            auctionAddDto.setImage(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        String username = authentication != null ? ((UserDetailsImpl) authentication.getPrincipal()).getUsername() :
                null;

        auctionAddDto.setUsername(username);

        Long result = auctionService.addAuction(auctionAddDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/auctions/{auctionId}")
                .buildAndExpand(result).toUri();

        String message = messageService.getMessage("info.auctionAdded");

        return ResponseEntity.status(HttpStatus.CREATED).location(location).body(new ApiResponseDto(true, message));
    }

    /**
     * Zwraca aukcje spełniające dane kryteria.
     *
     * @param auctionCriteria obiekt typu {@link AuctionCriteria}
     * @return Kod odpowiedzi HTTP 200 z listą stronnicowanych aukcji, aktualnym numerem strony oraz całkowitą ilością
     * aukcji
     */
    @GetMapping
    public ResponseEntity<?> getAuctions(AuctionCriteria auctionCriteria) {
        Page<AuctionDto> auctionDtoPage = auctionService.getAuctions(auctionCriteria);

        if (auctionDtoPage.getContent().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            Map<String, Object> response = new HashMap<>();

            response.put("auctions", auctionDtoPage.getContent());
            response.put("currentPage", auctionDtoPage.getNumber());
            response.put("totalItems", auctionDtoPage.getTotalElements());

            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }

    /**
     * Zwraca własne aukcje spełniające dane kryteria.
     *
     * @param auctionCriteria obiekt typu {@link AuctionCriteria}
     * @param authentication  obiekt typu {@link Authentication}
     * @return Kod odpowiedzi HTTP 200 z listą stronnicowanych aukcji, aktualnym numerem strony oraz całkowitą ilością
     * aukcji
     */
    @GetMapping("/selling")
    public ResponseEntity<?> getOwnAuctions(AuctionCriteria auctionCriteria, Authentication authentication) {
        String username = authentication != null ? ((UserDetailsImpl) authentication.getPrincipal()).getUsername() :
                null;

        Page<AuctionDto> auctionDtoPage = auctionService.getAuctionsByUsername(auctionCriteria, username);

        if (auctionDtoPage.getContent().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            Map<String, Object> response = new HashMap<>();

            response.put("auctions", auctionDtoPage.getContent());
            response.put("currentPage", auctionDtoPage.getNumber());
            response.put("totalItems", auctionDtoPage.getTotalElements());

            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }

    /**
     * Zwraca aukcje, w których bierzemy udział, spełniające dane kryteria.
     *
     * @param auctionCriteria obiekt typu {@link AuctionCriteria}
     * @param authentication  obiekt typu {@link Authentication}
     * @return Kod odpowiedzi HTTP 200 z listą stronnicowanych aukcji, aktualnym numerem strony oraz całkowitą ilością
     * aukcji
     */
    @GetMapping("/buying")
    public ResponseEntity<?> getOwnBiddings(AuctionCriteria auctionCriteria, Authentication authentication) {
        String username = authentication != null ? ((UserDetailsImpl) authentication.getPrincipal()).getUsername() :
                null;

        Page<AuctionDto> auctionDtoPage = auctionService.getParticipatedAuctions(auctionCriteria, username);

        if (auctionDtoPage.getContent().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            Map<String, Object> response = new HashMap<>();

            response.put("auctions", auctionDtoPage.getContent());
            response.put("currentPage", auctionDtoPage.getNumber());
            response.put("totalItems", auctionDtoPage.getTotalElements());

            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }

    /**
     * Zwraca szczegóły aukcji o podanym id.
     *
     * @param auctionId id aukcji
     * @return Kod odpowiedzi HTTP 200 z obiektem typu {@link AuctionDetailsDto}
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    @GetMapping("/{auctionId}")
    public ResponseEntity<?> getAuctionDetails(@PathVariable(value = "auctionId") Long auctionId) throws ApplicationException {
        AuctionDetailsDto auctionDetailsDto = auctionService.getAuctionById(auctionId);

        return ResponseEntity.status(HttpStatus.OK).body(auctionDetailsDto);
    }

    /**
     * Zwraca szczegóły aukcji o podanym id.
     *
     * @param auctionId id aukcji
     * @return Kod odpowiedzi HTTP 200 z obiektem typu {@link AuctionDetailsDto}
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    @GetMapping("/selling/{auctionId}")
    public ResponseEntity<?> getOwnAuctionDetails(@PathVariable(value = "auctionId") Long auctionId) throws ApplicationException {
        AuctionDetailsDto auctionDetailsDto = auctionService.getOwnAuctionById(auctionId);

        return ResponseEntity.status(HttpStatus.OK).body(auctionDetailsDto);
    }

    /**
     * Zwraca szczegóły aukcji o podanym id.
     *
     * @param auctionId      id aukcji
     * @param authentication obiekt typu {@link Authentication}
     * @return Kod odpowiedzi HTTP 200 z obiektem typu {@link AuctionDetailsDto}
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    @GetMapping("/buying/{auctionId}")
    public ResponseEntity<?> getOwnBiddingDetails(@PathVariable(value = "auctionId") Long auctionId,
                                                  Authentication authentication) throws ApplicationException {
        String username = authentication != null ? ((UserDetailsImpl) authentication.getPrincipal()).getUsername() :
                null;

        AuctionDetailsDto auctionDetailsDto = auctionService.getOwnBiddingById(auctionId, username);

        return ResponseEntity.status(HttpStatus.OK).body(auctionDetailsDto);
    }

    /**
     * Aktualizuje dane naszej aukcji o podanym id.
     *
     * @param auctionId        id aukcji
     * @param auctionUpdateDto obiekt typu {@link AuctionUpdateDto}
     * @param authentication   obiekt typu {@link Authentication}
     * @return Kod odpowiedzi HTTP 200 z obiektem typu {@link ApiResponseDto}
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    @PatchMapping("/selling/{auctionId}")
    public ResponseEntity<?> updateAuctionDetails(@PathVariable(value = "auctionId") Long auctionId,
                                                  @Valid @RequestBody AuctionUpdateDto auctionUpdateDto,
                                                  Authentication authentication) throws ApplicationException {
        String username = authentication != null ? ((UserDetailsImpl) authentication.getPrincipal()).getUsername() :
                null;

        auctionService.updateAuctionById(auctionId, auctionUpdateDto, username);

        String message = messageService.getMessage("info.auctionUpdated");

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto(true, message));
    }

    /**
     * Dodaje nową ofertę do aukcji o podanym id.
     *
     * @param auctionId      id aukcji
     * @param bidPlaceDto    obiekt typu {@link BidPlaceDto}
     * @param authentication obiekt typu {@link Authentication}
     * @return Kod odpowiedzi HTTP 200 z obiektem typu {@link ApiResponseDto}
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    @PostMapping("/{auctionId}")
    public ResponseEntity<?> placeABid(@PathVariable(value = "auctionId") Long auctionId,
                                       @Valid @RequestBody BidPlaceDto bidPlaceDto, Authentication authentication) throws ApplicationException {
        String username = authentication != null ? ((UserDetailsImpl) authentication.getPrincipal()).getUsername() :
                null;

        bidPlaceDto.setUsername(username);

        Long result = auctionService.addBid(auctionId, bidPlaceDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/auctions/{auctionId}")
                .buildAndExpand(result).toUri();

        String message = messageService.getMessage("info.bidPlaced");

        return ResponseEntity.status(HttpStatus.CREATED).location(location).body(new ApiResponseDto(true, message));
    }

    /**
     * Usuwa aukcję o podanym id.
     *
     * @param auctionId      id aukcji
     * @param authentication obiekt typu {@link Authentication}
     * @return Kod odpowiedzi HTTP 200 z obiektem typu {@link ApiResponseDto}
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    @DeleteMapping("/{auctionId}")
    public ResponseEntity<?> deleteAuction(@PathVariable(value = "auctionId") Long auctionId,
                                           Authentication authentication) throws ApplicationException {
        String username = authentication != null ? ((UserDetailsImpl) authentication.getPrincipal()).getUsername() :
                null;

        auctionService.deleteAuctionById(auctionId, username);

        String message = messageService.getMessage("info.auctionDeleted");

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto(true, message));
    }
}