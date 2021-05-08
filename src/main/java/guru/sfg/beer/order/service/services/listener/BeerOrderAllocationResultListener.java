package guru.sfg.beer.order.service.services.listener;

import guru.sfg.beer.order.service.config.JmsConfig;
import guru.sfg.beer.order.service.services.BeerOrderManager;
import guru.sfg.brewery.model.event.AllocateOrderResultEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * Created by marecm on 5/8/2021
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class BeerOrderAllocationResultListener {

    private final BeerOrderManager beerOrderManager;

    @JmsListener(destination = JmsConfig.VALIDATE_ORDER_RESULT_QUEUE)
    public void listen(AllocateOrderResultEvent event){
        if(!event.getAllocationError() && !event.getPendingInventory()){
            // allocated normally
            beerOrderManager.beerOrderAllocationPassed(event.getBeerOrderDto());
        } else if (!event.getAllocationError() && event.getPendingInventory()){
            // pending inventory
            beerOrderManager.beerOrderAllocationPendingInventory(event.getBeerOrderDto());
        } else if (event.getAllocationError()) {
            // allocation error
            beerOrderManager.beerOrderAllocationFailed(event.getBeerOrderDto());
        }
    }
}
