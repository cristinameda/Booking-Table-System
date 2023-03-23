package com.nagarro.af.bookingtablesystem.repository;

import com.nagarro.af.bookingtablesystem.model.Booking;
import com.nagarro.af.bookingtablesystem.model.Customer;
import com.nagarro.af.bookingtablesystem.model.Restaurant;
import com.nagarro.af.bookingtablesystem.utils.PostgresDbContainer;
import com.nagarro.af.bookingtablesystem.utils.TestDataBuilder;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
//@Sql({"classpath:scripts/insert_restaurant_managers.sql", "classpath:scripts/insert_customers.sql",
//        "classpath:scripts/insert_restaurants.sql", "classpath:scripts/insert_bookings.sql"})
@Transactional
@Testcontainers
public class ITBookingRepository {

    private static final String UNUSED_ID = "fe1e83bd-70a6-4ffa-818d-05a4f1b4bcab";

    @Container
    public static PostgreSQLContainer<PostgresDbContainer> postgreSQLContainer = PostgresDbContainer.getInstance();

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private RestaurantCapacityRepository restaurantCapacityRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void contextLoads() {
    }

    @Test
    public void testFindAllByCustomerId_success() {
        List<Booking> bookings = bookingRepository.findAllByCustomerId(UUID.fromString(TestDataBuilder.CUSTOMER_ID));
        assertFalse(bookings.isEmpty());
    }

    @Test
    public void testFindAllByCustomerId_emptyList() {
        List<Booking> bookings = bookingRepository.findAllByCustomerId(UUID.fromString(UNUSED_ID));
        assertTrue(bookings.isEmpty());
    }

    @Test
    public void testFindAllByRestaurantId_success() {
        List<Booking> bookings = bookingRepository.findAllByRestaurantId(UUID.fromString(TestDataBuilder.RESTAURANT_ID));
        assertFalse(bookings.isEmpty());
    }

    @Test
    public void testFindAllByRestaurantId_notFound() {
        List<Booking> bookings = bookingRepository.findAllByRestaurantId(UUID.fromString(UNUSED_ID));
        assertTrue(bookings.isEmpty());
    }

//    @Test
//    public void testMakeBooking_success() {
//
//        Restaurant restaurant = restaurantRepository.save(TestDataBuilder.buildRestaurant());
//        Customer customer = customerRepository.save(TestDataBuilder.buildCustomer());
//
//        assertTrue(restaurantRepository.findById(restaurant.getId()).isPresent());
//        assertTrue(customerRepository.findById(customer.getId()).isPresent());
//
//        Booking booking = TestDataBuilder.buildBooking(restaurant, customer);
//
//        Booking createdBooking = bookingRepository.makeBooking(booking);
//
//        assertNotNull(createdBooking);
//        assertTrue(bookingRepository.findById(createdBooking.getId()).isPresent());
//    }

    @Test
    public void testSave() {
        Restaurant restaurant = restaurantRepository.save(TestDataBuilder.buildRestaurant());
        Customer customer = customerRepository.save(TestDataBuilder.buildCustomer());

        assertTrue(restaurantRepository.findById(restaurant.getId()).isPresent());
        assertTrue(customerRepository.findById(customer.getId()).isPresent());

        Booking booking = TestDataBuilder.buildBooking(restaurant, customer);

        Booking createdBooking = bookingRepository.save(booking);

        assertNotNull(createdBooking);
        assertTrue(bookingRepository.findById(createdBooking.getId()).isPresent());
        assertTrue(restaurantCapacityRepository
                .findById(createdBooking
                        .getRestaurant()
                        .getDateCapacityAvailability()
                        .get(createdBooking.getBookingDate())
                        .getId())
                .isPresent()
        );

    }
}
