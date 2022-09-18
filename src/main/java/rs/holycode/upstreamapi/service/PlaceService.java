package rs.holycode.upstreamapi.service;

import rs.holycode.upstreamapi.dto.response.PlaceResponseDto;

public interface PlaceService {
    PlaceResponseDto getPlaceData(String placeId);
}
