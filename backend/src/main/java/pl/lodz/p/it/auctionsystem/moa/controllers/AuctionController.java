package pl.lodz.p.it.auctionsystem.moa.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.lodz.p.it.auctionsystem.entities.Auction;
import pl.lodz.p.it.auctionsystem.entities.Bid;
import pl.lodz.p.it.auctionsystem.moa.dtos.AuctionAddDto;
import pl.lodz.p.it.auctionsystem.moa.dtos.AuctionDto;
import pl.lodz.p.it.auctionsystem.moa.services.AuctionService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/auctions")
public class AuctionController {

    private final AuctionService auctionService;

    private final ModelMapper modelMapper;

    @Value("${page.size}")
    private int pageSize;

    @Autowired
    public AuctionController(AuctionService auctionService, ModelMapper modelMapper) {
        this.auctionService = auctionService;
        this.modelMapper = modelMapper;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addAuction(@RequestPart("auction") @Valid AuctionAddDto auctionAddDto,
                                        @RequestPart("file") @Valid @NotBlank MultipartFile file) {
        try {
            byte[] lob = file.getBytes();
            System.out.println(lob.length);
            System.out.println(auctionAddDto.getItemName());
            System.out.println(auctionAddDto.getItemDescription());
            System.out.println(auctionAddDto.getStartingPrice());
            System.out.println(auctionAddDto.getStartDate());
            System.out.println(auctionAddDto.getDuration());
        } catch (IOException e) {
            System.out.println("File upload problem");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAuctions(
            @RequestParam(defaultValue = "0") int page) {
        List<Auction> auctions;
        List<AuctionDto> auctionDtos = new ArrayList<>();
        Pageable paging;
        Page<Auction> auctionPage;

        paging = PageRequest.of(page, pageSize);
        auctionPage = auctionService.getAuctions(paging);
        auctions = auctionPage.getContent();

        if (auctions.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        for (Auction auction : auctions) {
            AuctionDto auctionDto = modelMapper.map(auction, AuctionDto.class);

            auctionDto.setBidsNumber(auction.getBids().size());

            Optional<Bid> highestBid = auction.getBids().stream().max(Comparator.comparing(Bid::getPrice));

            auctionDto.setCurrentPrice(highestBid.map(Bid::getPrice).orElse(null));

            auctionDtos.add(auctionDto);
        }

        Map<String, Object> response = new HashMap<>();

        response.put("auctions", auctionDtos);
        response.put("currentPage", auctionPage.getNumber());
        response.put("totalItems", auctionPage.getTotalElements());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}