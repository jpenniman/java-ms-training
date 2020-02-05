package com.northwind.customerservice.repositories.impl;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.mysql.cj.MysqlConnection;
import com.northwind.customerservice.domain.Address;
import com.northwind.customerservice.domain.Customer;
import com.northwind.customerservice.infrastructure.LoggerFactory;
import com.northwind.customerservice.repositories.CustomerRepository;
import com.northwind.customerservice.repositories.RepositoryException;
import io.micrometer.core.instrument.MeterRegistry;
import org.apache.commons.logging.Log;
import org.springframework.dao.EmptyResultDataAccessException;
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
    private Log logger;
    private MeterRegistry meterRegistry;

    public MySqlCustomerRepository(DataSource dataSource,
                                   CustomerRowMapper customerRowMapper,
                                   AddressRowMapper addressRowMapper,
                                   LoggerFactory loggerFactory,
                                   MeterRegistry meterRegistry) {
        this.dataSource = dataSource;
        this.customerRowMapper = customerRowMapper;
        this.addressRowMapper = addressRowMapper;
        this.logger = loggerFactory.getLogger(MySqlCustomerRepository.class);
        this.meterRegistry = meterRegistry;
    }

    @Override
    public List<Customer> findByCompanyName(String companyName) throws RepositoryException {
        try {
            // We want to count and time the latency of each specific query so we can understand
            // the performance of each query.
            meterRegistry.counter("db.query.findByCompanyName.count").increment();

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

            List<Customer> customers = meterRegistry.timer("db.query.findByCompanyName.latency")
                .recordCallable(()->db.query(sql, params, customerRowMapper));

            return customers;
        } catch (Exception ex) {
            meterRegistry.counter("db.query.findByCompanyName.failure").increment();
            String msg = String.format("An error occurred finding by companyName [%s].", companyName);
            logger.debug(msg);
            throw new RepositoryException(msg, ex);
        }
    }

    @Override
    public Customer getByCustomerNo(String customerNo) throws RepositoryException {
        try {
            meterRegistry.counter("db.query.findByCustomerNo.count").increment();

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

            Customer customer = null;
            try {
                customer = meterRegistry.timer("db.query.findByCompanyName.latency")
                    .recordCallable(()->db.queryForObject(sql, params, customerRowMapper));
            } catch (EmptyResultDataAccessException ex) {
                // If no results are found, an EmptyResultDataAccessException is thrown.
                // So we don't want to bubble this up, but we will log it.
                logger.debug(String.format("No data found searching for customer no [%s]", customerNo), ex);
            }
            return customer;
        } catch (Exception ex) {
            meterRegistry.counter("db.query.findByCustomerNo.failure").increment();
            String msg = String.format("An error occurred finding by customerNo [%s].", customerNo);
            logger.debug(msg);
            throw new RepositoryException(msg, ex);
        }
    }

    @Override
    public Customer getById(long id) throws RepositoryException  {
        try {
            meterRegistry.counter("db.query.getById.count").increment();

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

            Customer customer = null;
            try {
                customer =  meterRegistry.timer("db.query.getById.latency")
                                .recordCallable(()->db.queryForObject(sql, params, customerRowMapper));
            } catch (EmptyResultDataAccessException ex) {
                // If no results are found, an EmptyResultDataAccessException is thrown.
                // So we don't want to bubble this up, but we will log it.
                logger.debug(String.format("No data found fetching customer with ID [%s]", id), ex);
            }

            String addressSql = "SELECT `Addresses`.`AddressID`,\n" +
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
            // Variables used in the closure (lambda) must be final,
            // so we'll assign our result to a final variable for use in the lambda.
            final Customer c = customer;
            if (customer != null) {
                List<Address> addresses = meterRegistry.timer("db.query.getById.address.latency")
                    .recordCallable(()->db.query(addressSql, params, addressRowMapper));

                addresses.stream().forEach(a -> c.addAddress(a));
            }
            return customer;
        } catch (Exception ex) {
            meterRegistry.counter("db.query.getById.failure").increment();
            String msg = String.format("An error occurred fetching by ID [%s].", id);
            logger.debug(msg);
            throw new RepositoryException(msg, ex);
        }
    }

    @Override
    public List<Customer> getAll(int offSet, int limit) throws RepositoryException {
        try {
            meterRegistry.counter("db.query.getAll.count").increment();

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

            List<Customer> customers = meterRegistry.timer("db.query.getAll.latency")
                    .recordCallable(()->db.query(sql, params, customerRowMapper));

            return customers;
        } catch (Exception ex) {
            meterRegistry.counter("db.query.getAll.failure").increment();
            String msg = String.format("An error occurred fetching all Customers.");
            logger.debug(msg);
            throw new RepositoryException(msg, ex);
        }
    }

    @Override
    public Customer save(Customer entity) throws RepositoryException {
        try {
            NamedParameterJdbcTemplate db = new NamedParameterJdbcTemplate(dataSource);
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("companyName", entity.getCompanyName())
                    .addValue("contactName", entity.getContactName())
                    .addValue("contactTitle", entity.getContactTitle())
                    .addValue("phone", entity.getPhone())
                    .addValue("fax", entity.getFax());

            if (entity.getId() == 0) {
                meterRegistry.counter("db.insert.count").increment();
                //insert
                String sql = "INSERT INTO `customers-db`.`Customers`\n" +
                        "(`CompanyName`,`ContactName`,`ContactTitle`,`Phone`,`Fax`)\n" +
                        "VALUES(:companyName, :contactName, :contactTitle, :phone, :fax)";

                KeyHolder keyHolder = new GeneratedKeyHolder();
                meterRegistry.timer("db.insert.latency")
                        .record(() -> db.update(sql, params, keyHolder));

                MapSqlParameterSource idParams = new MapSqlParameterSource();
                return getById(keyHolder.getKey().longValue());

            } else {
                meterRegistry.counter("db.update.count").increment();
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

                int rowsAffected = meterRegistry.timer("db.update.latency")
                        .recordCallable(() -> db.update(sql, params));
                if (rowsAffected == 0) {
                    throw new IllegalStateException("Concurrent modification detected");
                }

                return getById(entity.getId());
            }
        } catch (Exception ex) {
            meterRegistry.counter("db.save.failure").increment();
            String msg = String.format("An error occurred saving customer [%s]", entity.getId());
            logger.debug(msg);
            throw new RepositoryException(msg, ex);
        }
    }

    @Override
    public void delete(long id) throws RepositoryException {
        try {
            meterRegistry.counter("db.delete.count").increment();

            NamedParameterJdbcTemplate db = new NamedParameterJdbcTemplate(dataSource);
            MapSqlParameterSource params = new MapSqlParameterSource().addValue("id", id);
            meterRegistry.timer("db.update.latency")
                    .record(()->db.update("delete from Customers where CustomerID = :id", params));
        } catch (Exception ex) {
            meterRegistry.counter("db.delete.failure").increment();
            String msg = String.format("An error occurred deleting customer [%s]", id);
            logger.debug(msg);
            throw new RepositoryException(msg, ex);
        }
    }

    @Override
    public Address addAddress(long customerId, Address address) throws RepositoryException {
        try {
            meterRegistry.counter("db.addAddress.count").increment();

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
            meterRegistry.timer("db.addAddress.latency")
                .recordCallable(()->db.update(sql, params, keyHolder));

            MapSqlParameterSource idParams = new MapSqlParameterSource();
            idParams.addValue("id", keyHolder.getKey().longValue());
            String fetchSql = "SELECT `Addresses`.`AddressID`,\n" +
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

            Address savedAddress = meterRegistry.timer("db.fetchAddress.latency")
                .recordCallable(()->db.queryForObject(fetchSql, idParams, addressRowMapper));

            return savedAddress;
        } catch (Exception ex) {
            meterRegistry.counter("db.addAddress.failure").increment();
            String msg = String.format("An error occurred adding address to customer [%s]", customerId);
            logger.debug(msg);
            throw new RepositoryException(msg, ex);
        }
    }
}
