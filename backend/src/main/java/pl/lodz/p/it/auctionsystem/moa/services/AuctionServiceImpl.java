package pl.lodz.p.it.auctionsystem.moa.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.auctionsystem.entities.Auction;
import pl.lodz.p.it.auctionsystem.entities.Bid;
import pl.lodz.p.it.auctionsystem.entities.Item;
import pl.lodz.p.it.auctionsystem.entities.User;
import pl.lodz.p.it.auctionsystem.exceptions.ApplicationException;
import pl.lodz.p.it.auctionsystem.exceptions.EntityNotFoundException;
import pl.lodz.p.it.auctionsystem.moa.dtos.AuctionAddDto;
import pl.lodz.p.it.auctionsystem.moa.dtos.AuctionCriteria;
import pl.lodz.p.it.auctionsystem.moa.dtos.AuctionDto;
import pl.lodz.p.it.auctionsystem.moa.repositories.AuctionRepository;
import pl.lodz.p.it.auctionsystem.mok.repositories.UserRepository;
import pl.lodz.p.it.auctionsystem.mok.utils.MessageService;
import pl.lodz.p.it.auctionsystem.mok.utils.SortDirection;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Optional;

import static pl.lodz.p.it.auctionsystem.moa.utils.AuctionSpecs.containsTextInName;

@Service
@Transactional(rollbackFor = ApplicationException.class)
public class AuctionServiceImpl implements AuctionService {

    private final AuctionRepository auctionRepository;

    private final UserRepository userRepository;

    private final MessageService messageService;

    private final ModelMapper modelMapper;

    @Value("${page.size}")
    private int pageSize;

    @Autowired
    public AuctionServiceImpl(AuctionRepository auctionRepository, UserRepository userRepository,
                              MessageService messageService, ModelMapper modelMapper) {
        this.auctionRepository = auctionRepository;
        this.userRepository = userRepository;
        this.messageService = messageService;
        this.modelMapper = modelMapper;
    }

    @Override
    public Long addAuction(AuctionAddDto auctionAddDto) throws ApplicationException {
        String userNotFoundMessage = messageService.getMessage("exception.userNotFound");
        User user =
                userRepository.findByUsernameIgnoreCase(auctionAddDto.getUsername()).orElseThrow(() -> new EntityNotFoundException(userNotFoundMessage));
        Item item = new Item(auctionAddDto.getItemName(), auctionAddDto.getItemDescription(), auctionAddDto.getImage());
        LocalDateTime startDate;

        if (auctionAddDto.getStartDate() == null)
            startDate = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        else
            startDate = auctionAddDto.getStartDate().truncatedTo(ChronoUnit.MINUTES);

        LocalDateTime endDate = startDate.plusDays(auctionAddDto.getDuration());

        Auction auction = new Auction(auctionAddDto.getStartingPrice(), startDate, endDate, user,
                item);

        return auctionRepository.save(auction).getId();
    }

    @Override
    public Page<AuctionDto> getAuctions(AuctionCriteria auctionCriteria) {
        Pageable pageable;
        Page<Auction> auctionPage;

        if (auctionCriteria.getSortField() != null && auctionCriteria.getOrder() != null)
            pageable = PageRequest.of(auctionCriteria.getPage(), pageSize,
                    Sort.by(SortDirection.getSortDirection(auctionCriteria.getOrder()),
                            auctionCriteria.getSortField()));
        else
            pageable = PageRequest.of(auctionCriteria.getPage(), pageSize,
                    Sort.by(Sort.Direction.ASC, "startDate"));

        if (auctionCriteria.getQuery() == null && auctionCriteria.getStatus() == null)
            auctionPage = auctionRepository.findAll(pageable);
        else if (auctionCriteria.getQuery() != null && auctionCriteria.getStatus() == null)
            auctionPage = auctionRepository.findAll(containsTextInName(auctionCriteria.getQuery()), pageable);
//        else if (auctionCriteria.getQuery() == null) {
//            auctionPage = auctionRepository.findAll(isActive(auctionCriteria.getStatus()), pageable);
//        } else
//            auctionPage =
//                    auctionRepository.findAll(containsTextInName(auctionCriteria.getQuery()).and(isActive
//                    (auctionCriteria.getStatus())), pageable);

        return auctionPage.map(auction -> {
            AuctionDto auctionDto = modelMapper.map(auction, AuctionDto.class);
            Optional<Bid> highestBid = auction.getBids().stream().max(Comparator.comparing(Bid::getPrice));

            auctionDto.setBidsNumber(auction.getBids().size());
            auctionDto.setCurrentPrice(highestBid.map(Bid::getPrice).orElse(null));

            return auctionDto;
        });
    }
}