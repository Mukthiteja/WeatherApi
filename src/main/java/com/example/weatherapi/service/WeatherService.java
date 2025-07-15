package com.example.weatherapi.service;

import com.example.weatherapi.model.Pincode;
import com.example.weatherapi.model.Weather;
import com.example.weatherapi.repository.PincodeRepository;
import com.example.weatherapi.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WeatherService {

    @Autowired private WeatherRepository weatherRepo;
    @Autowired private PincodeRepository pincodeRepo;
    @Autowired private ExternalWeatherClient externalClient;

    public Weather getWeather(String pincode, String date) {
        Optional<Weather> cachedWeather = weatherRepo.findByPincodeAndDate(pincode, date);
        if (cachedWeather.isPresent()) return cachedWeather.get();

        Pincode pin = pincodeRepo.findById(pincode)
                .orElseGet(() -> {
                    double[] latLong = externalClient.getLatLongFromPincode(pincode);
                    Pincode newPin = new Pincode(pincode, latLong[0], latLong[1]);
                    return pincodeRepo.save(newPin);
                });

        Weather fetchedWeather = externalClient.getWeatherFromLatLong(pin.getLatitude(), pin.getLongitude(), date);
        fetchedWeather.setPincode(pincode);
        return weatherRepo.save(fetchedWeather);
    }
}
