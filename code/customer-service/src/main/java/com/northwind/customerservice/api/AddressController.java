package com.northwind.customerservice.api;

import com.northwind.customerservice.domain.Address;
import com.northwind.customerservice.domain.Customer;
import com.northwind.customerservice.services.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customers/{customerId}/addresses")
public class AddressController {

    private CustomerService service;

    public AddressController(CustomerService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<AddressModel>> get(@PathVariable long customerId) {
        Customer customer = service.getById(customerId);
        List<AddressModel> addresses =  customer.getAddresses().stream()
                .map(a->AddressMapper.toModel(a))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(addresses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressModel> get(@PathVariable long customerId, @PathVariable long id) {
        Customer customer = service.getById(customerId);
        AddressModel address =  customer.getAddresses().stream()
                .filter(a->a.getId() == id)
                .map(a->AddressMapper.toModel(a))
                .findFirst()
                .get();

        return ResponseEntity.ok().body(address);
    }

    @PostMapping
    public AddressModel create(@PathVariable long customerId,
                                @RequestBody AddressModel model) {
        Address address = AddressMapper.toEntity(model);
        Address savedAddress = service.addAddress(customerId, address);
        return AddressMapper.toModel(savedAddress);
    }
}
