package ru.practicum.shareit.ControllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.RequestController;
import ru.practicum.shareit.request.RequestService;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.Request;
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

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class RequestControllerTest {

    @Mock
    private RequestService requestService;

    @InjectMocks
    private RequestController controller;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;

    private RequestDto requestDto;
    Request request;
    User user;
    Item itemOne;
    Item itemTwo;
    List<Item> items;

    @BeforeEach
    void setUp() {
        mapper.registerModule(new JavaTimeModule());
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

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

        requestDto = new RequestDto(
                1L,
                "Шуруповерт",
                LocalDateTime.of(2022, 11, 20, 13, 00, 00));

        request = new Request(
                1L,
                "Шуруповерт",
                user,
                LocalDateTime.of(2022, 11, 20, 13, 00, 00),
                items
        );

    }

    @Test
    @DisplayName("01.Создание запрса")
    void createRequestTest() throws Exception {
        when(requestService.create(anyLong(), any()))
                .thenReturn(requestDto);

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(requestDto.getDescription())))
                .andExpect(jsonPath("$.created",
                        is(requestDto.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))));
    }

    @Test
    @DisplayName("02.Вывод заапроса")
    void findRequestTest() throws Exception {

        List<Request> requestList = new ArrayList<>();
        requestList.add(request);

        when(requestService.find(eq(1L)))
                .thenReturn(requestList);

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(request.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(request.getDescription())))
                .andExpect(jsonPath("$[0].requestor.name").value("Duo"))
                .andExpect(jsonPath("$[0].created",
                        is(request.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$[0].items", hasSize(2)))
                .andExpect(jsonPath("$[0].items[0].name").value("Шуруповерт"))
                .andExpect(jsonPath("$[0].items[1].name").value("Отвертка"));

    }

    @Test
    @DisplayName("03.Вывод нескольких запросов")
    void findAllRequestTest() throws Exception {

        List<Request> requestList = new ArrayList<>();
        requestList.add(request);

        when(requestService.findAll(eq(1L), anyInt(), anyInt()))
                .thenReturn(requestList);

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(request.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(request.getDescription())))
                .andExpect(jsonPath("$[0].requestor.name").value("Duo"))
                .andExpect(jsonPath("$[0].created",
                        is(request.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$[0].items", hasSize(2)))
                .andExpect(jsonPath("$[0].items[0].name").value("Шуруповерт"))
                .andExpect(jsonPath("$[0].items[1].name").value("Отвертка"));

    }

    @Test
    @DisplayName("04.Вывод запроса пользователя")
    void findByIdRequestTest() throws Exception {
        when(requestService.findById(anyLong(), anyLong()))
                .thenReturn(request);

        mvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(request))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(request.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(request.getDescription())))
                .andExpect(jsonPath("$.requestor.name").value("Duo"))
                .andExpect(jsonPath("$.created",
                        is(request.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))));
    }

}
