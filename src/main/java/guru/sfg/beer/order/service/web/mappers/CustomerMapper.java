package guru.sfg.beer.order.service.web.mappers;

import guru.sfg.beer.order.service.domain.Customer;
import guru.sfg.brewery.model.CustomerDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Created by marecm on 5/11/2021
 */
@Mapper(uses = {DateMapper.class})
public interface CustomerMapper {

    Customer customerDtoToCustomer(CustomerDto customerDto);

    @Mapping(target = "name", source = "customerName")
    CustomerDto customerToCustomerDto(Customer customer);
}
