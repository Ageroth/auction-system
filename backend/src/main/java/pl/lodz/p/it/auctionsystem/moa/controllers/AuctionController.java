package pl.lodz.p.it.auctionsystem.moa.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.auctionsystem.entities.Auction;
import pl.lodz.p.it.auctionsystem.entities.Bid;
import pl.lodz.p.it.auctionsystem.moa.dtos.AuctionDto;
import pl.lodz.p.it.auctionsystem.moa.services.AuctionService;

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