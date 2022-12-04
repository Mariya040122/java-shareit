package ru.practicum.shareit.request;


import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.Request;

public class RequestMapper {

    public static RequestDto toRequestDto(Request request) {
        RequestDto requestDto = new RequestDto();
        requestDto.setId(request.getId());
        requestDto.setDescription(request.getDescription());
        requestDto.setCreated(request.getCreated());
        return requestDto;
    }

    public static Request fromRequestDto(RequestDto requestDto) {
        Request request = new Request();
        request.setId(requestDto.getId());
        request.setDescription(requestDto.getDescription());
        request.setCreated(requestDto.getCreated());
        return request;
    }
}
