package back.tjv.business.impl;

import back.tjv.data.dto.event.EventReadDto;
import back.tjv.business.service.BuyerService;
import back.tjv.data.dto.buyer.BuyerCreateDto;
import back.tjv.data.dto.buyer.BuyerReadDto;
import back.tjv.data.dto.buyer.BuyerUpdateDto;
import back.tjv.data.entity.Buyer;
import back.tjv.data.mapper.BuyerMapper;
import back.tjv.data.mapper.EventMapper;
import back.tjv.data.repository.BuyerRepository;
import back.tjv.data.repository.EventRepository;
import back.tjv.exception.ConflictException;
import back.tjv.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BuyerServiceImpl implements BuyerService {

    private final BuyerRepository buyerRepository;
    private final EventRepository eventRepository;

    public BuyerServiceImpl(BuyerRepository buyerRepository, EventRepository eventRepository) {
        this.buyerRepository = buyerRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public BuyerReadDto create(BuyerCreateDto dto) {
        buyerRepository.findByEmail(dto.email()).ifPresent(b ->
        { throw new ConflictException("Buyer with email already exists: " + dto.email()); });

        Buyer entity = BuyerMapper.fromCreateDto(dto);
        Buyer saved = buyerRepository.save(entity);
        return BuyerMapper.toReadDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public BuyerReadDto get(Long id) {
        Buyer e = buyerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Buyer not found: id=" + id));
        return BuyerMapper.toReadDto(e);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BuyerReadDto> list() {
        return ((List<Buyer>) buyerRepository.findAll())
                .stream()
                .map(BuyerMapper::toReadDto)
                .toList();
    }

    @Override
    public BuyerReadDto update(Long id, BuyerUpdateDto dto) {
        Buyer e = buyerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Buyer not found: id=" + id));

        if (!e.getEmail().equals(dto.email()) && buyerRepository.existsByEmail(dto.email())) {
            throw new ConflictException("Buyer with email already exists: " + dto.email());
        }

        BuyerMapper.applyUpdate(e, dto);
        Buyer saved = buyerRepository.save(e);
        return BuyerMapper.toReadDto(saved);
    }

    @Override
    public void delete(Long id) {
        if (!buyerRepository.existsById(id)) {
            throw new NotFoundException("Buyer not found: id=" + id);
        }
        buyerRepository.deleteById(id);
    }
    @Override
    @Transactional(readOnly = true)
    public List<EventReadDto> listEvents(Long buyerId) {
        if (!buyerRepository.existsById(buyerId)) {
            throw new NotFoundException("Buyer not found: id=" + buyerId);
        }
        return eventRepository.findDistinctByBuyerId(buyerId)
                .stream()
                .map(EventMapper::toReadDto)
                .toList();
    }

}
