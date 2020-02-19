package com.northwind.customerservice.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class When_creating_a_customer {

    String validName = new String(new char[50]).replace("\0", "X");

    @Test
    public void given_a_valid_name_op_should_succeed() {
        Customer c = new Customer(validName);
        Assertions.assertEquals(validName, c.getCompanyName());
    }

    @ParameterizedTest
    @MethodSource("invalidNames")
    public void given_an_invalid_name_op_should_fail(String invalidName) {
        Assertions.assertThrows(IllegalArgumentException.class, ()->{
            new Customer(invalidName);
        });
    }

    private static Stream<String> invalidNames() {
        return Stream.of(null, "", " ", new String(new char[51]).replace("\0", "X"));
    }

    @ParameterizedTest
    @MethodSource("validContact")
    public void given_valid_contact_name_op_should_succeed(String contactName) {
        Customer c = new Customer(validName);
        c.setContactName(contactName);
        Assertions.assertEquals(contactName, c.getContactName());
    }

    @ParameterizedTest
    @MethodSource("invalidContact")
    public void given_an_invalid_contact_name_op_should_fail(String invalidName) {
        Assertions.assertThrows(IllegalArgumentException.class, ()->{
            Customer c = new Customer(validName);
            c.setContactName(invalidName);
        });
    }

    private static Stream<String> validContact() {
        return Stream.of(null, new String(new char[30]).replace("\0", "X"));
    }

    private static Stream<String> invalidContact() {
        return Stream.of("", " ", new String(new char[31]).replace("\0", "X"));
    }

    @ParameterizedTest
    @MethodSource("validContact")
    public void given_valid_contact_title_op_should_succeed(String contactTitle) {
        Customer c = new Customer(validName);
        c.setContactTitle(contactTitle);
        Assertions.assertEquals(contactTitle, c.getContactTitle());
    }

    @ParameterizedTest
    @MethodSource("invalidContact")
    public void given_an_invalid_contact_title_op_should_fail(String invalidTitle) {
        Assertions.assertThrows(IllegalArgumentException.class, ()->{
            Customer c = new Customer(validName);
            c.setContactTitle(invalidTitle);
        });
    }

    @ParameterizedTest
    @MethodSource("validPhone")
    public void given_valid_phone_op_should_succeed(String phone) {
        Customer c = new Customer(validName);
        c.setPhone(phone);
        Assertions.assertEquals(phone, c.getPhone());
    }

    @ParameterizedTest
    @MethodSource("invalidPhone")
    public void given_an_invalid_phone_op_should_fail(String phone) {
        Assertions.assertThrows(IllegalArgumentException.class, ()->{
            Customer c = new Customer(validName);
            c.setPhone(phone);
        });
    }
    @ParameterizedTest
    @MethodSource("validPhone")
    public void given_valid_fax_op_should_succeed(String fax) {
        Customer c = new Customer(validName);
        c.setFax(fax);
        Assertions.assertEquals(fax, c.getFax());
    }

    @ParameterizedTest
    @MethodSource("invalidPhone")
    public void given_an_invalid_fax_op_should_fail(String fax) {
        Assertions.assertThrows(IllegalArgumentException.class, ()->{
            Customer c = new Customer(validName);
            c.setFax(fax);
        });
    }

    private static Stream<String> validPhone() {
        return Stream.of(null, "555-555-5555",
                new String(new char[24]).replace("\0", "1"));
    }

    private static Stream<String> invalidPhone() {
        return Stream.of("",
                         " ",
                         //new String(new char[24]).replace("\0", "X"),
                         new String(new char[25]).replace("\0", "1"));
    }

    @Test
    public void given_an_id_and_name_generate_customerno() {
        Customer c = new Customer("Acme");
        c.setId(1);
        c.generateCustomerNo();
        Assertions.assertEquals("A2020000001", c.getCustomerNo());
    }
}
