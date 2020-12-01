package pl.lodz.p.it.auctionsystem.moa.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.auctionsystem.entities.Auction;
import pl.lodz.p.it.auctionsystem.entities.Item;
import pl.lodz.p.it.auctionsystem.entities.User;
import pl.lodz.p.it.auctionsystem.exceptions.ApplicationException;
import pl.lodz.p.it.auctionsystem.exceptions.EntityNotFoundException;
import pl.lodz.p.it.auctionsystem.moa.dtos.AuctionAddDto;
import pl.lodz.p.it.auctionsystem.moa.dtos.AuctionDto;
import pl.lodz.p.it.auctionsystem.moa.repositories.AuctionRepository;
import pl.lodz.p.it.auctionsystem.mok.repositories.UserRepository;
import pl.lodz.p.it.auctionsystem.mok.utils.MessageService;

import java.time.LocalDateTime;

@Service
@Transactional(rollbackFor = ApplicationException.class)
public class AuctionServiceImpl implements AuctionService {

    private final AuctionRepository auctionRepository;

    private final UserRepository userRepository;

    private final MessageService messageService;

    @Autowired
    public AuctionServiceImpl(AuctionRepository auctionRepository, UserRepository userRepository,
                              MessageService messageService) {
        this.auctionRepository = auctionRepository;
        this.userRepository = userRepository;
        this.messageService = messageService;
    }

    @Override
    public Long addAuction(AuctionAddDto auctionAddDto) throws ApplicationException {
        String userNotFoundMessage = messageService.getMessage("exception.userNotFound");
        User user =
                userRepository.findByUsername(auctionAddDto.getUsername()).orElseThrow(() -> new EntityNotFoundException(userNotFoundMessage));
        Item item = new Item(auctionAddDto.getItemName(), auctionAddDto.getItemDescription(), auctionAddDto.getImage());
        LocalDateTime endDate = auctionAddDto.getStartDate().plusDays(auctionAddDto.getDuration());
        Auction auction = new Auction(auctionAddDto.getStartingPrice(), auctionAddDto.getStartDate(), endDate, user, item);

        return auctionRepository.save(auction).getId();
    }

    @Override
    public Page<Auction> getAuctions(Pageable pageable) {
        return auctionRepository.findAll(pageable);
    }
}
