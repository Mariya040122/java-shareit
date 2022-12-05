package ru.practicum.shareit.item;

import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.item.dto.CommentDto;

public interface CommentService {

    CommentDto createComment(long userId, long itemId, CommentDto commentDTO) throws BadRequestException;
}
