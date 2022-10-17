package ru.practicum.shareit.item;

import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.item.dto.CommentDTO;

public interface CommentService {

    CommentDTO createComment(long userId, long itemId, CommentDTO commentDTO) throws BadRequestException;
}
