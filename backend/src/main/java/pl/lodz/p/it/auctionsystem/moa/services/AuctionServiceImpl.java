package pl.lodz.p.it.auctionsystem.moa.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.auctionsystem.entities.Auction;
import pl.lodz.p.it.auctionsystem.entities.Bid;
import pl.lodz.p.it.auctionsystem.entities.Item;
import pl.lodz.p.it.auctionsystem.entities.User;
import pl.lodz.p.it.auctionsystem.exceptions.AccessForbiddenException;
import pl.lodz.p.it.auctionsystem.exceptions.ApplicationException;
import pl.lodz.p.it.auctionsystem.exceptions.EntityNotFoundException;
import pl.lodz.p.it.auctionsystem.moa.dtos.*;
import pl.lodz.p.it.auctionsystem.moa.repositories.AuctionRepository;
import pl.lodz.p.it.auctionsystem.moa.repositories.BidRepository;
import pl.lodz.p.it.auctionsystem.moa.repositories.UserRepositoryMoa;
import pl.lodz.p.it.auctionsystem.utils.MessageService;
import pl.lodz.p.it.auctionsystem.utils.SortDirection;

import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Optional;

import static pl.lodz.p.it.auctionsystem.moa.utils.AuctionSpecs.*;

@Service
@Transactional(rollbackFor = ApplicationException.class)
@RequiredArgsConstructor
public class AuctionServiceImpl implements AuctionService {

    private final AuctionRepository auctionRepository;

    private final BidRepository bidRepository;

    private final UserRepositoryMoa userRepositoryMoa;

    private final MessageService messageService;

    private final ModelMapper modelMapper;

    @Value("${page.size}")
    private int pageSize;

    @Override
    @PreAuthorize("hasRole('MANAGER')")
    public Long addAuction(AuctionAddDto auctionAddDto) throws ApplicationException {
        String userNotFoundMessage = messageService.getMessage("exception.userNotFound");
        User user =
                userRepositoryMoa.findByUsernameIgnoreCase(auctionAddDto.getUsername()).orElseThrow(() -> new EntityNotFoundException(userNotFoundMessage));
        Item item = new Item(auctionAddDto.getItemName(), auctionAddDto.getItemDescription(), auctionAddDto.getImage());
        LocalDateTime startDate;

        if (auctionAddDto.getStartDate() == null)
            startDate = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        else
            startDate = auctionAddDto.getStartDate().truncatedTo(ChronoUnit.MINUTES);

        LocalDateTime endDate = startDate.plusDays(auctionAddDto.getDuration());

        Auction auction = new Auction(auctionAddDto.getStartingPrice().setScale(2, RoundingMode.DOWN), startDate,
                endDate, user, item);

        return auctionRepository.save(auction).getId();
    }

    @Override
    @PreAuthorize("hasAnyRole('MANAGER','CLIENT')")
    public Page<AuctionDto> getAuctions(AuctionCriteria auctionCriteria) {
        Pageable pageable;
        Page<Auction> auctionPage;

        if (auctionCriteria.getSortField() != null && auctionCriteria.getOrder() != null)
            pageable = PageRequest.of(auctionCriteria.getPage(), pageSize,
                    Sort.by(SortDirection.getSortDirection(auctionCriteria.getOrder()),
                            auctionCriteria.getSortField()));
        else
            pageable = PageRequest.of(auctionCriteria.getPage(), pageSize);

        if (auctionCriteria.getQuery() == null && auctionCriteria.getStatus() == null)
            auctionPage = auctionRepository.findAll(pageable);
        else if (auctionCriteria.getQuery() != null && auctionCriteria.getStatus() == null)
            auctionPage = auctionRepository.findAll(containsTextInName(auctionCriteria.getQuery()), pageable);
        else if (auctionCriteria.getQuery() == null) {
            auctionPage = auctionRepository.findAll(hasStatus(auctionCriteria.getStatus()), pageable);
        } else
            auctionPage =
                    auctionRepository.findAll(containsTextInName(auctionCriteria.getQuery()).and(hasStatus(auctionCriteria.getStatus())), pageable);

        return auctionPage.map(auction -> {
            AuctionDto auctionDto = modelMapper.map(auction, AuctionDto.class);
            Optional<Bid> highestBid = auction.getBids().stream().max(Comparator.comparing(Bid::getPrice));

            auctionDto.setBidsNumber(auction.getBids().size());
            auctionDto.setCurrentPrice(highestBid.map(Bid::getPrice).orElse(null));

            return auctionDto;
        });
    }

    @Override
    @PreAuthorize("hasRole('MANAGER')")
    public Page<AuctionDto> getAuctionsByUsername(AuctionCriteria auctionCriteria, String username) {
        Pageable pageable;
        Page<Auction> auctionPage;

        if (auctionCriteria.getSortField() != null && auctionCriteria.getOrder() != null)
            pageable = PageRequest.of(auctionCriteria.getPage(), pageSize,
                    Sort.by(SortDirection.getSortDirection(auctionCriteria.getOrder()),
                            auctionCriteria.getSortField()));
        else
            pageable = PageRequest.of(auctionCriteria.getPage(), pageSize);

        if (auctionCriteria.getQuery() == null && auctionCriteria.getStatus() == null)
            auctionPage = auctionRepository.findAll(isOwner(username), pageable);
        else if (auctionCriteria.getQuery() != null && auctionCriteria.getStatus() == null)
            auctionPage =
                    auctionRepository.findAll(isOwner(username).and(containsTextInName(auctionCriteria.getQuery())),
                            pageable);
        else if (auctionCriteria.getQuery() == null) {
            auctionPage = auctionRepository.findAll(isOwner(username).and(hasStatus(auctionCriteria.getStatus())),
                    pageable);
        } else
            auctionPage =
                    auctionRepository.findAll(isOwner(username).and(containsTextInName(auctionCriteria.getQuery()).and(hasStatus(auctionCriteria.getStatus()))), pageable);

        return auctionPage.map(auction -> {
            AuctionDto auctionDto = modelMapper.map(auction, AuctionDto.class);
            Optional<Bid> highestBid = auction.getBids().stream().max(Comparator.comparing(Bid::getPrice));

            auctionDto.setBidsNumber(auction.getBids().size());
            auctionDto.setCurrentPrice(highestBid.map(Bid::getPrice).orElse(null));

            return auctionDto;
        });
    }

    @Override
    @PreAuthorize("hasRole('CLIENT')")
    public Page<AuctionDto> getParticipatedAuctions(AuctionCriteria auctionCriteria, String username) {
        Pageable pageable;
        Page<Auction> auctionPage;

        if (auctionCriteria.getSortField() != null && auctionCriteria.getOrder() != null)
            pageable = PageRequest.of(auctionCriteria.getPage(), pageSize,
                    Sort.by(SortDirection.getSortDirection(auctionCriteria.getOrder()),
                            auctionCriteria.getSortField()));
        else
            pageable = PageRequest.of(auctionCriteria.getPage(), pageSize);

        if (auctionCriteria.getQuery() == null && auctionCriteria.getStatus() == null)
            auctionPage = auctionRepository.findAll(hasBid(username), pageable);
        else if (auctionCriteria.getQuery() != null && auctionCriteria.getStatus() == null)
            auctionPage =
                    auctionRepository.findAll(hasBid(username).and(containsTextInName(auctionCriteria.getQuery())),
                            pageable);
        else if (auctionCriteria.getQuery() == null) {
            auctionPage = auctionRepository.findAll(hasBid(username).and(hasStatus(auctionCriteria.getStatus())),
                    pageable);
        } else
            auctionPage =
                    auctionRepository.findAll(hasBid(username).and(containsTextInName(auctionCriteria.getQuery()).and(hasStatus(auctionCriteria.getStatus()))), pageable);

        return auctionPage.map(auction -> {
            AuctionDto auctionDto = modelMapper.map(auction, AuctionDto.class);
            Optional<Bid> highestBid = auction.getBids().stream().max(Comparator.comparing(Bid::getPrice));

            auctionDto.setBidsNumber(auction.getBids().size());
            auctionDto.setCurrentPrice(highestBid.map(Bid::getPrice).orElse(null));
            auctionDto.setTopBidderName(highestBid.map(bid -> bid.getUser().getUsername()).orElse(null));

            return auctionDto;
        });
    }

    @Override
    @PreAuthorize("hasAnyRole('MANAGER','CLIENT')")
    public AuctionDetailsDto getAuctionById(Long auctionId) throws ApplicationException {
        String auctionNotFoundMessage = messageService.getMessage("exception.auctionNotFound");
        Auction auction =
                auctionRepository.findById(auctionId).orElseThrow(() -> new EntityNotFoundException(auctionNotFoundMessage));

        return modelMapper.map(auction, AuctionDetailsDto.class);
    }

    @Override
    @PreAuthorize("hasRole('MANAGER')")
    @PostAuthorize("returnObject.userUsername == authentication.principal.username")
    public AuctionDetailsDto getOwnAuctionById(Long auctionId) throws ApplicationException {
        String auctionNotFoundMessage = messageService.getMessage("exception.auctionNotFound");
        Auction auction =
                auctionRepository.findById(auctionId).orElseThrow(() -> new EntityNotFoundException(auctionNotFoundMessage));

        return modelMapper.map(auction, AuctionDetailsDto.class);
    }

    @Override
    @PreAuthorize("hasRole('CLIENT')")
    public AuctionDetailsDto getOwnBiddingById(Long auctionId, String username) throws ApplicationException {
        String auctionNotFoundMessage = messageService.getMessage("exception.auctionNotFound");
        Auction auction =
                auctionRepository.findById(auctionId).orElseThrow(() -> new EntityNotFoundException(auctionNotFoundMessage));

        if (auction.getBids().stream().noneMatch(bid -> bid.getUser().getUsername().equals(username))) {
            String accessForbiddenMessage = messageService.getMessage("exception.accessForbiddenException");

            throw new AccessForbiddenException(accessForbiddenMessage);
        }

        return modelMapper.map(auction, AuctionDetailsDto.class);
    }

    @Override
    @PreAuthorize("hasRole('MANAGER')")
    public void updateAuctionById(Long auctionId, AuctionUpdateDto auctionUpdateDto, String username) throws ApplicationException {
        String auctionNotFoundMessage = messageService.getMessage("exception.auctionNotFound");
        Auction auction =
                auctionRepository.findById(auctionId).orElseThrow(() -> new EntityNotFoundException(auctionNotFoundMessage));

        if (!auction.getUser().getUsername().equals(username)) {
            String accessForbiddenMessage = messageService.getMessage("exception.accessForbiddenException");

            throw new AccessForbiddenException(accessForbiddenMessage);
        }

        auction.setStartingPrice(auctionUpdateDto.getStartingPrice());
        auction.getItem().setName(auctionUpdateDto.getItemName());
        auction.getItem().setDescription(auctionUpdateDto.getItemDescription());
    }

    @Override
    @PreAuthorize("hasRole('CLIENT')")
    public Long addBid(Long auctionId, BidPlaceDto bidPlaceDto) throws ApplicationException {
        String userNotFoundMessage = messageService.getMessage("exception.userNotFound");
        User user =
                userRepositoryMoa.findByUsernameIgnoreCase(bidPlaceDto.getUsername()).orElseThrow(() -> new EntityNotFoundException(userNotFoundMessage));
        String auctionNotFoundMessage = messageService.getMessage("exception.auctionNotFound");
        Auction auction =
                auctionRepository.findById(auctionId).orElseThrow(() -> new EntityNotFoundException(auctionNotFoundMessage));
        LocalDateTime date = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);

        Bid bid = new Bid(date, bidPlaceDto.getPrice().setScale(2, RoundingMode.DOWN), user, auction);

        Bid savedBid = bidRepository.save(bid);

        return savedBid.getId();
    }
}