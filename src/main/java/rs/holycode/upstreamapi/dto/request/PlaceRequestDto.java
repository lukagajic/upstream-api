package rs.holycode.upstreamapi.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlaceRequestDto {
    @JsonProperty("displayed_what")
    private String name;
    @JsonProperty("displayed_where")
    private String address;
    @JsonProperty("opening_hours")
    private OpeningHours openingHours;
}
