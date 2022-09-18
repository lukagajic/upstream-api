package rs.holycode.upstreamapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OpeningHoursRange {
    private String start;
    private String end;
    private String type;
}
