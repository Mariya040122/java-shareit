package ru.practicum.shareit.JsonTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.item.dto.CommentDTO;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JsonCommentTesting {

    @Autowired
    private JacksonTester<CommentDTO> json;

    CommentDTO commentDTO;

    @BeforeAll
    void setUp() {

        commentDTO = new CommentDTO(
                1L,
                "Шуруповерт хороший",
                "Duo",
                LocalDateTime.of(2022, 11, 25, 17, 00, 00));
    }

    @Test
    void сommentDtoSerializeTest() throws IOException {

        assertThat(this.json.write(commentDTO))
                .isEqualToJson(getClass().getResourceAsStream("/commentDTO.json"));
    }

    @Test
    void сommentDtoDeserializeTest() throws IOException, URISyntaxException {

        assertThat(this.json.parse(Files.readAllBytes(Path.of(ClassLoader.getSystemResource("commentDTO.json")
                .toURI()))).getObject())
                .hasFieldOrPropertyWithValue("id", commentDTO.getId())
                .hasFieldOrPropertyWithValue("text", commentDTO.getText())
                .hasFieldOrPropertyWithValue("authorName", commentDTO.getAuthorName())
                .hasFieldOrPropertyWithValue("created", commentDTO.getCreated());
    }
}
