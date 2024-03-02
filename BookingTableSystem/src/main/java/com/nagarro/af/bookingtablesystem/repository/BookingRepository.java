package com.nagarro.af.bookingtablesystem.repository;

import com.nagarro.af.bookingtablesystem.model.Booking;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID>, CustomBookingRepository {
    @Modifying
    @Transactional
    @Query(
            value = "delete from bookings b where b.id = ?1",
            nativeQuery = true
    )
    void deleteById(UUID id);

    List<Booking> findAllByCustomerId(UUID id);

    List<Booking> findAllByRestaurantId(UUID id);

    List<Booking> findAllByRestaurantName(String name);
}
