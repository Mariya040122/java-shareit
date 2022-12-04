package ru.practicum.shareit.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Page<Item> findAllByOwnerId(Long owner, Pageable pageable);

    @Query(value = "select * " +
            "from public.items " +
            "where available and (LOWER(name) like concat('%', LOWER(?1), '%') or LOWER(description) " +
            "like concat('%', LOWER(?1), '%'))",
            nativeQuery = true)
    Page<Item> search(String text, Pageable pageable);
}
