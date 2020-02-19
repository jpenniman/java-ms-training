package com.northwind.customerservice.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/health")
public class HealthController {
    private DataSource dataSource;

    public HealthController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public ResponseEntity get() {
        Map<String, String> body = new HashMap<>();
        try {
            Connection cn = dataSource.getConnection();
            Statement stm = cn.createStatement();
            stm.execute("select current_date()");

            body.put("status","healthy");
            body.put("database","OK");

            //check Order Order-Service
            //restemplate....
            //If order service is down
            body.put("status","degraded");
            body.put("database","OK");
            body.put("order-service","unhealthy");
            return ResponseEntity.ok()
                    .body(body);
        } catch (SQLException ex) {
            body.put("status","critical");
            body.put("database","Inaccessable");
            return new ResponseEntity(body, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
