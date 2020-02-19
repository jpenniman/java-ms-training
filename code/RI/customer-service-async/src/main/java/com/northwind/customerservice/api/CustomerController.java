package com.northwind.customerservice.api;

import com.northwind.customerservice.domain.Customer;
import com.northwind.customerservice.services.CustomerService;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customers")
@Api(tags="Customers")
public class CustomerController {

    private CustomerService service;
    private Log logger = LogFactory.getLog(CustomerController.class);

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    // Specifiying required = false and using Optional<T> allows us to use
    // just the path /customers (without parameters) to query using default offset/limit values
    @GetMapping(produces = "application/json")
    @ApiOperation(value = "List Customers", notes="This operations supports paging with limit and offset.")
    public ResponseEntity<Observable<CustomerModel>> get(
            @RequestParam(required = false)
            @ApiParam(value = "Number of records to skip when paging. Default value is 0.")
            Optional<Integer> offset,
            @RequestParam(required = false)
            @ApiParam(value = "Number of records to take when paging. Default value is 50.")
            Optional<Integer> limit) {
        int skip = offset.orElse(0); //default offset to 0 if not specified
        int take = limit.orElse(50); //default limit to 50 if not sepcified
        if (take > 50) {
            throw new IllegalArgumentException("Limit cannot be more than 50.");
        }

//        List<CustomerModel> customers = service.getAll(skip, take)
//                .stream().map(c->CustomerMapper.toModel(c))
//                .collect(Collectors.toList());

            Observable<CustomerModel> customers = service.getAll(skip, take)
                    .map(entity->CustomerMapper.toModel(entity));

            return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    @ApiOperation(value="Retrieve a Customer by ID")
    public Single<ResponseEntity<CustomerModel>> get(@PathVariable long id) {
        return service.getById(id)
                .map(customer->{
                    if (customer.isPresent())
                        return new ResponseEntity<>(CustomerMapper.toModel(customer.get()), HttpStatus.OK);
                    else
                        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                });
    }

    @PostMapping
    @ApiOperation(value="Create a new Customer", produces = "application/json")
    public ResponseEntity<CustomerModel> create(@RequestBody CustomerModel model) {
        Customer customer = CustomerMapper.toEntity(model);
        Customer savedCustomer = service.save(customer);
        return ResponseEntity.created(URI.create("/customers/" + savedCustomer.getId()))
                .body(CustomerMapper.toModel(savedCustomer));
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update an existing Customer", produces = "application/json")
    public ResponseEntity<CustomerModel> update(@PathVariable long id,
                                                        @RequestBody CustomerModel model) {
        Customer existingCustomer = service.getById(id).blockingGet().get();

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
    @ApiOperation(value = "Delete an existing Customer.", notes = "This operation is idempotent.")
    public void delete(@PathVariable long id) {
        Customer existingCustomer = service.getById(id).blockingGet().get();
        if (existingCustomer ==  null)
            return;

        service.delete(existingCustomer);
    }

    // Specifying expected query parameters tells MVC to map to this action when the parameter is present
    @GetMapping(params = "companyName", produces = "application/json")
    //@ApiOperation(value="Retrieve a Customer by Company Name")
    @ApiIgnore
    public ResponseEntity<List<CustomerModel>> findByCustomerName(@RequestParam String companyName) {
        return ResponseEntity
                .ok()
                .body(service.findByCompanyName(companyName).stream()
                                .map(c->CustomerMapper.toModel(c))
                                .collect(Collectors.toList()));
    }

    @GetMapping(params = "customerNo", produces = "application/json")
    //@ApiOperation(value = "Retrieve a Customer by Customer Number")
    @ApiIgnore
    public ResponseEntity<CustomerModel> getByCustomerNo(@RequestParam String customerNo) {
        Customer customer = service.getByCustomerNo(customerNo);
        if (customer == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok().body(CustomerMapper.toModel(customer));
    }
}
