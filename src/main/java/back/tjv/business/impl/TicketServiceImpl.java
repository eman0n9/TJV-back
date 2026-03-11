package back.tjv.business.impl;


import back.tjv.business.service.TicketService;
import back.tjv.data.dto.ticket.TicketCreateDto;
import back.tjv.data.dto.ticket.TicketReadDto;
import back.tjv.data.dto.ticket.TicketSeatDtos;
import back.tjv.data.dto.ticket.TicketUpdateDto;
import back.tjv.data.entity.Buyer;
import back.tjv.data.entity.Event;
import back.tjv.data.entity.Ticket;
import back.tjv.data.mapper.SpeakerMapper;
import back.tjv.data.mapper.TicketMapper;
import back.tjv.data.repository.BuyerRepository;
import back.tjv.data.repository.EventRepository;
import back.tjv.data.repository.TicketRepository;
import back.tjv.exception.ConflictException;
import back.tjv.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import back.tjv.data.dto.speaker.SpeakerReadDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;
    private final BuyerRepository buyerRepository;

    public TicketServiceImpl(TicketRepository ticketRepository,
                             EventRepository eventRepository,
                             BuyerRepository buyerRepository) {
        this.ticketRepository = ticketRepository;
        this.eventRepository = eventRepository;
        this.buyerRepository = buyerRepository;
    }

    private static String seatId(String row, Integer seat) { return row + seat; }

    private record SeatSpec(String row, int number) {}

    private static SeatSpec parseSeat(String seatId) {
        if (seatId == null) {
            return null;
        }
        String s = seatId.trim().toUpperCase();
        if (s.length() < 2) {
            return null;
        }
        String row = s.substring(0, 1);
        int num;
        try {
            num = Integer.parseInt(s.substring(1));
        } catch (NumberFormatException ex) {
            return null;
        }
        if (row.charAt(0) < 'A' || row.charAt(0) > 'J' || num < 1 || num > 12) {
            return null;
        }
        return new SeatSpec(row, num);
    }

    @Override
    public TicketReadDto create(TicketCreateDto dto) {
        Event event = eventRepository.findById(dto.eventId())
                .orElseThrow(() -> new NotFoundException("Event not found: id=" + dto.eventId()));
        Buyer buyer = buyerRepository.findById(dto.buyerId())
                .orElseThrow(() -> new NotFoundException("Buyer not found: id=" + dto.buyerId()));

        Ticket entity = TicketMapper.fromCreateDto(dto);


        String row = entity.getRowLabel();
        Integer seat = entity.getSeatNumber();
        if (row == null || row.length()!=1 || row.charAt(0) < 'A' || row.charAt(0) > 'J'
                || seat == null || seat < 1 || seat > 12) {
            throw new IllegalArgumentException("Seat out of range: " + (row==null?"?":row) + seat);
        }


        if (ticketRepository.existsByEvent_IdAndRowLabelAndSeatNumber(event.getId(), row, seat)) {
            throw new ConflictException("Seat already sold: " + row + seat);
        }

        entity.setEvent(event);
        entity.setBuyer(buyer);
        entity.setPrice(event.getPrice());

        try {
            Ticket saved = ticketRepository.save(entity);
            return TicketMapper.toReadDto(saved);
        } catch (org.springframework.dao.DataIntegrityViolationException dup) {
            throw new ConflictException("Seat already sold: " + row + seat);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public TicketReadDto get(Long id) {
        Ticket e = ticketRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ticket not found: id=" + id));
        return TicketMapper.toReadDto(e);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketReadDto> list() {
        var out = new ArrayList<TicketReadDto>();
        ticketRepository.findAll().forEach(t -> out.add(TicketMapper.toReadDto(t)));
        return out;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketReadDto> listByBuyer(Long buyerId) {
        if (!buyerRepository.existsById(buyerId)) {
            throw new NotFoundException("Buyer not found: id=" + buyerId);
        }
        return ticketRepository.findByBuyer_Id(buyerId)
                .stream()
                .map(TicketMapper::toReadDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketReadDto> listByEvent(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException("Event not found: id=" + eventId);
        }
        return ticketRepository.findByEvent_Id(eventId)
                .stream()
                .map(TicketMapper::toReadDto)
                .toList();
    }

    @Override
    public TicketReadDto update(Long id, TicketUpdateDto dto) {
        Ticket e = ticketRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ticket not found: id=" + id));

        if (dto.seatId() != null && !dto.seatId().isBlank()) {
            String seat = dto.seatId().trim().toUpperCase();
            SeatSpec spec = parseSeat(seat);
            if (spec == null) {
                throw new IllegalArgumentException("Invalid seatId: " + seat);
            }

            Long eventId = e.getEvent().getId();

            boolean takenByAnother = ticketRepository
                    .existsByEvent_IdAndRowLabelAndSeatNumberAndIdNot(eventId, spec.row(), spec.number(), e.getId());
            if (takenByAnother) {
                throw new ConflictException("Seat already sold: " + seat);
            }

            e.setRowLabel(spec.row());
            e.setSeatNumber(spec.number());
        }


        if (dto.price() != null) {
            e.setPrice(dto.price());
        }

        try {
            Ticket saved = ticketRepository.save(e);
            return TicketMapper.toReadDto(saved);
        } catch (org.springframework.dao.DataIntegrityViolationException dup) {
            throw new ConflictException("Seat already sold: " + dto.seatId());
        }
    }


    @Override
    public void delete(Long id) {
        if (!ticketRepository.existsById(id)) {
            throw new NotFoundException("Ticket not found: id=" + id);
        }
        ticketRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SpeakerReadDto> listSpeakers(Long ticketId) {
        if (!ticketRepository.existsById(ticketId)) {
            throw new NotFoundException("Ticket not found: id=" + ticketId);
        }
        return ticketRepository.findSpeakersByTicketId(ticketId)
                .stream()
                .map(SpeakerMapper::toReadDto)
                .toList();
    }

    @Override
    public TicketSeatDtos.SeatMapDto getSeatMap(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException("Event not found: id=" + eventId);
        }
        int rows = 10, cols = 12;
        var taken = ticketRepository.findSeatedByEvent(eventId).stream()
                .map(t -> seatId(t.getRowLabel(), t.getSeatNumber()))
                .collect(Collectors.toSet());

        var list = new ArrayList<TicketSeatDtos.SeatDto>();
        for (int r = 0; r < rows; r++) {
            String row = String.valueOf((char)('A' + r));
            for (int c = 1; c <= cols; c++) {
                String id = row + c;
                list.add(new TicketSeatDtos.SeatDto(id, taken.contains(id) ? "sold" : "available"));
            }
        }
        return new TicketSeatDtos.SeatMapDto(rows, cols, list);
    }

    @Override
    public TicketSeatDtos.ValidateResp validateSeats(Long eventId, TicketSeatDtos.SeatsReq req) {
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException("Event not found: id=" + eventId);
        }
        List<String> unavailable = new ArrayList<>();
        if (req.seats() != null) {
            for (String s : req.seats()) {
                SeatSpec spec = parseSeat(s);
                if (spec == null) {
                    throw new IllegalArgumentException("Invalid seatId: " + s);
                }
                if (ticketRepository.existsByEvent_IdAndRowLabelAndSeatNumber(eventId, spec.row(), spec.number())) {
                    unavailable.add(s);
                }
            }
        }
        return new TicketSeatDtos.ValidateResp(unavailable.isEmpty(), unavailable);
    }

    @Override
    public List<TicketReadDto> purchaseSeats(Long eventId, TicketSeatDtos.PurchaseReq req) {
        if (req.buyerId() == null || req.seats() == null || req.seats().isEmpty())
            throw new IllegalArgumentException("buyerId and seats are required");
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException("Event not found: id=" + eventId);
        }
        if (!buyerRepository.existsById(req.buyerId())) {
            throw new NotFoundException("Buyer not found: id=" + req.buyerId());
        }

        var out = new ArrayList<TicketReadDto>();
        for (String seat : req.seats()) {
            out.add(create(new TicketCreateDto(eventId, req.buyerId(), seat)));
        }
        return out;
    }

}
