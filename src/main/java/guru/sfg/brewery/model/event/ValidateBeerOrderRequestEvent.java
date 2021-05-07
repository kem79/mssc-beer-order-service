package guru.sfg.brewery.model.event;

import guru.sfg.brewery.model.BeerOrderDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Created by marecm on 5/6/2021
 */
@Data
@AllArgsConstructor
@Builder
public class ValidateBeerOrderRequestEvent {

    BeerOrderDto beerOrderDto;
}
