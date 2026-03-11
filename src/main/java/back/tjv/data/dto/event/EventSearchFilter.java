package back.tjv.data.dto.event;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record EventSearchFilter(
        String q,
        String title,
        String venue,
        OffsetDateTime startFrom,
        OffsetDateTime startTo,
        BigDecimal minPrice,
        BigDecimal maxPrice
) {}
