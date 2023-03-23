package com.nagarro.af.bookingtablesystem.repository.impl;

import com.nagarro.af.bookingtablesystem.model.Booking;
import com.nagarro.af.bookingtablesystem.model.Restaurant;
import com.nagarro.af.bookingtablesystem.model.RestaurantCapacity;
import com.nagarro.af.bookingtablesystem.repository.CustomBookingMethods;
import com.nagarro.af.bookingtablesystem.repository.exception.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.*;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

public class CustomBookingMethodsImpl implements CustomBookingMethods {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomBookingMethodsImpl.class);

    public static final String INSERT_RESTAURANTS_CAPACITY = "INSERT INTO restaurants_capacity(id, tables_no, customers_no)" +
            "VALUES(uuid_generate_v4(), ?, ?)";

    public static final String INSERT_RESTAURANT_DATES_CAPCITY = "INSERT INTO restaurant_dates_capacity(date_capacity_availability_key, restaurant_id, restaurant_capacity_id)" +
            "VALUES(?, ?, ?)";

    private static final String INSERT_BOOKING_STATEMENT = "INSERT INTO bookings(id, customer_id, restaurant_id, date_hour, customers_no, tables_no)" +
            "VALUES(uuid_generate_v4(), ?, ?, ?, ?, ?)";

    private static final String UPDATE_RESTAURANT_TABLES_NO = "UPDATE restaurants_capacity SET tables_no = ? WHERE id = ?";

    private static final String UPDATE_RESTAURANT_CUSTOMERS_NO = "UPDATE restaurants_capacity SET customers_no = ? WHERE id = ?";

    private final JdbcTemplate jdbcTemplate;

    public CustomBookingMethodsImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Booking makeBooking(Booking booking) {
        Restaurant restaurant = booking.getRestaurant();
        int tablesNo = booking.getTablesNo();
        int customersNo = booking.getCustomersNo();
        Date bookingDate = booking.getBookingDate();

        RestaurantCapacity availableCapacity = restaurant.getDateCapacityAvailability().get(bookingDate);

        if (isEnoughRestaurantCapacity(availableCapacity, tablesNo, customersNo)) {
            try {
                KeyHolder keyHolder = new GeneratedKeyHolder();

                jdbcTemplate.update(con -> getInsertRestaurantCapacityPs(con, availableCapacity), keyHolder);
                availableCapacity.setId(getKey(keyHolder));

                jdbcTemplate.update(con -> getInsertRestaurantDatesCapacityPs(con, booking, restaurant, availableCapacity));

                jdbcTemplate.update(con -> getInsertBookingPs(con, booking));

                int remainingTablesNo = availableCapacity.getTablesNo() - tablesNo;
                int remainingCustomersNo = availableCapacity.getCustomersNo() - customersNo;

                availableCapacity.setTablesNo(remainingTablesNo);
                availableCapacity.setCustomersNo(remainingCustomersNo);

                updateRestaurantCapacity(
                        availableCapacity,
                        remainingTablesNo,
                        remainingCustomersNo);
                LOGGER.info("New booking created with id {}", booking.getId());
                return booking;
            } catch (DataAccessException err) {
                LOGGER.error("CustomBookingMethodsImpl: makeBooking with id {}", booking.getId(), err);
                return null;
            }
        }
        throw new RepositoryException("Not enough tables or capacity at the restaurant!");
    }

    private boolean isEnoughRestaurantCapacity(RestaurantCapacity restaurantCapacity, int tablesNo, int customersNo) {
        return (restaurantCapacity.getTablesNo() >= tablesNo) && (restaurantCapacity.getCustomersNo() >= customersNo);
    }

    private PreparedStatement getInsertRestaurantCapacityPs(Connection con, RestaurantCapacity restaurantCapacity) throws SQLException {
        PreparedStatement preparedStatement = con.prepareStatement(INSERT_RESTAURANTS_CAPACITY, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setInt(1, restaurantCapacity.getTablesNo());
        preparedStatement.setInt(2, restaurantCapacity.getCustomersNo());
        return preparedStatement;
    }

    private PreparedStatement getInsertRestaurantDatesCapacityPs(Connection con, Booking booking, Restaurant restaurant,
                                                                 RestaurantCapacity restaurantCapacity) throws SQLException {
        PreparedStatement preparedStatement = con.prepareStatement(INSERT_RESTAURANT_DATES_CAPCITY, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setDate(1, booking.getBookingDate());
        preparedStatement.setObject(2, restaurant.getId());
        preparedStatement.setObject(3, restaurantCapacity.getId());
        return preparedStatement;
    }

    private PreparedStatement getInsertBookingPs(Connection con, Booking booking) throws
            SQLException {
        PreparedStatement preparedStatement = con.prepareStatement(INSERT_BOOKING_STATEMENT, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setObject(1, booking.getCustomer().getId());
        preparedStatement.setObject(2, booking.getRestaurant().getId());
        preparedStatement.setTimestamp(3, Timestamp.valueOf(booking.getDateHour()));
        preparedStatement.setInt(4, booking.getCustomersNo());
        preparedStatement.setInt(5, booking.getTablesNo());
        return preparedStatement;
    }

    private UUID getKey(KeyHolder keyHolder) {
        return (UUID) Optional.ofNullable(keyHolder.getKeys())
                .orElseThrow(() -> new RepositoryException("Error retrieving key!"))
                .get("id");
    }

    private void updateRestaurantCapacity(RestaurantCapacity restaurantCapacity, int tablesNo, int customersNo) {
        jdbcTemplate.update(UPDATE_RESTAURANT_TABLES_NO, tablesNo, restaurantCapacity.getId());
        jdbcTemplate.update(UPDATE_RESTAURANT_CUSTOMERS_NO, customersNo, restaurantCapacity.getId());
    }
}
