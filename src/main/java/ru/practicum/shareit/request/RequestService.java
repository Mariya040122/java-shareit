package ru.practicum.shareit.request;


import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.request.model.Request;

import java.util.List;

public interface RequestService {
    public RequestDto create(long userId, RequestDto requestDto) throws NotFoundException, BadRequestException;

    public List<Request> find(long userId) throws NotFoundException;

    public List<Request> findAll(long userId, int from, int size) throws BadRequestException;

    public Request findById(long userId, long requestId) throws NotFoundException;
}
