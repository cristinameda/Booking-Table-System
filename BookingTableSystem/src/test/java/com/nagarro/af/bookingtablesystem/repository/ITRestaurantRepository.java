package com.nagarro.af.bookingtablesystem.repository;

import com.nagarro.af.bookingtablesystem.model.Menu;
import com.nagarro.af.bookingtablesystem.model.Restaurant;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql({"classpath:scripts/insert_restaurant_managers.sql", "classpath:scripts/insert_restaurants.sql"})
@Testcontainers
@Transactional
public class ITRestaurantRepository {

    @Container
    public static PostgreSQLContainer<PostgresDbContainer> postgreSQLContainer = PostgresDbContainer.getInstance();

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Test
    void contextLoads() {
    }

    @Test
    public void testSave_OneToOneRelationship() {
        Restaurant restaurant = TestDataBuilder.buildRestaurant();
        Menu menu = TestDataBuilder.buildMenu(restaurant);
        restaurant.setMenu(menu);

        Restaurant returnedRestaurant = restaurantRepository.save(restaurant);

        assertTrue(restaurantRepository.findById(returnedRestaurant.getId()).isPresent());
        assertTrue(menuRepository.findById(returnedRestaurant.getId()).isPresent());
    }

    @Test
    public void testFindByName_success() {
        Optional<Restaurant> restaurantOptional = restaurantRepository.findByName("Meat Up");
        assertTrue(restaurantOptional.isPresent());
    }

    @Test
    public void testFindByName_notFound() {
        Optional<Restaurant> restaurantOptional = restaurantRepository.findByName("Nothing");
        assertTrue(restaurantOptional.isEmpty());
    }

    @Test
    public void testFindAllByCountryAndCity_success() {
        List<Restaurant> restaurants = restaurantRepository.findAllByCountryAndCity("Romania", "Cluj-Napoca");
        assertFalse(restaurants.isEmpty());
    }

    @Test
    public void testFindAllByCountryAndCity_emptyList() {
        List<Restaurant> restaurants = restaurantRepository.findAllByCountryAndCity("Bulgaria", "Sofia");
        assertTrue(restaurants.isEmpty());
    }

}
