package pl.lodz.p.it.auctionsystem.moa.controllers;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    @SubscribeMapping("/auction/changes/{auctionId}")
    public void auctionIdSubscription(@DestinationVariable Long auctionId) {}
}