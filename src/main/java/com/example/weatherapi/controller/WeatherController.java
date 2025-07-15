package com.example.weatherapi.controller;

import com.example.weatherapi.model.Weather;
import com.example.weatherapi.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    @Autowired private WeatherService weatherService;

    @GetMapping
    public Weather getWeather(@RequestParam String pincode, @RequestParam String date) {
        return weatherService.getWeather(pincode, date);
    }
}
