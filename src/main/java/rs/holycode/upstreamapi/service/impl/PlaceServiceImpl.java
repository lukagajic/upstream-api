package rs.holycode.upstreamapi.service.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.holycode.upstreamapi.client.PlaceRestClient;
import rs.holycode.upstreamapi.dto.request.OpeningHoursRange;
import rs.holycode.upstreamapi.dto.request.PlaceRequestDto;
import rs.holycode.upstreamapi.dto.response.PlaceResponseDto;
import rs.holycode.upstreamapi.service.PlaceService;
import rs.holycode.upstreamapi.util.DateUtil;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Data
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {
    private final PlaceRestClient placeRestClient;

    @Override
    public PlaceResponseDto getPlaceData(String placeId) {
        PlaceRequestDto request = placeRestClient.getPlaceData(placeId);

        PlaceResponseDto placeResponse = new PlaceResponseDto();
        placeResponse.setPlaceName(request.getName());
        placeResponse.setPlaceAddress(request.getAddress());


        LinkedHashMap<String, List<OpeningHoursRange>> openingHoursByDays = request.getOpeningHours().getDays();
        LinkedHashMap<String, List<String>> openingHoursFlat = new LinkedHashMap<>();

        placeResponse.setCurrentlyOpen(true);
        placeResponse.setCurrentlyOpen(isPlaceOpenNow(request.getOpeningHours().getDays()));

        List<String> daysRange = new ArrayList<>();
        List<String> skippedDays = new ArrayList<>();

        for (DayOfWeek day : DayOfWeek.values()) {
            String dayNameNormalized = day.getDisplayName(TextStyle.FULL, Locale.getDefault()).toLowerCase();

            if (!openingHoursByDays.containsKey(dayNameNormalized) && !skippedDays.contains(dayNameNormalized)) {
                openingHoursFlat.put(dayNameNormalized, Collections.emptyList());
            } else {
                List<OpeningHoursRange> currentHours = openingHoursByDays.get(dayNameNormalized);

                if (skippedDays.contains(dayNameNormalized)) {
                    continue;
                }

                openingHoursByDays.remove(dayNameNormalized);
                var iterator = openingHoursByDays.entrySet().iterator();

                while (iterator.hasNext()) {
                    var nextDay = iterator.next();

                    if (currentHours.size() != nextDay.getValue().size()) {
                        break;
                    }

                    boolean allRangesMatch = true;

                    for (int i = 0; i < currentHours.size(); i++) {
                        if (!currentHours.get(i).getStart().equals(nextDay.getValue().get(i).getStart()) ||
                                !currentHours.get(i).getEnd().equals(nextDay.getValue().get(i).getEnd())
                        ) {
                            allRangesMatch = false;
                        }
                    }

                    if (allRangesMatch) {
                        daysRange.add(nextDay.getKey());
                        skippedDays.add(nextDay.getKey());
                        iterator.remove();
                    }
                }

                List<String> hourStrings = currentHours.stream().map(it -> it.getStart() + " - " + it.getEnd()).collect(Collectors.toList());

                if (daysRange.isEmpty()) {
                    openingHoursFlat.put(dayNameNormalized, hourStrings);
                } else {
                    openingHoursFlat.put(dayNameNormalized + " - " + daysRange.get(daysRange.size() - 1), hourStrings);
                }

                daysRange.clear();
            }
        }

        placeResponse.setOpeningHours(openingHoursFlat);

        return placeResponse;
    }

    private LocalDateTime getOpeningOrClosingDateTime(LinkedHashMap<String, List<OpeningHoursRange>> openingHoursByDays, boolean isOpen) {
        List<OpeningHoursRange> openingHoursByDay = openingHoursByDays.getOrDefault(DateUtil.getCurrentDayOfWeekAsString(), Collections.emptyList());
        LocalTime current = LocalTime.now();

        if (isOpen) {
            return openingHoursByDay.stream().filter(range -> {
                        LocalTime start = LocalTime.parse(range.getStart());
                        LocalTime end = LocalTime.parse(range.getEnd());

                        return (current.equals(start) || current.isAfter(start)) && current.isBefore(end);
                    }).findFirst()
                    .map(openingHoursRange -> LocalDateTime.of(LocalDate.now(), LocalTime.parse(openingHoursRange.getEnd())))
                    .orElse(null);
        } else {
            return null;
        }
    }


    private boolean isPlaceOpenNow(LinkedHashMap<String, List<OpeningHoursRange>> openingHoursByDays) {
        List<OpeningHoursRange> openingHoursByDay = openingHoursByDays.getOrDefault(DateUtil.getCurrentDayOfWeekAsString(), Collections.emptyList());

        if (openingHoursByDay.isEmpty()) {
            return false;
        }

        LocalTime current = LocalTime.now();

        return openingHoursByDay.stream().filter(range -> {
                    LocalTime start = LocalTime.parse(range.getStart());
                    LocalTime end = LocalTime.parse(range.getEnd());

                    return (current.equals(start) || current.isAfter(start)) && current.isBefore(end);
                }).findFirst()
                .isPresent();
    }
}
