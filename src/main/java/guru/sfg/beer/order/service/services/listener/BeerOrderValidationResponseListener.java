package guru.sfg.beer.order.service.services.listener;

import guru.sfg.beer.order.service.config.JmsConfig;
import guru.sfg.beer.order.service.services.BeerOrderManager;
import guru.sfg.brewery.model.event.ValidateBeerOrderResponseEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Created by marecm on 5/7/2021
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class BeerOrderValidationResponseListener {

    private final BeerOrderManager beerOrderManager;

    @JmsListener(destination = JmsConfig.VALIDATE_ORDER_RESULT_QUEUE)
    public void listen(ValidateBeerOrderResponseEvent event) {
        log.debug("Processing beer order validation response " + event.toString());
        beerOrderManager.processBeerOrderValidationResult(UUID.fromString(event.getOrderId()), event.getIsValid());
    }
}
