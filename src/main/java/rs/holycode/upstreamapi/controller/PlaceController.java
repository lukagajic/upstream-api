package rs.holycode.upstreamapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.holycode.upstreamapi.dto.response.PlaceResponseDto;
import rs.holycode.upstreamapi.service.PlaceService;

@RestController
@RequestMapping("/places")
@RequiredArgsConstructor
@CrossOrigin("*")
public class PlaceController {
    private final PlaceService placeService;

    @GetMapping("/{placeId}")
    public ResponseEntity<PlaceResponseDto> getPlaceData(@PathVariable String placeId) {
        return ResponseEntity.ok(placeService.getPlaceData(placeId));
    }
}
