package com.northwind.customerservice.api;

import com.northwind.customerservice.domain.Customer;
import com.northwind.customerservice.services.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    // Specifiying required = false and using Optional<T> allows us to use
    // just the path /customers (without parameters) to query using default offset/limit values
    @GetMapping
    public ResponseEntity<List<CustomerModel>> get(@RequestParam(required = false) Optional<Integer> offset, @RequestParam(required = false) Optional<Integer> limit) {
        int skip = offset.orElse(0); //default offset to 0 if not specified
        int take = limit.orElse(50); //default limit to 50 if not sepcified
        if (take > 50) {
            throw new IllegalArgumentException("Limit cannot be more than 50.");
        }

        List<CustomerModel> customers = service.getAll(skip, take)
                .stream().map(c->CustomerMapper.toModel(c))
                .collect(Collectors.toList());

        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<CustomerModel> get(@PathVariable long id) {
        Customer customer = service.getById(id);
        if (customer == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(CustomerMapper.toModel(customer), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CustomerModel> create(@RequestBody CustomerModel model) {
        Customer customer = CustomerMapper.toEntity(model);
        Customer savedCustomer = service.save(customer);
        return ResponseEntity.created(URI.create("/customers/" + savedCustomer.getId()))
                .body(CustomerMapper.toModel(savedCustomer));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerModel> update(@PathVariable long id,
                                                @RequestBody CustomerModel model) {
        Customer existingCustomer = service.getById(id);
        if (existingCustomer == null) {
            return ResponseEntity.notFound().build();
        }

        if (existingCustomer.getVersion() != model.getVersion()) {
            return new ResponseEntity<>(CustomerMapper.toModel(existingCustomer), HttpStatus.CONFLICT);
        }
        Customer mergedCustomer = CustomerMapper.toEntity(model, existingCustomer);

        Customer savedCustomer = service.save(mergedCustomer);

        return new ResponseEntity<>(CustomerMapper.toModel(savedCustomer), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        Customer existingCustomer = service.getById(id);
        if (existingCustomer ==  null)
            return;

        service.delete(existingCustomer);
    }

    // Specifying expected query parameters tells MVC to map to this action when the parameter is present
    @GetMapping(params = "companyName")
    public ResponseEntity<List<CustomerModel>> findByCustomerName(@RequestParam String companyName) {
        return ResponseEntity
                .ok()
                .body(service.findByCompanyName(companyName).stream()
                                .map(c->CustomerMapper.toModel(c))
                                .collect(Collectors.toList()));
    }

    @GetMapping(params = "customerNo")
    public ResponseEntity<CustomerModel> getByCustomerNo(@RequestParam String customerNo) {
        Customer customer = service.getByCustomerNo(customerNo);
        if (customer == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok().body(CustomerMapper.toModel(customer));
    }
}
