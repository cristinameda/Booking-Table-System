package com.nagarro.af.bookingtablesystem.repository;

import com.nagarro.af.bookingtablesystem.model.Restaurant;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, UUID> {
    Optional<Restaurant> findByName(String name);

    Optional<Restaurant> findByEmail(String email);

    List<Restaurant> findAllByRestaurantManagerId(UUID id);

    List<Restaurant> findAllByCountryAndCity(String country, String city);

    @Modifying
    @Transactional
    @Query(
            value = "INSERT INTO customers_fave_restaurants(customer_id, fave_restaurants_id) VALUES(?2, ?1)",
            nativeQuery = true)
    void addToFavourites(UUID restaurantId, UUID customerId);

    @Modifying
    @Transactional
    @Query(
            value = "DELETE FROM customers_fave_restaurants f WHERE f.fave_restaurants_id = ?1 AND f.customer_id = ?2",
            nativeQuery = true)
    void deleteFromFavourites(UUID restaurantId, UUID customerId);

    @Query(
            value = "SELECT r.id, r.address, r.city, r.country, r.description, r.email, r.max_customers_no, " +
                    "r.max_tables_no, r.name, r.phone_no, r.restaurant_manager_id, " +
                    "m.content, m.content_type, m.file_name " +
                    "FROM (customers_fave_restaurants f " +
                    "LEFT JOIN restaurants r ON f.fave_restaurants_id = r.id) " +
                    "LEFT JOIN menus m ON r.id = m.id " +
                    "WHERE f.customer_id = ?1",
            nativeQuery = true)
    List<Restaurant> findFaveRestaurantsByCustomerId(UUID customerId);
}
