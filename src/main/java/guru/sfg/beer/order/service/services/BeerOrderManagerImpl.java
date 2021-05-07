package guru.sfg.beer.order.service.services;

import guru.sfg.beer.order.service.domain.BeerOrder;
import guru.sfg.beer.order.service.domain.BeerOrderEventEnum;
import guru.sfg.beer.order.service.domain.BeerOrderStatusEnum;
import guru.sfg.beer.order.service.repositories.BeerOrderRepository;
import guru.sfg.beer.order.service.sm.BeerOrderStateInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Created by marecm on 5/6/2021
 */
@RequiredArgsConstructor
@Service
public class BeerOrderManagerImpl implements BeerOrderManager {

    private final StateMachineFactory<BeerOrderStatusEnum, BeerOrderEventEnum> stateMachineFactory;
    private final BeerOrderRepository beerOrderRepository;
    public final static String BEER_ORDER_ID_HEADER = "beer-order-id";
    private final BeerOrderStateInterceptor beerOrderStateInterceptor;

    @Transactional
    @Override
    public BeerOrder newBeerOrder(BeerOrder beerOrder) {
        beerOrder.setId(null);
        beerOrder.setOrderStatus(BeerOrderStatusEnum.NEW);

        BeerOrder savedBeerOrder = beerOrderRepository.save(beerOrder);
        sendBeerOrderEvent(beerOrder, BeerOrderEventEnum.VALIDATE_ORDER);
        return savedBeerOrder;
    }

    @Override
    public void processBeerOrderValidationResult(UUID orderId, Boolean isValid) {
        BeerOrder beerOrder = beerOrderRepository.getOne(orderId);

        if (isValid == Boolean.TRUE) {
            sendBeerOrderEvent(beerOrder, BeerOrderEventEnum.VALIDATION_PASSED);

            BeerOrder savedBeerOrder = beerOrderRepository.findOneById(beerOrder.getId());

            sendBeerOrderEvent(savedBeerOrder, BeerOrderEventEnum.ALLOCATE_ORDER);
        } else {
            sendBeerOrderEvent(beerOrder, BeerOrderEventEnum.VALIDATION_FAILED);
        }
    }

    private void sendBeerOrderEvent(BeerOrder beerOrder, BeerOrderEventEnum eventEnum) {
        StateMachine<BeerOrderStatusEnum, BeerOrderEventEnum> sm = build(beerOrder);

        Message message = MessageBuilder
                .withPayload(eventEnum)
                .setHeader(BEER_ORDER_ID_HEADER, beerOrder.getId().toString())
                .build();
        sm.sendEvent(message);
    }

    private StateMachine<BeerOrderStatusEnum, BeerOrderEventEnum> build(BeerOrder beerOrder) {
        StateMachine<BeerOrderStatusEnum, BeerOrderEventEnum> sm = stateMachineFactory.getStateMachine(beerOrder.getId());

        sm.stop();

        sm.getStateMachineAccessor()
                .doWithAllRegions(sma -> {
                    sma.addStateMachineInterceptor(beerOrderStateInterceptor);
                    sma.resetStateMachine(new DefaultStateMachineContext(beerOrder.getOrderStatus(), null, null, null));
                });

        sm.start();

        return sm;
    }
}
