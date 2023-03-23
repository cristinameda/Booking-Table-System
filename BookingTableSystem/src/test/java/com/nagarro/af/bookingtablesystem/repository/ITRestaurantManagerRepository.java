package com.nagarro.af.bookingtablesystem.repository;

import com.nagarro.af.bookingtablesystem.model.RestaurantManager;
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
@Sql("classpath:scripts/insert_restaurant_managers.sql")
@Transactional
@Testcontainers
public class ITRestaurantManagerRepository {

    @Container
    public static PostgreSQLContainer<PostgresDbContainer> postgreSQLContainer = PostgresDbContainer.getInstance();

    @Autowired
    private RestaurantManagerRepository restaurantManagerRepository;

    @Test
    void contextLoads() {
    }

    @Test
    public void testFindByEmail_success() {
        RestaurantManager manager = TestDataBuilder.buildRestaurantManager();
        Optional<RestaurantManager> optionalRestaurantManager = restaurantManagerRepository.findByEmail(manager.getEmail());
        assertTrue(optionalRestaurantManager.isPresent());
    }

    @Test
    public void testFindByEmail_notFound() {
        Optional<RestaurantManager> optionalRestaurantManager = restaurantManagerRepository.findByEmail("nothing");
        assertTrue(optionalRestaurantManager.isEmpty());
    }
}
