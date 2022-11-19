package ru.practicum.shareit;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class OffsetPageRequest extends PageRequest {
    private int limit;
    private int offset;
    private Sort sort;

    public OffsetPageRequest(int offset, int limit, Sort sort) {
        super(offset, limit, sort);
        this.limit = limit;
        this.offset = offset;
        this.sort = sort;
    }

    @Override
    public long getOffset(){
        return this.offset;
    }
}
