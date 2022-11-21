package ru.practicum.shareit.JsonTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.request.dto.RequestDto;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JsonRequestTesting {

    @Autowired
    private JacksonTester<RequestDto> json;

    RequestDto requestDto;

    @BeforeAll
    void setUp() {

        requestDto = new RequestDto(
                1L,
                "Шуруповерт ручной",
                LocalDateTime.of(2022, 11, 25, 17, 00, 00));
    }


    @Test
    void requestDtoSerializeTest() throws IOException {

        assertThat(this.json.write(requestDto))
                .isEqualToJson(getClass().getResourceAsStream("/requestDTO.json"));
    }

    @Test
    void requestDtoDeserializeTest() throws IOException, URISyntaxException {

        assertThat(this.json.parse(Files.readAllBytes(Path.of(ClassLoader.getSystemResource("requestDTO.json")
                .toURI()))).getObject())
                .hasFieldOrPropertyWithValue("id", requestDto.getId())
                .hasFieldOrPropertyWithValue("description", requestDto.getDescription())
                .hasFieldOrPropertyWithValue("created", requestDto.getCreated());
    }
}
