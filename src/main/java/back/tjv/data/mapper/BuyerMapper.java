package back.tjv.data.mapper;

import back.tjv.data.dto.buyer.BuyerCreateDto;
import back.tjv.data.dto.buyer.BuyerReadDto;
import back.tjv.data.dto.buyer.BuyerUpdateDto;
import back.tjv.data.entity.Buyer;

public final class BuyerMapper {
    private BuyerMapper() { }


    public static BuyerReadDto toReadDto(Buyer e) {
        if (e == null) return null;
        return new BuyerReadDto(
                e.getId(),
                e.getName(),
                e.getEmail(),
                e.getCreatedAt()
        );
    }


    public static Buyer fromCreateDto(BuyerCreateDto d) {
        if (d == null) return null;
        return new Buyer(d.name(), d.email());
    }


    public static void applyUpdate(Buyer e, BuyerUpdateDto d) {
        if (e == null || d == null) return;
        e.setName(d.name());
        e.setEmail(d.email());
    }
}