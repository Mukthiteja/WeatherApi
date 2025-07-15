package com.example.weatherapi.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WeatherTest {

    @Test
    void testAllArgsConstructorAndGetters() {
        Weather weather = new Weather(1L, "411014", "2025-07-15", "clear sky", 30.5, 60.0);

        assertEquals(1L, weather.getId());
        assertEquals("411014", weather.getPincode());
        assertEquals("2025-07-15", weather.getDate());
        assertEquals("clear sky", weather.getDescription());
        assertEquals(30.5, weather.getTemperature());
        assertEquals(60.0, weather.getHumidity());
    }

    @Test
    void testSetters() {
        Weather weather = new Weather();
        weather.setId(2L);
        weather.setPincode("560001");
        weather.setDate("2025-07-16");
        weather.setDescription("partly cloudy");
        weather.setTemperature(28.0);
        weather.setHumidity(55.0);

        assertEquals(2L, weather.getId());
        assertEquals("560001", weather.getPincode());
        assertEquals("2025-07-16", weather.getDate());
        assertEquals("partly cloudy", weather.getDescription());
        assertEquals(28.0, weather.getTemperature());
        assertEquals(55.0, weather.getHumidity());
    }

    @Test
    void testNoArgsConstructor() {
        Weather weather = new Weather();
        assertNotNull(weather);
    }
}
