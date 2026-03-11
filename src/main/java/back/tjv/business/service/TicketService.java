package back.tjv.business.service;

import back.tjv.data.dto.speaker.SpeakerReadDto;
import back.tjv.data.dto.ticket.TicketCreateDto;
import back.tjv.data.dto.ticket.TicketReadDto;
import back.tjv.data.dto.ticket.TicketSeatDtos;
import back.tjv.data.dto.ticket.TicketUpdateDto;

import java.util.List;

public interface TicketService {
    TicketReadDto create(TicketCreateDto dto);
    TicketReadDto get(Long id);
    List<TicketReadDto> list();
    List<TicketReadDto> listByBuyer(Long buyerId);
    List<TicketReadDto> listByEvent(Long eventId);
    TicketReadDto update(Long id, TicketUpdateDto dto);
    void delete(Long id);
    List<SpeakerReadDto> listSpeakers(Long ticketId);
    TicketSeatDtos.SeatMapDto getSeatMap(Long eventId);
    TicketSeatDtos.ValidateResp validateSeats(Long eventId, TicketSeatDtos.SeatsReq req);
    List<TicketReadDto> purchaseSeats(Long eventId, TicketSeatDtos.PurchaseReq req);
}
