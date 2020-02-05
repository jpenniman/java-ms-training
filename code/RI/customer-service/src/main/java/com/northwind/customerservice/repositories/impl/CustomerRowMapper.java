package com.northwind.customerservice.repositories.impl;

import com.northwind.customerservice.domain.Customer;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerRowMapper implements RowMapper<Customer> {
    @Override
    public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
        Customer customer = new Customer(rs.getString("CompanyName"));
        customer.setId(rs.getLong("CustomerID"));
        customer.setCustomerNo(rs.getString("CustomerNo"));
        customer.setContactName(rs.getString("ContactName"));
        customer.setContactTitle(rs.getString("ContactTitle"));
        customer.setPhone(rs.getString("Phone"));
        customer.setFax(rs.getString("Fax"));
        customer.setVersion(rs.getLong("Version"));

        return customer;
    }
}
