package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Booking.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookerIdOrderByStartDesc(Long bookerId, Pageable pageable);

    List<Booking> findByBookerIdAndStatusOrderByStartDesc(Long bookerId, BookingStatus status, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :bookerId AND b.start <= :now AND b.end >= :now ORDER BY b.start DESC")
    List<Booking> findCurrentBookingsByBookerId(Long bookerId, LocalDateTime now, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :bookerId AND b.end < :now ORDER BY b.start DESC")
    List<Booking> findPastBookingsByBookerId(Long bookerId, LocalDateTime now, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :bookerId AND b.start > :now ORDER BY b.start DESC")
    List<Booking> findFutureBookingsByBookerId(Long bookerId, LocalDateTime now, Pageable pageable);

    List<Booking> findByItemOwnerIdOrderByStartDesc(Long ownerId, Pageable pageable);

    List<Booking> findByItemOwnerIdAndStatusOrderByStartDesc(Long ownerId, BookingStatus status, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = :ownerId AND b.start <= :now AND b.end >= :now ORDER BY b.start DESC")
    List<Booking> findCurrentBookingsByOwnerId(Long ownerId, LocalDateTime now, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = :ownerId AND b.end < :now ORDER BY b.start DESC")
    List<Booking> findPastBookingsByOwnerId(Long ownerId, LocalDateTime now, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = :ownerId AND b.start > :now ORDER BY b.start DESC")
    List<Booking> findFutureBookingsByOwnerId(Long ownerId, LocalDateTime now, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.item.id = :itemId AND b.status = 'APPROVED' AND b.start <= :end AND b.end >= :start")
    List<Booking> findOverlappingBookings(Long itemId, LocalDateTime start, LocalDateTime end);

    List<Booking> findBookingsByBookerIdAndItemIdAndStatusAndEndBefore(Long bookerId, Long itemId, BookingStatus status, LocalDateTime nowtime);
}