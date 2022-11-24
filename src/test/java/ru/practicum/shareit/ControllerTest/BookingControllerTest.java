package ru.practicum.shareit.ControllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.practicum.shareit.Status.WAITING;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class BookingControllerTest {

    @MockBean
    private BookingService bookingService;

    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private MockMvc mvc;

    private Booking booking;
    User user;
    Item itemOne;
    Item itemTwo;
    List<Item> items;

    @BeforeEach
    void setUp() {

        mapper.registerModule(new JavaTimeModule());

        user = new User(1L, "Duo", "Duo@test.ru");

        itemOne = new Item(
                1L,
                "Шуруповерт",
                "Шуруповерт ручной",
                true,
                user,
                null,
                1L);

        itemTwo = new Item(
                2L,
                "Отвертка",
                "Отвертка ручная",
                true,
                user,
                null,
                1L);

        items = new ArrayList<>();
        items.add(itemOne);
        items.add(itemTwo);

        booking = new Booking(
                1L,
                LocalDateTime.of(2022, 11, 20, 13, 00, 00),
                LocalDateTime.of(2022, 11, 30, 13, 00, 00),
                itemOne,
                user,
                WAITING

        );
    }

    @Test
    @DisplayName("01.Создание бронирования")
    void createBookingTest() throws Exception {
        when(bookingService.create(anyLong(), any()))
                .thenReturn(booking);

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(booking))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$.start",
                        is(booking.getStart().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$.end",
                        is(booking.getEnd().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$.item.name").value("Шуруповерт"))
                .andExpect(jsonPath("$.booker.name").value("Duo"))
                .andExpect(jsonPath("$.status", is("WAITING")));
    }

    @Test
    @DisplayName("02.Подтверждение бронирования")
    void confirmationOrRejectionBookingTest() throws Exception {
        when(bookingService.confirmationOrRejection(eq(1L), eq(1L), eq(true)))
                .thenReturn(booking);

        mvc.perform(patch("/bookings/1?approved=true")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(booking))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$.start",
                        is(booking.getStart().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$.end",
                        is(booking.getEnd().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$.item.name").value("Шуруповерт"))
                .andExpect(jsonPath("$.booker.name").value("Duo"))
                .andExpect(jsonPath("$.status", is("WAITING")));
    }

    @Test
    @DisplayName("03.Вывод бронирования")
    void findBookingTest() throws Exception {

        when(bookingService.find(anyLong(), anyLong()))
                .thenReturn(booking);

        mvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(booking))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$.start",
                        is(booking.getStart().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$.end",
                        is(booking.getEnd().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$.item.name").value("Шуруповерт"))
                .andExpect(jsonPath("$.booker.name").value("Duo"))
                .andExpect(jsonPath("$.status", is("WAITING")));
    }

    @Test
    @DisplayName("04.Вывод нескольких бронирований")
    void findAllBookingTest() throws Exception {
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);

        when(bookingService.findAll(eq(1L), anyInt(), anyInt(), any()))
                .thenReturn(bookings);

        mvc.perform(get("/bookings/")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$[0].start",
                        is(booking.getStart().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$[0].end",
                        is(booking.getEnd().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$[0].item.name").value("Шуруповерт"))
                .andExpect(jsonPath("$[0].booker.name").value("Duo"))
                .andExpect(jsonPath("$[0].status", is("WAITING")));
    }

    @Test
    @DisplayName("05.Вывод нескольких бронирований пользователя")
    void allUserItemsBookingTest() throws Exception {
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);

        when(bookingService.allUserItems(eq(1L), anyInt(), anyInt(), any()))
                .thenReturn(bookings);

        mvc.perform(get("/bookings/owner?state=CURRENT")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$[0].start",
                        is(booking.getStart().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$[0].end",
                        is(booking.getEnd().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$[0].item.name").value("Шуруповерт"))
                .andExpect(jsonPath("$[0].booker.name").value("Duo"))
                .andExpect(jsonPath("$[0].status", is("WAITING")));
    }

    @Test
    @DisplayName("06.Исключение")
    void exceptionBookingTest() throws Exception {
        mvc.perform(get("/bookings/owner?state=CURENT")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Unknown state: UNSUPPORTED_STATUS")));
    }
}

