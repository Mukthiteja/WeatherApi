package com.example.weatherapi.service;

import com.example.weatherapi.model.Pincode;
import com.example.weatherapi.model.Weather;
import com.example.weatherapi.repository.PincodeRepository;
import com.example.weatherapi.repository.WeatherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WeatherServiceTest {

    @InjectMocks
    private WeatherService weatherService;

    @Mock
    private WeatherRepository weatherRepo;

    @Mock
    private PincodeRepository pincodeRepo;

    @Mock
    private ExternalWeatherClient externalClient;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetWeather_FromCache() {
        String pincode = "411014";
        String date = "2025-07-13";
        Weather cachedWeather = new Weather();
        cachedWeather.setPincode(pincode);
        cachedWeather.setDate(date);
        cachedWeather.setTemperature(30.5);
        cachedWeather.setHumidity(70.0);

        when(weatherRepo.findByPincodeAndDate(pincode, date)).thenReturn(Optional.of(cachedWeather));

        Weather result = weatherService.getWeather(pincode, date);

        assertEquals(30.5, result.getTemperature());
        assertEquals(70.0, result.getHumidity());
        verify(weatherRepo).findByPincodeAndDate(pincode, date);
        verifyNoMoreInteractions(weatherRepo, pincodeRepo, externalClient);
    }

    @Test
    void testGetWeather_FetchFromApiAndSave() {
        String pincode = "411014";
        String date = "2025-07-13";

        // 1. No cached weather
        when(weatherRepo.findByPincodeAndDate(pincode, date)).thenReturn(Optional.empty());

        // 2. No existing pincode
        when(pincodeRepo.findById(pincode)).thenReturn(Optional.empty());

        // 3. Get lat/long from external API
        double[] latLong = {18.5204, 73.8567};
        when(externalClient.getLatLongFromPincode(pincode)).thenReturn(latLong);

        Pincode newPincode = new Pincode(pincode, latLong[0], latLong[1]);
        when(pincodeRepo.save(any(Pincode.class))).thenReturn(newPincode);

        // 4. Get weather from external API
        Weather apiWeather = new Weather();
        apiWeather.setDate(date);
        apiWeather.setTemperature(29.0);
        apiWeather.setHumidity(60.0);
        apiWeather.setDescription("sunny");
        when(externalClient.getWeatherFromLatLong(latLong[0], latLong[1], date)).thenReturn(apiWeather);

        // 5. Save weather to DB
        Weather savedWeather = new Weather();
        savedWeather.setPincode(pincode);
        savedWeather.setDate(date);
        savedWeather.setTemperature(29.0);
        savedWeather.setHumidity(60.0);
        savedWeather.setDescription("sunny");
        when(weatherRepo.save(apiWeather)).thenReturn(savedWeather);

        Weather result = weatherService.getWeather(pincode, date);

        assertEquals(29.0, result.getTemperature());
        assertEquals(60.0, result.getHumidity());
        assertEquals("sunny", result.getDescription());
        assertEquals(pincode, result.getPincode());

        verify(weatherRepo).findByPincodeAndDate(pincode, date);
        verify(pincodeRepo).findById(pincode);
        verify(externalClient).getLatLongFromPincode(pincode);
        verify(pincodeRepo).save(any(Pincode.class));
        verify(externalClient).getWeatherFromLatLong(latLong[0], latLong[1], date);
        verify(weatherRepo).save(apiWeather);
    }
}
