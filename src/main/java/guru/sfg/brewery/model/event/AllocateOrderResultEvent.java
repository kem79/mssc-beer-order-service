package guru.sfg.brewery.model.event;

import guru.sfg.brewery.model.BeerOrderDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by marecm on 5/8/2021
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AllocateOrderResultEvent {
    private BeerOrderDto beerOrderDto;
    private Boolean allocationError = Boolean.FALSE;
    private Boolean pendingInventory = Boolean.FALSE;
}
