package rs.holycode.upstreamapi.dto.response;

import lombok.*;

import java.util.LinkedHashMap;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlaceResponseDto {
    private String placeName;
    private String placeAddress;
    private LinkedHashMap<String, List<String>> openingHours;
}
