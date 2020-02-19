package com.northwind.customerservice.repositories.impl;

import com.mysql.cj.MysqlConnection;
import com.northwind.customerservice.domain.Address;
import com.northwind.customerservice.domain.Customer;
import com.northwind.customerservice.infrastructure.LoggerFactory;
import com.northwind.customerservice.infrastructure.TraceContext;
import com.northwind.customerservice.repositories.CustomerRepository;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import org.apache.commons.logging.Log;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MySqlCustomerRepository implements CustomerRepository {
    private DataSource dataSource;
    private CustomerRowMapper customerRowMapper;
    private AddressRowMapper addressRowMapper;
    private Log log;
    private MeterRegistry meterRegistry;
    private TraceContext traceContext;

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
                                   AddressRowMapper addressRowMapper,
                                   LoggerFactory loggerFactory,
                                   MeterRegistry meterRegistry,
                                   TraceContext traceContext) {
        this.dataSource = dataSource;
        this.customerRowMapper = customerRowMapper;
        this.addressRowMapper = addressRowMapper;
        this.log = loggerFactory.getLog(MySqlCustomerRepository.class);
        this.meterRegistry = meterRegistry;
        this.traceContext = traceContext;
    }

    @Override
    public List<Customer> findByCompanyName(String companyName) {
        meterRegistry.counter("database.query.findByCompanyName.count").increment();
        try {
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

            Timer timer = meterRegistry.timer("database.query.findByCompanyName.latency");

            List<Customer> customers = db.query(sql, params, customerRowMapper);

            return customers;
        } catch (Exception ex) {
            log.debug(String.format("Error occurred finding by company name: [%s].", companyName), ex);
            throw ex;
        }
    }

    @Override
    public Customer getByCustomerNo(String customerNo) {
        Customer customer = null;
        try {
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

            customer = db.queryForObject(sql, params, customerRowMapper);
        } catch (EmptyResultDataAccessException ex) {
            // If no results are found, an EmptyResultDataAccessException is thrown.
            // So we don't want to bubble this up, but we will log it.
            log.debug(String.format("No data found for customer no [%s]. TraceId: %s", customerNo, traceContext.getTraceId()), ex);
        } catch (Exception ex) {
            log.debug(String.format("Error occurred while searching for customer no [%s]", customerNo), ex);
            throw ex;
        }
        return customer;
    }

    @Override
    public Single<Optional<Customer>> getById(long id) {
        Customer customer = null;
        NamedParameterJdbcTemplate db = null;
        MapSqlParameterSource params = null;
        try {
            db = new NamedParameterJdbcTemplate(dataSource);
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

            params = new MapSqlParameterSource()
                    .addValue("id", id);

            customer = db.queryForObject(sql, params, customerRowMapper);

            // Variables used in the closure (lambda) must be final,
            // so we'll assign our result to a final variable for use in the lambda.
            final Customer c = customer;
            if (customer != null) {
                List<Address> addresses = db.query(getAddressSql, params, addressRowMapper);
                addresses.stream().forEach(a -> c.addAddress(a));
            }
        } catch (EmptyResultDataAccessException ex) {
            // If no results are found, an EmptyResultDataAccessException is thrown.
            // So we don't want to bubble this up, but we will log it.
            log.debug(String.format("No data found for customer id [%s]", id), ex);
        } catch (Exception ex) {
            log.debug(String.format("Error occurred while searching for customer id [%s]", id), ex);
            throw ex;
        }
        return Single.just(Optional.ofNullable(customer));
    }

    @Override
    public Observable<Customer> getAll(int offSet, int limit) {
        Subject<Customer> customers = PublishSubject.create();

        Executors.newCachedThreadPool().submit(()->{
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

            //List<Customer> customers = db.query(sql, params, customerRowMapper);

            CustomerRowMapper rowMapper = new CustomerRowMapper();
            db.query(sql, params, new RowCallbackHandler() {
                @Override
                public void processRow(ResultSet rs) throws SQLException {
                    customers.onNext(rowMapper.mapRow(rs, rs.getRow()));
                }
            });

            customers.onComplete();
        });
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
            return getById(keyHolder.getKey().longValue()).blockingGet().get();

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

            return getById(entity.getId()).blockingGet().get();
        }
    }

    @Override
    public void delete(long id) {
        NamedParameterJdbcTemplate db = new NamedParameterJdbcTemplate(dataSource);
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("id", id);
        db.update("delete from Customers where CustomerID = :id", params);
    }

    @Override
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
