package ru.practicum.shareit.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;
import java.time.Instant;

@Getter
@Setter
public class ErrorResponse {

    private Timestamp timestamp;
    private int code;
    private String status;
    private String error;

    public ErrorResponse() {
        timestamp = Timestamp.from(Instant.now());
    }

    public ErrorResponse(HttpStatus httpStatus, String error) {
        this();

        this.code = httpStatus.value();
        this.status = httpStatus.name();
        this.error = error;
    }

}
