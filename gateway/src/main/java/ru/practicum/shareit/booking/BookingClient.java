package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> create(Long userId, BookingDtoIn bookingDtoIn) {
        return post("", userId, bookingDtoIn);
    }

    public ResponseEntity<Object> setStatusBooking(Long ownerId, Long bookingId, Boolean approved) {
        Map<String, Object> parameters = Map.of(
                "approved", approved
        );
        return patch("/" + bookingId + "?approved={approved}", ownerId, parameters, null);
    }

    public ResponseEntity<Object> findBookingById(Long userId, Long bookingId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> findBookingByUser(Long ownerId, String state, Integer from, Integer size) {
        Map<String, Object> parameters;
        if (from != null && size != null) {
            parameters = Map.of(
                    "state", state,
                    "from", from,
                    "size", size
            );
            return get("?state={state}&from={from}&size={size}", ownerId, parameters);
        } else {
            parameters = Map.of(
                    "state", state
            );
            return get("?state={state}", ownerId, parameters);
        }
    }

    public ResponseEntity<Object> findBookingAllItemsByUser(Long ownerId, String state, Integer from, Integer size) {
        Map<String, Object> parameters;
        if (from != null && size != null) {
            parameters = Map.of(
                    "state", state,
                    "from", from,
                    "size", size
            );
            return get("/owner?state={state}&from={from}&size={size}", ownerId, parameters);
        } else {
            parameters = Map.of(
                    "state", state
            );
            return get("/owner?state={state}", ownerId, parameters);
        }
    }
}
