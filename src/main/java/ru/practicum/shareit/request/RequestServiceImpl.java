package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.OffsetPageRequest;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RequestServiceImpl implements RequestService {
    @Autowired
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;

    public RequestServiceImpl(RequestRepository requestRepository, UserRepository userRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
    }

    @Override
    public RequestDto create(long userId, RequestDto requestDto) throws NotFoundException, BadRequestException {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(""));
        Request newRequest = RequestMapper.fromRequestDto(requestDto);
        newRequest.setCreated(LocalDateTime.now());
        newRequest.setRequestor(user);
        return RequestMapper.toRequestDto(requestRepository.save(newRequest));
    }

    @Override
    public List<Request> find(long userId) throws NotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(""));
        return requestRepository.findByRequestorId(userId);
    }

    @Override
    public List<Request> findAll(long userId, int from, int size) throws BadRequestException {
        if (from < 0 || size < 1) {
            throw new BadRequestException("");
        }
        return requestRepository.findByRequestorIdNot(userId, new OffsetPageRequest(from, size,
                Sort.by("created").descending())).getContent();
    }

    @Override
    public Request findById(long userId, long requestId) throws NotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(""));
        Request request = requestRepository.findById(requestId).orElseThrow(() -> new NotFoundException(""));
        return request;
    }
}
