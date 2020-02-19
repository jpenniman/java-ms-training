package com.northwind.customerservice.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class When_creating_addresses {

    private String streetOrPoBox = new String(new char[60]).replace("\0","X");
    private String postalCode = new String(new char[10]).replace("\0","X");

    @Test
    public void given_a_valid_address_and_postal_code_op_should_succeed() {
        Address a = new Address(streetOrPoBox, postalCode);
        Assertions.assertEquals(streetOrPoBox, a.getStreetOrPoBox());
        Assertions.assertEquals(postalCode, a.getPostalCode());
    }

    @ParameterizedTest
    @MethodSource("invalidAddresses")
    public void given_invalid_address_op_should_fail(String address) {
        Assertions.assertThrows(IllegalArgumentException.class, ()->{
            new Address(address, postalCode);
        });
    }

    private static Stream<String> invalidAddresses() {
        return Stream.of(null, "", " ", new String(new char[61]).replace("\0","X"));
    }

}
