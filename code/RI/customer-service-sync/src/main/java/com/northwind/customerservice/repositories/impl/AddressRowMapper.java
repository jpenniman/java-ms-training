package com.northwind.customerservice.repositories.impl;

import com.northwind.customerservice.domain.Address;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AddressRowMapper implements RowMapper<Address> {
    @Override
    public Address mapRow(ResultSet rs, int rowNum) throws SQLException {
        Address address = new Address(rs.getString("Address"), rs.getString("PostalCode"));
        address.setStateOrProvince(rs.getString("Region"));
        address.setCity(rs.getString("City"));
        address.setDefaultBilling(rs.getBoolean("IsDefaultBilling"));
        address.setDefaultShipping(rs.getBoolean("IsDefaultShipping"));
        address.setCountry(rs.getString("Country"));
        address.setVersion(rs.getLong("Version"));
        address.setId(rs.getLong("AddressID"));
        return address;
    }
}
