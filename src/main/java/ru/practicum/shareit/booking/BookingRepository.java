package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

import ru.practicum.shareit.booking.model.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query(value = "select * " +
            "from public.bookings " +
            "where booker_id = ?1 " +
            "order by start_date desc", nativeQuery = true)
    List<Booking> findAllBookings(Long userId);

    @Query(value = "select * " +
            "from public.bookings " +
            "where booker_id = ?1 and start_date < ?2 and end_date > ?2 " +
            "order by start_date desc", nativeQuery = true)
    List<Booking> findCurrentBookings(Long userId, LocalDateTime time);

    @Query(value = "select * " +
            "from public.bookings " +
            "where booker_id = ?1 and end_date < ?2 " +
            "order by start_date desc", nativeQuery = true)
    List<Booking> findPastBookings(Long userId, LocalDateTime time);

    @Query(value = "select * " +
            "from public.bookings " +
            "where booker_id = ?1 and start_date > ?2 " +
            "order by start_date desc", nativeQuery = true)
    List<Booking> findFutureBookings(Long userId, LocalDateTime time);

    @Query(value = "select * " +
            "from public.bookings " +
            "where booker_id = ?1 and status = ?2 " +
            "order by start_date desc", nativeQuery = true)
    List<Booking> findByStatusBookings(Long userId, String status);


    @Query(value = "select * " +
            "from public.bookings as bk " +
            "inner join public.items as it on it.id = bk.item_id " +
            "where it.owner = ?1 " +
            "order by bk.start_date desc", nativeQuery = true)
    List<Booking> findAllUserItemsBookings(Long userId);

    @Query(value = "select * " +
            "from public.bookings as bk " +
            "inner join public.items as it on it.id = bk.item_id " +
            "where it.owner = ?1 and bk.start_date < ?2 and bk.end_date > ?2 " +
            "order by bk.start_date desc", nativeQuery = true)
    List<Booking> findCurrentUserItemsBookings(Long userId, LocalDateTime time);

    @Query(value = "select * " +
            "from public.bookings as bk " +
            "inner join public.items as it on it.id = bk.item_id " +
            "where it.owner = ?1 and bk.end_date < ?2 " +
            "order by bk.start_date desc", nativeQuery = true)
    List<Booking> findPastUserItemsBookings(Long userId, LocalDateTime time);

    @Query(value = "select * " +
            "from public.bookings as bk " +
            "inner join public.items as it on it.id = bk.item_id " +
            "where it.owner = ?1 and bk.start_date > ?2 " +
            "order by bk.start_date desc", nativeQuery = true)
    List<Booking> findFutureUserItemsBookings(Long userId, LocalDateTime time);

    @Query(value = "select * " +
            "from public.bookings as bk " +
            "inner join public.items as it on it.id = bk.item_id " +
            "where it.owner = ?1 and bk.status = ?2 " +
            "order by bk.start_date desc", nativeQuery = true)
    List<Booking> findByStatusUserItemsBookings(Long userId, String status);

    @Query(value = "select * " +
            "from public.bookings " +
            "where item_id = ?1 and end_date < ?2 " +
            "order by end_date desc", nativeQuery = true)
    List<Booking> findByItemIdAndEndDate(Long itemId, LocalDateTime endDate);

    @Query(value = "select * " +
            "from public.bookings " +
            "where item_id = ?1 and start_date > ?2 " +
            "order by start_date asc", nativeQuery = true)
    List<Booking> findByItemIdAndStartDate(Long itemId, LocalDateTime startDate);

    @Query(value = "select * " +
            "from public.bookings " +
            "where item_id = ?1 " +
            "and booker_id = ?2 " +
            "and status = ?3 " +
            "and start_date < ?4 " +
            "order by start_date desc", nativeQuery = true)
    List<Booking> findBookingByItemAndUser(long itemId, long userId, String status, LocalDateTime date);
}
