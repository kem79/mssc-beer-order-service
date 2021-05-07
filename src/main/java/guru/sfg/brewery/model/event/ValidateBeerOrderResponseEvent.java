package guru.sfg.brewery.model.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Created by marecm on 5/7/2021
 */
@Data
@AllArgsConstructor
@Builder
public class ValidateBeerOrderResponseEvent {

    private final String orderId;
    private final Boolean isValid;
}
