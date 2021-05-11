package guru.sfg.beer.order.service.services;

import guru.sfg.beer.order.service.domain.Customer;
import guru.sfg.beer.order.service.repositories.CustomerRepository;
import guru.sfg.beer.order.service.web.mappers.CustomerMapper;
import guru.sfg.brewery.model.CustomerPagedList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * Created by marecm on 5/11/2021
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public CustomerPagedList getCustomerList(Pageable pageable) {
        Page<Customer> customerPage = customerRepository.findAll(pageable);

        return new CustomerPagedList(customerPage
                .stream()
                .map(customerMapper::customerToCustomerDto)
                .collect(Collectors.toList()),
            PageRequest.of(
                    customerPage.getPageable().getPageNumber(),
                    customerPage.getPageable().getPageSize()),
                    customerPage.getTotalElements());
    }
}
