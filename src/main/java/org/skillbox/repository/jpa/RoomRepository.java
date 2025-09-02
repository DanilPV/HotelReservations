package org.skillbox.repository.jpa;

import org.skillbox.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long>, JpaSpecificationExecutor<Room> {

    boolean existsByNumber(String number);
    List<Room> findByHotelId(Long hotelId);

    @Query("SELECT r FROM Room r WHERE r.hotel.id = :hotelId AND " +
            "NOT EXISTS (SELECT b FROM Booking b WHERE b.room.id = r.id AND " +
            "b.checkInDate <= :checkOut AND b.checkOutDate >= :checkIn)")
    List<Room> findAvailableRooms(@Param("hotelId") Long hotelId,
                                  @Param("checkIn") LocalDate checkIn,
                                  @Param("checkOut") LocalDate checkOut);
}