package com.example.weatherapi.service;

import com.example.weatherapi.model.Weather;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class ExternalWeatherClient {
    @Value("${openweather.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public double[] getLatLongFromPincode(String pincode) {
        String url = "http://api.openweathermap.org/geo/1.0/zip?zip=" + pincode + ",IN&appid=" + apiKey;
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        Map body = response.getBody();
        return new double[]{(Double) body.get("lat"), (Double) body.get("lon")};
    }

    public Weather getWeatherFromLatLong(double lat, double lon, String date) {
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat +
                "&lon=" + lon + "&appid=" + apiKey + "&units=metric";

        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        Map body = response.getBody();

        Weather w = new Weather();
        Map weatherDetails = ((List<Map>) body.get("weather")).get(0);
        Map mainDetails = (Map) body.get("main");

        w.setDescription((String) weatherDetails.get("description"));
        w.setTemperature(((Number) mainDetails.get("temp")).doubleValue());
        w.setHumidity(((Number) mainDetails.get("humidity")).doubleValue());
        w.setDate(date); // use requested date

        return w;
    }
}
