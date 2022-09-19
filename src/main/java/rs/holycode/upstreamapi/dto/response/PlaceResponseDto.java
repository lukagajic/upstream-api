package rs.holycode.upstreamapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private boolean currentlyOpen;
}
