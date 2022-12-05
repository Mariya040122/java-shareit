package ru.practicum.shareit.item;


import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;

public class CommentMapper {

    public static CommentDto toCommentDto(Comment comment) {
        CommentDto commentDTO = new CommentDto();
        commentDTO.setId(comment.getId());
        commentDTO.setText(comment.getText());
        commentDTO.setAuthorName(comment.getAuthor().getName());
        commentDTO.setCreated(comment.getCreated());
        return commentDTO;
    }

    public static Comment fromCommentDto(CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setText(commentDto.getText());
        comment.setCreated(commentDto.getCreated());
        return comment;
    }
}
