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
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.CommentService;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.CommentDTO;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDTO;

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
public class ItemControllerTest {
    @Mock
    private ItemService itemService;
    @Mock
    private CommentService commentService;

    @InjectMocks
    private ItemController controller;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;

    private ItemDto itemDto;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        itemDto = new ItemDto(
                1L,
                "Шуруповерт",
                "Шуруповерт ручной",
                true,
                1L);
    }

    @Test
    @DisplayName("01.Создание вещи")
    void createItemTest() throws Exception {
        when(itemService.create(anyLong(), any()))
                .thenReturn(itemDto);

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(true)))
                .andExpect(jsonPath("$.requestId", is(itemDto.getRequestId()), Long.class));
    }

    @Test
    @DisplayName("02.Обновление вещи")
    void updateItemTest() throws Exception {
        when(itemService.update(eq(1L), eq(1L), any()))
                .thenReturn(itemDto);

        mvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(true)))
                .andExpect(jsonPath("$.requestId", is(itemDto.getRequestId()), Long.class));
    }

    @Test
    @DisplayName("03.Вывод вещи")
    void findItemTest() throws Exception {

        ItemWithBookingDTO itemWithBookingDTO = new ItemWithBookingDTO(
                0L,
                "Шуруповерт",
                "Шуруповерт ручной",
                true,
                null,
                null,
                null);


        when(itemService.find(anyLong(), anyLong()))
                .thenReturn(itemWithBookingDTO);

        mvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemWithBookingDTO.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemWithBookingDTO.getName())))
                .andExpect(jsonPath("$.description", is(itemWithBookingDTO.getDescription())))
                .andExpect(jsonPath("$.available", is(true)))
                .andExpect(jsonPath("$.lastBooking", is(itemWithBookingDTO.getLastBooking())))
                .andExpect(jsonPath("$.nextBooking", is(itemWithBookingDTO.getNextBooking())))
                .andExpect(jsonPath("$.comments", is(itemWithBookingDTO.getComments())));
    }

    @Test
    @DisplayName("04.Вывод нескольких вещей")
    void findAllItemTest() throws Exception {
        ItemWithBookingDTO itemWithBookingDTO = new ItemWithBookingDTO(
                0L,
                "Шуруповерт",
                "Шуруповерт ручной",
                true,
                new BookingDto(0L, null, null, 1L, null, WAITING),
                null,
                null);
        List<ItemWithBookingDTO> itemWithBookingDTOList = new ArrayList<>();
        itemWithBookingDTOList.add(itemWithBookingDTO);

        when(itemService.findAll(eq(1L), anyInt(), anyInt()))
                .thenReturn(itemWithBookingDTOList);

        mvc.perform(get("/items/")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(itemDto.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemDto.getAvailable())));

    }

    @Test
    @DisplayName("05.Поиск вещи")
    void searchItemTest() throws Exception {
        List<ItemDto> items = new ArrayList<>();
        items.add(itemDto);

        when(itemService.search(anyString(), anyInt(), anyInt()))
                .thenReturn(items);


        mvc.perform(get("/items/search?text=шУр")
                        .content(mapper.writeValueAsString(items))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemDto.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$[0].requestId", is(itemDto.getRequestId()), Long.class));
    }

    @Test
    @DisplayName("06.Создание комментария")
    void createCommentItemTest() throws Exception {
        CommentDTO сomment = new CommentDTO(
                0L,
                "Шуруповерт хороший",
                "Duo",
                null);

        when(commentService.createComment(eq(1L), eq(1L), any()))
                .thenReturn(сomment);

        mvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(сomment))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(сomment.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(сomment.getText())))
                .andExpect(jsonPath("$.authorName", is(сomment.getAuthorName())))
                .andExpect(jsonPath("$.created", is(сomment.getCreated())));
    }
}
