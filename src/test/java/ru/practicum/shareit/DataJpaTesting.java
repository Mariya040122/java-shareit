package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.practicum.shareit.Status.WAITING;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(properties = {"db.name=test"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class DataJpaTesting {

    @Autowired
    private final TestEntityManager testEM;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    UserRepository userRepository;

    User addedUser;
    Item itemOne;
    Item itemTwo;

    @BeforeAll
    void setUp() {

        User user = new User(0L, "Duo", "duo@test.com");

        addedUser = userRepository.save(user);

        itemOne = itemRepository.save(new Item(
                0L,
                "Шуруповерт",
                "Шуруповерт ручной",
                true,
                addedUser,
                null,
                1L));

        itemTwo = itemRepository.save(new Item(
                0L,
                "Отвертка",
                "Отвертка ручная",
                true,
                addedUser,
                null,
                1L));
    }


    @Test
    @DisplayName("01.Тест метода поиск вещи")
    public void searchTest() {

        List<Item> searchResult = itemRepository.search("шУР",
                new OffsetPageRequest(0, 1, Sort.unsorted())).getContent();

        assertThat(searchResult)
                .isNotNull()
                .hasSize(1);
        assertThat(searchResult.get(0))
                .hasFieldOrPropertyWithValue("name", "Шуруповерт")
                .hasFieldOrPropertyWithValue("description", "Шуруповерт ручной")
                .hasFieldOrPropertyWithValue("available", true)
                .hasFieldOrPropertyWithValue("requestId", 1L)
                .extracting("owner")
                .hasFieldOrPropertyWithValue("name", "Duo")
                .hasFieldOrPropertyWithValue("email", "duo@test.com");
    }

    @Test
    @DisplayName("02.Тест метода вывод бронирования")
    public void findBookingByItemAndUserTest() {

        LocalDateTime timeNow = LocalDateTime.now();

        Booking bookingOne = bookingRepository.save(new Booking(
                0L,
                timeNow.plusMinutes(2L),
                timeNow.plusDays(1L),
                //LocalDateTime.of(2022,11,12,13,10),
                itemOne,
                addedUser,
                WAITING
        ));

        Booking bookingTwo = bookingRepository.save(new Booking(
                0L,
                timeNow.plusMinutes(3L),
                timeNow.plusDays(2L),
                itemTwo,
                addedUser,
                WAITING
        ));

        List<Booking> result = bookingRepository.findBookingByItemAndUser(1L, 1L, "WAITING", timeNow.plusMinutes(3L));

        assertThat(result)
                .isNotNull()
                .hasSize(1);
        assertThat(result.get(0))
                .hasFieldOrPropertyWithValue("start", timeNow.plusMinutes(2L))
                .hasFieldOrPropertyWithValue("end", timeNow.plusDays(1L))
                .hasFieldOrPropertyWithValue("status", WAITING)
                .extracting("item")
                .isNotNull()
                .hasFieldOrPropertyWithValue("name", "Шуруповерт")
                .hasFieldOrPropertyWithValue("description", "Шуруповерт ручной")
                .hasFieldOrPropertyWithValue("available", true)
                .hasFieldOrPropertyWithValue("requestId", 1L);
        assertThat(result.get(0))
                .extracting("booker")
                .isNotNull()
                .hasFieldOrPropertyWithValue("name", "Duo")
                .hasFieldOrPropertyWithValue("email", "duo@test.com");
    }
}
