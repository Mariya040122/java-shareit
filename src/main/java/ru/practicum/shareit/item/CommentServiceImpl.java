package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.item.dto.CommentDTO;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static ru.practicum.shareit.booking.model.Booking.Status.APPROVED;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    public CommentServiceImpl(ItemRepository itemRepository,
                              UserRepository userRepository,
                              BookingRepository bookingRepository,
                              CommentRepository commentRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public CommentDTO createComment(long userId, long itemId, CommentDTO commentDTO) throws BadRequestException {
        Comment comment = CommentMapper.fromCommentDto(commentDTO);
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new BadRequestException(""));
        User user = userRepository.findById(userId).orElseThrow(() -> new BadRequestException(""));
        LocalDateTime now = LocalDateTime.now();
        if (!(bookingRepository.findBookingByItemAndUser(itemId, userId, APPROVED.name(), now)).isEmpty()) {
            comment.setItemId(itemId);
            comment.setAuthor(user);
            comment.setCreated(now);
            return (CommentMapper.toCommentDto(commentRepository.save(comment)));
        } else throw new BadRequestException("");
    }
}
