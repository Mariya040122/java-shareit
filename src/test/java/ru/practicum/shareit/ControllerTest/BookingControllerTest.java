package ru.practicum.shareit.ControllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.CommentService;
import ru.practicum.shareit.item.ItemService;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.practicum.shareit.Status.WAITING;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class BookingControllerTest {

    @Mock
    private ItemService itemService;
    @Mock
    private CommentService commentService;
    @Mock
    private BookingService bookingService;
    @InjectMocks
    private BookingController controller;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;

    private Booking booking;

    @BeforeEach
    void setUp() {


        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        booking = new Booking(
                1L,
                null,
                null,
                null,
                null,
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
                .andExpect(jsonPath("$.start", is(booking.getStart())))
                .andExpect(jsonPath("$.end", is(booking.getEnd())))
                .andExpect(jsonPath("$.item", is(booking.getItem())))
                .andExpect(jsonPath("$.booker", is(booking.getBooker())))
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
                .andExpect(jsonPath("$.start", is(booking.getStart())))
                .andExpect(jsonPath("$.end", is(booking.getEnd())))
                .andExpect(jsonPath("$.item", is(booking.getItem())))
                .andExpect(jsonPath("$.booker", is(booking.getBooker())))
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
                .andExpect(jsonPath("$.start", is(booking.getStart())))
                .andExpect(jsonPath("$.end", is(booking.getEnd())))
                .andExpect(jsonPath("$.item", is(booking.getItem())))
                .andExpect(jsonPath("$.booker", is(booking.getBooker())))
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
                .andExpect(jsonPath("$[0].start", is(booking.getStart())))
                .andExpect(jsonPath("$[0].end", is(booking.getEnd())))
                .andExpect(jsonPath("$[0].item", is(booking.getItem())))
                .andExpect(jsonPath("$[0].booker", is(booking.getBooker())))
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
                .andExpect(jsonPath("$[0].start", is(booking.getStart())))
                .andExpect(jsonPath("$[0].end", is(booking.getEnd())))
                .andExpect(jsonPath("$[0].item", is(booking.getItem())))
                .andExpect(jsonPath("$[0].booker", is(booking.getBooker())))
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
