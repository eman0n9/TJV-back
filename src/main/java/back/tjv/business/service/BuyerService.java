package back.tjv.business.service;

import back.tjv.data.dto.buyer.BuyerCreateDto;
import back.tjv.data.dto.buyer.BuyerReadDto;
import back.tjv.data.dto.buyer.BuyerUpdateDto;
import back.tjv.data.dto.event.EventReadDto;

import java.util.List;

public interface BuyerService {
    BuyerReadDto create(BuyerCreateDto dto);
    BuyerReadDto get(Long id);
    List<BuyerReadDto> list();
    BuyerReadDto update(Long id, BuyerUpdateDto dto);
    void delete(Long id);
    List<EventReadDto> listEvents(Long buyerId);
}
