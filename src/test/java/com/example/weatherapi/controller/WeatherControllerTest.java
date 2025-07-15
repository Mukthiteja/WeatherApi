package com.example.weatherapi.controller;

import com.example.weatherapi.model.Weather;
import com.example.weatherapi.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.when;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.Matchers.*;

/**
 * Unit test for WeatherController using MockMvc and Mockito.
 */
@WebMvcTest(WeatherController.class)
public class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherService weatherService;

    @Test
    public void testGetWeatherSuccess() throws Exception {
        // Arrange
        String pincode = "411014";
        String date = "2020-10-15";

        Weather dummyWeather = new Weather();
        dummyWeather.setPincode(pincode);
        dummyWeather.setDate(date);
        dummyWeather.setDescription("clear sky");
        dummyWeather.setTemperature(30.5);
        dummyWeather.setHumidity(60.0);

        when(weatherService.getWeather(pincode, date)).thenReturn(dummyWeather);

        // Act & Assert
        mockMvc.perform(get("/api/weather")
                        .param("pincode", pincode)
                        .param("date", date))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pincode", is(pincode)))
                .andExpect(jsonPath("$.date", is(date)))
                .andExpect(jsonPath("$.description", is("clear sky")))
                .andExpect(jsonPath("$.temperature", is(30.5)))
                .andExpect(jsonPath("$.humidity", is(60.0)));
    }

    @Test
    public void testMissingParams() throws Exception {
        // Only pincode passed, no date
        mockMvc.perform(get("/api/weather")
                        .param("pincode", "411014"))
                .andExpect(status().isBadRequest());
    }
}
