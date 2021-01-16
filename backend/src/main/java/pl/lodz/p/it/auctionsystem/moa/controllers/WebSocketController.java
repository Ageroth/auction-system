package pl.lodz.p.it.auctionsystem.moa.controllers;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

/**
 * Kontroler obsługujący WebSockety.
 */
@Controller
public class WebSocketController {

    /**
     * Metoda wykorzystywana do zasubskrybowania określonej destynacji.
     *
     * @param auctionId id aukcji, która ma być zasubskrybowana
     */
    @SubscribeMapping("/auction/changes/{auctionId}")
    public void auctionIdSubscription(@DestinationVariable Long auctionId) {}
}