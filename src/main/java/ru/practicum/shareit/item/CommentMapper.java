package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDTO;
import ru.practicum.shareit.item.model.Comment;

public class CommentMapper {

    public static CommentDTO toCommentDto(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setText(comment.getText());
        commentDTO.setAuthorName(comment.getAuthor().getName());
        commentDTO.setCreated(comment.getCreated());
        return commentDTO;
    }

    public static Comment fromCommentDto(CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setId(commentDTO.getId());
        comment.setText(commentDTO.getText());
        comment.setCreated(commentDTO.getCreated());
        return comment;
    }
}
