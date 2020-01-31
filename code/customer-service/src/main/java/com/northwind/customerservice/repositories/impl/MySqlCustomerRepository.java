package com.northwind.customerservice.repositories.impl;

import com.mysql.cj.MysqlConnection;
import com.northwind.customerservice.domain.Address;
import com.northwind.customerservice.domain.Customer;
import com.northwind.customerservice.repositories.CustomerRepository;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

public class MySqlCustomerRepository implements CustomerRepository {
    private DataSource dataSource;
    private CustomerRowMapper customerRowMapper;
    private AddressRowMapper addressRowMapper;

    private final static String getAddressSql  =
            "SELECT `Addresses`.`AddressID`,\n" +
            "    `Addresses`.`CustomerID`,\n" +
            "    `Addresses`.`Address`,\n" +
            "    `Addresses`.`City`,\n" +
            "    `Addresses`.`Region`,\n" +
            "    `Addresses`.`PostalCode`,\n" +
            "    `Addresses`.`Country`,\n" +
            "    `Addresses`.`IsDefaultShipping`,\n" +
            "    `Addresses`.`IsDefaultBilling`,\n" +
            "    `Addresses`.`Version`,\n" +
            "    `Addresses`.`ObjectID`\n" +
            "FROM `customers-db`.`Addresses`\n" +
            "WHERE CustomerID = :id";

    public MySqlCustomerRepository(DataSource dataSource,
                                   CustomerRowMapper customerRowMapper,
                                   AddressRowMapper addressRowMapper) {
        this.dataSource = dataSource;
        this.customerRowMapper = customerRowMapper;
        this.addressRowMapper = addressRowMapper;
    }

    @Override
    public List<Customer> findByCompanyName(String companyName) {
        NamedParameterJdbcTemplate db = new NamedParameterJdbcTemplate(dataSource);
        String sql = "SELECT `Customers`.`CustomerID`,\n" +
                "    `Customers`.`CustomerNo`,\n" +
                "    `Customers`.`CompanyName`,\n" +
                "    `Customers`.`ContactName`,\n" +
                "    `Customers`.`ContactTitle`,\n" +
                "    `Customers`.`Phone`,\n" +
                "    `Customers`.`Fax`,\n" +
                "    `Customers`.`Version`,\n" +
                "    `Customers`.`ObjectID`\n" +
                "FROM `customers-db`.`Customers`\n" +
                "WHERE CompanyName LIKE :companyName";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("companyName", companyName + "%");

        List<Customer> customers = db.query(sql, params, customerRowMapper);
        return customers;
    }

    @Override
    public Customer getByCustomerNo(String customerNo) {
        NamedParameterJdbcTemplate db = new NamedParameterJdbcTemplate(dataSource);
        String sql = "SELECT `Customers`.`CustomerID`,\n" +
                "    `Customers`.`CustomerNo`,\n" +
                "    `Customers`.`CompanyName`,\n" +
                "    `Customers`.`ContactName`,\n" +
                "    `Customers`.`ContactTitle`,\n" +
                "    `Customers`.`Phone`,\n" +
                "    `Customers`.`Fax`,\n" +
                "    `Customers`.`Version`,\n" +
                "    `Customers`.`ObjectID`\n" +
                "FROM `customers-db`.`Customers`\n" +
                "WHERE CustomerNo = :customerNo";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("customerNo", customerNo);

        Customer customer = db.queryForObject(sql, params, customerRowMapper);
        return customer;
    }

    @Override
    public Customer getById(long id) {
        NamedParameterJdbcTemplate db = new NamedParameterJdbcTemplate(dataSource);
        String sql = "SELECT `Customers`.`CustomerID`,\n" +
                "    `Customers`.`CustomerNo`,\n" +
                "    `Customers`.`CompanyName`,\n" +
                "    `Customers`.`ContactName`,\n" +
                "    `Customers`.`ContactTitle`,\n" +
                "    `Customers`.`Phone`,\n" +
                "    `Customers`.`Fax`,\n" +
                "    `Customers`.`Version`,\n" +
                "    `Customers`.`ObjectID`\n" +
                "FROM `customers-db`.`Customers`\n" +
                "WHERE CustomerID = :id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);

        Customer customer = db.queryForObject(sql, params, customerRowMapper);

        if (customer != null) {
            List<Address> addresses = db.query(getAddressSql, params, addressRowMapper);
            addresses.stream().forEach(a -> customer.addAddress(a));
        }
        return customer;
    }

    @Override
    public List<Customer> getAll(int offSet, int limit) {
        NamedParameterJdbcTemplate db = new NamedParameterJdbcTemplate(dataSource);
        String sql = "SELECT `Customers`.`CustomerID`,\n" +
                "    `Customers`.`CustomerNo`,\n" +
                "    `Customers`.`CompanyName`,\n" +
                "    `Customers`.`ContactName`,\n" +
                "    `Customers`.`ContactTitle`,\n" +
                "    `Customers`.`Phone`,\n" +
                "    `Customers`.`Fax`,\n" +
                "    `Customers`.`Version`,\n" +
                "    `Customers`.`ObjectID`\n" +
                "FROM `customers-db`.`Customers`\n" +
                "LIMIT :offset, :limit";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("offset", offSet)
                .addValue("limit", limit);

        List<Customer> customers = db.query(sql, params, customerRowMapper);
        return customers;
    }

    @Override
    public Customer save(Customer entity) {
        NamedParameterJdbcTemplate db = new NamedParameterJdbcTemplate(dataSource);
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("companyName", entity.getCompanyName())
                .addValue("contactName", entity.getContactName())
                .addValue("contactTitle", entity.getContactTitle())
                .addValue("phone", entity.getPhone())
                .addValue("fax", entity.getFax());

        if (entity.getId() == 0) {
            //insert
            String sql = "INSERT INTO `customers-db`.`Customers`\n" +
                    "(`CompanyName`,`ContactName`,`ContactTitle`,`Phone`,`Fax`)\n" +
                    "VALUES(:companyName, :contactName, :contactTitle, :phone, :fax)";

            KeyHolder keyHolder = new GeneratedKeyHolder();
            db.update(sql, params, keyHolder);
            MapSqlParameterSource idParams = new MapSqlParameterSource();
            return getById(keyHolder.getKey().longValue());

        } else {
            //update
            String sql = "UPDATE `customers-db`.`Customers`\n" +
                        "SET `CustomerNo` = :customerNo,\n" +
                            "`CompanyName` = :companyName,\n" +
                            "`ContactName` = :contactName,\n" +
                            "`ContactTitle` = :contactTitle,\n" +
                            "`Phone` = :phone,\n" +
                            "`Fax` = :fax,\n" +
                            "`Version` = Version + 1\n" +
                        "WHERE `CustomerID` = :id and Version = :version";
            params.addValue("customerNo", entity.getCustomerNo())
                    .addValue("id", entity.getId())
                    .addValue("version", entity.getVersion());

            int rowsAffected = db.update(sql, params);
            if (rowsAffected == 0) {
                throw new IllegalStateException("Concurrent modification detected");
            }

            return getById(entity.getId());
        }
    }

    @Override
    public void delete(long id) {
        NamedParameterJdbcTemplate db = new NamedParameterJdbcTemplate(dataSource);
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("id", id);
        db.update("delete from Customers where CustomerID = :id", params);
    }

    @Override
    @Transactional
    public Address addAddress(long customerId, Address address) {

        NamedParameterJdbcTemplate db = new NamedParameterJdbcTemplate(dataSource);
        String sql = "INSERT INTO `customers-db`.`Addresses`\n" +
                "(`CustomerID`,`Address`,`City`,`Region`,`PostalCode`,`Country`,`IsDefaultShipping`,`IsDefaultBilling`)\n" +
                "VALUES\n" +
                "(:customerID,:address,:city,:region,:postalCode,:country,:isDefaultShipping,:isDefaultBilling)";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("customerID", customerId)
                .addValue("address", address.getStreetOrPoBox())
                .addValue("city", address.getCity())
                .addValue("region", address.getStateOrProvince())
                .addValue("postalCode", address.getPostalCode())
                .addValue("country", address.getCountry())
                .addValue("isDefaultShipping", address.isDefaultShipping())
                .addValue("isDefaultBilling", address.isDefaultBilling());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        db.update(sql, params, keyHolder);

        MapSqlParameterSource idParams = new MapSqlParameterSource();
        idParams.addValue("id", keyHolder.getKey().longValue());
        sql = "SELECT `Addresses`.`AddressID`,\n" +
                "    `Addresses`.`CustomerID`,\n" +
                "    `Addresses`.`Address`,\n" +
                "    `Addresses`.`City`,\n" +
                "    `Addresses`.`Region`,\n" +
                "    `Addresses`.`PostalCode`,\n" +
                "    `Addresses`.`Country`,\n" +
                "    `Addresses`.`IsDefaultShipping`,\n" +
                "    `Addresses`.`IsDefaultBilling`,\n" +
                "    `Addresses`.`Version`,\n" +
                "    `Addresses`.`ObjectID`\n" +
                "FROM `customers-db`.`Addresses`\n" +
                "WHERE AddressID = :id";

        Address savedAddress = db.queryForObject(sql, idParams, addressRowMapper);

        return savedAddress;
    }
}
