package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

import ru.practicum.shareit.Status;
import ru.practicum.shareit.booking.model.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Page<Booking> findByBookerId(long bookerId, Pageable pageable);

    Page<Booking> findByBookerIdAndStartBeforeAndEndAfter(Long userId,
                                                          LocalDateTime timeStart,
                                                          LocalDateTime timeEnd,
                                                          Pageable pageable);

    Page<Booking> findByBookerIdAndEndBefore(Long userId, LocalDateTime time, Pageable pageable);

    Page<Booking> findByBookerIdAndStartAfter(Long userId, LocalDateTime time, Pageable pageable);

    Page<Booking> findByBookerIdAndStatus(Long userId, Status status, Pageable pageable);

    Page<Booking> findByItem_OwnerId(long userId, Pageable pageable);

    Page<Booking> findByItem_OwnerIdAndStartBeforeAndEndAfter(Long userId,
                                                              LocalDateTime timeStart,
                                                              LocalDateTime timeEnd,
                                                              Pageable pageable);

    Page<Booking> findByItem_OwnerIdAndEndBefore(Long userId, LocalDateTime time, Pageable pageable);

    Page<Booking> findByItem_OwnerIdAndStartAfter(Long userId, LocalDateTime time, Pageable pageable);

    Page<Booking> findByItem_OwnerIdAndStatus(Long userId, Status status, Pageable pageable);

    List<Booking> findByItemIdAndEndBeforeOrderByEndDesc(Long itemId, LocalDateTime endDate);

    List<Booking> findByItemIdAndStartAfterOrderByStartAsc(Long itemId, LocalDateTime startDate);

    @Query(value = "select * " +
            "from public.bookings " +
            "where item_id = ?1 " +
            "and booker_id = ?2 " +
            "and status = ?3 " +
            "and start_date < ?4 " +
            "order by start_date desc", nativeQuery = true)
    List<Booking> findBookingByItemAndUser(long itemId, long userId, String status, LocalDateTime date);
}
