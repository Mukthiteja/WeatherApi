package com.example.weatherapi.controller;

import com.example.weatherapi.model.Weather;
import com.example.weatherapi.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for fetching weather information for a specific pincode and date.
 */
@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    /**
     * Fetch weather information for the given pincode and date.
     *
     * @param pincode Indian postal code (e.g., 411014)
     * @param date    Date in format YYYY-MM-DD
     * @return Weather object containing temperature, humidity, etc.
     */
    @GetMapping
    public Weather getWeather(@RequestParam String pincode, @RequestParam String date) {
        return weatherService.getWeather(pincode, date);
    }
}
