package back.tjv.business;


import back.tjv.business.impl.BuyerServiceImpl;
import back.tjv.data.dto.buyer.BuyerCreateDto;
import back.tjv.data.dto.buyer.BuyerReadDto;
import back.tjv.data.dto.buyer.BuyerUpdateDto;
import back.tjv.data.entity.Buyer;
import back.tjv.data.repository.BuyerRepository;
import back.tjv.data.repository.EventRepository;
import back.tjv.exception.ConflictException;
import back.tjv.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuyerServiceImplTest {

    @Mock BuyerRepository buyerRepository;
    @Mock EventRepository eventRepository;

    @InjectMocks BuyerServiceImpl service;

    @BeforeEach
    void setUp() {

    }

    @Test
    void create_SavesAndReturnsDto_WhenEmailIsNew() {
        BuyerCreateDto dto = new BuyerCreateDto("Ana", "ana@example.com");

        when(buyerRepository.findByEmail("ana@example.com")).thenReturn(Optional.empty());
        when(buyerRepository.save(any(Buyer.class))).thenAnswer(inv -> {
            Buyer b = inv.getArgument(0);

            ReflectionTestUtils.setField(b, "id", 42L);
            b.setCreatedAt(OffsetDateTime.now());
            return b;
        });

        BuyerReadDto out = service.create(dto);

        assertNotNull(out);
        assertEquals(42L, out.id());
        assertEquals("Ana", out.name());
        assertEquals("ana@example.com", out.email());

        ArgumentCaptor<Buyer> captor = ArgumentCaptor.forClass(Buyer.class);
        verify(buyerRepository).save(captor.capture());
        assertEquals("Ana", captor.getValue().getName());
        assertEquals("ana@example.com", captor.getValue().getEmail());
    }

    @Test
    void create_ThrowsConflict_WhenEmailExists() {
        BuyerCreateDto dto = new BuyerCreateDto("Ana", "dup@example.com");
        when(buyerRepository.findByEmail("dup@example.com"))
                .thenReturn(Optional.of(new Buyer("Old", "dup@example.com")));

        ConflictException ex = assertThrows(ConflictException.class,
                () -> service.create(dto));
        assertTrue(ex.getMessage().contains("already exists"));
        verify(buyerRepository, never()).save(any());
    }

    @Test
    void get_ThrowsNotFound_WhenMissing() {
        when(buyerRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.get(99L));
    }

    @Test
    void update_ChangesNameAndEmail_WhenUnique() {
        Buyer existing = new Buyer("Old", "old@ex.com");
        ReflectionTestUtils.setField(existing, "id", 7L);

        when(buyerRepository.findById(7L)).thenReturn(Optional.of(existing));
        when(buyerRepository.existsByEmail("new@ex.com")).thenReturn(false);
        when(buyerRepository.save(any(Buyer.class))).thenAnswer(inv -> inv.getArgument(0));

        BuyerUpdateDto dto = new BuyerUpdateDto("NewName", "new@ex.com");
        BuyerReadDto out = service.update(7L, dto);

        assertEquals("NewName", out.name());
        assertEquals("new@ex.com", out.email());
    }

    @Test
    void listEvents_ThrowsNotFound_WhenBuyerMissing() {
        when(buyerRepository.existsById(123L)).thenReturn(false);
        assertThrows(NotFoundException.class, () -> service.listEvents(123L));
        verify(eventRepository, never()).findDistinctByBuyerId(anyLong());
    }
}
