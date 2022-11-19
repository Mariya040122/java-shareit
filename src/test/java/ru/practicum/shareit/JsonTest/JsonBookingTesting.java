package ru.practicum.shareit.JsonTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.practicum.shareit.Status.WAITING;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JsonBookingTesting {

    @Autowired
    private JacksonTester<BookingDto> json;

    BookingDto bookingDto;

    @BeforeAll
    void setUp() {

        bookingDto = new BookingDto(
                1L,
                LocalDateTime.of(2022, 11, 19, 19, 00, 00),
                LocalDateTime.of(2022, 11, 25, 17, 00, 00),
                1L,
                3L,
                WAITING);

    }

    @Test
    void bookingDtoSerializeTest() throws IOException {

        assertThat(this.json.write(bookingDto))
                .isEqualToJson(getClass().getResourceAsStream("/bookingDTO.json"));
    }

    @Test
    void bookingDtoDeserializeTest() throws IOException, URISyntaxException {

        assertThat(this.json.parse(Files.readAllBytes(Path.of(ClassLoader.getSystemResource("bookingDTO.json")
                .toURI()))).getObject())
                .hasFieldOrPropertyWithValue("id", bookingDto.getId())
                .hasFieldOrPropertyWithValue("start", bookingDto.getStart())
                .hasFieldOrPropertyWithValue("end", bookingDto.getEnd())
                .hasFieldOrPropertyWithValue("itemId", bookingDto.getItemId())
                .hasFieldOrPropertyWithValue("bookerId", bookingDto.getBookerId())
                .hasFieldOrPropertyWithValue("status", bookingDto.getStatus());
    }
}
