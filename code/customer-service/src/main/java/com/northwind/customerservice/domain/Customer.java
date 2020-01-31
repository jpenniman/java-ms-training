package com.northwind.customerservice.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class Customer {

    private long id;
    private String customerNo;
    private String companyName;
    private String contactName;
    private String contactTitle;
    private String phone;
    private String fax;
    private long version;
    private List<Address> addresses = new ArrayList<>();

    public Customer(String companyName) {
        setCompanyName(companyName);
    }

    public Customer(long id, String customerNo, String customerName) {
        this(customerName);
        setCustomerNo(customerNo);
        setId(id);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        if (companyName == null ) {
            throw new IllegalArgumentException("CompanyName is required.");
        }
        String cleanName = companyName.trim();
        if (cleanName.length() == 0 || cleanName.length() > 50) {
            throw new IllegalArgumentException("CompanyName must be between 1 and 50 characters.");
        }
        this.companyName = cleanName;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        String cleanName = null;
        if (contactName != null) {
            cleanName = contactName.trim();
        }
        if (cleanName != null && (cleanName.length() == 0 || cleanName.length() > 30)) {
            throw new IllegalArgumentException("ContactName must be between 1 and 30 characters.");
        }
        this.contactName = cleanName;
    }

    public String getContactTitle() {
        return contactTitle;
    }

    public void setContactTitle(String contactTitle) {
        String cleanTitle = null;
        if (contactTitle != null) {
            cleanTitle = contactTitle.trim();
        }
        if (cleanTitle != null && (cleanTitle.length() == 0 || cleanTitle.length() > 30)) {
            throw new IllegalArgumentException("ContactTitle must be between 1 and 30 characters.");
        }
        this.contactTitle = cleanTitle;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        String cleanPhone = null;
        if (phone != null) {
            cleanPhone = phone.trim();
        }
        if (cleanPhone != null && (cleanPhone.length() == 0 || cleanPhone.length() > 24 )) {
            throw new IllegalArgumentException("Phone must be between 1 and 24 characters.");
        }
        this.phone = cleanPhone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        String cleanFax = null;
        if (fax != null) {
            cleanFax = fax.trim();
        }
        if (cleanFax != null && (cleanFax.length() == 0 || cleanFax.length() > 24 )) {
            throw new IllegalArgumentException("Fax must be between 1 and 24 characters.");
        }
        this.fax = cleanFax;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        if (version == 0)
            throw new IllegalArgumentException("Version cannot be zero.");

        if (version < this.version)
            throw new IllegalArgumentException("Version cannot be older than the current version.");

        this.version = version;
    }

    public List<Address> getAddresses() {
        return Collections.unmodifiableList(addresses);
    }

    public void addAddress(Address address) {
        addresses.add(address);
    }

    public void addAddress(String streetOrPoBox, String postalCode) {
        addAddress(new Address(streetOrPoBox, postalCode));
    }

    public void generateCustomerNo() {
        if (customerNo != null)
            throw new UnsupportedOperationException("CustomerNo has already been generated");
        if (id == 0)
            throw new UnsupportedOperationException("CustomerNo cannot be generated till the ID has bee set.");

        customerNo = String.format("%s%s",
                companyName.trim().toUpperCase().charAt(0),
                Calendar.getInstance().getWeekYear() * 1000000 + id);
    }
}
