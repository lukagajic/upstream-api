package rs.holycode.upstreamapi.client;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import rs.holycode.upstreamapi.dto.request.PlaceRequestDto;
import rs.holycode.upstreamapi.exception.RestTemplateResponseErrorHandler;

import javax.annotation.PostConstruct;

@Component
@Getter
@Setter
@RequiredArgsConstructor
public class PlaceRestClient {
    private final RestTemplate restTemplate;
    private final RestTemplateResponseErrorHandler errorHandler;
    @Value("${places.resource.base-url}")
    private String placesBaseURL;

    @PostConstruct
    public void afterInit() {
        restTemplate.setErrorHandler(errorHandler);
    }

    public PlaceRequestDto getPlaceData(String placeId) {
        final String URL = placesBaseURL + "/" + placeId;

        ResponseEntity<PlaceRequestDto> placeDataEntity = restTemplate.getForEntity(URL, PlaceRequestDto.class);

        return placeDataEntity.getBody();
    }
}
