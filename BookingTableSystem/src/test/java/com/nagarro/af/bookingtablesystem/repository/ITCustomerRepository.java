package com.nagarro.af.bookingtablesystem.repository;

import com.nagarro.af.bookingtablesystem.model.Customer;
import com.nagarro.af.bookingtablesystem.utils.PostgresDbContainer;
import com.nagarro.af.bookingtablesystem.utils.TestDataBuilder;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql("classpath:scripts/insert_customers.sql")
@Transactional
@Testcontainers
public class ITCustomerRepository {

    @Container
    public static PostgreSQLContainer<PostgresDbContainer> postgreSQLContainer = PostgresDbContainer.getInstance();

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void contextLoads() {
    }

    @Test
    public void testFindByEmail_success() {
        Customer customer = TestDataBuilder.buildCustomer();
        Optional<Customer> optionalCustomer = customerRepository.findByEmail(customer.getEmail());
        assertTrue(optionalCustomer.isPresent());
    }

    @Test
    public void testFindByEmail_notFound() {
        Optional<Customer> optionalCustomer = customerRepository.findByEmail("nothing");
        assertTrue(optionalCustomer.isEmpty());
    }
}
