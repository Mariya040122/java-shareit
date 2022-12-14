package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.BookingServiceImpl;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.request.RequestService;
import ru.practicum.shareit.request.RequestServiceImpl;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.UserServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static ru.practicum.shareit.Status.APPROVED;
import static ru.practicum.shareit.Status.WAITING;

public class UnitServiceTesting {

    @Test
    void testCreateUserWithMock() throws ConflictException, BadRequestException {
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        UserService userService = new UserServiceImpl(mockUserRepository);

        Mockito
                .when(mockUserRepository.save(Mockito.any(User.class)))
                .thenReturn(new User());

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> userService.create(new UserDto(0L, "", "user1@test.com")));
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> userService.create(new UserDto(0L, "login", "")));
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> userService.create(new UserDto(0L, "login", "user1test")));

        userService.create(new UserDto(1L, "login", "user1@test.com"));
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(mockUserRepository).save(userCaptor.capture());
        User user = userCaptor.getValue();
        assertThat(user)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "login")
                .hasFieldOrPropertyWithValue("email", "user1@test.com");
    }

    @Test
    void testUpdateUserWithMock() throws NotFoundException {
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        UserService userService = new UserServiceImpl(mockUserRepository);
        User testUser = new User();
        testUser.setId(5L);
        testUser.setName("Vasya");
        testUser.setEmail("vasyandr@yandex.ru");

        Mockito
                .when(mockUserRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(testUser));
        Mockito
                .when(mockUserRepository.save(Mockito.any(User.class)))
                .thenReturn(new User());

        userService.update(5L, new UserDto(null, null, "MrVasiliy@gmail.com"));
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(mockUserRepository).save(userCaptor.capture());
        User user = userCaptor.getValue();
        assertThat(user)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 5L)
                .hasFieldOrPropertyWithValue("name", "Vasya")
                .hasFieldOrPropertyWithValue("email", "MrVasiliy@gmail.com");
    }

    @Test
    void testCreateItemWithMock() throws BadRequestException, NotFoundException {
        ItemRepository mockItemRepository = Mockito.mock(ItemRepository.class);
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        BookingRepository mockBookingRepository = Mockito.mock(BookingRepository.class);
        ItemService itemService = new ItemServiceImpl(mockItemRepository, mockUserRepository, mockBookingRepository);

        Mockito
                .when(mockItemRepository.save(Mockito.any(Item.class)))
                .thenReturn(new Item());

        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> itemService.create(1L,
                        new ItemDto(0L, "????????????????????", "???????????????????? ????????????????????????????", true, 0L)));
        User testUser = new User();
        testUser.setId(1L);
        testUser.setName("Vasya");
        testUser.setEmail("vasyandr@yandex.ru");

        Mockito
                .when(mockUserRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(testUser));

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> itemService.create(1L,
                        new ItemDto(0L, "", "???????????????????? ????????????????????????????", true, 0L)));

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> itemService.create(1L,
                        new ItemDto(0L, "????????????????????", "", true, 0L)));

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> itemService.create(1L,
                        new ItemDto(0L, "????????????????????", "???????????????????? ????????????????????????????", false, 0L)));
        itemService.create(1L,
                new ItemDto(0L, "????????????????????", "???????????????????? ????????????????????????????", true, 0L));

        ArgumentCaptor<Item> itemCaptor = ArgumentCaptor.forClass(Item.class);
        Mockito.verify(mockItemRepository).save(itemCaptor.capture());
        Item item = itemCaptor.getValue();
        assertThat(item)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 0L)
                .hasFieldOrPropertyWithValue("name", "????????????????????")
                .hasFieldOrPropertyWithValue("description", "???????????????????? ????????????????????????????")
                .hasFieldOrPropertyWithValue("available", true)
                .hasFieldOrPropertyWithValue("requestId", 0L)
                .extracting("owner")
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "Vasya")
                .hasFieldOrPropertyWithValue("email", "vasyandr@yandex.ru");
    }

    @Test
    void testUpdateItemWithMock() throws NotFoundException {
        ItemRepository mockItemRepository = Mockito.mock(ItemRepository.class);
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        BookingRepository mockBookingRepository = Mockito.mock(BookingRepository.class);
        ItemService itemService = new ItemServiceImpl(mockItemRepository, mockUserRepository, mockBookingRepository);

        User testUser = new User();
        testUser.setId(1L);
        testUser.setName("Vasya");
        testUser.setEmail("vasyandr@yandex.ru");

        Mockito
                .when(mockUserRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(testUser));

        Item testItem = new Item();
        testItem.setId(2L);
        testItem.setName("????????????????????");
        testItem.setDescription("???????????????????? ????????????");
        testItem.setAvailable(true);
        testItem.setOwner(testUser);
        testItem.setRequestId(0L);

        Mockito
                .when(mockItemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(testItem));
        Mockito
                .when(mockItemRepository.save(Mockito.any(Item.class)))
                .thenReturn(new Item());

        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> itemService.update(2L, 1L,
                        new ItemDto(0L, "????????????????????", "???????????????????? ????????????", true, 0L)));

        itemService.update(1L, 2L,
                new ItemDto(2L, "", "???????????????????? ???????????? ????????????????????????????", true, 0L));
        ArgumentCaptor<Item> itemCaptor = ArgumentCaptor.forClass(Item.class);
        Mockito.verify(mockItemRepository).save(itemCaptor.capture());
        Item item = itemCaptor.getValue();

        assertThat(item)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 2L)
                .hasFieldOrPropertyWithValue("name", "????????????????????")
                .hasFieldOrPropertyWithValue("description", "???????????????????? ???????????? ????????????????????????????")
                .hasFieldOrPropertyWithValue("available", true)
                .hasFieldOrPropertyWithValue("requestId", 0L)
                .extracting("owner")
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "Vasya")
                .hasFieldOrPropertyWithValue("email", "vasyandr@yandex.ru");
    }

    @Test
    void testCreateBookingWithMock() throws BadRequestException, NotFoundException {
        BookingRepository mockBookingRepository = Mockito.mock(BookingRepository.class);
        ItemRepository mockItemRepository = Mockito.mock(ItemRepository.class);
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        BookingService bookingService = new BookingServiceImpl(mockBookingRepository, mockItemRepository, mockUserRepository);

        Mockito
                .when(mockBookingRepository.save(Mockito.any(Booking.class)))
                .thenReturn(new Booking());

        LocalDateTime startTime = LocalDateTime.now().plusMinutes(2L);
        LocalDateTime endTime = LocalDateTime.now().plusDays(1L);

        User testUserOne = new User(); // ???????????????? ????????
        testUserOne.setId(1L);
        testUserOne.setName("Vasia");
        testUserOne.setEmail("vasia@yandex.ru");

        Mockito
                .when(mockUserRepository.findById(Mockito.eq(1L)))
                .thenReturn(Optional.of(testUserOne));

        User testUserTwo = new User(); // ?????????????????? ????????
        testUserTwo.setId(2L);
        testUserTwo.setName("Ben");
        testUserTwo.setEmail("Ben@yandex.ru");

        Mockito
                .when(mockUserRepository.findById(Mockito.eq(2L)))
                .thenReturn(Optional.of(testUserTwo));

        Item testItem = new Item();
        testItem.setId(2L);
        testItem.setName("????????????????????");
        testItem.setDescription("???????????????????? ????????????");
        testItem.setAvailable(true);
        testItem.setOwner(testUserOne);
        testItem.setRequestId(3L);

        Mockito
                .when(mockItemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(testItem));
        Mockito
                .when(mockItemRepository.save(Mockito.any(Item.class)))
                .thenReturn(new Item());


        assertThatExceptionOfType(NotFoundException.class).isThrownBy(() -> bookingService.create(999L,
                new BookingDto(1L, startTime,
                        endTime,
                        2L, 2L, WAITING)));

        assertThatExceptionOfType(NotFoundException.class).isThrownBy(() -> bookingService.create(1L,
                new BookingDto(1L, startTime,
                        endTime,
                        999L, 2L, WAITING)));

        assertThatExceptionOfType(NotFoundException.class).isThrownBy(() -> bookingService.create(1L,
                new BookingDto(1L, startTime,
                        endTime,
                        2L, 999L, WAITING)));

        bookingService.create(2L,
                new BookingDto(1L, startTime,
                        endTime, 2L, 2L, WAITING));
        ArgumentCaptor<Booking> bookingCaptor = ArgumentCaptor.forClass(Booking.class);
        Mockito.verify(mockBookingRepository).save(bookingCaptor.capture());
        Booking booking = bookingCaptor.getValue();
        assertThat(booking)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("start", startTime)
                .hasFieldOrPropertyWithValue("end", endTime)
                .hasFieldOrPropertyWithValue("status", WAITING);
        assertThat(booking)
                .extracting("item").isNotNull()
                .hasFieldOrPropertyWithValue("id", 2L);
        assertThat(booking)
                .extracting("booker").isNotNull()
                .hasFieldOrPropertyWithValue("id", 2L);

        Mockito
                .when(mockBookingRepository.findById(Mockito.eq(1L)))
                .thenReturn(Optional.of(booking));


        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> bookingService.confirmationOrRejection(999L, 1L, true));

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> bookingService.confirmationOrRejection(2L, 999L, true));

        assertThat(bookingService.confirmationOrRejection(1L, 1L, true));

        Booking bookingConfirm = bookingCaptor.getValue();
        assertThat(bookingConfirm)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("start", startTime)
                .hasFieldOrPropertyWithValue("end", endTime)
                .hasFieldOrPropertyWithValue("status", APPROVED);
        assertThat(bookingConfirm)
                .extracting("item").isNotNull()
                .hasFieldOrPropertyWithValue("id", 2L);
        assertThat(bookingConfirm)
                .extracting("booker").isNotNull()
                .hasFieldOrPropertyWithValue("id", 2L);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> bookingService.confirmationOrRejection(1L, 1L, false));
    }

    @Test
    void testCreateRequestWithMock() throws NotFoundException {
        RequestRepository mockRequestRepository = Mockito.mock(RequestRepository.class);
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        RequestService requestService = new RequestServiceImpl(mockRequestRepository, mockUserRepository);


        User testUserOne = new User();
        testUserOne.setId(1L);
        testUserOne.setName("Vasia");
        testUserOne.setEmail("vasia@yandex.ru");

        Mockito
                .when(mockRequestRepository.save(Mockito.any(Request.class)))
                .thenReturn(new Request(1L, "???????????????????? ????????????", testUserOne, LocalDateTime.now(), null));

        Mockito
                .when(mockUserRepository.findById(Mockito.eq(1L)))
                .thenReturn(Optional.of(testUserOne));


        requestService.create(1L, new RequestDto(0L, "???????????????????? ????????????",
                null));
        ArgumentCaptor<Request> requestCaptor = ArgumentCaptor.forClass(Request.class);
        Mockito.verify(mockRequestRepository).save(requestCaptor.capture());
        Request request = requestCaptor.getValue();
        assertThat(request)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 0L)
                .hasFieldOrPropertyWithValue("description", "???????????????????? ????????????")
                .extracting("created")
                .isNotNull();
        assertThat(request)
                .extracting("requestor")
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "Vasia");

        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> requestService.create(999L, new RequestDto(0L, "???????????????????? ????????????",
                        LocalDateTime.now())));
    }


}
