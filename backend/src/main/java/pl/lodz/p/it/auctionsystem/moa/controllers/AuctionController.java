package pl.lodz.p.it.auctionsystem.moa.controllers;

import org.springframework.beans.factory.annotation.Autowired;
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
import pl.lodz.p.it.auctionsystem.mok.dtos.ApiResponseDto;
import pl.lodz.p.it.auctionsystem.mok.security.services.UserDetailsImpl;
import pl.lodz.p.it.auctionsystem.utils.MessageService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auctions")
public class AuctionController {

    private final AuctionService auctionService;

    private final MessageService messageService;

    @Autowired
    public AuctionController(AuctionService auctionService, MessageService messageService) {
        this.auctionService = auctionService;
        this.messageService = messageService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addAuction(@RequestPart("auction") @Valid AuctionAddDto auctionAddDto,
                                        @RequestPart("file") @Valid @NotBlank MultipartFile file,
                                        Authentication authentication) throws ApplicationException {
        try {
            auctionAddDto.setImage(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        auctionAddDto.setUsername(((UserDetailsImpl) authentication.getPrincipal()).getUsername());

        Long result = auctionService.addAuction(auctionAddDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/auctions/{auctionId}")
                .buildAndExpand(result).toUri();

        String message = messageService.getMessage("info.auctionAdded");

        return ResponseEntity.created(location).body(new ApiResponseDto(true, message));
    }

    @GetMapping
    public ResponseEntity<?> getAuctions(AuctionCriteria auctionCriteria) {
        Page<AuctionDto> auctionDtoPage = auctionService.getAuctions(auctionCriteria);

        if (auctionDtoPage.getContent().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            Map<String, Object> response = new HashMap<>();

            response.put("auctions", auctionDtoPage.getContent());
            response.put("currentPage", auctionDtoPage.getNumber());
            response.put("totalItems", auctionDtoPage.getTotalElements());

            return new ResponseEntity<>(response, HttpStatus.OK);
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

        return new ResponseEntity<>(auctionDetailsDto, HttpStatus.OK);
    }

    /**
     * Aktualizuje dane aukcji o podanym id.
     *
     * @param auctionId      id aukcji
     * @param auctionEditDto obiekt typu {@link AuctionEditDto}
     * @return Kod odpowiedzi HTTP 200 z obiektem typu {@link ApiResponseDto}
     * @throws ApplicationException wyjątek aplikacyjny w przypadku niepowodzenia
     */
    @PatchMapping("/{auctionId}")
    public ResponseEntity<?> updateAuctionDetails(@PathVariable(value = "auctionId") Long auctionId,
                                                  @Valid @RequestBody AuctionEditDto auctionEditDto) throws ApplicationException {
        auctionService.updateAuctionById(auctionId, auctionEditDto);

        String message = messageService.getMessage("info.auctionUpdated");

        return ResponseEntity.ok().body(new ApiResponseDto(true, message));
    }
}