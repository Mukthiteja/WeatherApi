package com.example.weatherapi.service;

import com.example.weatherapi.model.Weather;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

/**
 * Unit tests for ExternalWeatherClient.
 * All RestTemplate API calls are mocked.
 */
public class ExternalWeatherClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ExternalWeatherClient client;

    @BeforeEach
    public void setup() {
        openMocks(this);
        ReflectionTestUtils.setField(client, "apiKey", "dummy-api-key");
    }

    @Test
    public void testGetLatLongFromPincode_Success() {
        String pincode = "411014";
        Map<String, Object> mockResponse = Map.of(
                "lat", 18.5528,
                "lon", 73.9255
        );

        when(restTemplate.getForEntity(anyString(), eq(Map.class)))
                .thenReturn(ResponseEntity.ok(mockResponse));

        double[] latLong = client.getLatLongFromPincode(pincode);

        assertEquals(18.5528, latLong[0]);
        assertEquals(73.9255, latLong[1]);
    }

    @Test
    public void testGetWeatherFromLatLong_Success() {
        double lat = 18.5528;
        double lon = 73.9255;
        String date = "2020-10-15";

        Map<String, Object> weatherMap = Map.of(
                "description", "clear sky"
        );

        Map<String, Object> mainMap = Map.of(
                "temp", 30.0,
                "humidity", 65
        );

        Map<String, Object> mockResponse = Map.of(
                "weather", List.of(weatherMap),
                "main", mainMap
        );

        when(restTemplate.getForEntity(anyString(), eq(Map.class)))
                .thenReturn(ResponseEntity.ok(mockResponse));

        Weather weather = client.getWeatherFromLatLong(lat, lon, date);

        assertNotNull(weather);
        assertEquals("clear sky", weather.getDescription());
        assertEquals(30.0, weather.getTemperature());
        assertEquals(65.0, weather.getHumidity());
        assertEquals(date, weather.getDate());
    }
}
