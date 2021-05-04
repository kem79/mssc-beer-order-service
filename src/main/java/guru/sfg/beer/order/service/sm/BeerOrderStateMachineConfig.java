package guru.sfg.beer.order.service.sm;

import guru.sfg.beer.order.service.domain.BeerOderEventEnum;
import guru.sfg.beer.order.service.domain.BeerOrderStatusEnum;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;

import java.util.EnumSet;

/**
 * Created by marecm on 5/4/2021
 */
@Configuration
@EnableStateMachineFactory
public class BeerOrderStateMachineConfig extends StateMachineConfigurerAdapter<BeerOrderStatusEnum, BeerOderEventEnum> {
    @Override
    public void configure(StateMachineStateConfigurer<BeerOrderStatusEnum, BeerOderEventEnum> states) throws Exception {
        states.withStates()
                .initial(BeerOrderStatusEnum.NEW)
                .states(EnumSet.allOf(BeerOrderStatusEnum.class))
                .end(BeerOrderStatusEnum.PICKED_UP)
                .end(BeerOrderStatusEnum.DELIVERED)
                .end(BeerOrderStatusEnum.VALIDATION_EXCEPTION)
                .end(BeerOrderStatusEnum.ALLOCATION_EXCEPTION)
                .end(BeerOrderStatusEnum.DELIVERY_EXCEPTION);
    }
}
