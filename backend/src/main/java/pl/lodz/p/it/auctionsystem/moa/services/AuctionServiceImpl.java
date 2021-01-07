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
import pl.lodz.p.it.auctionsystem.exceptions.*;
import pl.lodz.p.it.auctionsystem.moa.dtos.*;
import pl.lodz.p.it.auctionsystem.moa.repositories.AuctionRepository;
import pl.lodz.p.it.auctionsystem.moa.repositories.BidRepository;
import pl.lodz.p.it.auctionsystem.moa.repositories.UserRepositoryMoa;
import pl.lodz.p.it.auctionsystem.utils.MessageService;
import pl.lodz.p.it.auctionsystem.utils.SortDirection;

import java.math.BigDecimal;
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
        else {
            startDate = auctionAddDto.getStartDate().truncatedTo(ChronoUnit.MINUTES);

            if (startDate.isBefore(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))) {
                String addingInvalidDateMessage = messageService.getMessage("exception.addingInvalidDateException");

                throw new InvalidDateException(addingInvalidDateMessage);
            }
        }

        LocalDateTime endDate = startDate.plusDays(auctionAddDto.getDuration());

        Auction auction = new Auction(auctionAddDto.getStartingPrice().setScale(2, RoundingMode.DOWN), startDate,
                endDate, user, item);

        return auctionRepository.save(auction).getId();
    }

    @Override
    @PreAuthorize("hasAnyRole('MANAGER','CLIENT')")
    public Page<BasicAuctionDto> getAuctions(AuctionCriteria auctionCriteria) {
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
            BasicAuctionDto basicAuctionDto = modelMapper.map(auction, BasicAuctionDto.class);
            Optional<Bid> highestBid = auction.getBids().stream().max(Comparator.comparing(Bid::getPrice));

            basicAuctionDto.setBidsNumber(auction.getBids().size());
            basicAuctionDto.setCurrentPrice(highestBid.map(Bid::getPrice).orElse(null));

            return basicAuctionDto;
        });
    }

    @Override
    @PreAuthorize("hasRole('MANAGER')")
    public Page<BasicAuctionDto> getAuctionsByUsername(AuctionCriteria auctionCriteria, String username) {
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
            BasicAuctionDto basicAuctionDto = modelMapper.map(auction, BasicAuctionDto.class);
            Optional<Bid> highestBid = auction.getBids().stream().max(Comparator.comparing(Bid::getPrice));

            basicAuctionDto.setBidsNumber(auction.getBids().size());
            basicAuctionDto.setCurrentPrice(highestBid.map(Bid::getPrice).orElse(null));

            return basicAuctionDto;
        });
    }

    @Override
    @PreAuthorize("hasRole('CLIENT')")
    public Page<BasicAuctionDto> getParticipatedAuctions(AuctionCriteria auctionCriteria, String username) {
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
            BasicAuctionDto basicAuctionDto = modelMapper.map(auction, BasicAuctionDto.class);
            Optional<Bid> highestBid = auction.getBids().stream().max(Comparator.comparing(Bid::getPrice));

            basicAuctionDto.setBidsNumber(auction.getBids().size());
            basicAuctionDto.setCurrentPrice(highestBid.map(Bid::getPrice).orElse(null));
            basicAuctionDto.setTopBidderName(highestBid.map(bid -> bid.getUser().getUsername()).orElse(null));

            return basicAuctionDto;
        });
    }

    @Override
    @PreAuthorize("hasAnyRole('MANAGER','CLIENT')")
    public AuctionDto getAuctionById(Long auctionId) throws ApplicationException {
        String auctionNotFoundMessage = messageService.getMessage("exception.auctionNotFound");
        Auction auction =
                auctionRepository.findById(auctionId).orElseThrow(() -> new EntityNotFoundException(auctionNotFoundMessage));

        return modelMapper.map(auction, AuctionDto.class);
    }

    @Override
    @PreAuthorize("hasRole('MANAGER')")
    @PostAuthorize("returnObject.userUsername == authentication.principal.username")
    public AuctionDto getOwnAuctionById(Long auctionId) throws ApplicationException {
        String auctionNotFoundMessage = messageService.getMessage("exception.auctionNotFound");
        Auction auction =
                auctionRepository.findById(auctionId).orElseThrow(() -> new EntityNotFoundException(auctionNotFoundMessage));

        return modelMapper.map(auction, AuctionDto.class);
    }

    @Override
    @PreAuthorize("hasRole('CLIENT')")
    public AuctionDto getOwnBiddingById(Long auctionId, String username) throws ApplicationException {
        String auctionNotFoundMessage = messageService.getMessage("exception.auctionNotFound");
        Auction auction =
                auctionRepository.findById(auctionId).orElseThrow(() -> new EntityNotFoundException(auctionNotFoundMessage));

        if (auction.getBids().stream().noneMatch(bid -> bid.getUser().getUsername().equals(username))) {
            String accessForbiddenMessage = messageService.getMessage("exception.accessForbiddenException");

            throw new AccessForbiddenException(accessForbiddenMessage);
        }

        return modelMapper.map(auction, AuctionDto.class);
    }

    @Override
    @PreAuthorize("hasRole('MANAGER')")
    public void updateAuctionById(Long auctionId, AuctionUpdateDto auctionUpdateDto, String username, String ifMatch) throws ApplicationException {
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

        Auction auctionCopy = new Auction(Long.parseLong(ifMatch.replace("\"", "")), auction.getBusinessKey(),
                auction.getId(), auctionUpdateDto.getStartingPrice(), auction.getStartDate(), auction.getEndDate(),
                auction.getUser(), new Item(Long.parseLong(ifMatch.replace("\"", "")),
                auction.getItem().getBusinessKey(), auction.getItem().getId(), auctionUpdateDto.getItemName(),
                auctionUpdateDto.getItemDescription(), auction.getItem().getImage()));

        auctionRepository.saveAndFlush(auctionCopy);
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

        if (auction.getEndDate().isBefore(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)) ||
                auction.getStartDate().isAfter(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))) {
            String bidPlaceInvalidDateMessage = messageService.getMessage("exception.bidPlaceInvalidDateException");

            throw new InvalidDateException(bidPlaceInvalidDateMessage);
        }

        if (auction.getUser().getUsername().equals(bidPlaceDto.getUsername())) {
            String auctionOwnerMessage = messageService.getMessage("exception.auctionOwnerException");

            throw new AuctionOwnerException(auctionOwnerMessage);
        }

        BigDecimal currentPrice;

        bidPlaceDto.setPrice(bidPlaceDto.getPrice().setScale(2, RoundingMode.DOWN));

        if (auction.getBids().size() > 0) {
            currentPrice = auction.getBids().stream()
                    .map(Bid::getPrice)
                    .min(Comparator.naturalOrder())
                    .orElse(BigDecimal.ZERO);
        } else {
            currentPrice = auction.getStartingPrice();
        }

        if (bidPlaceDto.getPrice().compareTo(currentPrice) <= 0) {
            String invalidBidPriceMessage = messageService.getMessage("exception.invalidBidPriceException");

            throw new InvalidBidPriceException(invalidBidPriceMessage);
        }

        LocalDateTime date = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);

        Bid bid = new Bid(date, bidPlaceDto.getPrice(), user, auction);

        Bid savedBid = bidRepository.save(bid);

        return savedBid.getId();
    }

    @Override
    @PreAuthorize("hasRole('MANAGER')")
    public void deleteAuctionById(Long auctionId, String username) throws ApplicationException {
        String auctionNotFoundMessage = messageService.getMessage("exception.auctionNotFound");
        Auction auction =
                auctionRepository.findById(auctionId).orElseThrow(() -> new EntityNotFoundException(auctionNotFoundMessage));

        if (!auction.getUser().getUsername().equals(username)) {
            String accessForbiddenMessage = messageService.getMessage("exception.accessForbiddenException");

            throw new AccessForbiddenException(accessForbiddenMessage);
        }

        if (auction.getStartDate().isBefore(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))) {
            String deletionInvalidDateMessage = messageService.getMessage("exception.deletionInvalidDateException");

            throw new InvalidDateException(deletionInvalidDateMessage);
        }

        auctionRepository.delete(auction);
    }
}