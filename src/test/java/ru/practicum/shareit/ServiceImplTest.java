package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.CommentService;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.CommentDTO;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDTO;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.RequestService;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.practicum.shareit.State.ALL;
import static ru.practicum.shareit.Status.*;

@SpringBootTest(classes = ShareItApp.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(properties = {"db.name=test"})
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class ServiceImplTest {

    private final EntityManager em;
    private final UserService serviceUser;
    private final ItemService serviceItem;
    private final CommentService serviceComment;
    private final BookingService serviceBooking;
    private final RequestService serviceRequest;


    @Test
    void contextLoads() {
    }

    @Test
    @DisplayName("01.Создание пользователя")
    void createUserTest() throws ConflictException, BadRequestException {

        UserDto userDto = new UserDto(0L, "Vasea", "Vasea@test.com");
        serviceUser.create(userDto);
        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);
        User user = query
                .setParameter("email", userDto.getEmail())
                .getSingleResult();
        assertThat(user)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "Vasea")
                .hasFieldOrPropertyWithValue("email", "Vasea@test.com");
        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> serviceUser.create(new UserDto(0L, "Vasea", "Vasea@test.com")));
    }

    @Test
    @DisplayName("02.Обновление пользователя")
    void updateUserTest() throws NotFoundException, ConflictException, BadRequestException {

        UserDto updatedUser = new UserDto(0L, "Vasya", null);
        serviceUser.update(1L, updatedUser);

        TypedQuery<User> query = em.createQuery("Select u from User u where u.id = :id", User.class);
        User userTwo = query
                .setParameter("id", 1L)
                .getSingleResult();


        assertThat(userTwo)
                .isNotNull()
                .hasFieldOrPropertyWithValue("name", "Vasya")
                .hasFieldOrPropertyWithValue("email", "Vasea@test.com");

        UserDto userDto = new UserDto(0L, "Duo", "Duo@test.com");
        serviceUser.create(userDto);
        query = em.createQuery("Select u from User u where u.email = :email", User.class);
        User user = query
                .setParameter("email", userDto.getEmail())
                .getSingleResult();
        assertThat(user)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 3L)
                .hasFieldOrPropertyWithValue("name", "Duo")
                .hasFieldOrPropertyWithValue("email", "Duo@test.com");

        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> serviceUser.update(1L,
                        new UserDto(0L, null, "Duo@test.com")));
    }

    @Test
    @DisplayName("03.Создание предмета")
    void createItemTest() throws NotFoundException, BadRequestException {

        ItemDto itemDto = new ItemDto(0L, "Шуруповерт", "Шуруповерт аккумуляторный", true, 0L);
        serviceItem.create(1L, itemDto);

        TypedQuery<Item> query = em.createQuery("Select i from Item i where i.id = :id", Item.class);
        Item item = query
                .setParameter("id", 1L)
                .getSingleResult();

        assertThat(item)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "Шуруповерт")
                .hasFieldOrPropertyWithValue("description", "Шуруповерт аккумуляторный")
                .hasFieldOrPropertyWithValue("available", true)
                .hasFieldOrPropertyWithValue("requestId", 0L)
                .extracting("owner")
                .isNotNull()
                .hasFieldOrPropertyWithValue("name", "Vasya");
    }

    @Test
    @DisplayName("04.Обновление предмета")
    void updateItemTest() throws NotFoundException {

        ItemDto updateItem = new ItemDto(0L, "Шуруповерт", "Шуруповерт ручной", true, 0L);
        serviceItem.update(1L, 10L, updateItem);

        TypedQuery<Item> query = em.createQuery("Select i from Item i where i.id = :id", Item.class);
        Item item = query
                .setParameter("id", 1L)
                .getSingleResult();

        assertThat(updateItem)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 0L)
                .hasFieldOrPropertyWithValue("name", "Шуруповерт")
                .hasFieldOrPropertyWithValue("description", "Шуруповерт ручной")
                .hasFieldOrPropertyWithValue("available", true)
                .hasFieldOrPropertyWithValue("requestId", 0L);
    }

    @Test
    @DisplayName("05.Поиск предмета")
    void searchItemTest() {

        TypedQuery<Item> query = em.createQuery("Select i from Item i where i.id = :id", Item.class);
        Item item = query
                .setParameter("id", 1L)
                .getSingleResult();

        assertThat(serviceItem.search("шур", 0, 1))
                .isNotNull()
                .hasSize(1)
                .extracting(ItemDto::getName).containsOnly("Шуруповерт");
    }

    @Test
    @DisplayName("06.Создание бронирования")
    void createBookingTest() throws BadRequestException, NotFoundException {

        LocalDateTime startTime = LocalDateTime.now().plusNanos(100000000L).truncatedTo(ChronoUnit.MILLIS);
        LocalDateTime endTime = LocalDateTime.now().plusDays(1L).truncatedTo(ChronoUnit.MILLIS);


        BookingDto bookingNew = new BookingDto(0L, startTime, endTime, 1L, null, WAITING);
        serviceBooking.create(3L, bookingNew);

        TypedQuery<Booking> query = em.createQuery("Select b from Booking b where b.id = :id", Booking.class);
        Booking booking = query
                .setParameter("id", 1L)
                .getSingleResult();

        assertThat(booking)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("start", startTime)
                .hasFieldOrPropertyWithValue("end", endTime)
                .hasFieldOrPropertyWithValue("status", WAITING);
        assertThat(booking)
                .extracting("item").isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L);
        assertThat(booking)
                .extracting("booker").isNotNull()
                .hasFieldOrPropertyWithValue("id", 3L);

    }

    @Test
    @DisplayName("07. Подтверждение или отказ бронирования")
    void confirmationOrRejectionBookingTest() throws BadRequestException, NotFoundException {

        serviceBooking.confirmationOrRejection(1L, 1L, true);

        TypedQuery<Booking> query = em.createQuery("Select b from Booking b where b.id = :id", Booking.class);
        Booking booking = query
                .setParameter("id", 1L)
                .getSingleResult();

        assertThat(booking)
                .isNotNull()
                .hasFieldOrPropertyWithValue("status", APPROVED);

    }

    @Test
    @DisplayName("08.Вывод бронирования")
    void findBookingTest() throws NotFoundException {

        serviceBooking.find(1L, 1L);

        TypedQuery<Booking> query = em.createQuery("Select b from Booking b where b.id = :id", Booking.class);
        Booking booking = query
                .setParameter("id", 1L)
                .getSingleResult();

        assertThat(booking)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    @DisplayName("09.Вывод всех бронирований")
    void findAllBookingTest() throws NotFoundException, BadRequestException {

        TypedQuery<Booking> query = em.createQuery("Select b from Booking b where b.id = :id", Booking.class);
        List<Booking> bookings = query
                .setParameter("id", 1L)
                .getResultList();
        List<Booking> bookingService = serviceBooking.findAll(3L, 0, 1, ALL);
        assertThat(bookingService)
                .isNotNull()
                .hasSize(1);
        assertThat(bookings)
                .isNotNull()
                .hasSize(1);
        assertThat(bookings.get(0))
                .hasFieldOrPropertyWithValue("id", bookingService.get(0).getId());
    }

    @Test
    @DisplayName("10.Вывод бронирования")
    void allUserItemsBookingTest() throws NotFoundException {

        serviceBooking.allUserItems(1L, 0, 1, ALL);
        TypedQuery<Booking> query = em.createQuery("Select b from Booking b where b.id = :id", Booking.class);
        Booking booking = query
                .setParameter("id", 1L)
                .getSingleResult();

        assertThat(booking)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    @DisplayName("11.Создание комментария")
    void createCommentTest() throws BadRequestException {

        CommentDTO commentDto = new CommentDTO(0L, "Шуруповерт хороший", "Duo",
                null);
        serviceComment.createComment(3L, 1L, commentDto);

        TypedQuery<Comment> query = em.createQuery("Select c from Comment c where c.id = :id", Comment.class);
        Comment comment = query
                .setParameter("id", 1L)
                .getSingleResult();
        assertThat(comment)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("text", "Шуруповерт хороший")
                .extracting("created")
                .isNotNull();
        assertThat(comment)
                .extracting("author")
                .isNotNull()
                .hasFieldOrPropertyWithValue("name", "Duo");
    }

    @Test
    @DisplayName("12.Вывод предмета")
    void findItemTest() throws NotFoundException {

        TypedQuery<Item> query = em.createQuery("Select i from Item i where i.id = :id", Item.class);
        Item item = query
                .setParameter("id", 1L)
                .getSingleResult();

        assertThat(serviceItem.find(1L, 1L))
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    @DisplayName("13.Вывод списка предметов")
    void findAllItemTest() {

        TypedQuery<Item> query = em.createQuery("Select i from Item i where i.owner.id = :id", Item.class);
        List<Item> items = query
                .setParameter("id", 1L)
                .getResultList();
        List<ItemWithBookingDTO> itemService = serviceItem.findAll(1L, 0, 1);
        assertThat(itemService)
                .isNotNull()
                .hasSize(1);
        assertThat(items)
                .isNotNull()
                .hasSize(1);
        assertThat(items.get(0))
                .hasFieldOrPropertyWithValue("id", itemService.get(0).getId());
    }

    @Test
    @DisplayName("14.Создание запроса")
    void createRequestTest() throws NotFoundException, BadRequestException {

        RequestDto requestDto = new RequestDto(0L, "Шуруповерт ручной", null);
        serviceRequest.create(1L, requestDto);
        TypedQuery<Request> query = em.createQuery("Select r from Request r where r.id = :id", Request.class);
        Request request = query
                .setParameter("id", 1L)
                .getSingleResult();
        assertThat(request)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("description", "Шуруповерт ручной")
                .extracting("created")
                .isNotNull();
    }

    @Test
    @DisplayName("15.Вывод запроса пользователя")
    void findRequestTest() throws NotFoundException {

        TypedQuery<Request> query = em.createQuery("Select r from Request r where r.requestor.id = :id", Request.class);
        List<Request> requests = query
                .setParameter("id", 1L)
                .getResultList();
        List<Request> requestService = serviceRequest.find(1L);
        assertThat(requestService)
                .isNotNull()
                .hasSize(1);
        assertThat(requests)
                .isNotNull()
                .hasSize(1);
        assertThat(requests.get(0))
                .hasFieldOrPropertyWithValue("id", requestService.get(0).getId());

    }

    @Test
    @DisplayName("16.Вывод запросов других пользователей")
    void findAllRequestTest() throws BadRequestException {

        TypedQuery<Request> query = em.createQuery("Select r from Request r where not r.requestor.id = :id", Request.class);

        List<Request> requests = query
                .setParameter("id", 2L)
                .getResultList();
        List<Request> requestService = serviceRequest.findAll(2L, 0, 1);
        assertThat(requestService)
                .isNotNull()
                .hasSize(1);
        assertThat(requests)
                .isNotNull()
                .hasSize(1);
        assertThat(requests.get(0))
                .hasFieldOrPropertyWithValue("id", requestService.get(0).getId());
    }

    @Test
    @DisplayName("17.Вывод запроса пользователя")
    void findByIdRequestTest() throws NotFoundException {

        TypedQuery<Request> query = em.createQuery("Select r from Request r where r.id = :id", Request.class);
        Request request = query
                .setParameter("id", 1L)
                .getSingleResult();

        assertThat(serviceRequest.findById(1L, 1L))
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L);
    }
}