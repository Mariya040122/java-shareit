package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
public class RequestDto {

    long id; //уникальный идентификатор запроса;
    @NotBlank
    String description; //текст запроса, содержащий описание требуемой вещи;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime created;  //дата и время создания запроса.
}
